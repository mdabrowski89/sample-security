package pl.mobite.sample.security.ui.components.secretkey

import androidx.lifecycle.SavedStateHandle
import pl.mobite.sample.security.ui.base.mvi.MviViewModel
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyActionProcessor
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult


class SecretKeyViewModel(
    savedStateHandle : SavedStateHandle
): MviViewModel<SecretKeyAction, SecretKeyResult, SecretKeyViewState>(
    savedStateHandle,
    SecretKeyActionProcessor(),
    SecretKeyViewState.default()
) {

    fun onStart() {
        accept(CheckKeyAction(KEY_ALIAS))
    }

    fun generateNewKey() {
        accept(GenerateNewKeyAction(KEY_ALIAS))
    }

    fun removeKey() {
        accept(RemoveKeyAction(KEY_ALIAS))
    }

    fun encryptMessage(message: String) {
        val secretKey = viewState.secretKey
        if (secretKey != null) {
            accept(EncryptMessageAction(secretKey, message))
        }
    }

    fun decryptMessage() {
        val messageToDecrypt = viewState.encryptionFormViewState.messageEncrypted
        val secretKey = viewState.secretKey
        if (secretKey != null && !messageToDecrypt.isNullOrEmpty()) {
            accept(DecryptMessageAction(secretKey, messageToDecrypt))
        }
    }

    fun clearMessage() {
        accept(ClearMessagesAction)
    }

    companion object {

        private const val KEY_ALIAS = "BLOSSOM"
    }
}