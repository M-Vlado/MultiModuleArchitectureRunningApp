package sk.vmproject.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
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
    suspend fun getAllFinishedRuns(): List<FinishedRunEntity>

    @Transaction
    @Query("SELECT * FROM finished_run WHERE finished_run_id= :finishedRunId")
    suspend fun getFinishedRunWithObstacles(finishedRunId: Long): List<FinishedRunWithObstacles>
}