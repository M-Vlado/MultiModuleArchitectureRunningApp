plugins {
    alias(libs.plugins.virtualocr.android.library)
}

android {
    namespace = "sk.vmproject.core.data"
}

dependencies {
    implementation(libs.bundles.koin)
    implementation(projects.core.domain)
    implementation(projects.core.database)

}