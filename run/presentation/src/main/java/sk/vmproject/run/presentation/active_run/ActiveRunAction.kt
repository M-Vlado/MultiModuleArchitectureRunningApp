package sk.vmproject.run.presentation.active_run

sealed interface ActiveRunAction {
    data object OnToggleRunClick : ActiveRunAction
    data object OnFinishRunClick : ActiveRunAction
    data object OnResumeRunClick : ActiveRunAction
    data object OnBackClick : ActiveRunAction
    data object OnAcceptPermission : ActiveRunAction
}