plugins {
    alias(libs.plugins.virtualocr.android.library)
}

android {
    namespace = "sk.vmproject.run.location"
}

dependencies {

    implementation(libs.bundles.koin)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.google.android.gms.play.services.location)

    implementation(projects.core.domain)
    implementation(projects.run.domain)
}