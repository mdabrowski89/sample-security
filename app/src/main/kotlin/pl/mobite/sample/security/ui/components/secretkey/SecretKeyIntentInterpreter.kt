package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.functions.Function
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyIntent.*


class SecretKeyIntentInterpreter: Function<SecretKeyIntent, SecretKeyAction> {

    override fun apply(intent: SecretKeyIntent): SecretKeyAction {
        return when (intent) {
            is InitialIntent -> CheckKeyAction(intent.keyAlias)
            is GenerateKeyIntent -> GenerateNewKeyAction(intent.keyAlias)
            is RemoveKeyIntent -> RemoveKeyAction(intent.keyAlias)
            is EncryptMessageIntent -> EncryptMessageAction(intent.keyAlias, intent.messageToEncrypt)
            is DecryptMessageIntent -> DecryptMessageAction(intent.keyAlias, intent.messageEncrypted)
            is ClearMessagesIntent -> ClearMessagesAction
        }
    }
}