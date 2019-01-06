package pl.mobite.sample.security.ui.components.secretkey

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import pl.mobite.sample.security.ui.components.secretkey.mvi.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyIntent.*


class SecretKeyIntentInterpreterTest {

    private lateinit var interpreter: SecretKeyIntentInterpreter

    @Before
    fun setUp() {
        interpreter = SecretKeyIntentInterpreter()
    }

    @Test
    fun testIntentInterpretation() {
        assertEquals(CheckKeyAction(dummyKeyAlias), interpreter.apply(InitialIntent(dummyKeyAlias)))

        assertEquals(GenerateNewKeyAction(dummyKeyAlias), interpreter.apply(GenerateKeyIntent(dummyKeyAlias)))

        assertEquals(RemoveKeyAction(dummyKeyAlias), interpreter.apply(RemoveKeyIntent(dummyKeyAlias)))

        assertEquals(EncryptMessageAction(dummyKeyAlias, dummyMessage), interpreter.apply(EncryptMessageIntent(dummyKeyAlias, dummyMessage)))

        assertEquals(DecryptMessageAction(dummyKeyAlias, dummyMessage), interpreter.apply(DecryptMessageIntent(dummyKeyAlias, dummyMessage)))

        assertEquals(ClearMessagesAction, interpreter.apply(ClearMessagesIntent))

    }

    companion object {

        private const val dummyKeyAlias = "dummyAlias"
        private const val dummyMessage = "dummyMessage"
    }
}