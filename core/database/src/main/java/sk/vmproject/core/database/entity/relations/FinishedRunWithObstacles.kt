package sk.vmproject.core.database.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import sk.vmproject.core.database.entity.FinishedRunEntity
import sk.vmproject.core.database.entity.ObstacleOvercomeEntity
import sk.vmproject.core.database.entity.toObstacleModel
import sk.vmproject.core.domain.run.model.FinishedRunWithObstaclesModel
import java.time.Instant
import java.time.ZoneId
import kotlin.time.Duration.Companion.milliseconds

data class FinishedRunWithObstacles(
    @Embedded val finishedRun: FinishedRunEntity,
    @Relation(
        parentColumn = "finished_run_id",
        entityColumn = "finished_run_id"
    )
    val obstacles: List<ObstacleOvercomeEntity>
)

fun FinishedRunWithObstacles.toFinishedRunWithObstaclesModel(): FinishedRunWithObstaclesModel {
    return FinishedRunWithObstaclesModel(
        finishedRunId = this.finishedRun.finishedRunId,
        levelType = this.finishedRun.levelType,
        obstacleCount = this.finishedRun.obstacleCount,
        duration = this.finishedRun.durationInMillis.milliseconds,
        distanceInMeters = this.finishedRun.distanceInMeters,
        dateTimeUtc = Instant.parse(this.finishedRun.dateTimeUtc)
            .atZone(ZoneId.of("UTC")),
        latitude = this.finishedRun.latitude,
        longitude = this.finishedRun.longitude,
        avgSpeedInKmh = this.finishedRun.avgSpeedInKmh,
        maxSpeedInKmh = this.finishedRun.maxSpeedInKmh,
        totalElevationInMeters = this.finishedRun.totalElevationInMeters,
        obstacles = obstacles.map { it.toObstacleModel() }
    )
}