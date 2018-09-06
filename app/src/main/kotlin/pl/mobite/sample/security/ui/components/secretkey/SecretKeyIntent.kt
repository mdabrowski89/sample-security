package pl.mobite.sample.security.ui.components.secretkey


sealed class SecretKeyIntent {

    data class InitialIntent(val keyAlias: String): SecretKeyIntent()

    data class GenerateKeyIntent(val keyAlias: String): SecretKeyIntent()

    data class RemoveKeyIntent(val keyAlias: String): SecretKeyIntent()

    data class EncryptMessageIntent(val keyAlias: String, val messageToEncrypt: String): SecretKeyIntent()

    data class DecryptMessageIntent(val keyAlias: String, val messageEncrypted: String): SecretKeyIntent()

    object ClearMessagesIntent: SecretKeyIntent()
}