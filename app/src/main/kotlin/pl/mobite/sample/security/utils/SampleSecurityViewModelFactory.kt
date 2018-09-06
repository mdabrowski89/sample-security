package pl.mobite.sample.security.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.mobite.sample.security.data.repositories.SecretKeyRepositoryImpl
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyViewModel
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyViewState

class SampleSecurityViewModelFactory private constructor(private val args: Array<out Any?>) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass == SecretKeyViewModel::class.java) {
            return SecretKeyViewModel(
                    SecretKeyRepositoryImpl(),
                    AndroidSchedulerProvider.instance,
                    args[0] as SecretKeyViewState?) as T
        }
        throw IllegalStateException("Unknown view model class")
    }

    companion object {

        fun getInstance(vararg args: Any?) = SampleSecurityViewModelFactory(args)
    }
}