package pl.mobite.sample.security.ui.components.secretkey

import org.junit.Before
import org.junit.Test
import pl.mobite.sample.security.data.models.ViewStateError
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.*
import pl.mobite.sample.security.utils.assertSecretKeyViewState


class SecretKeyReducerTest {

    private lateinit var reducer: SecretKeyReducer

    @Before
    fun setUp() {
        reducer = SecretKeyReducer()
    }

    @Test
    fun testHasValidKeyResult() {
        testHasValidKeyResult(createDefaultState())
        testHasValidKeyResult(createLoadingState())
        testHasValidKeyResult(createHasKeyState())
        testHasValidKeyResult(createMessageEncryptedState())
        testHasValidKeyResult(createMessageDecryptedState())
        testHasValidKeyResult(createErrorState())
    }

    @Test
    fun testNoValidKeyResult() {
        testNoValidKeyResult(createDefaultState())
        testNoValidKeyResult(createLoadingState())
        testNoValidKeyResult(createHasKeyState())
        testNoValidKeyResult(createMessageEncryptedState())
        testNoValidKeyResult(createMessageDecryptedState())
        testNoValidKeyResult(createErrorState())
    }

    @Test
    fun testEncryptMessageResult() {
        testEncryptMessageResult(createDefaultState())
        testEncryptMessageResult(createLoadingState())
        testEncryptMessageResult(createHasKeyState())
        testEncryptMessageResult(createMessageEncryptedState())
        testEncryptMessageResult(createMessageDecryptedState())
        testEncryptMessageResult(createErrorState())
    }

    @Test
    fun testDecryptMessageResult() {
        testDecryptMessageResult(createDefaultState())
        testDecryptMessageResult(createLoadingState())
        testDecryptMessageResult(createHasKeyState())
        testDecryptMessageResult(createMessageEncryptedState())
        testDecryptMessageResult(createMessageDecryptedState())
        testDecryptMessageResult(createErrorState())
    }

    @Test
    fun testClearMessagesResult() {
        testClearMessagesResult(createDefaultState())
        testClearMessagesResult(createLoadingState())
        testClearMessagesResult(createHasKeyState())
        testClearMessagesResult(createMessageEncryptedState())
        testClearMessagesResult(createMessageDecryptedState())
        testClearMessagesResult(createErrorState())
    }

    @Test
    fun testInFlightResult() {
        testInFlightResult(createDefaultState())
        testInFlightResult(createLoadingState())
        testInFlightResult(createHasKeyState())
        testInFlightResult(createMessageEncryptedState())
        testInFlightResult(createMessageDecryptedState())
        testInFlightResult(createErrorState())
    }

    @Test
    fun testErrorResult() {
        testErrorResult(createDefaultState())
        testErrorResult(createLoadingState())
        testErrorResult(createHasKeyState())
        testErrorResult(createMessageEncryptedState())
        testErrorResult(createMessageDecryptedState())
        testErrorResult(createErrorState())
    }

    private fun testHasValidKeyResult(initialState: SecretKeyViewState) {
        val expectedState = initialState.copy(
                secretKeyAlias = dummyKeyAlias,
                isLoading = false,
                error = null
        )
        testReducer(initialState, HasValidKeyResult(dummyKeyAlias), expectedState)
    }

    private fun testNoValidKeyResult(initialState: SecretKeyViewState) {
        val expectedState = initialState.copy(
                secretKeyAlias = null,
                messageEncrypted = null,
                messageDecrypted = null,
                isLoading = false,
                error = null
        ).apply { clearMessage.set(true) }
        testReducer(initialState, NoValidKeyResult, expectedState)
    }

    private fun testEncryptMessageResult(initialState: SecretKeyViewState) {
        val expectedState = initialState.copy(
                secretKeyAlias = dummyKeyAlias,
                messageEncrypted = dummyMessageEncrypted,
                messageDecrypted = null,
                isLoading = false,
                error = null
        )
        testReducer(initialState, EncryptMessageResult(dummyKeyAlias, dummyMessageEncrypted), expectedState)
    }

    private fun testDecryptMessageResult(initialState: SecretKeyViewState) {
        val expectedState = initialState.copy(
                secretKeyAlias = dummyKeyAlias,
                messageEncrypted = dummyMessageEncrypted,
                messageDecrypted = dummyMessageDecrypted,
                isLoading = false,
                error = null
        )
        testReducer(initialState, DecryptMessageResult(dummyKeyAlias, dummyMessageEncrypted, dummyMessageDecrypted), expectedState)
    }

    private fun testClearMessagesResult(initialState: SecretKeyViewState) {
        val expectedState = initialState.copy(
                messageEncrypted = null,
                messageDecrypted = null,
                isLoading = false,
                error = null
        ).apply { clearMessage.set(true) }
        testReducer(initialState, ClearMessagesResult, expectedState)
    }

    private fun testInFlightResult(initialState: SecretKeyViewState) {
        val expectedState = initialState.copy(
                isLoading = true,
                error = null
        )
        testReducer(initialState, InFlightResult, expectedState)
    }

    private fun testErrorResult(initialState: SecretKeyViewState) {
        val expectedState = initialState.copy(
                isLoading = false,
                error = ViewStateError(dummyThrowable)
        )
        testReducer(initialState, ErrorResult(dummyThrowable), expectedState)
    }

    private fun testReducer(initialState: SecretKeyViewState, secretKeyResult: SecretKeyResult, expectedState: SecretKeyViewState) {
        val newState = reducer.apply(initialState, secretKeyResult)
        assertSecretKeyViewState(expectedState, newState)
    }

    companion object {

        private const val dummyKeyAlias = "dummyAlias"
        private const val dummyMessageEncrypted = "dummyMessageEncrypted"
        private const val dummyMessageDecrypted = "dummyMessageDecrypted"
        private val dummyThrowable = Throwable("dummy message")

        fun createDefaultState() = SecretKeyViewState.default()
        fun createLoadingState() = SecretKeyViewState.default().copy(
                isLoading = true
        )
        fun createHasKeyState() = SecretKeyViewState.default().copy(
                secretKeyAlias = dummyKeyAlias
        )
        fun createMessageEncryptedState() = SecretKeyViewState.default().copy(
                secretKeyAlias = dummyKeyAlias,
                messageEncrypted = dummyMessageEncrypted
        )
        fun createMessageDecryptedState() = SecretKeyViewState.default().copy(
                secretKeyAlias = dummyKeyAlias,
                messageEncrypted = dummyMessageEncrypted,
                messageDecrypted = dummyMessageDecrypted
        )
        fun createErrorState() = SecretKeyViewState.default().copy(
                secretKeyAlias = dummyKeyAlias,
                error = ViewStateError(dummyThrowable)
        )
    }
}