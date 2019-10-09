package pl.mobite.sample.security.ui.components.pin

import androidx.lifecycle.SavedStateHandle
import pl.mobite.sample.security.ui.base.mvi.MviViewModel
import pl.mobite.sample.security.ui.components.pin.mvi.PinAction
import pl.mobite.sample.security.ui.components.pin.mvi.PinAction.*
import pl.mobite.sample.security.ui.components.pin.mvi.PinActionProcessor
import pl.mobite.sample.security.ui.components.pin.mvi.PinResult


class PinViewModel(
    savedStateHandle : SavedStateHandle
): MviViewModel<PinAction, PinResult, PinViewState>(
    savedStateHandle,
    PinActionProcessor(),
    PinViewState.default()
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
        viewState.keyPair?.public?.let { accept(EncryptMessageAction(it, messageToEncrypt)) }
    }

    fun decryptMessage() {
        decryptMessage(authenticateIfNeeded = true)
    }

    fun onUserAuthenticated() {
        decryptMessage(authenticateIfNeeded = false)
    }

    fun clearMessage() {
        accept(ClearMessagesAction)
    }

    private fun decryptMessage(authenticateIfNeeded: Boolean) {
        val privateKey = viewState.keyPair?.private
        val messageToDecrypt = viewState.encryptionFormViewState.messageEncrypted
        if (privateKey != null && messageToDecrypt != null) {
            accept(DecryptMessageAction(privateKey, messageToDecrypt, authenticateIfNeeded))
        }
    }

    companion object {

        private const val KEY_ALIAS = "BUBBLES"
    }
}