package sk.vmproject.core.database.entity.relations

import androidx.room.Embedded
import androidx.room.Relation
import sk.vmproject.core.database.entity.ObstacleForRunSetupEntity
import sk.vmproject.core.database.entity.TypeOfRunEntity
import sk.vmproject.core.database.entity.toObstacleForRunSetupModel
import sk.vmproject.core.domain.run.model.RunWithObstaclesModel

data class RunWithObstacles(
    @Embedded val run: TypeOfRunEntity,
    @Relation(
        parentColumn = "type_of_run_id",
        entityColumn = "type_of_run_id"
    )
    val obstacles: List<ObstacleForRunSetupEntity>
)

fun RunWithObstacles.toRunWithObstaclesModel(): RunWithObstaclesModel {
    return RunWithObstaclesModel(
        typeOfRunId = run.typeOfRunId,
        title = run.title,
        distanceInKilometers = run.distanceInKilometers,
        obstacleCount = run.obstacleCount,
        obstacles = obstacles.map { it.toObstacleForRunSetupModel() }
    )
}