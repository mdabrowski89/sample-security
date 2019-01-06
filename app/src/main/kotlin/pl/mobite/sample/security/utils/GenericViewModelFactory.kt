package pl.mobite.sample.security.utils

import androidx.lifecycle.ViewModel
import pl.mobite.sample.security.data.repositories.SecretKeyRepositoryImpl
import pl.mobite.sample.security.ui.base.mvi.MviViewModelFactory
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyViewModel
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyViewState

class GenericViewModelFactory: MviViewModelFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == SecretKeyViewModel::class.java) {
            return SecretKeyViewModel(
                SecretKeyRepositoryImpl(),
                AndroidSchedulerProvider.instance,
                args[0] as SecretKeyViewState?
            ) as T
        }
        throw IllegalStateException("Unknown view model class")
    }
}