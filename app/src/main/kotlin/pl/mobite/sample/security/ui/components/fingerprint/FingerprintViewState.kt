package pl.mobite.sample.security.ui.components.fingerprint

import kotlinx.android.parcel.Parcelize
import pl.mobite.sample.security.ui.base.mvi.MviViewState
import pl.mobite.sample.security.ui.base.mvi.ViewStateErrorEvent
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult.*

@Parcelize
data class FingerprintViewState(
    val isMarshmallow: Boolean,
    val isHardwareAvailable: Boolean,
    val hasFingerprintEnrolled: Boolean,
    val inProgress: Boolean,
    val error: ViewStateErrorEvent?
): MviViewState<FingerprintResult> {

    companion object {

        fun default() = FingerprintViewState(
            isMarshmallow = false,
            isHardwareAvailable = false,
            hasFingerprintEnrolled = false,
            inProgress = true,
            error = null
        )
    }

    override fun reduce(result: FingerprintResult): FingerprintViewState {
        return when (result) {
            is InFlightResult -> result.reduce()
            is ErrorResult -> result.reduce()
            is CheckPreconditionsResult -> result.reduce()
        }
    }

    override fun isSavable() = !inProgress

    private fun InFlightResult.reduce() = copy(
        inProgress = true,
        error = null
    )

    private fun ErrorResult.reduce() = copy(
        inProgress = false,
        error = ViewStateErrorEvent(t)
    )

    private fun CheckPreconditionsResult.reduce() = copy(
        isMarshmallow = this.isMarshmallow,
        isHardwareAvailable = this.hasFingerprintScanner,
        hasFingerprintEnrolled = this.hasFingerprintEnrolled
    )
}