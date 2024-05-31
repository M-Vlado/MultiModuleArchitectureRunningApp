import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import sk.vmproject.convention.configureAndroidCompose

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("virtualocr.android.library")
            }

            val extension = extensions.getByType<LibraryExtension>()
            configureAndroidCompose(commonExtension = extension)
        }
    }
}