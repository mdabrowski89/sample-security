package pl.mobite.sample.security.ui.components.fingerprint.mvi

import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import org.koin.core.KoinComponent
import org.koin.core.inject
import pl.mobite.sample.security.ui.base.mvi.*
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintAction.CheckPreconditionsAction
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult.*
import pl.mobite.sample.security.uscases.CheckFingerprintEnrolledUseCase
import pl.mobite.sample.security.uscases.CheckFingerprintHardwareUseCase
import pl.mobite.sample.security.utils.hasMarshmallow


class FingerprintActionProcessor: MviActionsProcessor<FingerprintAction, FingerprintResult>(), KoinComponent {

    private val schedulersProvider: SchedulersProvider by inject()
    private val checkFingerprintHardwareUseCase: CheckFingerprintHardwareUseCase by inject()
    private val checkFingerprintEnrolledUseCase: CheckFingerprintEnrolledUseCase by inject()

    override fun getActionProcessors(shared: Observable<FingerprintAction>) = listOf(
        shared.connect(checkPreconditionsProcessor)
    )

    private val checkPreconditionsProcessor =
        createFingerprintActionProcessor<CheckPreconditionsAction> {
            val isMarshmallow = hasMarshmallow()
            val hasFingerprintScanner = checkFingerprintHardwareUseCase()
            val hasFingerprintEnrolled = checkFingerprintEnrolledUseCase()
            onNextSafe(CheckPreconditionsResult(isMarshmallow, hasFingerprintScanner, hasFingerprintEnrolled))
            onCompleteSafe()
        }

    private fun <A: FingerprintAction>createFingerprintActionProcessor(
        doStuff: ObservableEmitter<FingerprintResult>.(action: A) -> Unit
    ) = createActionProcessor(
        schedulersProvider,
        { InFlightResult },
        { ErrorResult(it) },
        doStuff
    )
}