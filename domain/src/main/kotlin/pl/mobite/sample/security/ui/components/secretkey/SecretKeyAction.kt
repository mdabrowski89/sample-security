package pl.mobite.sample.security.ui.components.secretkey


sealed class SecretKeyAction {

    data class LoadKeyAction(val keyAlias: String): SecretKeyAction()

    data class GenerateNewKeyAction(val keyAlias: String): SecretKeyAction()

    data class ClearKeyAction(val keyAlias: String): SecretKeyAction()

    data class EncryptMessageAction(val keyAlias: String, val messageToEncrypt: String): SecretKeyAction()

    data class DecryptMessageAction(val keyAlias: String, val messageToDecrypt: String): SecretKeyAction()

    object ClearMessagesAction: SecretKeyAction()
}