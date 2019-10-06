/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package pl.mobite.sample.security.ui.components.fingerprint.fingerprintdialog

import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import pl.mobite.sample.security.R
import javax.crypto.Cipher

/**
 * A dialog which uses fingerprint APIs to authenticate the user
 * Based on: https://github.com/googlesamples/android-FingerprintDialog/blob/master/kotlinApp/app/src/main/java/com/example/android/fingerprintdialog/FingerprintAuthenticationDialogFragment.kt
 */
@RequiresApi(Build.VERSION_CODES.M)
class FingerprintAuthenticationDialogFragment(
    private val cipher: Cipher
): DialogFragment(), FingerprintUiHelper.Callback {

    private lateinit var cancelButton: Button
    private lateinit var fingerprintContainer: View

    private lateinit var callback: Callback
    private lateinit var fingerprintUiHelper: FingerprintUiHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        retainInstance = true
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.setTitle(getString(R.string.fingerprint_confirmation_title))
        return inflater.inflate(R.layout.fingerprint_dialog_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cancelButton = view.findViewById(R.id.cancel_button)
        fingerprintContainer = view.findViewById(R.id.fingerprint_container)

        cancelButton.setOnClickListener { dismiss() }

        fingerprintUiHelper = FingerprintUiHelper(
            fingerprintMgr = requireActivity().getSystemService(FingerprintManager::class.java),
            icon = view.findViewById(R.id.fingerprint_icon),
            errorTextView = view.findViewById(R.id.fingerprint_status),
            cryptoObject = FingerprintManager.CryptoObject(cipher),
            callback = this
        )

        // If fingerprint authentication is not available, switch immediately to the backup
        // (password) screen.
        if (!fingerprintUiHelper.isFingerprintAuthAvailable) {
            Toast.makeText(
                requireContext(),
                "Fingerprint not available on this device",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()
        fingerprintUiHelper.startListening()
    }

    override fun onPause() {
        super.onPause()
        fingerprintUiHelper.stopListening()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    override fun onError() {
        callback.error()
    }

    override fun onAuthenticated(cryptoObject: FingerprintManager.CryptoObject?) {
        // Callback from FingerprintUiHelper. Let the activity know that authentication succeeded.
        callback.authenticated(cryptoObject)
        dismiss()
    }

    interface Callback {
        fun authenticated(cryptoObject: FingerprintManager.CryptoObject?)
        fun error()
    }
}
