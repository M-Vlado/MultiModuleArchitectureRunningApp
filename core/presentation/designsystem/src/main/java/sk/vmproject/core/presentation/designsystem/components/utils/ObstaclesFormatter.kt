package sk.vmproject.core.presentation.designsystem.components.utils

import android.content.Context
import sk.vmproject.core.presentation.designsystem.R
import java.text.DecimalFormat

object ObstaclesFormatter {
    fun getObstacleTypeWithNumberOfRepsText(numberOfReps: Int, obstacleType: String): String {
        return if (numberOfReps <= 1) {
            "$numberOfReps x $obstacleType"
        } else {
            "$numberOfReps x ${obstacleType}s"
        }
    }

    fun getDistanceInKilometersText(distanceInMeters: Long): String {
        val df = DecimalFormat("#.##")
        val km = df.format(distanceInMeters / 1000f)

        return "$km km"
    }

    fun getObstacleDescription(obstaclesCount: Int, context: Context): String {
        return if (obstaclesCount >= 1) {
            String.format(context.getString(R.string.obstacle), obstaclesCount)
        } else {
            String.format(context.getString(R.string.obstacles), obstaclesCount)
        }
    }

    fun getObstacleDescriptionForTTS(numberOfReps: Int, obstacleType: String): String {
        return if (numberOfReps >= 1) {
            "$numberOfReps $obstacleType"
        } else {
            "$numberOfReps ${obstacleType}s"
        }
    }
}