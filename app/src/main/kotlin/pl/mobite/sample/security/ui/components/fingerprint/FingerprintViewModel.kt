package pl.mobite.sample.security.ui.components.fingerprint

import androidx.lifecycle.SavedStateHandle
import pl.mobite.sample.security.ui.base.mvi.MviViewModel
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintAction
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintAction.*
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintActionProcessor
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult
import javax.crypto.Cipher


class FingerprintViewModel(
    savedStateHandle : SavedStateHandle
): MviViewModel<FingerprintAction, FingerprintResult, FingerprintViewState>(
    savedStateHandle,
    FingerprintActionProcessor(),
    FingerprintViewState.default()
) {

    fun onStart() {
        accept(CheckPreconditionsAndKeyAction(KEY_ALIAS))
    }

    fun generateNewKey() {
        accept(GenerateNewKeyAction(KEY_ALIAS))
    }

    fun removeKey() {
        accept(RemoveKeyAction(KEY_ALIAS))
    }

    fun encryptMessage(messageToEncrypt: String) {
        val secretKey = viewState.secretKey
        if (secretKey != null) {
            accept(PrepareEncryptionCipherAction(secretKey, messageToEncrypt))
        }
    }

    fun onEncryptionCipherReady(authenticatedCipher: Cipher) {
        val messageToEncrypt = viewState.messageToEncrypt
        if (messageToEncrypt != null) {
            accept(EncryptMessageAction(authenticatedCipher, messageToEncrypt))
        }
    }

    fun decryptMessage() {
        val secretKey = viewState.secretKey
        if (secretKey != null) {
            accept(PrepareDecryptionCipherAction(secretKey))
        }
    }

    fun onDecryptionCipherReady(authenticatedCipher: Cipher) {
        val messageToDecrypt = viewState.encryptionFormViewState.messageEncrypted
        if (messageToDecrypt != null) {
            accept(DecryptMessageAction(authenticatedCipher, messageToDecrypt))
        }
    }

    fun clearMessage() {
        accept(ClearMessagesAction)
    }

    companion object {

        private const val KEY_ALIAS = "BUTTERCUP"
    }
}