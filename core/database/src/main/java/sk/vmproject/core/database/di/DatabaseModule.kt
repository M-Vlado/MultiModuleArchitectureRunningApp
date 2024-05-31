package sk.vmproject.core.database.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import sk.vmproject.core.database.VirtualOcrDatabase

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            VirtualOcrDatabase::class.java,
            "virtual_ocr.db"
        )
            .createFromAsset("database/init_virtual_ocr_db.db")
            .build()
    }

    single { get<VirtualOcrDatabase>().typeOfRunDao }
    single { get<VirtualOcrDatabase>().finishedRunDao }
}