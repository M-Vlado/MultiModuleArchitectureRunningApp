package sk.vmproject.run.presentation.main_runs

import sk.vmproject.core.domain.run.model.TypeOfRunModel

data class MainRunsState(
    val runTypes: List<TypeOfRunModel> = emptyList(),
    val isLoadingRuns: Boolean = true
)
