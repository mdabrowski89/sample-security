package pl.mobite.sample.security.ui.components.fingerprint.mvi

import pl.mobite.sample.security.ui.base.mvi.MviAction
import javax.crypto.Cipher
import javax.crypto.SecretKey


sealed class FingerprintAction: MviAction {

    data class CheckPreconditionsAndKeyAction(val keyAlias: String): FingerprintAction()

    data class GenerateNewKeyAction(val keyAlias: String): FingerprintAction()

    data class RemoveKeyAction(val keyAlias: String): FingerprintAction()

    data class PrepareEncryptionCipherAction(val secretKey: SecretKey, val messageToEncrypt: String): FingerprintAction()

    data class EncryptMessageAction(val authenticatedCipher: Cipher, val messageToEncrypt: String): FingerprintAction()

    data class PrepareDecryptionCipherAction(val secretKey: SecretKey): FingerprintAction()

    data class DecryptMessageAction(val authenticatedCipher: Cipher, val messageToDecrypt: String): FingerprintAction()

    object ClearMessagesAction: FingerprintAction()
}