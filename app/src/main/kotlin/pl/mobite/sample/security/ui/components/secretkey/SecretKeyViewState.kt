package pl.mobite.sample.security.ui.components.secretkey

import android.os.Parcel
import android.os.Parcelable
import pl.mobite.sample.security.data.models.ViewStateError
import java.util.concurrent.atomic.AtomicBoolean


data class SecretKeyViewState(
        val secretKeyAlias: String?,
        val messageEncrypted: String?,
        val messageDecrypted: String?,
        val isLoading: Boolean,
        val clearMessage: AtomicBoolean,
        val error: ViewStateError?
) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readSerializable() as AtomicBoolean,
            source.readParcelable<ViewStateError>(ViewStateError::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(secretKeyAlias)
        writeString(messageEncrypted)
        writeString(messageDecrypted)
        writeInt((if (isLoading) 1 else 0))
        writeSerializable(clearMessage)
        writeParcelable(error, 0)
    }

    companion object {
        fun default() = SecretKeyViewState(
                secretKeyAlias = null,
                messageEncrypted = null,
                messageDecrypted = null,
                isLoading = false,
                clearMessage = AtomicBoolean(),
                error = null
        )

        @JvmField
        val CREATOR: Parcelable.Creator<SecretKeyViewState> = object : Parcelable.Creator<SecretKeyViewState> {
            override fun createFromParcel(source: Parcel): SecretKeyViewState = SecretKeyViewState(source)
            override fun newArray(size: Int): Array<SecretKeyViewState?> = arrayOfNulls(size)
        }

        val PARCEL_KEY = SecretKeyViewState.toString()
    }
}