package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito.`when`
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyIntent.*
import pl.mobite.sample.security.utils.*


class SecretKeyViewModelTest {

    private val secretKeyRepositoryMock: SecretKeyRepository by lazyMock()

    @Test
    fun testInitialIntentSuccessAndHasValidKey() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.just(true))

        val intents = listOf(
                InitialIntent(dummyKeyAlias)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withKey(dummyKeyAlias) }
        )

        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testInitialIntentSuccessAndNoValidKey() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.just(false))

        val intents = listOf(
                InitialIntent(dummyKeyAlias)
        )
        val stateTransformers = listOf(
                SecretKeyViewState::loading,
                SecretKeyViewState::withoutKey
        )

        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testInitialIntentFailure() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.error(dummyThrowable))

        val intents = listOf(
                InitialIntent(dummyKeyAlias)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withError(dummyThrowable) }
        )
        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testGenerateKeyIntentSuccess() {
        `when`(secretKeyRepositoryMock.generateKey(dummyKeyAlias)).thenReturn(Completable.complete())

        val intents = listOf(
                GenerateKeyIntent(dummyKeyAlias)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withKey(dummyKeyAlias) }
        )

        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testGenerateKeyIntentFailure() {
        `when`(secretKeyRepositoryMock.generateKey(dummyKeyAlias)).thenReturn(Completable.error(dummyThrowable))

        val intents = listOf(
                GenerateKeyIntent(dummyKeyAlias)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withError(dummyThrowable) }
        )
        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testRemoveKeyIntentSuccess() {
        `when`(secretKeyRepositoryMock.removeKey(dummyKeyAlias)).thenReturn(Completable.complete())

        val intents = listOf(
                RemoveKeyIntent(dummyKeyAlias)
        )
        val stateTransformers = listOf(
                SecretKeyViewState::loading,
                SecretKeyViewState::withoutKey
        )

        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testRemoveKeyIntentFailure() {
        `when`(secretKeyRepositoryMock.removeKey(dummyKeyAlias)).thenReturn(Completable.error(dummyThrowable))

        val intents = listOf(
                RemoveKeyIntent(dummyKeyAlias)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withError(dummyThrowable) }
        )
        initialStates.forEach(test(intents, stateTransformers))
    }
    @Test
    fun testEncryptMessageIntentSuccess() {
        `when`(secretKeyRepositoryMock.encrypt(dummyKeyAlias, dummyMessageToEncrypt)).thenReturn(Single.just(dummyMessageEncrypted))

        val intents = listOf(
                EncryptMessageIntent(dummyKeyAlias, dummyMessageToEncrypt)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withMessageEncrypted(dummyKeyAlias, dummyMessageEncrypted) }
        )

        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testEncryptMessageIntentFailure() {
        `when`(secretKeyRepositoryMock.encrypt(dummyKeyAlias, dummyMessageToEncrypt)).thenReturn(Single.error(dummyThrowable))

        val intents = listOf(
                EncryptMessageIntent(dummyKeyAlias, dummyMessageToEncrypt)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withError(dummyThrowable) }
        )
        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testDecryptMessageIntentSuccess() {
        `when`(secretKeyRepositoryMock.decrypt(dummyKeyAlias, dummyMessageEncrypted)).thenReturn(Single.just(dummyMessageDecrypted))

        val intents = listOf(
                DecryptMessageIntent(dummyKeyAlias, dummyMessageEncrypted)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withMessageDecrypted(dummyKeyAlias, dummyMessageEncrypted, dummyMessageDecrypted) }
        )

        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testDecryptMessageIntentFailure() {
        `when`(secretKeyRepositoryMock.decrypt(dummyKeyAlias, dummyMessageEncrypted)).thenReturn(Single.error(dummyThrowable))

        val intents = listOf(
                DecryptMessageIntent(dummyKeyAlias, dummyMessageEncrypted)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withError(dummyThrowable) }
        )
        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testClearMessagesIntent() {
        val intents = listOf(
                ClearMessagesIntent
        )
        val stateTransformers = listOf(
                SecretKeyViewState::withMessageCleared
        )

        initialStates.forEach(test(intents, stateTransformers))
    }

    @Test
    fun testScenario1() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.just(false))
        `when`(secretKeyRepositoryMock.generateKey(dummyKeyAlias)).thenReturn(Completable.complete())
        `when`(secretKeyRepositoryMock.encrypt(dummyKeyAlias, dummyMessageToEncrypt)).thenReturn(Single.just(dummyMessageEncrypted))
        `when`(secretKeyRepositoryMock.decrypt(dummyKeyAlias, dummyMessageEncrypted)).thenReturn(Single.just(dummyMessageDecrypted))
        `when`(secretKeyRepositoryMock.removeKey(dummyKeyAlias)).thenReturn(Completable.complete())

        val intents = listOf(
                InitialIntent(dummyKeyAlias),
                GenerateKeyIntent(dummyKeyAlias),
                EncryptMessageIntent(dummyKeyAlias, dummyMessageToEncrypt),
                DecryptMessageIntent(dummyKeyAlias, dummyMessageEncrypted),
                ClearMessagesIntent,
                RemoveKeyIntent(dummyKeyAlias)
        )
        val stateTransformers = listOf(
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withoutKey() },
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withKey(dummyKeyAlias) },
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withMessageEncrypted(dummyKeyAlias, dummyMessageEncrypted) },
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withMessageDecrypted(dummyKeyAlias, dummyMessageEncrypted, dummyMessageDecrypted) },
                { state: SecretKeyViewState -> state.withMessageCleared() },
                { state: SecretKeyViewState -> state.loading() },
                { state: SecretKeyViewState -> state.withoutKey() }
        )
        initialStates.forEach(test(intents, stateTransformers))

    }

    private fun test(intents: List<SecretKeyIntent>, stateTransformers: List<StateTransformer<SecretKeyViewState>>)
            = { initialState: SecretKeyViewState ->
        val expectedStates = createExpectedStates(initialState, stateTransformers)
        test(initialState, intents, expectedStates)
    }

    private fun test(initialState: SecretKeyViewState, intents: List<SecretKeyIntent>, expectedStates: List<SecretKeyViewState>) {
        val viewModel = SecretKeyViewModel(secretKeyRepositoryMock, ImmediateSchedulerProvider.instance, initialState)
        val testObserver = viewModel.states().test()

        viewModel.processIntents(Observable.fromIterable(intents))

        testObserver.assertValueCount(expectedStates.size)

        testObserver.values().forEachIndexed {i, tested ->
            assertSecretKeyViewState(expectedStates[i], tested)
        }
        testObserver.assertNoErrors()
        testObserver.assertNotComplete()
    }

    companion object {

        private const val dummyKeyAlias = "dummyAlias"
        private const val dummyMessageToEncrypt = "dummyMessageToEncrypt"
        private const val dummyMessageEncrypted = "dummyMessageEncrypted"
        private const val dummyMessageDecrypted = dummyMessageToEncrypt
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