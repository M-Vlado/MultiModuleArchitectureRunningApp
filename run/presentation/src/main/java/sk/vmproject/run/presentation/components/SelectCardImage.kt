package sk.vmproject.run.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun SelectCardImage(title: String, modifier: Modifier = Modifier) {
    val image = when (title.uppercase()) {
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