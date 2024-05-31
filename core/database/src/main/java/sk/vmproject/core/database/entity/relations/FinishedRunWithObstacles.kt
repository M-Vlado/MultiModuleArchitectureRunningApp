package sk.vmproject.core.database.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import sk.vmproject.core.database.entity.FinishedRunEntity
import sk.vmproject.core.database.entity.ObstacleOvercomeEntity

data class FinishedRunWithObstacles(
    @Embedded val finishedRun: FinishedRunEntity,
    @Relation(
        parentColumn = "finished_run_id",
        entityColumn = "finished_run_id"
    )
    val obstacles: List<ObstacleOvercomeEntity>
)
