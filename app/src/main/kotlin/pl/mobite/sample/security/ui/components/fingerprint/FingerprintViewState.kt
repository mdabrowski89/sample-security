package pl.mobite.sample.security.ui.components.fingerprint

import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import pl.mobite.sample.security.ui.base.mvi.MviViewState
import pl.mobite.sample.security.ui.base.mvi.ViewStateEmptyEvent
import pl.mobite.sample.security.ui.base.mvi.ViewStateErrorEvent
import pl.mobite.sample.security.ui.base.mvi.ViewStateNonParcelableEvent
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult.*
import javax.crypto.Cipher

@Parcelize
data class FingerprintViewState(
    val isMarshmallow: Boolean,
    val isHardwareAvailable: Boolean,
    val hasFingerprintEnrolled: Boolean,
    val isDeviceSecure: Boolean,
    val inProgress: Boolean,
    val secretKeyAlias: String?,
    val messageEncrypted: String?,
    val messageDecrypted: String?,
    val clearEvent: ViewStateEmptyEvent?,
    val errorEvent: ViewStateErrorEvent?
): MviViewState<FingerprintResult> {

    @IgnoredOnParcel
    var encryptionCipherReadyEvent: ViewStateNonParcelableEvent<Cipher>? = null
    @IgnoredOnParcel
    var decryptionCipherReadyEvent: ViewStateNonParcelableEvent<Cipher>? = null

    companion object {

        fun default() = FingerprintViewState(
            isMarshmallow = false,
            isHardwareAvailable = false,
            hasFingerprintEnrolled = false,
            isDeviceSecure = false,
            inProgress = false,
            secretKeyAlias = null,
            messageEncrypted = null,
            messageDecrypted = null,
            clearEvent = null,
            errorEvent = null
        )
    }

    override fun reduce(result: FingerprintResult): FingerprintViewState {
        return when (result) {
            is InFlightResult -> result.reduce()
            is ErrorResult -> result.reduce()
            is CheckPreconditionsResult -> result.reduce()
            is HasValidKeyResult -> result.reduce()
            is NoValidKeyResult -> result.reduce()
            is EncryptionCipherReadyResult -> result.reduce()
            is EncryptMessageResult -> result.reduce()
            is DecryptionCipherReadyResult -> result.reduce()
            is DecryptMessageResult -> result.reduce()
            is ClearMessagesResult -> result.reduce()
        }
    }

    override fun isSavable() = !inProgress

    private fun InFlightResult.reduce() = copy(
        inProgress = true,
        errorEvent = null
    )

    private fun ErrorResult.reduce() = copy(
        inProgress = false,
        errorEvent = ViewStateErrorEvent(t)
    )

    private fun CheckPreconditionsResult.reduce() = copy(
        inProgress = false,
        errorEvent = null,
        isMarshmallow = this.isMarshmallow,
        isHardwareAvailable = this.hasFingerprintScanner,
        hasFingerprintEnrolled = this.hasFingerprintEnrolled,
        isDeviceSecure = this.isDeviceSecure
    )
    private fun HasValidKeyResult.reduce() = copy(
        inProgress = false,
        secretKeyAlias = keyAlias,
        errorEvent = null
    )

    private fun NoValidKeyResult.reduce() = copy(
        inProgress = false,
        secretKeyAlias = null,
        messageEncrypted = null,
        messageDecrypted = null,
        errorEvent = null,
        clearEvent = ViewStateEmptyEvent()
    )

    private fun EncryptionCipherReadyResult.reduce() = copy(
        inProgress = false,
        messageDecrypted = null,
        errorEvent = null
    ).apply {
        encryptionCipherReadyEvent = ViewStateNonParcelableEvent(authenticatedCipher)
    }

    private fun EncryptMessageResult.reduce() = copy(
        inProgress = false,
        secretKeyAlias = keyAlias,
        messageEncrypted = messageEncrypted,
        messageDecrypted = null,
        errorEvent = null
    )

    private fun DecryptionCipherReadyResult.reduce() = copy(
        inProgress = false,
        errorEvent = null
    ).apply {
        decryptionCipherReadyEvent = ViewStateNonParcelableEvent(authenticatedCipher)
    }

    private fun DecryptMessageResult.reduce() = copy(
        inProgress = false,
        secretKeyAlias = keyAlias,
        messageEncrypted = messageEncrypted,
        messageDecrypted = messageDecrypted,
        errorEvent = null
    )

    private fun ClearMessagesResult.reduce() = copy(
        inProgress = false,
        messageEncrypted = null,
        messageDecrypted = null,
        errorEvent = null,
        clearEvent = ViewStateEmptyEvent()
    )
}