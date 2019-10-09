package pl.mobite.sample.security.uscases

import pl.mobite.sample.security.utils.hasMarshmallow
import pl.mobite.sample.security.wrappers.CipherWrapper
import pl.mobite.sample.security.wrappers.KeystoreWrapper
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey


interface GenerateKeyForPinUseCase: (String) -> KeyPair
interface GetKeyForPinUseCase: (String) -> KeyPair?
interface EncryptWithPinUseCase: (String, PublicKey) -> String
interface DecryptWithPinUseCase: (String, PrivateKey) -> String

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

class EncryptWithPinUseCaseImpl(
    private val cipherWrapper: CipherWrapper
): EncryptWithPinUseCase {

    override fun invoke(message: String, key: PublicKey): String {
        return cipherWrapper.encrypt(message, key)
    }
}

class DecryptWithPinUseCaseImpl(
    private val cipherWrapper: CipherWrapper
): DecryptWithPinUseCase {

    override fun invoke(message: String, key: PrivateKey): String {
        return cipherWrapper.decrypt(message, key)
    }
}