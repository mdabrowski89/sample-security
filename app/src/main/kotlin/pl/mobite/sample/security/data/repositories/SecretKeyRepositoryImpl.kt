package pl.mobite.sample.security.data.repositories


class SecretKeyRepositoryImpl: SecretKeyRepository {

    private var hasKey: Boolean = false

    override fun checkKey(keyAlias: String): Boolean {
        // TODO: implement
        return hasKey
    }

    override fun generateKey(keyAlias: String) {
        // TODO: implement
        hasKey = true
    }

    override fun removeKey(keyAlias: String) {
        // TODO: implement
        hasKey = false
    }

    override fun encrypt(keyAlias: String, message: String): String {
        // TODO: implement
        return "dummy encrypted message"
    }

    override fun decrypt(keyAlias: String, message: String): String {
        // TODO: implement
        //return "dummy decrypted message"
        throw Throwable("dummy decrypted message")
    }

}