package sk.vmproject.core.domain.run.model

import kotlin.time.Duration

data class ObstacleModel(
    val obstacleId: Long,
    val ordinalNumber: Int,
    val obstacleType: String,
    val numberOfReps: Int,
    val distanceFromStartInMeters: Long,
    val latitude: Double?,
    val longitude: Double?,
    val obstacleReachTimeStamp: Duration?,
    val wasIntroduced: Boolean = false,
)
