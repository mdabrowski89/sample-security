package pl.mobite.sample.security

import android.app.Application
import org.koin.android.ext.android.startKoin
import pl.mobite.sample.security.di.appModule
import pl.mobite.sample.security.di.repositoriesModule


class SecurityApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        initKoin()
    }

    private fun initKoin() {
        startKoin(this, listOf(appModule, repositoriesModule))
    }

    companion object {

        @JvmStatic
        lateinit var instance: SecurityApp
            private set
    }
}