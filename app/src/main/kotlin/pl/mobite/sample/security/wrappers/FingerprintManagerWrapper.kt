package pl.mobite.sample.security.wrappers

import android.content.Context
import androidx.core.hardware.fingerprint.FingerprintManagerCompat


class FingerprintManagerWrapper(
    context: Context
) {
    private val fingerprintManager = FingerprintManagerCompat.from(context)

    fun isHardwareDetected(): Boolean {
        return fingerprintManager.isHardwareDetected
    }

    fun hasEnrolledFingerprints(): Boolean {
        return fingerprintManager.hasEnrolledFingerprints()
    }
}