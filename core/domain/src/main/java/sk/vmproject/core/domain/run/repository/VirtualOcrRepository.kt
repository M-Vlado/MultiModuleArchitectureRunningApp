package sk.vmproject.core.domain.run.repository

import kotlinx.coroutines.flow.Flow
import sk.vmproject.core.domain.run.model.FinishedRunModel
import sk.vmproject.core.domain.run.model.FinishedRunWithObstaclesModel
import sk.vmproject.core.domain.run.model.ObstacleModel
import sk.vmproject.core.domain.run.model.RunWithObstaclesModel
import sk.vmproject.core.domain.run.model.TypeOfRunModel
import sk.vmproject.core.domain.util.DataError
import sk.vmproject.core.domain.util.Result

typealias FinishedRunId = Long
typealias ObstacleId = Long

interface VirtualOcrRepository {

    suspend fun getAllTypeOfRuns(): List<TypeOfRunModel>

    suspend fun getTypeOfRunWithObstacles(typeOfRunId: Long): List<RunWithObstaclesModel>

    suspend fun upsertFinishedRun(finishedRunModel: FinishedRunModel): Result<FinishedRunId, DataError.Local>

    suspend fun upsertOvercomeObstacles(
        obstacles: List<ObstacleModel>,
        belongToRunId: Long
    ): Result<List<ObstacleId>, DataError.Local>

    fun getAllFinishedRuns(): Flow<List<FinishedRunModel>>

    fun getFinishedRunWithObstacles(finishedRunId: Long): Flow<List<FinishedRunWithObstaclesModel>>
}