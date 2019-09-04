package pl.mobite.sample.security.wrappers

import android.util.Base64
import java.security.Key
import java.security.KeyPair
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


class CipherWrapper {

    private val cipherRSA: Cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    private val cipherAES: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")

    fun encrypt(message: String, keyPair: KeyPair): String {
        cipherRSA.init(Cipher.ENCRYPT_MODE, keyPair.public)
        val bytes = cipherRSA.doFinal(message.toByteArray())
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun decrypt(message: String, keyPair: KeyPair): String {
        cipherRSA.init(Cipher.DECRYPT_MODE, keyPair.private)
        val encryptedData = Base64.decode(message, Base64.DEFAULT)
        val decodedData = cipherRSA.doFinal(encryptedData)
        return String(decodedData)
    }

    fun encrypt(message: String, secretKey: SecretKey): EncryptionResult {
        cipherAES.init(Cipher.ENCRYPT_MODE, secretKey)
        val bytes = cipherAES.doFinal(message.toByteArray())
        return EncryptionResult(Base64.encodeToString(bytes, Base64.DEFAULT), Base64.encodeToString(cipherAES.iv, Base64.DEFAULT))
    }

    fun decrypt(message: String, initializationVector: String, secretKey: SecretKey): String {
        val iv = Base64.decode(initializationVector, Base64.DEFAULT)
        cipherAES.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encryptedData = Base64.decode(message, Base64.DEFAULT)
        val decodedData = cipherAES.doFinal(encryptedData)
        return String(decodedData)
    }

    fun wrapKey(keyToBeWrapped: Key, keyToWrapWith: Key?): String {
        cipherRSA.init(Cipher.WRAP_MODE, keyToWrapWith)
        val decodedData = cipherRSA.wrap(keyToBeWrapped)
        return Base64.encodeToString(decodedData, Base64.DEFAULT)
    }

    fun unWrapKey(wrappedKeyData: String, algorithm: String, wrappedKeyType: Int, keyToUnWrapWith: Key?): Key {
        val encryptedKeyData = Base64.decode(wrappedKeyData, Base64.DEFAULT)
        cipherRSA.init(Cipher.UNWRAP_MODE, keyToUnWrapWith)
        return cipherRSA.unwrap(encryptedKeyData, algorithm, wrappedKeyType)
    }

    data class EncryptionResult(val encryptedMessage: String, val initializationVector: String)
}