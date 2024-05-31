package sk.vmproject.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import sk.vmproject.core.domain.run.model.ObstacleModel

@Entity(tableName = "obstacle_overcome")
data class ObstacleOvercomeEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "obstacle_overcome_id") val obstacleOvercomeId: Long,
    @ColumnInfo(name = "ordinal_number") val ordinalNumber: Int,
    @ColumnInfo(name = "obstacle_type") val obstacleType: String,
    @ColumnInfo(name = "number_of_reps") val numberOfReps: Int,
    @ColumnInfo(name = "distance_from_start_in_meters") val distanceFromStartInMeters: Long,
    val latitude: Double?,
    val longitude: Double?,
    @ColumnInfo(name = "obstacle_reach_in_time_in_millis") val obstacleReachTimeInMillis: Long?,
    @ColumnInfo(name = "finished_run_id") val finishedRunId: Long,
)

fun ObstacleModel.toObstacleOvercomeEntity(belongToRunId: Long): ObstacleOvercomeEntity {
    return ObstacleOvercomeEntity(
        obstacleOvercomeId = 0,
        ordinalNumber = this.ordinalNumber,
        obstacleType = this.obstacleType,
        numberOfReps = this.numberOfReps,
        distanceFromStartInMeters = this.distanceFromStartInMeters,
        latitude = this.latitude,
        longitude = this.longitude,
        obstacleReachTimeInMillis = this.obstacleReachTimeStamp?.inWholeMilliseconds,
        finishedRunId = belongToRunId
    )
}
