package sk.vmproject.run.presentation.main_runs.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.core.presentation.designsystem.VirtualOcrAzure
import sk.vmproject.core.presentation.designsystem.VirtualOcrAzure30
import sk.vmproject.core.presentation.designsystem.components.GradientBackground

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

    OutlinedCard(
        onClick = { onClick(id) },
        colors = CardDefaults.outlinedCardColors(
            containerColor = VirtualOcrAzure30.copy(alpha = 0.15f),
        ),
        border = BorderStroke(1.dp, color = VirtualOcrAzure),
        modifier = modifier
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

@Composable
private fun SelectCardImage(title: String, modifier: Modifier = Modifier) {
    val image = when (title) {
        "EASY" -> sk.vmproject.core.presentation.designsystem.R.drawable.runner_red
        "MEDIUM" -> sk.vmproject.core.presentation.designsystem.R.drawable.runner_blue
        "HARD" -> sk.vmproject.core.presentation.designsystem.R.drawable.runner_green
        else -> sk.vmproject.core.presentation.designsystem.R.drawable.runner_purple
    }
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        modifier = modifier
    )
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