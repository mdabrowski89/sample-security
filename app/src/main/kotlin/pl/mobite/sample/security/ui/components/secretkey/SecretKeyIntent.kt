package pl.mobite.sample.security.ui.components.secretkey

import pl.mobite.sample.security.ui.base.mvi.MviAction
import pl.mobite.sample.security.ui.base.mvi.MviResult

sealed class SecretKeyAction: MviAction {

    data class CheckKeyAction(val keyAlias: String): SecretKeyAction()

    data class GenerateNewKeyAction(val keyAlias: String): SecretKeyAction()

    data class RemoveKeyAction(val keyAlias: String): SecretKeyAction()

    data class EncryptMessageAction(val keyAlias: String, val messageToEncrypt: String): SecretKeyAction()

    data class DecryptMessageAction(val keyAlias: String, val messageToDecrypt: String): SecretKeyAction()

    object ClearMessagesAction: SecretKeyAction()
}

sealed class SecretKeyResult: MviResult {

    data class HasValidKeyResult(val keyAlias: String): SecretKeyResult()

    object NoValidKeyResult: SecretKeyResult()

    data class EncryptMessageResult(val keyAlias: String, val messageEncrypted: String): SecretKeyResult()

    data class DecryptMessageResult(val keyAlias: String, val messageEncrypted: String, val messageDecrypted: String): SecretKeyResult()

    object ClearMessagesResult: SecretKeyResult()

    object InFlightResult: SecretKeyResult()

    data class ErrorResult(val error: Throwable): SecretKeyResult()
}
