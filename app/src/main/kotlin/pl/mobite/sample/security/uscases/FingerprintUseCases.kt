package pl.mobite.sample.security.uscases

import pl.mobite.sample.security.wrappers.FingerprintManagerWrapper


interface CheckFingerprintHardwareUseCase: () -> Boolean
interface CheckFingerprintEnrolledUseCase: () -> Boolean

class CheckFingerprintHardwareUseCaseImpl(
    private val fingerprintManagerWrapper: FingerprintManagerWrapper
): CheckFingerprintHardwareUseCase {

    override fun invoke(): Boolean {
        return fingerprintManagerWrapper.isHardwareDetected()
    }
}

class CheckFingerprintEnrolledUseCaseImpl(
    private val fingerprintManagerWrapper: FingerprintManagerWrapper
): CheckFingerprintEnrolledUseCase {

    override fun invoke(): Boolean {
        return fingerprintManagerWrapper.hasEnrolledFingerprints()
    }
}