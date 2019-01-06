package pl.mobite.sample.security.ui.components.secretkey.mvi

import pl.mobite.sample.security.ui.base.mvi.MviAction

sealed class SecretKeyAction: MviAction {

    data class CheckKeyAction(val keyAlias: String): SecretKeyAction()

    data class GenerateNewKeyAction(val keyAlias: String): SecretKeyAction()

    data class RemoveKeyAction(val keyAlias: String): SecretKeyAction()

    data class EncryptMessageAction(val keyAlias: String, val messageToEncrypt: String): SecretKeyAction()

    data class DecryptMessageAction(val keyAlias: String, val messageToDecrypt: String): SecretKeyAction()

    object ClearMessagesAction: SecretKeyAction()
}