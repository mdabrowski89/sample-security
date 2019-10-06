package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.uscases.*


val pinUseCasesModule = module {

    factory<GenerateKeyForPinUseCase> { GenerateKeyForPinUseCaseImpl(get()) }

    factory<GetKeyForPinUseCase> { GetKeyForPinUseCaseImpl(get()) }

    factory<GetRSADecryptionCipherUseCase> { GetRSADecryptionCipherUseCaseImpl(get(), get()) }

    factory<EncryptWithPinUseCase> { EncryptWithPinUseCaseImpl(get(), get()) }

    factory<DecryptWithPinCipherUseCase> { DecryptWithPinCipherUseCaseImpl(get()) }
}