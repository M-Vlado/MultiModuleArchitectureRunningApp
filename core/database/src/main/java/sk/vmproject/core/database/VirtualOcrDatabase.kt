package sk.vmproject.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import sk.vmproject.core.database.dao.FinishedRunDao
import sk.vmproject.core.database.dao.TypeOfRunDao
import sk.vmproject.core.database.entity.FinishedRunEntity
import sk.vmproject.core.database.entity.ObstacleForRunSetupEntity
import sk.vmproject.core.database.entity.ObstacleOvercomeEntity
import sk.vmproject.core.database.entity.TypeOfRunEntity

@Database(
    entities = [
        TypeOfRunEntity::class,
        ObstacleForRunSetupEntity::class,
        FinishedRunEntity::class,
        ObstacleOvercomeEntity::class
    ],
    version = 1
)
abstract class VirtualOcrDatabase : RoomDatabase() {

    abstract val typeOfRunDao: TypeOfRunDao
    abstract val finishedRunDao: FinishedRunDao

}