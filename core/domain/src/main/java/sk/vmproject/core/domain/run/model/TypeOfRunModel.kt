package sk.vmproject.core.domain.run.model

data class TypeOfRunModel(
    val typeOfRunId: Long,
    val title: String,
    val distanceInKilometers: Int,
    val obstacleCount: Int,
)
