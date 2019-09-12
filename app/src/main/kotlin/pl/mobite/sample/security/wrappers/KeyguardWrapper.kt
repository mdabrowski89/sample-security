package pl.mobite.sample.security.wrappers

import android.app.KeyguardManager
import android.content.Context
import pl.mobite.sample.security.utils.hasMarshmallow


class KeyguardWrapper(
    context: Context
) {

    private val keyguardManager: KeyguardManager? = if (hasMarshmallow()) {
        context.getSystemService(KeyguardManager::class.java)
    } else {
        null
    }

    fun isDeviceSecure(): Boolean {
        return hasMarshmallow() && keyguardManager?.isDeviceSecure ?: false
    }
}