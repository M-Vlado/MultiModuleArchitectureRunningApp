package sk.vmproject.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import sk.vmproject.core.domain.run.model.FinishedRunModel

@Entity(tableName = "finished_run")
data class FinishedRunEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "finished_run_id") val finishedRunId: Long,
    @ColumnInfo(name = "duration_in_millis") val durationInMillis: Long,
    @ColumnInfo(name = "distance_in_meters") val distanceInMeters: Int,
    @ColumnInfo(name = "date_time_utc") val dateTimeUtc: String,
    val latitude: Double,
    val longitude: Double,
    @ColumnInfo(name = "avg_speed_in_kmh") val avgSpeedInKmh: Double,
    @ColumnInfo(name = "max_speed_in_kmh") val maxSpeedInKmh: Double,
    @ColumnInfo(name = "total_elevation_in_meters") val totalElevationInMeters: Int,
)

fun FinishedRunModel.toFinishedRunEntity(): FinishedRunEntity {
    return FinishedRunEntity(
        finishedRunId = 0,
        durationInMillis = this.duration.inWholeMilliseconds,
        distanceInMeters = this.distanceInMeters,
        dateTimeUtc = this.dateTimeUtc.toInstant().toString(),
        latitude = this.latitude,
        longitude = this.longitude,
        avgSpeedInKmh = this.avgSpeedInKmh,
        maxSpeedInKmh = this.maxSpeedInKmh,
        totalElevationInMeters = this.totalElevationInMeters
    )
}