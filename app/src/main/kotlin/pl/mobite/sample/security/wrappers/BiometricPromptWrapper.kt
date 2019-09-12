package pl.mobite.sample.security.wrappers

import android.app.Activity
import android.content.DialogInterface
import android.hardware.biometrics.BiometricPrompt
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.components.fingerprint.fingerprintdialog.FingerprintAuthenticationDialogFragment
import pl.mobite.sample.security.utils.hasMarshmallow
import pl.mobite.sample.security.utils.hasPie
import javax.crypto.Cipher


class BiometricPromptWrapper {

    fun authenticate(activity: FragmentActivity, cipher: Cipher, onSuccess: (Cipher) -> Unit, onFailed: () -> Unit) {
        when {
            hasPie() -> authenticateApi28(activity, cipher, onSuccess, onFailed)
            hasMarshmallow() -> authenticateApi23(activity, cipher, onSuccess, onFailed)
            else -> onFailed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun authenticateApi23(activity: FragmentActivity, cipher: Cipher, onSuccess: (Cipher) -> Unit, onFailed: () -> Unit) {
        val fragment = FingerprintAuthenticationDialogFragment(cipher)
        fragment.setCallback(object: FingerprintAuthenticationDialogFragment.Callback {

            override fun authenticated(cryptoObject: FingerprintManager.CryptoObject?) {
                cryptoObject?.cipher?.let { onSuccess(it) } ?: onFailed()
            }

            override fun error() {
                onFailed()
            }
        })
        fragment.show(activity.supportFragmentManager, "FingerprintAuthenticationDialogFragment")
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun authenticateApi28(activity: Activity, cipher: Cipher, onSuccess: (Cipher) -> Unit, onFailed: () -> Unit) {
        val biometricPrompt = BiometricPrompt.Builder(activity)
            .setTitle(activity.getString(R.string.fingerprint_confirmation_title))
            .setNegativeButton(activity.getString(R.string.button_cancel), activity.mainExecutor,
                DialogInterface.OnClickListener { _, _ -> onFailed() })
            .build()
        val cryptoObject = BiometricPrompt.CryptoObject(cipher)
        biometricPrompt.authenticate(cryptoObject, CancellationSignal(), activity.mainExecutor, object: BiometricPrompt.AuthenticationCallback() {

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                onFailed()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                result?.cryptoObject?.cipher?.let { onSuccess(it) } ?: onFailed()
            }

            override fun onAuthenticationFailed() {
                onFailed()
            }
        })
    }
}