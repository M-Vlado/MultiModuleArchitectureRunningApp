package sk.vmproject.run.presentation.main_runs.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.core.presentation.designsystem.VirtualOcrAzure
import sk.vmproject.core.presentation.designsystem.VirtualOcrAzure10
import sk.vmproject.core.presentation.designsystem.components.GradientBackground
import sk.vmproject.run.presentation.components.SelectCardImage

@Composable
fun RunCard(
    id: Long,
    title: String,
    distance: Int,
    obstacleCount: Int,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit
) {

    val context = LocalContext.current

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
                onClick(id)
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SelectCardImage(
                title = title,
                modifier = Modifier
                    .weight(1f)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(2f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    letterSpacing = TextUnit(8f, TextUnitType.Sp)
                )
                Text(
                    text = "$distance KM",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic
                )
                Text(
                    text = sk.vmproject.core.presentation.designsystem.components.utils.ObstaclesFormatter.getObstacleDescription(
                        obstaclesCount = obstacleCount,
                        context = context
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}


@Preview
@Composable
private fun RunCardPreview() {
    VirtualOCRTheme {
        GradientBackground {
            Spacer(modifier = Modifier.height(150.dp))
            RunCard(
                id = 1L,
                title = "EASY",
                distance = 5,
                obstacleCount = 10,
                onClick = {},
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}