package pl.mobite.sample.security.uscases

import pl.mobite.sample.security.data.local.EncryptionPreferences
import pl.mobite.sample.security.utils.hasMarshmallow
import pl.mobite.sample.security.wrappers.CipherWrapper
import pl.mobite.sample.security.wrappers.FingerprintManagerWrapper
import pl.mobite.sample.security.wrappers.KeyguardWrapper
import pl.mobite.sample.security.wrappers.KeystoreWrapper
import javax.crypto.Cipher
import javax.crypto.SecretKey


interface CheckFingerprintHardwareUseCase: () -> Boolean
interface CheckFingerprintEnrolledUseCase: () -> Boolean
interface CheckIfDeviceIsSecureUseCase: () -> Boolean
interface GenerateSecretKeyForFingerprintUseCase: (String) -> SecretKey
interface GetAESEncryptionCipherUseCase: (SecretKey) -> Cipher
interface GetAESDecryptionCipherUseCase: (SecretKey) -> Cipher
interface EncryptWithFingerprintCipherUseCase: (String, Cipher) -> String
interface DecryptWithFingerprintCipherUseCase: (String, Cipher) -> String

class CheckFingerprintHardwareUseCaseImpl(
    private val fingerprintManagerWrapper: FingerprintManagerWrapper
): CheckFingerprintHardwareUseCase {

    override fun invoke(): Boolean {
        return fingerprintManagerWrapper.isHardwareDetected()
    }
}

class CheckFingerprintEnrolledUseCaseImpl(
    private val fingerprintManagerWrapper: FingerprintManagerWrapper
): CheckFingerprintEnrolledUseCase {

    override fun invoke(): Boolean {
        return fingerprintManagerWrapper.hasEnrolledFingerprints()
    }
}

class CheckIfDeviceIsSecureUseCaseImpl(
    private val keyguardWrapper: KeyguardWrapper
): CheckIfDeviceIsSecureUseCase {

    override fun invoke(): Boolean {
        return keyguardWrapper.isDeviceSecure()
    }
}

class GenerateSecretKeyForFingerprintUseCaseImpl(
    private val keystoreWrapper: KeystoreWrapper
): GenerateSecretKeyForFingerprintUseCase {

    override fun invoke(alias: String): SecretKey {
        if (hasMarshmallow()) {
            return keystoreWrapper.generateSymmetricKeyApi23(alias, authenticationRequired = true)
        } else {
            throw Exception("Marshmallow required")
        }
    }
}

class GetAESEncryptionCipherUseCaseImpl(
    private val cipherWrapper: CipherWrapper
): GetAESEncryptionCipherUseCase {

    override fun invoke(secretKey: SecretKey): Cipher {
        return cipherWrapper.getEncryptionCipher(secretKey)
    }
}

class GetAESDecryptionCipherUseCaseImpl(
    private val cipherWrapper: CipherWrapper,
    private val encryptionPreferences: EncryptionPreferences
): GetAESDecryptionCipherUseCase {

    override fun invoke(secretKey: SecretKey): Cipher {
        val initializationVector = encryptionPreferences.fingerprintIv ?: throw Exception("Missing initialization vector")
        return cipherWrapper.getDecryptionCipher(secretKey, initializationVector)
    }
}

class EncryptWithFingerprintCipherUseCaseImpl(
    private val cipherWrapper: CipherWrapper,
    private val encryptionPreferences: EncryptionPreferences
): EncryptWithFingerprintCipherUseCase {

    override fun invoke(message: String, cipher: Cipher): String {
        val (encryptedMessage, initializationVector) = cipherWrapper.encrypt(message, cipher)
        encryptionPreferences.fingerprintIv = initializationVector
        return encryptedMessage
    }
}

class DecryptWithFingerprintCipherUseCaseImpl(
    private val cipherWrapper: CipherWrapper
): DecryptWithFingerprintCipherUseCase {

    override fun invoke(message: String, cipher: Cipher): String {
        return cipherWrapper.decrypt(message, cipher)
    }
}