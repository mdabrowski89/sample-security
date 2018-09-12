package pl.mobite.sample.security.ui.components.secretkey

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_secret_key.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.data.models.ViewStateError
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyIntent.*
import pl.mobite.sample.security.utils.CustomTextWatcher
import pl.mobite.sample.security.utils.SampleSecurityViewModelFactory
import pl.mobite.sample.security.utils.debounceButton
import pl.mobite.sample.security.utils.setVisibleOrGone


class SecretKeyFragment: Fragment() {

    private lateinit var disposable: CompositeDisposable
    private lateinit var viewModel: SecretKeyViewModel

    private var lastViewState: SecretKeyViewState? = null

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        messageInput.addTextChangedListener(object : CustomTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                encryptButton.isEnabled = s?.toString()?.isNotBlank() ?: false
            }
        })
    }

    override fun onStart() {
        super.onStart()
        disposable = CompositeDisposable()
        disposable.add(viewModel.states().subscribe(this::render))
        viewModel.processIntents(intents())
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(SecretKeyViewState.PARCEL_KEY, lastViewState)
    }

    private fun intents(): Observable<SecretKeyIntent> {
        return Observable.merge(listOf(
                Observable.just(InitialIntent(KEY_ALIAS)),
                generateKeyIntent(),
                removeKeyIntent(),
                encryptIntent(),
                decryptIntent(),
                clearMessagesIntent()
        ))
    }

    private fun generateKeyIntent() = RxView
            .clicks(generateKeyButton)
            .debounceButton()
            .map { GenerateKeyIntent(KEY_ALIAS) }

    private fun removeKeyIntent() = RxView
            .clicks(removeKeyButton)
            .debounceButton()
            .map { RemoveKeyIntent(KEY_ALIAS) }

    private fun encryptIntent() = RxView
            .clicks(encryptButton)
            .debounceButton()
            .map { EncryptMessageIntent(KEY_ALIAS, messageInput.text.toString()) }

    private fun decryptIntent() = RxView
            .clicks(decryptButton)
            .debounceButton()
            .map { DecryptMessageIntent(KEY_ALIAS, encryptedMessageText.text.toString() ) }

    private fun clearMessagesIntent() = RxView
            .clicks(clearMessagesButton)
            .debounceButton()
            .map { ClearMessagesIntent }

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

    private fun handleError(error: ViewStateError) {
        if (error.shouldDisplay.getAndSet(false)) {
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