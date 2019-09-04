package pl.mobite.sample.security.ui.components.fingerprint.mvi

import pl.mobite.sample.security.ui.base.mvi.MviAction


sealed class FingerprintAction: MviAction {

    object CheckPreconditionsAction: FingerprintAction()
}