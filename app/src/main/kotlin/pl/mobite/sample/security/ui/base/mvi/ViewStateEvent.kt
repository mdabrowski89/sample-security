package pl.mobite.sample.security.ui.base.mvi

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.concurrent.atomic.AtomicBoolean


@Parcelize
data class ViewStateEvent<T: Serializable>(
    val payload: T,
    val isConsumed: AtomicBoolean = AtomicBoolean(false)
): Parcelable {

    fun isNotConsumed(setAsConsumed: Boolean = true) = !isConsumed.getAndSet(setAsConsumed)
}