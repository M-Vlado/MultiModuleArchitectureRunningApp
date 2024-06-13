package sk.vmproject.run.presentation.active_run

import sk.vmproject.core.domain.location.Location
import sk.vmproject.core.domain.run.model.RunWithObstaclesModel
import sk.vmproject.run.domain.RunData
import kotlin.time.Duration

data class ActiveRunState(
    val elapsedTime: Duration = Duration.ZERO,
    val runData: RunData = RunData(),
    val runWithObstacles: RunWithObstaclesModel? = null,
    val shouldTrack: Boolean = false,
    val hasStartedRunning: Boolean = false,
    val currentLocation: Location? = null,
    val isSavingRun: Boolean = false,
    val showLocationRationale: Boolean = false,
    val showNotificationRationale: Boolean = false,
    val countDownLabel: String? = null,
    val isCountDownActive: Boolean = false,
    val showTTSInitErrorDialog: Boolean? = null,
    val showTTSLanguageErrorDialog: Boolean? = null,
)
