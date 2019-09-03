package pl.mobite.sample.security.encryption

import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.*
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.security.auth.x500.X500Principal


class KeystoreWrapper(
    private val context: Context
) {

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")

    init {
        keyStore.load(null)
    }

    fun getAsymmetricKeyPair(alias: String): KeyPair? {
        val privateKey = keyStore.getKey(alias, null) as PrivateKey?
        val publicKey = keyStore.getCertificate(alias)?.publicKey
        return if (privateKey != null && publicKey != null) {
            KeyPair(publicKey, privateKey)
        } else {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun getSymmetricKey(alias: String): SecretKey? {
        return keyStore.getKey(alias, null) as SecretKey?
    }

    fun removeKey(alias: String) {
        keyStore.deleteEntry(alias)
    }

    fun generateAsymmetricKey(alias: String): KeyPair {
        val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        endDate.add(Calendar.YEAR, 20)

        val builder = KeyPairGeneratorSpec.Builder(context)
            .setAlias(alias)
            .setSerialNumber(BigInteger.ONE)
            .setSubject(X500Principal("CN=${alias} CA Certificate"))
            .setStartDate(startDate.time)
            .setEndDate(endDate.time)

        generator.initialize(builder.build())
        return generator.generateKeyPair()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun generateAsymmetricKeyApi23(alias: String) {
        val generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore")
        val purpose = KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        val builder = KeyGenParameterSpec.Builder(alias, purpose)
            .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
        generator.initialize(builder.build())
        generator.generateKeyPair()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun generateSymmetricKeyApi23(alias: String) {
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
        keyGenerator.init(builder.build())
        keyGenerator.generateKey()
    }

    fun generateDefaultSymmetricKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        return keyGenerator.generateKey()
    }
}