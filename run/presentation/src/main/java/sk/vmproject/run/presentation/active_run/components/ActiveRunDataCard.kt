package sk.vmproject.run.presentation.active_run.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.vmproject.core.domain.run.model.ObstacleModel
import sk.vmproject.core.domain.run.model.RunWithObstaclesModel
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.core.presentation.designsystem.components.utils.ObstaclesFormatter
import sk.vmproject.core.presentation.ui.formatted
import sk.vmproject.core.presentation.ui.toFormattedKm
import sk.vmproject.run.domain.RunData
import sk.vmproject.run.presentation.R
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

@Composable
fun ActiveRunDataCard(
    elapsedTime: Duration,
    runData: RunData,
    runWithObstacles: RunWithObstaclesModel?,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "${runWithObstacles?.title?.uppercase() ?: ""} ${runWithObstacles?.distanceInKilometers} KM + ${
                    ObstaclesFormatter.getObstacleDescription(
                        runWithObstacles?.obstacleCount ?: 0,
                        context
                    )
                }"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                RunDataItem(
                    title = stringResource(id = R.string.distance),
                    value = (runData.distanceMeters / 1000.0).toFormattedKm(),
                    modifier = Modifier
                        .defaultMinSize(minWidth = 75.dp)
                )
                RunDataItem(
                    title = stringResource(id = R.string.duration),
                    value = elapsedTime.formatted(),
                    valueFontSize = 20.sp,
                    modifier = Modifier
                        .defaultMinSize(90.dp)
                )
                RunDataItem(
                    title = stringResource(id = R.string.obstacles_run_data_card),
                    value = "${runWithObstacles?.obstacles?.count { it.obstacleReachTimeStamp != null } ?: 0} / ${runWithObstacles?.obstacleCount ?: 0}",
                    modifier = Modifier
                        .defaultMinSize(minWidth = 75.dp)
                )
            }
        }

    }
}

@Composable
private fun RunDataItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueFontSize: TextUnit = 12.sp
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = valueFontSize
        )
    }
}

@Preview
@Composable
private fun RunDataCardPreview() {
    VirtualOCRTheme {
        ActiveRunDataCard(
            elapsedTime = 128.minutes,
            runData = RunData(
                distanceMeters = 2312,
                pace = 4.minutes,
            ),
            runWithObstacles = RunWithObstaclesModel(
                typeOfRunId = 3L,
                title = "Medium",
                distanceInKilometers = 10,
                obstacleCount = 20,
                obstacles = listOf(
                    ObstacleModel(
                        obstacleId = 1L,
                        ordinalNumber = 1,
                        obstacleType = "burpee",
                        numberOfReps = 5,
                        distanceFromStartInMeters = 750,
                        null,
                        null,
                        obstacleReachTimeStamp = 1.minutes
                    )
                )
            )
        )
    }
}