package pl.mobite.sample.security.ui.components.pin.mvi

import pl.mobite.sample.security.ui.base.mvi.MviResult
import java.security.KeyPair


sealed class PinResult: MviResult {

    object InFlightResult: PinResult()

    data class ErrorResult(val t: Throwable): PinResult()

    data class CheckPreconditionsResult(
        val isMarshmallow: Boolean,
        val isDeviceSecure: Boolean
    ): PinResult()

    data class HasValidKeyResult(val keyPair: KeyPair, val keyAlias: String): PinResult()

    object NoValidKeyResult: PinResult()

    data class EncryptMessageResult(val messageEncrypted: String): PinResult()

    object AuthenticationRequiredResult: PinResult()

    data class DecryptMessageResult(val messageDecrypted: String): PinResult()

    object ClearMessagesResult: PinResult()
}