package sk.vmproject.run.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import sk.vmproject.run.domain.RunningTracker
import sk.vmproject.run.presentation.active_run.ActiveRunViewModel
import sk.vmproject.run.presentation.active_run.RequestPermissionsViewModel
import sk.vmproject.run.presentation.main_runs.MainRunsViewModel

val runPresentationModule = module {
    singleOf(::RunningTracker)
    viewModelOf(::MainRunsViewModel)
    viewModelOf(::ActiveRunViewModel)
    viewModelOf(::RequestPermissionsViewModel)
}