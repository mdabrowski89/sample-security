package pl.mobite.sample.security.ui.components.secretkey

import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.base.mvi.MviViewModel
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyActionProcessor
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult
import pl.mobite.sample.security.utils.SchedulerProvider


class SecretKeyViewModel(
    secretKeyRepository: SecretKeyRepository,
    schedulerProvider: SchedulerProvider,
    initialState: SecretKeyViewState?
): MviViewModel<SecretKeyAction, SecretKeyResult, SecretKeyViewState>(
    SecretKeyActionProcessor(secretKeyRepository, schedulerProvider),
    initialState ?: SecretKeyViewState.default()
)