package sk.vmproject.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import sk.vmproject.core.domain.run.model.ObstacleModel

@Entity(tableName = "obstacle_for_run_setup")
data class ObstacleForRunSetupEntity(
    @PrimaryKey
    @ColumnInfo(name = "obstacle_id") val obstacleId: Long,
    @ColumnInfo(name = "ordinal_number") val ordinalNumber: Int,
    @ColumnInfo(name = "obstacle_type") val obstacleType: String,
    @ColumnInfo(name = "number_of_reps") val numberOfReps: Int,
    @ColumnInfo(name = "distance_from_start_in_meters") val distanceFromStartInMeters: Long,
    @ColumnInfo(name = "type_of_run_id") val typeOfRunId: Long
)

fun ObstacleForRunSetupEntity.toObstacleForRunSetupModel(): ObstacleModel {
    return ObstacleModel(
        obstacleId = obstacleId,
        ordinalNumber = ordinalNumber,
        obstacleType = obstacleType,
        numberOfReps = numberOfReps,
        distanceFromStartInMeters = distanceFromStartInMeters,
        latitude = null,
        longitude = null,
        obstacleReachTimeStamp = null
    )
}