package pl.mobite.sample.security

import android.app.Application


class SecurityApp: Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        @JvmStatic lateinit var instance: SecurityApp
            private set
    }
}