package pl.mobite.sample.security.data.local

import android.content.Context


class EncryptionPreferences(
    context: Context
): SharedPrefs(context, "EncryptionPreferences") {

    var encryptedSecretKey by sharedPrefs.string("encryptedSecretKey", null)

    var secretKeyIv by sharedPrefs.string("secretKeyIv", null)

    var fingerprintIv by sharedPrefs.string("fingerprintIv", null)
}