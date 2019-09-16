package pl.mobite.sample.security.ui.custom.encryptionform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.mobite.sample.security.ui.base.mvi.ViewStateEmptyEvent


@Parcelize
data class EncryptionFormViewState(
    val inProgress: Boolean,
    val keyAlias: String?,
    val messageEncrypted: String?,
    val messageDecrypted: String?,
    val clearEvent: ViewStateEmptyEvent?
): Parcelable {
    companion object {

        fun default() = EncryptionFormViewState(
            inProgress = false,
            keyAlias = null,
            messageEncrypted = null,
            messageDecrypted = null,
            clearEvent = null
        )
    }
}