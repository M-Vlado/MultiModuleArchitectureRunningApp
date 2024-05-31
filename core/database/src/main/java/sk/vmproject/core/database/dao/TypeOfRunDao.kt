package sk.vmproject.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import sk.vmproject.core.database.entity.TypeOfRunEntity
import sk.vmproject.core.database.entity.relations.RunWithObstacles

@Dao
interface TypeOfRunDao {

    @Query("SELECT * FROM type_or_run")
    suspend fun getAllTypeOfRuns(): List<TypeOfRunEntity>

    @Transaction
    @Query("SELECT * FROM type_or_run WHERE type_of_run_id= :typeOfRunId")
    suspend fun getTypeOfRunWithObstacles(typeOfRunId: Long): List<RunWithObstacles>

}