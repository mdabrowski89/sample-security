package pl.mobite.sample.security.ui.components.secretkey

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_secret_key.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.base.ViewStateEvent
import pl.mobite.sample.security.ui.custom.CustomTextWatcher
import pl.mobite.sample.security.utils.SampleSecurityViewModelFactory
import pl.mobite.sample.security.utils.extensions.setVisibleOrGone


class SecretKeyFragment: Fragment() {

    private val actionsRelay = PublishRelay.create<SecretKeyAction>()
    private lateinit var viewModel: SecretKeyViewModel
    private var lastViewState: SecretKeyViewState? = null

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val initialViewState: SecretKeyViewState? = savedInstanceState?.getParcelable(SecretKeyViewState.PARCEL_KEY)
        viewModel = ViewModelProviders.of(this,
                SampleSecurityViewModelFactory.getInstance(initialViewState))
                .get(SecretKeyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_secret_key, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageInput.addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                encryptButton.isEnabled = s?.toString()?.isNotBlank() ?: false
            }
        })

        generateKeyButton.setOnClickListener { actionsRelay.accept(SecretKeyAction.GenerateNewKeyAction(KEY_ALIAS)) }

        removeKeyButton.setOnClickListener { actionsRelay.accept(SecretKeyAction.RemoveKeyAction(KEY_ALIAS)) }

        encryptButton.setOnClickListener { actionsRelay.accept(SecretKeyAction.EncryptMessageAction(KEY_ALIAS, messageInput.text.toString())) }

        decryptButton.setOnClickListener { actionsRelay.accept(SecretKeyAction.DecryptMessageAction(KEY_ALIAS, encryptedMessageText.text.toString() )) }

        clearMessagesButton.setOnClickListener { SecretKeyAction.ClearMessagesAction }
    }

    override fun onStart() {
        super.onStart()
        disposable.add(viewModel.states.subscribe(this::render))
        viewModel.processActions(actionsRelay)

        actionsRelay.accept(SecretKeyAction.CheckKeyAction(KEY_ALIAS))
    }

    override fun onStop() {
        disposable.clear()
        viewModel.clear()
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SecretKeyViewState.PARCEL_KEY, lastViewState)
    }

    private fun render(viewState: SecretKeyViewState) {
        saveViewState(viewState)

        with(viewState) {
            if (error != null) {
                handleError(error)
            }

            val hasSecretKey = secretKeyAlias != null
            val hasEncryptedMessage = messageEncrypted != null
            val hasDecryptedMessage = messageDecrypted != null

            keyAliasLabel.text = getString(R.string.label_key_alias, if (hasSecretKey) secretKeyAlias else getString(R.string.label_key_missing))

            generateKeyButton.setVisibleOrGone(!hasSecretKey)
            removeKeyButton.setVisibleOrGone(hasSecretKey)
            clearMessagesButton.setVisibleOrGone(hasSecretKey)
            messageInputLayout.setVisibleOrGone(hasSecretKey)
            encryptButton.setVisibleOrGone(hasSecretKey)
            encryptedMessageLabel.setVisibleOrGone(hasSecretKey && hasEncryptedMessage)
            encryptedMessageText.setVisibleOrGone(hasSecretKey && hasEncryptedMessage)
            decryptButton.setVisibleOrGone(hasSecretKey && hasEncryptedMessage)
            decryptedMessageLabel.setVisibleOrGone(hasSecretKey && hasDecryptedMessage)
            decryptedMessageText.setVisibleOrGone(hasSecretKey && hasDecryptedMessage)

            encryptedMessageText.text = messageEncrypted.orEmpty()
            decryptedMessageText.text = messageDecrypted

            if (clearMessage.getAndSet(false)) {
                messageInput.text?.clear()
            }

            generateKeyButton.isEnabled = !isLoading
            removeKeyButton.isEnabled = !isLoading
            clearMessagesButton.isEnabled = !isLoading
            messageInput.isEnabled = !isLoading
            encryptButton.isEnabled = !isLoading && messageInput.text?.isNotBlank() ?: false
            decryptButton.isEnabled = !isLoading
        }
    }

    private fun saveViewState(viewState: SecretKeyViewState) {
        if (!viewState.isLoading) {
            lastViewState = viewState
        }
    }

    private fun handleError(error: ViewStateEvent<Throwable>) {
        if (error.isNotConsumed()) {
            Toast.makeText(activity, R.string.error_message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        fun getInstance(): Fragment {
            return SecretKeyFragment()
        }

        private const val KEY_ALIAS = "BLOSSOM"
    }
}