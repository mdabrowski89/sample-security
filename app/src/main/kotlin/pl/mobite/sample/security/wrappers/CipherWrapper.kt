package pl.mobite.sample.security.wrappers

import android.util.Base64
import java.security.Key
import java.security.KeyPair
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


class CipherWrapper {

    fun encrypt(message: String, keyPair: KeyPair): String {
        val cipher = Cipher.getInstance(TRANSFORMATION_RSA)
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.public)
        val bytes = cipher.doFinal(message.toByteArray())
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    fun decrypt(message: String, keyPair: KeyPair): String {
        val cipher = Cipher.getInstance(TRANSFORMATION_RSA)
        cipher.init(Cipher.DECRYPT_MODE, keyPair.private)
        val encryptedData = Base64.decode(message, Base64.DEFAULT)
        val decodedData = cipher.doFinal(encryptedData)
        return String(decodedData)
    }

    fun encrypt(message: String, secretKey: SecretKey): EncryptionResult {
        val cipher = Cipher.getInstance(TRANSFORMATION_AES)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return encrypt(message, cipher)
    }

    fun decrypt(message: String, initializationVector: String, secretKey: SecretKey): String {
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION_AES)
        val iv = Base64.decode(initializationVector, Base64.DEFAULT)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        return decrypt(message, cipher)
    }

    fun encrypt(message: String, cipher: Cipher): EncryptionResult {
        val bytes = cipher.doFinal(message.toByteArray())
        val encryptedMessage = Base64.encodeToString(bytes, Base64.DEFAULT)
        val initializationVector = Base64.encodeToString(cipher.iv, Base64.DEFAULT)
        return EncryptionResult(encryptedMessage, initializationVector)
    }

    fun decrypt(message: String, cipher: Cipher): String {
        val encryptedData = Base64.decode(message, Base64.DEFAULT)
        val decryptedData = cipher.doFinal(encryptedData)
        return String(decryptedData)
    }

    fun wrapKey(keyToBeWrapped: Key, keyToWrapWith: Key?): String {
        val cipher = Cipher.getInstance(TRANSFORMATION_RSA)
        cipher.init(Cipher.WRAP_MODE, keyToWrapWith)
        val decodedData = cipher.wrap(keyToBeWrapped)
        return Base64.encodeToString(decodedData, Base64.DEFAULT)
    }

    fun unWrapKey(wrappedKeyData: String, algorithm: String, wrappedKeyType: Int, keyToUnWrapWith: Key?): Key {
        val cipher = Cipher.getInstance(TRANSFORMATION_RSA)
        val encryptedKeyData = Base64.decode(wrappedKeyData, Base64.DEFAULT)
        cipher.init(Cipher.UNWRAP_MODE, keyToUnWrapWith)
        return cipher.unwrap(encryptedKeyData, algorithm, wrappedKeyType)
    }

    fun getEncryptionCipher(secretKey: SecretKey): Cipher {
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION_AES)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return cipher
    }

    fun getDecryptionCipher(secretKey: SecretKey, initializationVector: String): Cipher {
        val cipher: Cipher = Cipher.getInstance(TRANSFORMATION_AES)
        val iv = Base64.decode(initializationVector, Base64.DEFAULT)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        return cipher
    }

    data class EncryptionResult(val encryptedMessage: String, val initializationVector: String)

    companion object {

        private const val TRANSFORMATION_AES = "AES/CBC/PKCS7Padding"
        private const val TRANSFORMATION_RSA = "RSA/ECB/PKCS1Padding"
    }
}