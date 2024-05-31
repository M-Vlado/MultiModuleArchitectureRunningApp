package sk.vmproject.run.presentation.active_run.components

import android.content.Context
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import sk.vmproject.run.presentation.R

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onGoToAppSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            if (isPermanentlyDeclined) {
                TextButton(onClick = onGoToAppSettings) {
                    Text(text = stringResource(R.string.grant_permission))
                }
            } else {
                TextButton(onClick = onConfirm) {
                    Text(text = stringResource(R.string.ok))
                }
            }
        },
        title = {
            Text(text = stringResource(R.string.permission_required_title))
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(
                    isPermanentlyDeclined = isPermanentlyDeclined,
                    context = context
                )
            )
        },
        modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String
}

class AccessLocationTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.permanently_declined_access_location_permission)
        } else {
            context.getString(R.string.location_permission_rational_dialog)
        }
    }
}

class NotificationPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean, context: Context): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.permanently_declined_notification_permission)
        } else {
            context.getString(R.string.notification_permission_rational_dialog)
        }
    }
}