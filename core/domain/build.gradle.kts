plugins {
    alias(libs.plugins.virtualocr.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}