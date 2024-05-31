package sk.vmproject.virtualocr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.koin.android.ext.android.inject
import sk.vmproject.core.domain.tts.TextToSpeechController
import sk.vmproject.core.presentation.designsystem.VirtualOCRTheme
import sk.vmproject.virtualocr.navigation.NavigationRoot

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VirtualOCRTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavigationRoot(navController = navController)

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val textToSpeechController by inject<TextToSpeechController>()
        textToSpeechController.shutDownTTS()
    }
}
