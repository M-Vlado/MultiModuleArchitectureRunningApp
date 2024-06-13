package sk.vmproject.run.presentation.overview_runs.components

import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.vmproject.core.domain.run.model.FinishedRunModel
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.core.presentation.designsystem.VirtualOcrAzure
import sk.vmproject.core.presentation.designsystem.VirtualOcrAzure10
import sk.vmproject.core.presentation.designsystem.components.GradientBackground
import sk.vmproject.core.presentation.designsystem.components.utils.ObstaclesFormatter
import sk.vmproject.run.presentation.components.SelectCardImage
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.minutes

@Composable
fun FinishedRunItem(
    finishedRunModel: FinishedRunModel,
    onClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    var placeOfRun by remember {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = true) {
        if (Build.VERSION.SDK_INT >= 33) {
            Geocoder(context).getFromLocation(
                finishedRunModel.latitude, finishedRunModel.longitude, 1
            ) { addresses ->
                val locality = addresses[0]?.locality
                val subLocality = addresses[0]?.subLocality
                placeOfRun = "$locality, \n$subLocality"
            }
        } else {
            val addresses = Geocoder(context).getFromLocation(
                finishedRunModel.latitude,
                finishedRunModel.longitude,
                1
            )
            val locality = addresses?.get(0)?.locality
            val subLocality = addresses?.get(0)?.subLocality
            placeOfRun = "$locality, \n$subLocality"
        }

    }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    val screenWidthPx = with(density) {
        configuration.screenWidthDp.dp.roundToPx()
    }

    val smallDimension = minOf(
        configuration.screenWidthDp.dp,
        configuration.screenHeightDp.dp
    )
    val smallDimensionPx = with(density) {
        smallDimension.roundToPx()
    }
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.radialGradient(
                    listOf(
                        MaterialTheme.colorScheme.onPrimary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.onPrimary,
                    ),
                    center = Offset(
                        x = screenWidthPx / 8f,
                        y = 80f
                    ),
                    radius = smallDimensionPx / 3.5f
                )
            )
            .background(
                brush = Brush.linearGradient(
                    listOf(
                        MaterialTheme.colorScheme.primary,
                        VirtualOcrAzure10
                    ),
                    start = Offset(x = 100f, y = 0f)
                ),
                alpha = 0.2f
            )
            .border(
                width = 1.dp,
                color = VirtualOcrAzure,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .clickable {
                onClick(finishedRunModel.finishedRunId!!)
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SelectCardImage(
                title = finishedRunModel.levelType,
                modifier = Modifier
                    .size(80.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                Text(
                    text = finishedRunModel.levelType,
                    style = MaterialTheme.typography.headlineMedium,
                    letterSpacing = TextUnit(8f, TextUnitType.Sp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${ObstaclesFormatter.getDistanceInKilometersText(finishedRunModel.distanceInMeters.toLong())} + ${
                        ObstaclesFormatter.getObstacleDescription(
                            finishedRunModel.obstacleCount,
                            context
                        )
                    }",
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = placeOfRun,
                color = MaterialTheme.colorScheme.onSurface,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp,
                maxLines = 2,
                lineHeight = TextUnit(12f, type = TextUnitType.Sp),
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = DateTimeFormatter
                    .ofPattern("dd. MMM, yyyy")
                    .format(finishedRunModel.dateTimeUtc),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .weight(1f)
            )
        }

    }


}

@Preview
@Composable
private fun FinishedRunItemPreview() {
    VirtualOCRTheme {
        GradientBackground {
            Spacer(modifier = Modifier.height(100.dp))
            FinishedRunItem(
                finishedRunModel = FinishedRunModel(
                    finishedRunId = 1L,
                    levelType = "EASY",
                    obstacleCount = 10,
                    duration = 42.minutes,
                    distanceInMeters = 5421,
                    dateTimeUtc = ZonedDateTime.now(),
                    latitude = 48.826451,
                    longitude = 17.297512,
                    avgSpeedInKmh = 12.35,
                    maxSpeedInKmh = 16.25,
                    totalElevationInMeters = 365
                ),
                onClick = {}
            )
        }

    }
}