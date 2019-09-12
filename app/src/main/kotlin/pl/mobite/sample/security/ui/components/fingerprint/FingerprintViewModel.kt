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

    fun onEncryptButtonClicked() {
        accept(PrepareEncryptionCipherAction(KEY_ALIAS))
    }

    fun onEncryptionCipherReady(authenticatedCipher: Cipher, message: String) {
        accept(EncryptMessageAction(KEY_ALIAS, authenticatedCipher, message))
    }

    fun onDecryptButtonClicked() {
        accept(PrepareDecryptionCipherAction(KEY_ALIAS))
    }

    fun onDecryptionCipherReady(authenticatedCipher: Cipher, message: String) {
        accept(DecryptMessageAction(KEY_ALIAS, authenticatedCipher, message))
    }

    fun clearMessage() {
        accept(ClearMessagesAction)
    }

    companion object {

        private const val KEY_ALIAS = "BUTTERCUP"
    }
}