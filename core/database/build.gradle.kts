plugins {
    alias(libs.plugins.virtualocr.android.library)
    alias(libs.plugins.virtualocr.android.room)
}

android {
    namespace = "sk.vmproject.core.database"
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.bundles.koin)
}