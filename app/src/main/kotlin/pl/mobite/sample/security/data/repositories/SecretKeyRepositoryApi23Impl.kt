package pl.mobite.sample.security.data.repositories

import android.annotation.TargetApi
import android.os.Build
import org.koin.core.KoinComponent
import org.koin.core.inject
import pl.mobite.sample.security.data.local.EncryptionPreferences
import pl.mobite.sample.security.encryption.CipherWrapper
import pl.mobite.sample.security.encryption.KeystoreWrapper
import javax.crypto.SecretKey

@TargetApi(Build.VERSION_CODES.M)
class SecretKeyRepositoryApi23Impl: SecretKeyRepository, KoinComponent {

    private val keystoreWrapper: KeystoreWrapper by inject()
    private val cipherWrapper: CipherWrapper by inject()
    private val encryptionPreferences: EncryptionPreferences by inject()

    override fun checkKey(keyAlias: String): Boolean {
        return keystoreWrapper.getSymmetricKey(keyAlias) != null
    }

    override fun generateKey(keyAlias: String) {
        keystoreWrapper.generateSymmetricKeyApi23(keyAlias)
    }

    override fun removeKey(keyAlias: String) {
        keystoreWrapper.removeKey(keyAlias)
        encryptionPreferences.clear()
    }

    override fun encrypt(keyAlias: String, message: String): String {
        return keystoreWrapper.getSymmetricKey(keyAlias)?.let {
            val (encryptedMessage, initializationVector) = cipherWrapper.encrypt(message, it)
            encryptionPreferences.initializationVector = initializationVector
            return encryptedMessage
        } ?: throw Exception("Secret key not generated for alias: $keyAlias")
    }

    override fun decrypt(keyAlias: String, message: String): String {
        val secretKey: SecretKey = keystoreWrapper.getSymmetricKey(keyAlias) ?: throw Exception("Secret key not generated for alias: $keyAlias")
        val initializationVector: String = encryptionPreferences.initializationVector ?: throw Exception("Missing initialization vector")
        return cipherWrapper.decrypt(message, initializationVector, secretKey)
    }

}