package pl.mobite.sample.security.ui.custom.encryptionform

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.view_encryption_form.view.*
import pl.mobite.sample.security.R
import pl.mobite.sample.security.ui.custom.CustomTextWatcher
import pl.mobite.sample.security.utils.setVisibleOrGone


class EncryptionFormView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    private var viewState: EncryptionFormViewState? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_encryption_form, this, true)
        orientation = VERTICAL

        messageInput.addTextChangedListener(object: CustomTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                encryptButton.isEnabled = s?.toString()?.isNotBlank() ?: false
            }
        })
    }

    fun init(
        generateKeyAction: () -> Unit = {},
        removeKeyAction: () -> Unit = {},
        encryptAction: (String) -> Unit = {},
        decryptAction: () -> Unit = {},
        clearAction: () -> Unit = {}
    ) {

        generateKeyButton.setOnClickListener { generateKeyAction() }
        removeKeyButton.setOnClickListener { removeKeyAction() }
        encryptButton.setOnClickListener { encryptAction(messageInput.text.toString()) }
        decryptButton.setOnClickListener { decryptAction() }
        clearMessagesButton.setOnClickListener { clearAction() }
    }

    fun render(viewState: EncryptionFormViewState) {
        with(viewState) {
            val hasKeys = keyAlias != null
            val hasEncryptedMessage = messageEncrypted != null
            val hasDecryptedMessage = messageDecrypted != null

            keyAliasLabel.text = context.getString(
                R.string.label_key_alias,
                if (hasKeys) keyAlias else context.getString(R.string.label_key_missing)
            )

            generateKeyButton.setVisibleOrGone(!hasKeys)
            removeKeyButton.setVisibleOrGone(hasKeys)
            clearMessagesButton.setVisibleOrGone(hasKeys)
            messageInputLayout.setVisibleOrGone(hasKeys)
            encryptButton.setVisibleOrGone(hasKeys)
            encryptedMessageLabel.setVisibleOrGone(hasKeys && hasEncryptedMessage)
            encryptedMessageText.setVisibleOrGone(hasKeys && hasEncryptedMessage)
            decryptButton.setVisibleOrGone(hasKeys && hasEncryptedMessage)
            decryptedMessageLabel.setVisibleOrGone(hasKeys && hasDecryptedMessage)
            decryptedMessageText.setVisibleOrGone(hasKeys && hasDecryptedMessage)

            encryptedMessageText.text = messageEncrypted.orEmpty()
            decryptedMessageText.text = messageDecrypted

            clearEvent?.consume {
                messageInput.text?.clear()
            }

            generateKeyButton.isEnabled = !inProgress
            removeKeyButton.isEnabled = !inProgress
            clearMessagesButton.isEnabled = !inProgress
            messageInput.isEnabled = !inProgress
            encryptButton.isEnabled = !inProgress && messageInput.text?.isNotBlank() ?: false
            decryptButton.isEnabled = !inProgress
        }
    }
}