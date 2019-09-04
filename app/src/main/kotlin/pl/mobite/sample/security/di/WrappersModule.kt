package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.wrappers.CipherWrapper
import pl.mobite.sample.security.wrappers.FingerprintManagerWrapper
import pl.mobite.sample.security.wrappers.KeystoreWrapper


val wrappersModule = module {

    single { KeystoreWrapper(get()) }

    single { CipherWrapper() }

    single { FingerprintManagerWrapper(get()) }
}