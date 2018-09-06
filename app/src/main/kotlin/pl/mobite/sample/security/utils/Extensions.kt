package pl.mobite.sample.security.utils

import android.view.View
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


fun <T> Observable<T>.debounceButton(): Observable<T> {
    return this.debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
}

fun View.setVisibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}