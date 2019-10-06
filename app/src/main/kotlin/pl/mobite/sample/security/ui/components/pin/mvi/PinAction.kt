package pl.mobite.sample.security.ui.components.pin.mvi

import pl.mobite.sample.security.ui.base.mvi.MviAction
import java.security.PrivateKey
import java.security.PublicKey
import javax.crypto.Cipher


sealed class PinAction: MviAction {

    data class CheckPreconditionsAndKeyAction(val keyAlias: String): PinAction()

    data class GenerateNewKeyAction(val keyAlias: String): PinAction()

    data class RemoveKeyAction(val keyAlias: String): PinAction()

    data class EncryptMessageAction(val publicKey: PublicKey, val messageToEncrypt: String): PinAction()

    data class PrepareDecryptionCipherAction(val privateKey: PrivateKey): PinAction()

    data class DecryptMessageAction(val authenticatedCipher: Cipher, val messageToDecrypt: String): PinAction()

    object ClearMessagesAction: PinAction()
}