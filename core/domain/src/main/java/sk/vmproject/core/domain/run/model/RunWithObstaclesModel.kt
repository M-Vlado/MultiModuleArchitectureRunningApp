package sk.vmproject.core.domain.run.model

data class RunWithObstaclesModel(
    val typeOfRunId: Long,
    val title: String,
    val distanceInKilometers: Int,
    val obstacleCount: Int,
    val obstacles: List<ObstacleModel>
)
