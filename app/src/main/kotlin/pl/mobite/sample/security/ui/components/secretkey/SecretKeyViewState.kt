package pl.mobite.sample.security.ui.components.secretkey

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.mobite.sample.security.ui.base.MviViewState
import pl.mobite.sample.security.ui.base.ViewStateEvent
import java.util.concurrent.atomic.AtomicBoolean

@Parcelize
data class SecretKeyViewState(
        val secretKeyAlias: String?,
        val messageEncrypted: String?,
        val messageDecrypted: String?,
        val isLoading: Boolean,
        val clearMessage: AtomicBoolean,
        val error: ViewStateEvent<Throwable>?
) : Parcelable, MviViewState {

    companion object {
        fun default() = SecretKeyViewState(
                secretKeyAlias = null,
                messageEncrypted = null,
                messageDecrypted = null,
                isLoading = false,
                clearMessage = AtomicBoolean(),
                error = null
        )

        val PARCEL_KEY = SecretKeyViewState.toString()
    }
}

fun SecretKeyViewState.withKey(secretKeyAlias: String) = this.copy(
    isLoading = false,
    secretKeyAlias = secretKeyAlias,
    error = null
)

fun SecretKeyViewState.withoutKey() = this.copy(
    isLoading = false,
    secretKeyAlias = null,
    messageEncrypted = null,
    messageDecrypted = null,
    error = null
).apply { clearMessage.set(true) }

fun SecretKeyViewState.withMessageEncrypted(secretKeyAlias: String, messageEncrypted: String) = this.copy(
    isLoading = false,
    secretKeyAlias = secretKeyAlias,
    messageEncrypted = messageEncrypted,
    messageDecrypted = null,
    error = null
)

fun SecretKeyViewState.withMessageDecrypted(secretKeyAlias: String, messageEncrypted: String, messageDecrypted: String) = this.copy(
    isLoading = false,
    secretKeyAlias = secretKeyAlias,
    messageEncrypted = messageEncrypted,
    messageDecrypted = messageDecrypted,
    error = null
)

fun SecretKeyViewState.withMessageCleared() = this.copy(
    isLoading = false,
    messageEncrypted = null,
    messageDecrypted = null,
    error = null
).apply { clearMessage.set(true) }

fun SecretKeyViewState.loading() = this.copy(
    isLoading = true,
    error = null
)

fun SecretKeyViewState.withError(throwable: Throwable) = this.copy(
    isLoading = false,
    error = ViewStateEvent(throwable)
)