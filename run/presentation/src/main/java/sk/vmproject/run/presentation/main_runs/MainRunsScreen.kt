package sk.vmproject.run.presentation.main_runs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import sk.vmproject.core.presentation.designsystem.HistoryIcon
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.core.presentation.designsystem.components.VirtualOcrScaffold
import sk.vmproject.core.presentation.designsystem.components.VirtualOcrToolbar
import sk.vmproject.core.presentation.designsystem.components.utils.DropDownItem
import sk.vmproject.run.presentation.R
import sk.vmproject.run.presentation.main_runs.components.RunCard

@Composable
fun MainRunsScreenRoot(
    onMenuItemClick: () -> Unit,
    onRunSelected: (runId: Long) -> Unit,
    viewModel: MainRunsViewModel = koinViewModel()
) {
    MainRunsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                MainRunsAction.OnMenuItemClick -> {}
                is MainRunsAction.OnRunClick -> {
                    onRunSelected(action.runId)
                }
            }
        }
    )
}

@Composable
private fun MainRunsScreen(
    state: MainRunsState,
    onAction: (MainRunsAction) -> Unit
) {

    VirtualOcrScaffold(
        topAppBar = {
            VirtualOcrToolbar(
                showBackButton = false,
                title = stringResource(R.string.virtual_ocr),
                startContent = {
                    Image(
                        painter = painterResource(id = sk.vmproject.core.presentation.designsystem.R.drawable.virtual_ocr_logo_transparent),
                        contentDescription = stringResource(
                            id = R.string.logo
                        ),
                        modifier = Modifier
                            .size(60.dp)
                    )
                },
                menuItems = listOf(
                    DropDownItem(
                        title = stringResource(R.string.running_history),
                        icon = HistoryIcon
                    )
                ),
                onMenuItemClick = {

                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                state.runTypes,
                key = {
                    it.typeOfRunId
                }
            ) {
                RunCard(
                    id = it.typeOfRunId,
                    title = it.title,
                    distance = it.distanceInKilometers,
                    obstacleCount = it.obstacleCount,
                    onClick = { runId ->
                        onAction(MainRunsAction.OnRunClick(runId))
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun MainRunsScreenPreview() {
    VirtualOCRTheme {
        MainRunsScreen(
            state = MainRunsState(),
            onAction = {}
        )
    }
}