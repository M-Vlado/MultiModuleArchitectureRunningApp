package sk.vmproject.run.presentation.main_runs

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sk.vmproject.core.domain.run.repository.VirtualOcrRepository

class MainRunsViewModel(
    private val repository: VirtualOcrRepository
) : ViewModel() {

    var state by mutableStateOf(MainRunsState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isLoadingRuns = true)
            state = state.copy(runTypes = repository.getAllTypeOfRuns())
            state = state.copy(isLoadingRuns = false)
        }
    }

}