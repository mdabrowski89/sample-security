package pl.mobite.sample.security.data.repositories

import io.reactivex.Completable
import io.reactivex.Single
import java.util.concurrent.TimeUnit


class SecretKeyRepositoryImpl: SecretKeyRepository {

    private var hasKey: Boolean = false

    override fun checkKey(keyAlias: String): Single<Boolean> {
        // TODO: implement
        return Single.just(hasKey)
    }

    override fun generateKey(keyAlias: String): Completable {
        // TODO: implement
        hasKey = true
        return Completable.complete()
    }

    override fun removeKey(keyAlias: String): Completable {
        // TODO: implement
        return Completable.complete()
    }

    override fun encrypt(keyAlias: String, message: String): Single<String> {
        // TODO: implement
        return Single.just("dummy encrypted message").delay(2, TimeUnit.SECONDS)
    }

    override fun decrypt(keyAlias: String, message: String): Single<String> {
        // TODO: implement
       //return Single.just("dummy decrypted message")
        return Single.error(Throwable("dummy decrypted message"))
    }

}