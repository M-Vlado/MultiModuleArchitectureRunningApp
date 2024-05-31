package sk.vmproject.virtualocr.di

import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import sk.vmproject.virtualocr.VirtualOcrApp

val appModule = module {

    single<CoroutineScope> {
        (androidApplication() as VirtualOcrApp).applicationScope
    }
}