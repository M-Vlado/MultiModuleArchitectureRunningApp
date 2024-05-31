import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import sk.vmproject.convention.addUiLayerDependencies

class AndroidFeatureUiConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("virtualocr.android.library.compose")
            }

            dependencies {
                addUiLayerDependencies(project = target)
            }
        }
    }
}