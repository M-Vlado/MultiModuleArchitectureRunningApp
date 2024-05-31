plugins {
    alias(libs.plugins.virtualocr.android.feature.ui)
}

android {
    namespace = "sk.vmproject.run.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)

    implementation(projects.core.domain)
    implementation(projects.run.domain)
}