package sk.vmproject.core.data.di

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import sk.vmproject.core.data.VirtualOcrRepositoryImpl
import sk.vmproject.core.data.tts.TextToSpeechControllerImpl
import sk.vmproject.core.domain.run.repository.VirtualOcrRepository
import sk.vmproject.core.domain.tts.TextToSpeechController

val coreDataModule = module {

    singleOf(::VirtualOcrRepositoryImpl).bind<VirtualOcrRepository>()
    singleOf(::TextToSpeechControllerImpl).bind<TextToSpeechController>()
}