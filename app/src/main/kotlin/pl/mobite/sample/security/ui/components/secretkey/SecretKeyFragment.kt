package pl.mobite.sample.security.ui.components.secretkey

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_secret_key.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.base.MviBaseFragment
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyResult
import pl.mobite.sample.security.ui.custom.CustomTextWatcher
import pl.mobite.sample.security.utils.setVisibleOrGone


class SecretKeyFragment: MviBaseFragment<SecretKeyAction, SecretKeyResult, SecretKeyViewState, SecretKeyViewModel>(
    SecretKeyViewModel::class.java
) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_secret_key, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messageInput.addTextChangedListener(object: CustomTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                encryptButton.isEnabled = s?.toString()?.isNotBlank() ?: false
            }
        })

        generateKeyButton.setOnClickListener { mviController.accept(SecretKeyAction.GenerateNewKeyAction(KEY_ALIAS)) }

        removeKeyButton.setOnClickListener { mviController.accept(SecretKeyAction.RemoveKeyAction(KEY_ALIAS)) }

        encryptButton.setOnClickListener {
            mviController.accept(SecretKeyAction.EncryptMessageAction(KEY_ALIAS, messageInput.text.toString()))
        }

        decryptButton.setOnClickListener {
            mviController.accept(SecretKeyAction.DecryptMessageAction(KEY_ALIAS, encryptedMessageText.text.toString()))
        }

        clearMessagesButton.setOnClickListener { mviController.accept(SecretKeyAction.ClearMessagesAction) }
    }

    override fun initialAction(lastViewState: SecretKeyViewState?): SecretKeyAction? {
        return SecretKeyAction.CheckKeyAction(KEY_ALIAS)
    }

    override fun render(viewState: SecretKeyViewState) {
        with(viewState) {
            errorEvent?.consume {
                Toast.makeText(activity, R.string.error_message, Toast.LENGTH_SHORT).show()
            }

            val hasSecretKey = secretKeyAlias != null
            val hasEncryptedMessage = messageEncrypted != null
            val hasDecryptedMessage = messageDecrypted != null

            keyAliasLabel.text = getString(
                R.string.label_key_alias,
                if (hasSecretKey) secretKeyAlias else getString(R.string.label_key_missing)
            )

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

            clearEvent?.consume {
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

    companion object {

        private const val KEY_ALIAS = "BLOSSOM"
    }
}