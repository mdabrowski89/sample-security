package pl.mobite.sample.security.ui.components.fingerprint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_fingerprint.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.base.mvi.provide


class FingerprintFragment: Fragment() {

    private val viewModel: FingerprintViewModel by lazy { provide(FingerprintViewModel::class.java) }
    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fingerprint, container, false)
    }

    override fun onStart() {
        super.onStart()
        viewModel.subscribe(::render).addTo(compositeDisposable)
        viewModel.onStart()
    }

    override fun onStop() {
        compositeDisposable.dispose()
        super.onStop()
    }

    private fun render(viewState: FingerprintViewState) {
        with(viewState) {
            val hasError = !isMarshmallow || !isHardwareAvailable || !hasFingerprintEnrolled
            fingerprintContentView.isVisible = !hasError
            errorText.isVisible = hasError
            if (!isMarshmallow) {
                errorText.text = getString(R.string.fingerprint_error_pre_marshmallow)
            } else if (!isHardwareAvailable) {
                errorText.text = getString(R.string.fingerprint_error_missing_hardware)
            } else if (!hasFingerprintEnrolled) {
                errorText.text = getString(R.string.fingerprint_error_missing_fingerprints)
            } else {
                // TODO: implement
            }
        }
    }

}