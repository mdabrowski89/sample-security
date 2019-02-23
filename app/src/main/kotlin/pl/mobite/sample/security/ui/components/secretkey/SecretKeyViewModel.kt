package pl.mobite.sample.security.ui.components.secretkey

import pl.mobite.sample.security.ui.base.mvi.MviViewModel
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyActionProcessor
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult


class SecretKeyViewModel: MviViewModel<SecretKeyAction, SecretKeyResult, SecretKeyViewState>(
    SecretKeyActionProcessor(),
    SecretKeyViewState.default()
)