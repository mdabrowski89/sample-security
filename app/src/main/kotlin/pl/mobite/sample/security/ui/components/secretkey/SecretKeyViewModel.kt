package pl.mobite.sample.security.ui.components.secretkey

import androidx.lifecycle.SavedStateHandle
import pl.mobite.sample.security.ui.base.mvi.MviViewModel
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction
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
        accept(SecretKeyAction.CheckKeyAction(KEY_ALIAS))
    }

    fun generateNewKey() {
        accept(SecretKeyAction.GenerateNewKeyAction(KEY_ALIAS))
    }

    fun removeKey() {
        accept(SecretKeyAction.RemoveKeyAction(KEY_ALIAS))
    }

    fun encryptMessage(message: String) {
        accept(SecretKeyAction.EncryptMessageAction(KEY_ALIAS, message))
    }

    fun decryptMessage(message: String) {
        accept(SecretKeyAction.DecryptMessageAction(KEY_ALIAS, message))
    }

    fun clearMessage() {
        accept(SecretKeyAction.ClearMessagesAction)
    }

    companion object {

        private const val KEY_ALIAS = "BLOSSOM"
    }
}