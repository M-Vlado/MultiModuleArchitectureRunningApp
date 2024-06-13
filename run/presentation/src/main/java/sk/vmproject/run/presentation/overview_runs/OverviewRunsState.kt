package sk.vmproject.run.presentation.overview_runs

import sk.vmproject.core.domain.run.model.FinishedRunModel

data class OverviewRunsState(
    val finishedRuns: List<FinishedRunModel> = emptyList(),
    val isLoadingRuns: Boolean = false
)
