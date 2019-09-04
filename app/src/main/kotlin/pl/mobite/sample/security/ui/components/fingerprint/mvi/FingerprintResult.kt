package pl.mobite.sample.security.ui.components.fingerprint.mvi

import pl.mobite.sample.security.ui.base.mvi.MviResult


sealed class FingerprintResult: MviResult {

    object InFlightResult: FingerprintResult()

    data class ErrorResult(val t: Throwable): FingerprintResult()

    data class CheckPreconditionsResult(
        val isMarshmallow: Boolean,
        val hasFingerprintScanner: Boolean,
        val hasFingerprintEnrolled: Boolean
    ): FingerprintResult()

}