package pl.mobite.sample.security.ui.components.secretkey.mvi

import pl.mobite.sample.security.ui.base.mvi.MviResult
import javax.crypto.SecretKey

sealed class SecretKeyResult: MviResult {

    data class HasKeyResult(val secretKey: SecretKey, val keyAlias: String): SecretKeyResult()

    object NoKeyResult: SecretKeyResult()

    data class EncryptMessageResult(val messageEncrypted: String): SecretKeyResult()

    data class DecryptMessageResult(val messageDecrypted: String): SecretKeyResult()

    object ClearMessagesResult: SecretKeyResult()

    object InFlightResult: SecretKeyResult()

    data class ErrorResult(val error: Throwable): SecretKeyResult()
}
