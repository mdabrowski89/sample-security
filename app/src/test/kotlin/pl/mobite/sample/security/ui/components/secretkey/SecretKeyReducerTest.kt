package pl.mobite.sample.security.ui.components.secretkey

import org.junit.Before
import org.junit.Test
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.*
import pl.mobite.sample.security.utils.StateTransformer
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
        val stateTransformer = { state: SecretKeyViewState -> state.withKey(dummyKeyAlias) }

        initialStates.forEach(test(result, stateTransformer))
    }

    @Test
    fun testNoValidKeyResult() {
        val result = NoValidKeyResult
        val stateTransformer = SecretKeyViewState::withoutKey

        initialStates.forEach(test(result, stateTransformer))
    }

    @Test
    fun testEncryptMessageResult() {
        val result = EncryptMessageResult(dummyKeyAlias, dummyMessageEncrypted)
        val stateTransformer = { state: SecretKeyViewState -> state.withMessageEncrypted(dummyKeyAlias, dummyMessageEncrypted)}

        initialStates.forEach(test(result, stateTransformer))
    }

    @Test
    fun testDecryptMessageResult() {
        val result = DecryptMessageResult(dummyKeyAlias, dummyMessageEncrypted, dummyMessageDecrypted)
        val stateTransformer = { state: SecretKeyViewState -> state.withMessageDecrypted(dummyKeyAlias, dummyMessageEncrypted, dummyMessageDecrypted)}

        initialStates.forEach(test(result, stateTransformer))
    }

    @Test
    fun testClearMessagesResult() {
        val result = ClearMessagesResult
        val stateTransformer = SecretKeyViewState::withMessageCleared

        initialStates.forEach(test(result, stateTransformer))
    }

    @Test
    fun testInFlightResult() {
        val result = InFlightResult
        val stateTransformer = SecretKeyViewState::loading

        initialStates.forEach(test(result, stateTransformer))
    }

    @Test
    fun testErrorResult() {
        val result = ErrorResult(dummyThrowable)
        val stateTransformer = { state: SecretKeyViewState -> state.withError(dummyThrowable)}

        initialStates.forEach(test(result, stateTransformer))
    }

    private fun test(result: SecretKeyResult, stateTransformer: StateTransformer<SecretKeyViewState>) = { initialState: SecretKeyViewState ->
        val expectedState = stateTransformer(initialState)
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