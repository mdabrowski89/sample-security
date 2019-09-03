package pl.mobite.sample.security

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.mobite.sample.security.di.appModule
import pl.mobite.sample.security.di.encryptionModule
import pl.mobite.sample.security.di.repositoriesModule


class SecurityApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@SecurityApp)
            modules(appModule, repositoriesModule, encryptionModule)
        }
    }

    companion object {

        @JvmStatic
        lateinit var instance: SecurityApp
            private set
    }
}