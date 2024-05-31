package sk.vmproject.core.data.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sk.vmproject.core.domain.tts.TextToSpeechController
import java.util.Locale

class TextToSpeechControllerImpl(
    context: Context,
) : TextToSpeechController {

    private var textToSpeech: TextToSpeech? = null

    private val _isInitializeSuccess = MutableStateFlow(false)
    override val isInitializeSuccess: StateFlow<Boolean>
        get() = _isInitializeSuccess.asStateFlow()

    private val _isEnglishLanguageAvailable = MutableStateFlow(false)
    override val isEnglishLanguageAvailable: StateFlow<Boolean>
        get() = _isEnglishLanguageAvailable.asStateFlow()


    init {
        textToSpeech = TextToSpeech(context) {
            if (it == TextToSpeech.SUCCESS) {
                _isInitializeSuccess.value = true
                setLanguage()
            }
        }
    }

    override fun startSpeech(text: String) {
        if (_isInitializeSuccess.value && _isEnglishLanguageAvailable.value) {
            if (!textToSpeech!!.isSpeaking) {
                textToSpeech!!.speak(
                    text,
                    TextToSpeech.QUEUE_ADD,
                    null,
                    null
                )
            }
        }
    }

    override fun setLanguage() {
        if (_isInitializeSuccess.value) {
            textToSpeech?.let { tts ->
                val initLanguageResult = tts.setLanguage(Locale.ENGLISH)
                if (initLanguageResult == TextToSpeech.LANG_MISSING_DATA || initLanguageResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                    _isEnglishLanguageAvailable.value = false
                } else {
                    _isEnglishLanguageAvailable.value = true
                }
                tts.setSpeechRate(0.8f)
            }
        }
    }

    override fun shutDownTTS() {
        if (textToSpeech != null) {
            textToSpeech?.shutdown()
        }
    }
}