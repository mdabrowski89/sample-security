package pl.mobite.sample.security.ui.components.fingerprint.mvi

import pl.mobite.sample.security.ui.base.mvi.MviResult
import javax.crypto.Cipher
import javax.crypto.SecretKey


sealed class FingerprintResult: MviResult {

    object InFlightResult: FingerprintResult()

    data class ErrorResult(val t: Throwable): FingerprintResult()

    data class CheckPreconditionsResult(
        val isMarshmallow: Boolean,
        val hasFingerprintScanner: Boolean,
        val hasFingerprintEnrolled: Boolean,
        val isDeviceSecure: Boolean
    ): FingerprintResult()

    data class HasValidKeyResult(val secretKey: SecretKey, val keyAlias: String): FingerprintResult()

    object NoValidKeyResult: FingerprintResult()

    data class EncryptionCipherReadyResult(val encryptionCipher: Cipher, val messageToEncrypt: String): FingerprintResult()

    data class EncryptMessageResult(val messageEncrypted: String): FingerprintResult()

    data class DecryptionCipherReadyResult(val decryptionCipher: Cipher): FingerprintResult()

    data class DecryptMessageResult(val messageDecrypted: String): FingerprintResult()

    object ClearMessagesResult: FingerprintResult()
}