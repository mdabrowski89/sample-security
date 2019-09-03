package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.ui.base.mvi.DefaultSchedulersProvider
import pl.mobite.sample.security.ui.base.mvi.SchedulersProvider


val appModule = module {

    single<SchedulersProvider> { DefaultSchedulersProvider.instance }
}