package sk.vmproject.core.domain.run.model

import java.time.ZonedDateTime
import kotlin.time.Duration

data class FinishedRunModel(
    val finishedRunId: Long?,
    val duration: Duration,
    val distanceInMeters: Int,
    val dateTimeUtc: ZonedDateTime,
    val latitude: Double,
    val longitude: Double,
    val avgSpeedInKmh: Double,
    val maxSpeedInKmh: Double,
    val totalElevationInMeters: Int,
)
