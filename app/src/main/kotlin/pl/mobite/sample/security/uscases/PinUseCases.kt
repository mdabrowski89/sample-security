package pl.mobite.sample.security.uscases

import pl.mobite.sample.security.data.local.EncryptionPreferences
import pl.mobite.sample.security.utils.hasMarshmallow
import pl.mobite.sample.security.wrappers.CipherWrapper
import pl.mobite.sample.security.wrappers.KeystoreWrapper
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher


interface GenerateKeyForPinUseCase: (String) -> KeyPair
interface GetKeyForPinUseCase: (String) -> KeyPair?
interface GetRSADecryptionCipherUseCase: (PrivateKey) -> Cipher
interface EncryptWithPinUseCase: (String, PublicKey) -> String
interface DecryptWithPinCipherUseCase: (String, Cipher) -> String

class GenerateKeyForPinUseCaseImpl(
    private val keystoreWrapper: KeystoreWrapper
): GenerateKeyForPinUseCase {

    override fun invoke(alias: String): KeyPair {
        if (hasMarshmallow()) {
            return keystoreWrapper.generateAsymmetricKeyApi23(
                alias,
                authenticationRequired = true,
                validityDuration = 10
            )
        } else {
            throw Exception("Marshmallow required")
        }
    }
}

class GetKeyForPinUseCaseImpl(
    private val keystoreWrapper: KeystoreWrapper
): GetKeyForPinUseCase {

    override fun invoke(alias: String): KeyPair? {
        if (hasMarshmallow()) {
            return keystoreWrapper.getAsymmetricKeyPair(alias)
        } else {
            throw Exception("Marshmallow required")
        }
    }
}

class GetRSADecryptionCipherUseCaseImpl(
    private val cipherWrapper: CipherWrapper,
    private val encryptionPreferences: EncryptionPreferences
): GetRSADecryptionCipherUseCase {

    override fun invoke(privateKey: PrivateKey): Cipher {
        val initializationVector = encryptionPreferences.pinIv ?: throw Exception("Missing initialization vector")
        return cipherWrapper.getDecryptionCipher(privateKey, initializationVector)
    }
}


class EncryptWithPinUseCaseImpl(
    private val cipherWrapper: CipherWrapper,
    private val encryptionPreferences: EncryptionPreferences
): EncryptWithPinUseCase {

    override fun invoke(message: String, key: PublicKey): String {
        val (encryptedMessage, initializationVector) = cipherWrapper.encrypt(message, key)
        encryptionPreferences.pinIv = initializationVector
        return encryptedMessage
    }
}

class DecryptWithPinCipherUseCaseImpl(
    private val cipherWrapper: CipherWrapper
): DecryptWithPinCipherUseCase {

    override fun invoke(message: String, cipher: Cipher): String {
        return cipherWrapper.decrypt(message, cipher)
    }
}