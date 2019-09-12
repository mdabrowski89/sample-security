package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.uscases.*
import pl.mobite.sample.security.utils.hasMarshmallow

val secretKeyUseCasesModule = module {

    factory {
        if (hasMarshmallow()) {
            GenerateSecretKeyUseCaseApi23Impl(get())
        } else {
            GenerateSecretKeyUseCaseImpl(get(), get(), get())
        }
    }

    factory {
        if (hasMarshmallow()) {
            GetSecretKeyUseCaseApi23Impl(get())
        } else {
            GetSecretKeyUseCaseImpl(get(), get(), get())
        }
    }

    factory<RemoveSecretKeyUseCase> { RemoveSecretKeyUseCaseImpl(get(), get()) }

    factory<EncryptUseCase> { EncryptUseCaseImpl(get(), get()) }

    factory<DecryptUseCase> { DecryptUseCaseImpl(get(), get()) }
}