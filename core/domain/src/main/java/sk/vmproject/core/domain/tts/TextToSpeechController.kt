package sk.vmproject.core.domain.tts

import kotlinx.coroutines.flow.StateFlow

interface TextToSpeechController {

    val isInitializeSuccess: StateFlow<Boolean>
    val isEnglishLanguageAvailable: StateFlow<Boolean>

    fun startSpeech(text: String)

    fun setLanguage()

    fun shutDownTTS()
}