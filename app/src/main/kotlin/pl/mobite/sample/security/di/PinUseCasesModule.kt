package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.uscases.*


val pinUseCasesModule = module {

    factory<GenerateKeyForPinUseCase> { GenerateKeyForPinUseCaseImpl(get()) }

    factory<GetKeyForPinUseCase> { GetKeyForPinUseCaseImpl(get()) }

    factory<EncryptWithPinUseCase> { EncryptWithPinUseCaseImpl(get()) }

    factory<DecryptWithPinUseCase> { DecryptWithPinUseCaseImpl(get()) }
}