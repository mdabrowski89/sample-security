package pl.mobite.sample.security.ui.components.fingerprint.mvi

import pl.mobite.sample.security.ui.base.mvi.MviAction
import javax.crypto.Cipher


sealed class FingerprintAction: MviAction {

    data class CheckPreconditionsAndKeyAction(val keyAlias: String): FingerprintAction()

    data class GenerateNewKeyAction(val keyAlias: String): FingerprintAction()

    data class RemoveKeyAction(val keyAlias: String): FingerprintAction()

    data class PrepareEncryptionCipherAction(val keyAlias: String): FingerprintAction()

    data class EncryptMessageAction(val keyAlias: String, val authenticatedCipher: Cipher, val messageToEncrypt: String): FingerprintAction()

    data class PrepareDecryptionCipherAction(val keyAlias: String): FingerprintAction()

    data class DecryptMessageAction(val keyAlias: String, val authenticatedCipher: Cipher, val messageToDecrypt: String): FingerprintAction()

    object ClearMessagesAction: FingerprintAction()
}