package pl.mobite.sample.security.data.repositories

import io.reactivex.Completable
import io.reactivex.Single


interface SecretKeyRepository {

    /* emit true is key is available in keystore, false otherwise */
    fun checkKey(keyAlias: String): Single<Boolean>

    /* completes if key was successfully generated */
    fun generateKey(keyAlias: String): Completable

    /* completes if key was successfully removed */
    fun removeKey(keyAlias: String): Completable

    /* emit encrypted message */
    fun encrypt(keyAlias: String, message: String): Single<String>

    /* emit decrypted message */
    fun decrypt(keyAlias: String, message: String): Single<String>
}