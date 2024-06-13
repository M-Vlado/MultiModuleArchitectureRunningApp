package sk.vmproject.run.presentation.overview_runs.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.core.presentation.designsystem.components.GradientBackground
import sk.vmproject.run.domain.RunLevelType

@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    image: Int,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .padding(8.dp)

    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            colorFilter = if (title == "ALL") ColorFilter.tint(MaterialTheme.colorScheme.primary) else null,
            modifier = Modifier
                .size(60.dp)
        )
        Text(
            text = title,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Preview
@Composable
private fun TabItemPreview() {
    VirtualOCRTheme {
        GradientBackground {
            TabItem(
                title = RunLevelType.EASY.levelType,
                isSelected = true,
                image = sk.vmproject.core.presentation.designsystem.R.drawable.runner_red
            )
        }
    }
}

@Preview
@Composable
private fun TabItemPreviewAll() {
    VirtualOCRTheme {
        GradientBackground {
            TabItem(
                title = "ALl",
                isSelected = false,
                image = sk.vmproject.core.presentation.designsystem.R.drawable.runner_red
            )
        }
    }
}

data class TabItemData(
    val title: String,
    val image: Int,
)