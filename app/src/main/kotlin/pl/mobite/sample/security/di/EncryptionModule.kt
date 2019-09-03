package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.data.local.EncryptionPreferences
import pl.mobite.sample.security.encryption.CipherWrapper
import pl.mobite.sample.security.encryption.KeystoreWrapper


val encryptionModule = module {

    single<KeystoreWrapper> { KeystoreWrapper(get()) }

    single<CipherWrapper> { CipherWrapper() }

    single<EncryptionPreferences> { EncryptionPreferences(get()) }
}