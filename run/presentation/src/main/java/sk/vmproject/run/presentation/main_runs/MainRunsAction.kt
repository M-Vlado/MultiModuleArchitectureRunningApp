package sk.vmproject.run.presentation.main_runs

sealed interface MainRunsAction {
    data class OnRunClick(val runId: Long) : MainRunsAction
    data object OnMenuItemClick : MainRunsAction

}