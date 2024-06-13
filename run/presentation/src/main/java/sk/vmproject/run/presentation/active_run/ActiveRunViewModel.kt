package sk.vmproject.run.presentation.active_run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import sk.vmproject.core.domain.run.model.FinishedRunModel
import sk.vmproject.core.domain.run.repository.VirtualOcrRepository
import sk.vmproject.core.domain.tts.TextToSpeechController
import sk.vmproject.core.domain.util.Result
import sk.vmproject.core.presentation.ui.asUiText
import sk.vmproject.run.domain.LocationDataCalculator
import sk.vmproject.run.domain.RunningTracker
import sk.vmproject.run.presentation.active_run.service.ActiveRunService
import java.time.ZoneId
import java.time.ZonedDateTime

class ActiveRunViewModel(
    private val runningTracker: RunningTracker,
    private val virtualOcrRepository: VirtualOcrRepository,
    savedStateHandle: SavedStateHandle,
    private val textToSpeechController: TextToSpeechController,
) : ViewModel() {

    private val runId = savedStateHandle.get<String>("run_id")

    var state by mutableStateOf(
        ActiveRunState(
            shouldTrack = ActiveRunService.isServiceActive && runningTracker.isTracking.value,
            hasStartedRunning = ActiveRunService.isServiceActive,
        )
    )
        private set

    private val eventChannel = Channel<ActiveRunEvent>()
    val events = eventChannel.receiveAsFlow()

    private val shouldTrack = snapshotFlow { state.shouldTrack }
        .stateIn(viewModelScope, SharingStarted.Lazily, state.shouldTrack)

    private val hasLocationPermission = MutableStateFlow(false)
    var hasPermissionState = hasLocationPermission.asStateFlow()

    private val isTracking = combine(
        shouldTrack,
        hasLocationPermission
    ) { shouldTrack, hasPermission ->
        shouldTrack && hasPermission
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val isTTSActive = combine(
        textToSpeechController.isInitializeSuccess,
        textToSpeechController.isEnglishLanguageAvailable
    ) { isTTSInitializeSuccess, isEnglishLanguageAvailable ->
        isTTSInitializeSuccess && isEnglishLanguageAvailable
    }.stateIn(viewModelScope, SharingStarted.Lazily, false)

    init {
        if (!ActiveRunService.isServiceActive) {
            viewModelScope.launch {
                try {
                    runId?.toLong()
                        ?.let {
                            val obstacles =
                                virtualOcrRepository.getTypeOfRunWithObstacles(typeOfRunId = it)
                            state = state.copy(
                                runWithObstacles = obstacles.firstOrNull()
                            )
                        }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }
        }

        ActiveRunService.runWithObstacles.onEach {
            state = state.copy(
                runWithObstacles = it
            )
        }.launchIn(viewModelScope)

        textToSpeechController.isInitializeSuccess.onEach {
            state = state.copy(
                showTTSInitErrorDialog = !it
            )
        }.launchIn(viewModelScope)

        textToSpeechController.isEnglishLanguageAvailable.onEach {
            state = state.copy(
                showTTSLanguageErrorDialog = !it
            )
        }

        hasLocationPermission
            .onEach { hasPermission ->
                if (hasPermission) {
                    runningTracker.startObservingLocation()
                } else {
                    runningTracker.stopObservingLocation()
                }
            }
            .launchIn(viewModelScope)

        isTracking
            .onEach { isTracking ->
                runningTracker.setIsTracking(isTracking = isTracking)
            }
            .launchIn(viewModelScope)

        runningTracker
            .currentLocation
            .onEach {
                state = state.copy(currentLocation = it?.location)
            }
            .launchIn(viewModelScope)

        runningTracker
            .runData
            .onEach { runData ->
                state = state.copy(runData = runData)
            }
            .launchIn(viewModelScope)

        runningTracker
            .elapsedTime
            .onEach {
                state = state.copy(elapsedTime = it)
            }
            .launchIn(viewModelScope)


    }

    fun onAction(action: ActiveRunAction) {
        when (action) {
            ActiveRunAction.OnAcceptPermission -> {
                hasLocationPermission.value = true
            }

            ActiveRunAction.OnBackClick -> {
                state = state.copy(
                    shouldTrack = false
                )
            }

            ActiveRunAction.OnFinishRunClick -> {
                state = state.copy(
                    hasStartedRunning = false,
                    isSavingRun = true
                )
                finishRun()
            }

            ActiveRunAction.OnResumeRunClick -> {
                state = state.copy(
                    shouldTrack = true
                )
            }

            ActiveRunAction.OnToggleRunClick -> {
                if (!state.hasStartedRunning) {
                    if (!state.isCountDownActive) {
                        viewModelScope.launch {
                            var countDown = 3
                            while (countDown > 0) {
                                state = state.copy(
                                    countDownLabel = countDown.toString(),
                                    isCountDownActive = true
                                )
                                textToSpeechController.startSpeech(countDown.toString())
                                delay(800)
                                state = state.copy(
                                    countDownLabel = null
                                )
                                delay(200)
                                countDown--
                            }
                            state = state.copy(
                                hasStartedRunning = true,
                                shouldTrack = !state.shouldTrack,
                                countDownLabel = "GO"
                            )
                            textToSpeechController.startSpeech("Go!")
                            delay(500)
                            state = state.copy(
                                countDownLabel = null,
                                isCountDownActive = false
                            )
                        }
                    }

                } else {
                    state = state.copy(
                        hasStartedRunning = true,
                        shouldTrack = !state.shouldTrack
                    )
                }
            }
        }
    }

    private fun finishRun() {
        val locations = state.runData.locations
        if (locations.isEmpty() || locations.first().size <= 1) {
            state = state.copy(isSavingRun = false)
            return
        }
        viewModelScope.launch {
            val finishedRun = FinishedRunModel(
                finishedRunId = null,
                levelType = state.runWithObstacles!!.title,
                obstacleCount = state.runWithObstacles!!.obstacleCount,
                duration = state.elapsedTime,
                distanceInMeters = state.runData.distanceMeters,
                dateTimeUtc = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC")),
                latitude = state.currentLocation?.lat ?: 0.0,
                longitude = state.currentLocation?.long ?: 0.0,
                avgSpeedInKmh = LocationDataCalculator.getAvgSpeedInKmh(locations = locations),
                maxSpeedInKmh = LocationDataCalculator.getMaxSpeedKmh(locations = locations),
                totalElevationInMeters = LocationDataCalculator.getTotalElevationMeters(locations = locations),
            )

            val overcomeObstacles = state.runWithObstacles!!.obstacles

            when (val upsertFinishedRunResult =
                virtualOcrRepository.upsertFinishedRun(finishedRunModel = finishedRun)) {
                is Result.Success -> {
                    when (val upsertOvercomeObstaclesResult =
                        virtualOcrRepository.upsertOvercomeObstacles(
                            obstacles = overcomeObstacles,
                            belongToRunId = upsertFinishedRunResult.data
                        )) {
                        is Result.Success -> {
                            eventChannel.send(ActiveRunEvent.RunSaved)
                        }

                        is Result.Error -> {
                            eventChannel.send(ActiveRunEvent.Error(upsertOvercomeObstaclesResult.error.asUiText()))
                        }
                    }
                }

                is Result.Error -> {
                    eventChannel.send(ActiveRunEvent.Error(upsertFinishedRunResult.error.asUiText()))
                }

            }

            runningTracker.finishRun()
            state = state.copy(
                isSavingRun = false
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!ActiveRunService.isServiceActive) {
            runningTracker.stopObservingLocation()
        }
    }
}