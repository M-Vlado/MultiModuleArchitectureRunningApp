package sk.vmproject.run.location.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import sk.vmproject.run.domain.LocationObserver
import sk.vmproject.run.location.AndroidLocationObserver

val locationModule = module {
    singleOf(::AndroidLocationObserver).bind<LocationObserver>()
}