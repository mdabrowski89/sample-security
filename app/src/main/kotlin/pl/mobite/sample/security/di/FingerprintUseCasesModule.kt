package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.uscases.*


val fingerprintUseCasesModule = module {

    factory<CheckFingerprintHardwareUseCase> { CheckFingerprintHardwareUseCaseImpl(get()) }

    factory<CheckFingerprintEnrolledUseCase> { CheckFingerprintEnrolledUseCaseImpl(get()) }

    factory<CheckIfDeviceIsSecureUseCase> { CheckIfDeviceIsSecureUseCaseImpl(get()) }

    factory<GenerateSecretKeyForFingerprintUseCase> { GenerateSecretKeyForFingerprintUseCaseImpl(get()) }

    factory<GetAESEncryptionCipherUseCase> { GetAESEncryptionCipherUseCaseImpl(get()) }

    factory<EncryptWithFingerprintCipherUseCase> { EncryptWithFingerprintCipherUseCaseImpl(get(), get()) }

    factory<GetAESDecryptionCipherUseCase> { GetAESDecryptionCipherUseCaseImpl(get(), get()) }

    factory<DecryptWithFingerprintCipherUseCase> { DecryptWithFingerprintCipherUseCaseImpl(get()) }
}