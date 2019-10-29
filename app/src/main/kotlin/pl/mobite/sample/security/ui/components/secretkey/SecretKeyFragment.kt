package pl.mobite.sample.security.ui.components.secretkey

import android.os.Bundle
import android.view.View
import android.widget.Toast
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_secret_key.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.base.BaseFragment
import pl.mobite.sample.security.ui.base.mvi.mviViewModel


class SecretKeyFragment: BaseFragment() {

    private val viewModel: SecretKeyViewModel by mviViewModel()

    override fun getLayoutResId(): Int = R.layout.fragment_secret_key

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
        viewModel.subscribe(::render).addTo(onStopDisposables)
        viewModel.onStart()
    }

    private fun render(viewState: SecretKeyViewState) {
        with(viewState) {

            encryptionFormView.render(encryptionFormViewState)

            errorEvent?.consume {
                it.printStackTrace()
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}