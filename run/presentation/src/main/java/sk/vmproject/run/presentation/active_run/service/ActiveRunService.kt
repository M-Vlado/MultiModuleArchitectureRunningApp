package sk.vmproject.run.presentation.active_run.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import sk.vmproject.core.domain.run.model.ObstacleModel
import sk.vmproject.core.domain.run.model.RunWithObstaclesModel
import sk.vmproject.core.domain.run.repository.VirtualOcrRepository
import sk.vmproject.core.domain.tts.TextToSpeechController
import sk.vmproject.core.presentation.designsystem.components.utils.ObstaclesFormatter
import sk.vmproject.core.presentation.ui.formatted
import sk.vmproject.run.domain.RunningTracker
import sk.vmproject.run.presentation.R

class ActiveRunService : Service() {

    private val notificationManager by lazy {
        getSystemService<NotificationManager>()!!
    }

    private val baseNotification by lazy {
        NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(sk.vmproject.core.presentation.designsystem.R.drawable.virtual_ocr_logo_transparent)
            .setContentTitle("")
    }

    private var endOfRaceWasAnnounced = false

    private val runningTracker by inject<RunningTracker>()

    private val virtualOcrRepository by inject<VirtualOcrRepository>()

    private val textToSpeechController by inject<TextToSpeechController>()

    private var serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val activityClass = intent.getStringExtra(EXTRA_ACTIVITY_CLASS)
                    ?: throw IllegalArgumentException("No activity class provided")
                val runId = intent.getStringExtra(EXTRA_RUN_ID)
                    ?: throw IllegalArgumentException("No run id provided")
                start(Class.forName(activityClass), runId)
            }

            ACTION_STOP -> stop()
        }
        return START_STICKY
    }

    private fun start(activityClass: Class<*>, runId: String) {
        if (!isServiceActive) {
            isServiceActive = true
            createNotificationChannel()

            val activityIntent = Intent(applicationContext, activityClass).apply {
                data = "virtualocr://active_run_screen/$runId".toUri()
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }

            val pendingIntent = TaskStackBuilder.create(applicationContext).run {
                addNextIntentWithParentStack(activityIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }

            val notification = baseNotification
                .setContentText("00:00:00")
                .setContentIntent(pendingIntent)
                .build()

            startForeground(1, notification)
            updateNotification()

            getRunWithObstaclesFromDb(runId = runId)

            trackObstacles()

        }
    }

    private fun getRunWithObstaclesFromDb(runId: String) {
        try {
            serviceScope.launch {
                val obstacles =
                    virtualOcrRepository.getTypeOfRunWithObstacles(typeOfRunId = runId.toLong())
                _runWithObstacles.value = obstacles.firstOrNull()
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun trackObstacles() {

        runningTracker.runData.onEach { runData ->
            val currentDistanceInMeters = runData.distanceMeters
            val locationTimestamp = runData.locations.lastOrNull()?.lastOrNull()
            val elapsedTime = locationTimestamp?.durationTimestamp

            _runWithObstacles.value?.let {
                val runDistanceInMeters = it.distanceInKilometers * 1000
                if (currentDistanceInMeters >= runDistanceInMeters && !endOfRaceWasAnnounced) {
                    textToSpeechController.startSpeech(
                        String.format(
                            format = applicationContext.getString(
                                R.string.run_finish_tts_info
                            ), args = arrayOf(it.distanceInKilometers)
                        )
                    )
                    endOfRaceWasAnnounced = true
                } else {
                    val modifiedObstacles: List<ObstacleModel> = it.obstacles.map { obstacleModel ->
                        if (currentDistanceInMeters >= (obstacleModel.distanceFromStartInMeters - 50) && !obstacleModel.wasIntroduced) {
                            // next obstacle in 50 meters
                            textToSpeechController.startSpeech(getString(R.string._50_meters_to_the_next_obstacle))
                            obstacleModel.copy(
                                wasIntroduced = true
                            )
                        } else if (currentDistanceInMeters >= obstacleModel.distanceFromStartInMeters && obstacleModel.obstacleReachTimeStamp == null) {
                            //obstacle reach
                            val introduceObstacleText =
                                "Obstacle ${obstacleModel.obstacleType}s\n\n${
                                    ObstaclesFormatter.getObstacleDescriptionForTTS(
                                        numberOfReps = obstacleModel.numberOfReps,
                                        obstacleType = obstacleModel.obstacleType
                                    )
                                } start now!"
                            textToSpeechController.startSpeech(introduceObstacleText)
                            obstacleModel.copy(
                                obstacleReachTimeStamp = elapsedTime,
                                longitude = locationTimestamp?.location?.location?.long,
                                latitude = locationTimestamp?.location?.location?.lat
                            )
                        } else {
                            obstacleModel
                        }
                    }
                    _runWithObstacles.update { runWithObstaclesModel ->
                        runWithObstaclesModel?.copy(
                            obstacles = modifiedObstacles
                        )
                    }
                }

            }

        }.launchIn(serviceScope)
    }

    private fun updateNotification() {
        runningTracker.elapsedTime.onEach { elapsedTime ->
            val notification = baseNotification
                .setContentTitle(
                    "${_runWithObstacles.value?.title} ${_runWithObstacles.value?.distanceInKilometers} KM + ${
                        ObstaclesFormatter.getObstacleDescription(
                            obstaclesCount = _runWithObstacles.value?.obstacleCount ?: 0,
                            context = applicationContext
                        )
                    }"
                )
                .setContentText(elapsedTime.formatted())
                .build()

            notificationManager.notify(1, notification)
        }.launchIn(serviceScope)
    }

    private fun stop() {
        stopSelf()
        isServiceActive = false
        _runWithObstacles.value = null
        serviceScope.cancel()

        serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "${_runWithObstacles.value?.title} ${_runWithObstacles.value?.distanceInKilometers} KM + ${
                    ObstaclesFormatter.getObstacleDescription(
                        obstaclesCount = _runWithObstacles.value?.obstacleCount ?: 0,
                        context = applicationContext
                    )
                }",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {

        var isServiceActive = false
        private val _runWithObstacles = MutableStateFlow<RunWithObstaclesModel?>(null)
        val runWithObstacles = _runWithObstacles.asStateFlow()

        private const val CHANNEL_ID = "active_run"

        private const val ACTION_START = "ACTION_START"
        private const val ACTION_STOP = "ACTION_STOP"

        private const val EXTRA_ACTIVITY_CLASS = "EXTRA_ACTIVITY_CLASS"
        private const val EXTRA_RUN_ID = "EXTRA_RUN_ID"

        fun createStartIntent(context: Context, activityClass: Class<*>, runId: String): Intent {
            return Intent(context, ActiveRunService::class.java).apply {
                action = ACTION_START
                putExtra(EXTRA_ACTIVITY_CLASS, activityClass.name)
                putExtra(EXTRA_RUN_ID, runId)
            }
        }

        fun createStopIntent(context: Context): Intent {
            return Intent(context, ActiveRunService::class.java).apply {
                action = ACTION_STOP
            }
        }
    }
}