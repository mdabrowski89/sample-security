package pl.mobite.sample.security.ui.base.mvi

import android.os.Parcelable
import androidx.lifecycle.ViewModelProvider


interface MviAction

interface MviResult

interface MviViewState<R: MviResult>: Parcelable {

    fun reduce(result: R): MviViewState<R>

    fun isSavable(): Boolean
}

abstract class MviViewModelFactory: ViewModelProvider.Factory {

    protected lateinit var args: Array<out Any?>

    fun withArgs(vararg args: Any?): MviViewModelFactory {
        this.args = args
        return this
    }
}
