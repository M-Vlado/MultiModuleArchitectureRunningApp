package sk.vmproject.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import sk.vmproject.core.database.entity.FinishedRunEntity
import sk.vmproject.core.database.entity.ObstacleOvercomeEntity
import sk.vmproject.core.database.entity.relations.FinishedRunWithObstacles

@Dao
interface FinishedRunDao {

    @Upsert
    suspend fun upsertFinishedRun(finishedRunEntity: FinishedRunEntity): Long

    @Upsert
    suspend fun upsertObstacleSOvercome(obstaclesOvercomeEntities: List<ObstacleOvercomeEntity>): List<Long>

    @Query("SELECT * FROM finished_run")
    fun getAllFinishedRuns(): Flow<List<FinishedRunEntity>>

    @Transaction
    @Query("SELECT * FROM finished_run WHERE finished_run_id= :finishedRunId")
    fun getFinishedRunWithObstacles(finishedRunId: Long): Flow<List<FinishedRunWithObstacles>>
}