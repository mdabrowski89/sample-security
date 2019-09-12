package pl.mobite.sample.security.ui.components.fingerprint.mvi

import pl.mobite.sample.security.ui.base.mvi.MviResult
import javax.crypto.Cipher


sealed class FingerprintResult: MviResult {

    object InFlightResult: FingerprintResult()

    data class ErrorResult(val t: Throwable): FingerprintResult()

    data class CheckPreconditionsResult(
        val isMarshmallow: Boolean,
        val hasFingerprintScanner: Boolean,
        val hasFingerprintEnrolled: Boolean,
        val isDeviceSecure: Boolean
    ): FingerprintResult()

    data class HasValidKeyResult(val keyAlias: String): FingerprintResult()

    object NoValidKeyResult: FingerprintResult()

    data class EncryptionCipherReadyResult(val authenticatedCipher: Cipher): FingerprintResult()

    data class EncryptMessageResult(val keyAlias: String, val messageEncrypted: String): FingerprintResult()

    data class DecryptionCipherReadyResult(val authenticatedCipher: Cipher): FingerprintResult()

    data class DecryptMessageResult(val keyAlias: String, val messageEncrypted: String, val messageDecrypted: String):
        FingerprintResult()

    object ClearMessagesResult: FingerprintResult()
}