package pl.mobite.sample.security.encryption


data class EncryptionResult(val encryptedMessage: String, val initializationVector: String)