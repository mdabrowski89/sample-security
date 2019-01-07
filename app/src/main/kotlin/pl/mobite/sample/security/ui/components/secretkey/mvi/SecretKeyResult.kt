package pl.mobite.sample.security.ui.components.secretkey.mvi

import pl.mobite.sample.security.ui.base.mvi.MviResult

sealed class SecretKeyResult: MviResult {

    data class HasValidKeyResult(val keyAlias: String): SecretKeyResult()

    object NoValidKeyResult: SecretKeyResult()

    data class EncryptMessageResult(val keyAlias: String, val messageEncrypted: String): SecretKeyResult()

    data class DecryptMessageResult(val keyAlias: String, val messageEncrypted: String, val messageDecrypted: String):
        SecretKeyResult()

    object ClearMessagesResult: SecretKeyResult()

    object InFlightResult: SecretKeyResult()

    data class ErrorResult(val error: Throwable): SecretKeyResult()
}
