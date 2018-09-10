package pl.mobite.sample.security.ui.components.secretkey

import org.junit.Before
import org.junit.Test
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.*
import pl.mobite.sample.security.utils.SecretKeyViewStateModifier
import pl.mobite.sample.security.utils.assertSecretKeyViewState


class SecretKeyReducerTest {

    private lateinit var reducer: SecretKeyReducer

    @Before
    fun setUp() {
        reducer = SecretKeyReducer()
    }

    @Test
    fun testHasValidKeyResult() {
        val result = HasValidKeyResult(dummyKeyAlias)
        val stateModifier = { state: SecretKeyViewState -> state.withKey(dummyKeyAlias) }

        initialStates.forEach(test(result, stateModifier))
    }

    @Test
    fun testNoValidKeyResult() {
        val result = NoValidKeyResult
        val stateModifier = SecretKeyViewState::withoutKey

        initialStates.forEach(test(result, stateModifier))
    }

    @Test
    fun testEncryptMessageResult() {
        val result = EncryptMessageResult(dummyKeyAlias, dummyMessageEncrypted)
        val stateModifier = { state: SecretKeyViewState -> state.withMessageEncrypted(dummyKeyAlias, dummyMessageEncrypted)}

        initialStates.forEach(test(result, stateModifier))
    }

    @Test
    fun testDecryptMessageResult() {
        val result = DecryptMessageResult(dummyKeyAlias, dummyMessageEncrypted, dummyMessageDecrypted)
        val stateModifier = { state: SecretKeyViewState -> state.withMessageDecrypted(dummyKeyAlias, dummyMessageEncrypted, dummyMessageDecrypted)}

        initialStates.forEach(test(result, stateModifier))
    }

    @Test
    fun testClearMessagesResult() {
        val result = ClearMessagesResult
        val stateModifier = SecretKeyViewState::withMessageCleared

        initialStates.forEach(test(result, stateModifier))
    }

    @Test
    fun testInFlightResult() {
        val result = InFlightResult
        val stateModifier = SecretKeyViewState::loading

        initialStates.forEach(test(result, stateModifier))
    }

    @Test
    fun testErrorResult() {
        val result = ErrorResult(dummyThrowable)
        val stateModifier = { state: SecretKeyViewState -> state.withError(dummyThrowable)}

        initialStates.forEach(test(result, stateModifier))
    }

    private fun test(result: SecretKeyResult, stateModifier: SecretKeyViewStateModifier) = { initialState: SecretKeyViewState ->
        val expectedState = stateModifier(initialState)
        val testedState = reducer.apply(initialState, result)
        assertSecretKeyViewState(expectedState, testedState)
    }

    companion object {

        private const val dummyKeyAlias = "dummyAlias"
        private const val dummyMessageEncrypted = "dummyMessageEncrypted"
        private const val dummyMessageDecrypted = "dummyMessageDecrypted"
        private val dummyThrowable = Throwable("dummy message")

        private val initialStates = listOf(
                SecretKeyViewState.default(),
                SecretKeyViewState.default().loading(),
                SecretKeyViewState.default().withKey(dummyKeyAlias),
                SecretKeyViewState.default().withMessageEncrypted(dummyKeyAlias, dummyMessageEncrypted),
                SecretKeyViewState.default().withMessageDecrypted(dummyKeyAlias, dummyMessageEncrypted, dummyMessageDecrypted),
                SecretKeyViewState.default().withKey(dummyKeyAlias).withError(dummyThrowable)
        )
    }
}