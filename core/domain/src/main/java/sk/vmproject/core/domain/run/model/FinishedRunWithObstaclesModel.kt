package sk.vmproject.core.domain.run.model

import java.time.ZonedDateTime
import kotlin.time.Duration

data class FinishedRunWithObstaclesModel(
    val finishedRunId: Long?,
    val levelType: String,
    val obstacleCount: Int,
    val duration: Duration,
    val distanceInMeters: Int,
    val dateTimeUtc: ZonedDateTime,
    val latitude: Double,
    val longitude: Double,
    val avgSpeedInKmh: Double,
    val maxSpeedInKmh: Double,
    val totalElevationInMeters: Int,
    val obstacles: List<ObstacleModel>,
)
