package pl.mobite.sample.security.ui.components.fingerprint

import androidx.lifecycle.SavedStateHandle
import pl.mobite.sample.security.ui.base.mvi.MviViewModel
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintAction
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintAction.CheckPreconditionsAction
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintActionProcessor
import pl.mobite.sample.security.ui.components.fingerprint.mvi.FingerprintResult


class FingerprintViewModel(
    savedStateHandle : SavedStateHandle
): MviViewModel<FingerprintAction, FingerprintResult, FingerprintViewState>(
    savedStateHandle,
    FingerprintActionProcessor(),
    FingerprintViewState.default()
) {

    fun onStart() {
        accept(CheckPreconditionsAction)
    }

    companion object {

        private const val KEY_ALIAS = "BUTTERCUP"
    }
}