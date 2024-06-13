package sk.vmproject.core.data

import android.database.sqlite.SQLiteFullException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import sk.vmproject.core.database.dao.FinishedRunDao
import sk.vmproject.core.database.dao.TypeOfRunDao
import sk.vmproject.core.database.entity.relations.toFinishedRunWithObstaclesModel
import sk.vmproject.core.database.entity.relations.toRunWithObstaclesModel
import sk.vmproject.core.database.entity.toFinishedRunEntity
import sk.vmproject.core.database.entity.toFinishedRunModel
import sk.vmproject.core.database.entity.toObstacleOvercomeEntity
import sk.vmproject.core.database.entity.toTypeOfRunModel
import sk.vmproject.core.domain.run.model.FinishedRunModel
import sk.vmproject.core.domain.run.model.FinishedRunWithObstaclesModel
import sk.vmproject.core.domain.run.model.ObstacleModel
import sk.vmproject.core.domain.run.model.RunWithObstaclesModel
import sk.vmproject.core.domain.run.model.TypeOfRunModel
import sk.vmproject.core.domain.run.repository.FinishedRunId
import sk.vmproject.core.domain.run.repository.ObstacleId
import sk.vmproject.core.domain.run.repository.VirtualOcrRepository
import sk.vmproject.core.domain.util.DataError
import sk.vmproject.core.domain.util.Result

class VirtualOcrRepositoryImpl(
    private val typeOfRunDao: TypeOfRunDao,
    private val finishedRunDao: FinishedRunDao,
) : VirtualOcrRepository {
    override suspend fun getAllTypeOfRuns(): List<TypeOfRunModel> {
        return typeOfRunDao.getAllTypeOfRuns().map { it.toTypeOfRunModel() }
    }

    override suspend fun getTypeOfRunWithObstacles(typeOfRunId: Long): List<RunWithObstaclesModel> {
        return typeOfRunDao.getTypeOfRunWithObstacles(typeOfRunId = typeOfRunId)
            .map { it.toRunWithObstaclesModel() }
    }

    override suspend fun upsertFinishedRun(finishedRunModel: FinishedRunModel): Result<FinishedRunId, DataError.Local> {
        return try {
            val finishedRunId =
                finishedRunDao.upsertFinishedRun(finishedRunEntity = finishedRunModel.toFinishedRunEntity())
            Result.Success(finishedRunId)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertOvercomeObstacles(
        obstacles: List<ObstacleModel>,
        belongToRunId: Long
    ): Result<List<ObstacleId>, DataError.Local> {
        return try {
            val entities =
                obstacles.map { it.toObstacleOvercomeEntity(belongToRunId = belongToRunId) }
            val obstaclesId = finishedRunDao.upsertObstacleSOvercome(entities)
            Result.Success(obstaclesId)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override fun getAllFinishedRuns(): Flow<List<FinishedRunModel>> {
        return finishedRunDao.getAllFinishedRuns().map { listFinishedRunsEntity ->
            listFinishedRunsEntity.map { finishedRunEntity ->
                finishedRunEntity.toFinishedRunModel()
            }
        }
    }

    override fun getFinishedRunWithObstacles(finishedRunId: Long): Flow<List<FinishedRunWithObstaclesModel>> {
        return finishedRunDao.getFinishedRunWithObstacles(finishedRunId = finishedRunId)
            .map { listFinishedRunWithObstacles ->
                listFinishedRunWithObstacles.map { finishedRunWithObstacles ->
                    finishedRunWithObstacles.toFinishedRunWithObstaclesModel()
                }
            }
    }
}