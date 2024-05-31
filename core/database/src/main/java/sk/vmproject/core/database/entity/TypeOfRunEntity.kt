package sk.vmproject.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import sk.vmproject.core.domain.run.model.TypeOfRunModel

@Entity(tableName = "type_or_run")
data class TypeOfRunEntity(
    @PrimaryKey
    @ColumnInfo(name = "type_of_run_id") val typeOfRunId: Long,
    val title: String,
    @ColumnInfo(name = "distance_in_kilometers") val distanceInKilometers: Int,
    @ColumnInfo(name = "obstacle_count") val obstacleCount: Int,
)


fun TypeOfRunEntity.toTypeOfRunModel(): TypeOfRunModel {
    return TypeOfRunModel(
        typeOfRunId = typeOfRunId,
        title = title,
        distanceInKilometers = distanceInKilometers,
        obstacleCount = obstacleCount
    )
}