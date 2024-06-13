package sk.vmproject.run.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sk.vmproject.core.presentation.designsystem.DoubleDoneIcon
import sk.vmproject.core.presentation.designsystem.R
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.core.presentation.designsystem.VirtualOcrAzure

@Composable
fun ObstacleItem(
    ordinalNumber: Int,
    obstacleDescription: String,
    distanceFromStart: String,
    modifier: Modifier = Modifier,
    obstacleReachTime: String? = null,
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = VirtualOcrAzure,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "$ordinalNumber",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    text = obstacleDescription,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    DataItem(
                        title = stringResource(R.string.distance_from_start),
                        value = distanceFromStart,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 105.dp)
                    )

                    DataItem(
                        title = stringResource(R.string.time_of_overcome),
                        value = obstacleReachTime ?: "--:--:--",
                        isOvercome = obstacleReachTime != null,
                        modifier = Modifier
                            .defaultMinSize(minWidth = 105.dp)
                    )


                }
            }
        }

    }

}

@Composable
private fun DataItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    isOvercome: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = title,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .defaultMinSize(minWidth = 50.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (isOvercome) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = DoubleDoneIcon,
                    contentDescription = null,
                    tint = VirtualOcrAzure
                )
            }
        }

    }
}


@Preview
@Composable
private fun ObstacleItemPreview() {
    VirtualOCRTheme {
        ObstacleItem(
            ordinalNumber = 1,
            obstacleDescription = "5 x burpee",
            distanceFromStart = "1.80 km",
            obstacleReachTime = "6:42"
        )
    }
}

@Preview
@Composable
private fun ObstacleItemPreview2() {
    VirtualOCRTheme {
        ObstacleItem(
            ordinalNumber = 1,
            obstacleDescription = "15 x jumping jacks",
            distanceFromStart = "17.80 km",

            )
    }
}