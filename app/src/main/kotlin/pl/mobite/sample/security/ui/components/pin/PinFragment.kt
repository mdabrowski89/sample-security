package pl.mobite.sample.security.ui.components.pin

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_fingerprint.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.base.mvi.provide


class PinFragment: Fragment() {

    private val viewModel: PinViewModel by lazy { provide(PinViewModel::class.java) }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_pin, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        encryptionFormView.init(
            generateKeyAction = viewModel::generateNewKey,
            removeKeyAction = viewModel::removeKey,
            encryptAction = viewModel::encryptMessage,
            decryptAction = viewModel::decryptMessage,
            clearAction = viewModel::clearMessage
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.subscribe(::render).addTo(compositeDisposable)
        viewModel.onStart()
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == KEYGUARD_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.onUserAuthenticated()
        }
    }

    private fun render(viewState: PinViewState) {
        with(viewState) {
            val hasError = !isMarshmallow || !isDeviceSecure
            encryptionFormView.isVisible = !hasError
            errorText.isVisible = hasError
            if (!isMarshmallow) {
                errorText.text = getString(R.string.fingerprint_error_pre_marshmallow)
            } else if (!isDeviceSecure) {
                errorText.text = getString(R.string.fingerprint_error_device_is_not_secure)
            } else {
                encryptionFormView.render(encryptionFormViewState)

                errorEvent?.consume {
                    it.printStackTrace()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                authenticationRequired?.consume {
                    val keyguardIntent = (requireContext().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager)
                        .createConfirmDeviceCredentialIntent(getString(R.string.app_name), getString(R.string.pin_keyguard_description))
                    if (keyguardIntent != null) {
                        startActivityForResult(keyguardIntent, KEYGUARD_REQUEST_CODE)
                    } else {
                        Toast.makeText(context, "Missing keyguard", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    companion object {

        const val KEYGUARD_REQUEST_CODE = 801
    }
}