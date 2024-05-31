package sk.vmproject.core.presentation.ui

import sk.vmproject.core.domain.util.DataError

fun DataError.asUiText(): UiText {
    return when (this) {
        DataError.Local.DISK_FULL -> UiText.StringResource(R.string.error_disk_full)
        else -> UiText.StringResource(R.string.error_unknown)
    }
}