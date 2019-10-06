package pl.mobite.sample.security.ui.components.pin

import androidx.lifecycle.SavedStateHandle
import pl.mobite.sample.security.ui.base.mvi.MviViewModel
import pl.mobite.sample.security.ui.components.pin.mvi.PinAction
import pl.mobite.sample.security.ui.components.pin.mvi.PinAction.*
import pl.mobite.sample.security.ui.components.pin.mvi.PinActionProcessor
import pl.mobite.sample.security.ui.components.pin.mvi.PinResult
import javax.crypto.Cipher


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
        viewState.keyPair?.private?.let { accept(PrepareDecryptionCipherAction(it))  }
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

        private const val KEY_ALIAS = "BUBBLES"
    }
}