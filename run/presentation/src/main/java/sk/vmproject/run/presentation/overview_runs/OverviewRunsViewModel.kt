package sk.vmproject.run.presentation.overview_runs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import sk.vmproject.core.domain.run.repository.VirtualOcrRepository
import sk.vmproject.run.domain.RunLevelType

class OverviewRunsViewModel(
    private val virtualOcrRepository: VirtualOcrRepository
) : ViewModel() {

    var state by mutableStateOf(OverviewRunsState())
        private set

    init {
        getSortedFinishedRuns()
    }

    fun onAction(action: OverviewRunsAction) {
        when (action) {
            is OverviewRunsAction.OnTabFilterClick -> {
                getSortedFinishedRuns(runLevelType = action.levelType)
            }

            OverviewRunsAction.OnSwipePage -> {
                state = state.copy(
                    isLoadingRuns = true
                )
            }

            else -> Unit
        }
    }

    private fun getSortedFinishedRuns(runLevelType: RunLevelType? = null) {
        viewModelScope.launch {
            state = state.copy(isLoadingRuns = true)
            virtualOcrRepository.getAllFinishedRuns().map { finishedRunModel ->
                finishedRunModel
                    .filter {
                        if (runLevelType != null) {
                            runLevelType.levelType == it.levelType
                        } else true
                    }
            }.onEach {
                state = state.copy(
                    finishedRuns = it,
                    isLoadingRuns = false
                )
            }.launchIn(viewModelScope)

        }
    }


}