package pl.mobite.sample.security.ui.components.pin

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

                decryptionCipherReadyEvent?.consume { cipher ->
                    // TODO: prepare authenticated cipher
                    viewModel.onDecryptionCipherReady(cipher)
                }
            }
        }
    }
}