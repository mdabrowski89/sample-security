package pl.mobite.sample.security.di

import org.koin.dsl.module.module
import pl.mobite.sample.security.ui.base.mvi.SchedulerProvider
import pl.mobite.sample.security.utils.AndroidSchedulerProvider


val appModule = module {

    single<SchedulerProvider> { AndroidSchedulerProvider.instance }

}