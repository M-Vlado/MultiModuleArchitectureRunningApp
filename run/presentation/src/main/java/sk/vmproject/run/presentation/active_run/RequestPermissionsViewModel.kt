package sk.vmproject.run.presentation.active_run

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class RequestPermissionsViewModel : ViewModel() {

    val visiblePermissionDialogQueue = mutableStateListOf<String>()


    fun dismissDialog() {
        visiblePermissionDialogQueue.removeFirst()
    }

    fun onGoSettingsDismissDialog() {
        visiblePermissionDialogQueue.clear()
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}