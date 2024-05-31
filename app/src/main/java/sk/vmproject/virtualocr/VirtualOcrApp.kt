package sk.vmproject.virtualocr

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import sk.vmproject.core.data.di.coreDataModule
import sk.vmproject.core.database.di.databaseModule
import sk.vmproject.run.location.di.locationModule
import sk.vmproject.run.presentation.di.runPresentationModule
import sk.vmproject.virtualocr.di.appModule

class VirtualOcrApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@VirtualOcrApp)
            modules(
                appModule,
                databaseModule,
                coreDataModule,
                runPresentationModule,
                locationModule,
            )
        }
    }
}