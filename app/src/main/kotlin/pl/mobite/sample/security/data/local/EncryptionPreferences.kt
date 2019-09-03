package pl.mobite.sample.security.data.local

import android.content.Context


class EncryptionPreferences(
    context: Context
): SharedPrefs(context, "EncryptionPreferences") {

    var encryptedSecretKey by sharedPrefs.string("encryptedSecretKey", null)

    var initializationVector by sharedPrefs.string("initializationVector", null)
}