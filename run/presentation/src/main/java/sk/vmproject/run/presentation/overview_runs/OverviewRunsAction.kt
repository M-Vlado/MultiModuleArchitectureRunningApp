package sk.vmproject.run.presentation.overview_runs

import sk.vmproject.run.domain.RunLevelType


sealed interface OverviewRunsAction {
    data class OnRunItemClick(val finishedRunId: Long) : OverviewRunsAction
    data class OnTabFilterClick(val levelType: RunLevelType?) : OverviewRunsAction
    data object OnSwipePage : OverviewRunsAction
    data object OnBackClick : OverviewRunsAction
}