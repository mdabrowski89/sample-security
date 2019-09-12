package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.wrappers.*


val wrappersModule = module {

    single { KeystoreWrapper(get()) }

    single { CipherWrapper() }

    single { FingerprintManagerWrapper(get()) }

    single { KeyguardWrapper(get()) }

    single { BiometricPromptWrapper() }
}