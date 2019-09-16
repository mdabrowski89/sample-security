package pl.mobite.sample.security.ui.components.secretkey.mvi

import pl.mobite.sample.security.ui.base.mvi.MviAction
import javax.crypto.SecretKey

sealed class SecretKeyAction: MviAction {

    data class CheckKeyAction(val keyAlias: String): SecretKeyAction()

    data class GenerateNewKeyAction(val keyAlias: String): SecretKeyAction()

    data class RemoveKeyAction(val keyAlias: String): SecretKeyAction()

    data class EncryptMessageAction(val secretKey: SecretKey, val messageToEncrypt: String): SecretKeyAction()

    data class DecryptMessageAction(val secretKey: SecretKey, val messageToDecrypt: String): SecretKeyAction()

    object ClearMessagesAction: SecretKeyAction()
}