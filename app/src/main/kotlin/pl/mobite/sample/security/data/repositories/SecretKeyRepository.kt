package pl.mobite.sample.security.data.repositories


interface SecretKeyRepository {

    /* onNextSafe true is key is available in keystore, false otherwise */
    @Throws(Throwable::class)
    fun checkKey(keyAlias: String): Boolean

    /* completes if key was successfully generated */
    @Throws(Throwable::class)
    fun generateKey(keyAlias: String)

    /* completes if key was successfully removed */
    @Throws(Throwable::class)
    fun removeKey(keyAlias: String)

    /* onNextSafe encrypted message */
    @Throws(Throwable::class)
    fun encrypt(keyAlias: String, message: String): String

    /* onNextSafe decrypted message */
    @Throws(Throwable::class)
    fun decrypt(keyAlias: String, message: String): String
}