package pl.mobite.sample.security.ui.components.secretkey


data class SecretKeyViewState(
        val secretKeyAlias: String?,
        val messageToEncrypt: String?,
        val messageEncrypted: String?,
        val messageDecrypted: String?,
        val isLoading: Boolean,
        val error: Throwable?
) {

    companion object {

        fun default() = SecretKeyViewState(
                secretKeyAlias = null,
                messageToEncrypt = null,
                messageEncrypted = null,
                messageDecrypted = null,
                isLoading = false,
                error = null
        )
    }
}