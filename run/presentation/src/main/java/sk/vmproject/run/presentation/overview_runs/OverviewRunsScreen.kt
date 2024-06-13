package sk.vmproject.run.presentation.overview_runs

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import sk.vmproject.core.domain.run.model.FinishedRunModel
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.core.presentation.designsystem.components.VirtualOcrScaffold
import sk.vmproject.core.presentation.designsystem.components.VirtualOcrToolbar
import sk.vmproject.run.domain.RunLevelType
import sk.vmproject.run.presentation.R
import sk.vmproject.run.presentation.overview_runs.components.FinishedRunItem
import sk.vmproject.run.presentation.overview_runs.components.TabItem
import sk.vmproject.run.presentation.overview_runs.components.TabItemData
import java.time.ZonedDateTime
import kotlin.time.Duration.Companion.minutes

@Composable
fun OverviewRunsScreenRoot(
    viewModel: OverviewRunsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onRunItemClick: (finishedRunId: Long) -> Unit
) {

    OverviewRunsScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                OverviewRunsAction.OnBackClick -> {
                    onBackClick()
                }

                is OverviewRunsAction.OnRunItemClick -> {}
                else -> viewModel.onAction(action)
            }
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OverviewRunsScreen(
    state: OverviewRunsState,
    onAction: (OverviewRunsAction) -> Unit
) {
    val tabItems = listOf(
        TabItemData(
            title = "ALL",
            image = sk.vmproject.core.presentation.designsystem.R.drawable.runner_red
        ),
        TabItemData(
            title = RunLevelType.EASY.levelType,
            image = sk.vmproject.core.presentation.designsystem.R.drawable.runner_red
        ),
        TabItemData(
            title = RunLevelType.MEDIUM.levelType,
            image = sk.vmproject.core.presentation.designsystem.R.drawable.runner_blue
        ),
        TabItemData(
            title = RunLevelType.HARD.levelType,
            image = sk.vmproject.core.presentation.designsystem.R.drawable.runner_green
        ),
    )

    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }

    val startFiltering = remember {
        derivedStateOf { selectedTabIndex }
    }
    val pagerState = rememberPagerState {
        tabItems.size
    }
    LaunchedEffect(key1 = startFiltering.value) {
        pagerState.animateScrollToPage(selectedTabIndex)
        onAction(
            OverviewRunsAction.OnTabFilterClick(
                when (selectedTabIndex) {
                    1 -> RunLevelType.EASY
                    2 -> RunLevelType.MEDIUM
                    3 -> RunLevelType.HARD
                    else -> null
                }
            )
        )
    }

    LaunchedEffect(key1 = pagerState.currentPage, key2 = pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            selectedTabIndex = pagerState.currentPage
            onAction(
                OverviewRunsAction.OnTabFilterClick(
                    when (selectedTabIndex) {
                        1 -> RunLevelType.EASY
                        2 -> RunLevelType.MEDIUM
                        3 -> RunLevelType.HARD
                        else -> null
                    }
                )
            )
        } else {
            onAction(OverviewRunsAction.OnSwipePage)
        }
    }

    VirtualOcrScaffold(
        topAppBar = {
            VirtualOcrToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.running_history),
                onBackClick = {
                    onAction(OverviewRunsAction.OnBackClick)
                }
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(paddingValues)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent
            ) {
                tabItems.forEachIndexed { index, tabItemData ->
                    Tab(
                        selected = index == selectedTabIndex,
                        onClick = {
                            selectedTabIndex = index
                        },
                        interactionSource = MutableInteractionSource()
                    ) {
                        TabItem(
                            title = tabItemData.title,
                            isSelected = selectedTabIndex == index,
                            image = tabItemData.image
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (state.finishedRuns.isEmpty() && !state.isLoadingRuns) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_finished_runs_to_display),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    if (state.isLoadingRuns) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            items(
                                items = state.finishedRuns,
                                key = { it.finishedRunId!! }
                            ) { finishedRunModel ->
                                FinishedRunItem(
                                    finishedRunModel = finishedRunModel,
                                    onClick = {}
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun OverviewRunsScreenPreview() {
    VirtualOCRTheme {
        OverviewRunsScreen(
            state = OverviewRunsState(
                finishedRuns = listOf(
                    FinishedRunModel(
                        finishedRunId = 1L,
                        levelType = RunLevelType.EASY.levelType,
                        obstacleCount = 10,
                        duration = 48.minutes,
                        distanceInMeters = 5421,
                        dateTimeUtc = ZonedDateTime.now(),
                        latitude = 48.825945,
                        longitude = 17.296890,
                        avgSpeedInKmh = 12.5,
                        maxSpeedInKmh = 16.8,
                        totalElevationInMeters = 157
                    ),
                    FinishedRunModel(
                        finishedRunId = 2L,
                        levelType = RunLevelType.HARD.levelType,
                        obstacleCount = 25,
                        duration = 248.minutes,
                        distanceInMeters = 22421,
                        dateTimeUtc = ZonedDateTime.now(),
                        latitude = 48.825945,
                        longitude = 17.296890,
                        avgSpeedInKmh = 12.5,
                        maxSpeedInKmh = 16.8,
                        totalElevationInMeters = 542
                    ),
                    FinishedRunModel(
                        finishedRunId = 3L,
                        levelType = RunLevelType.MEDIUM.levelType,
                        obstacleCount = 15,
                        duration = 148.minutes,
                        distanceInMeters = 11421,
                        dateTimeUtc = ZonedDateTime.now(),
                        latitude = 48.825945,
                        longitude = 17.296890,
                        avgSpeedInKmh = 12.5,
                        maxSpeedInKmh = 16.8,
                        totalElevationInMeters = 357
                    )
                )
            ),
            onAction = {}
        )
    }
}