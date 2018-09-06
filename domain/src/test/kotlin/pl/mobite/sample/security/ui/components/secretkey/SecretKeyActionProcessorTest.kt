package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyAction.*
import pl.mobite.sample.security.utils.ImmediateSchedulerProvider
import pl.mobite.sample.security.utils.lazyMock


class SecretKeyActionProcessorTest {

    private val secretKeyRepositoryMock: SecretKeyRepository by lazyMock()

    private lateinit var processor: SecretKeyActionProcessor
    private lateinit var testObserver: TestObserver<SecretKeyResult>

    @Before
    fun setUp() {
        processor = SecretKeyActionProcessor(secretKeyRepositoryMock, ImmediateSchedulerProvider.instance)
        testObserver = TestObserver()
    }

    @Test
    fun testCheckKeyActionProcessorHasKey() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.just(true))

        processor.apply(Observable.just(CheckKeyAction(dummyKeyAlias)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.HasValidKeyResult(dummyKeyAlias)
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testCheckKeyActionProcessorDoesNotHaveKey() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.just(false))

        processor.apply(Observable.just(CheckKeyAction(dummyKeyAlias)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.NoValidKeyResult
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testCheckKeyActionProcessorFailure() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.error(dummyThrowable))

        processor.apply(Observable.just(CheckKeyAction(dummyKeyAlias)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.ErrorResult(dummyThrowable)
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testGenerateNewKeyActionProcessorSuccess() {
        `when`(secretKeyRepositoryMock.generateKey(dummyKeyAlias)).thenReturn(Completable.complete())

        processor.apply(Observable.just(GenerateNewKeyAction(dummyKeyAlias)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.HasValidKeyResult(dummyKeyAlias)
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testGenerateNewKeyActionProcessorFailure() {
        `when`(secretKeyRepositoryMock.generateKey(dummyKeyAlias)).thenReturn(Completable.error(dummyThrowable))

        processor.apply(Observable.just(GenerateNewKeyAction(dummyKeyAlias)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.ErrorResult(dummyThrowable)
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testRemoveKeyActionProcessorSuccess() {
        `when`(secretKeyRepositoryMock.removeKey(dummyKeyAlias)).thenReturn(Completable.complete())

        processor.apply(Observable.just(RemoveKeyAction(dummyKeyAlias)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.NoValidKeyResult
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testRemoveActionProcessorFailure() {
        `when`(secretKeyRepositoryMock.removeKey(dummyKeyAlias)).thenReturn(Completable.error(dummyThrowable))

        processor.apply(Observable.just(RemoveKeyAction(dummyKeyAlias)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.ErrorResult(dummyThrowable)
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testEncryptMessageActionSuccess() {
        `when`(secretKeyRepositoryMock.encrypt(dummyKeyAlias, dummyMessageDecrypted)).thenReturn(Single.just(dummyMessageEncrypted))

        processor.apply(Observable.just(EncryptMessageAction(dummyKeyAlias, dummyMessageDecrypted)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.EncryptMessageResult(dummyMessageEncrypted)
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testEncryptMessageActionFailure() {
        `when`(secretKeyRepositoryMock.encrypt(dummyKeyAlias, dummyMessageDecrypted)).thenReturn(Single.error(dummyThrowable))

        processor.apply(Observable.just(EncryptMessageAction(dummyKeyAlias, dummyMessageDecrypted)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.ErrorResult(dummyThrowable)
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testDecryptMessageActionSuccess() {
        `when`(secretKeyRepositoryMock.decrypt(dummyKeyAlias, dummyMessageEncrypted)).thenReturn(Single.just(dummyMessageDecrypted))

        processor.apply(Observable.just(DecryptMessageAction(dummyKeyAlias, dummyMessageEncrypted)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.DecryptMessageResult(dummyMessageDecrypted)
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testDecryptMessageActionFailure() {
        `when`(secretKeyRepositoryMock.decrypt(dummyKeyAlias, dummyMessageEncrypted)).thenReturn(Single.error(dummyThrowable))

        processor.apply(Observable.just(DecryptMessageAction(dummyKeyAlias, dummyMessageEncrypted)))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.InFlightResult,
                SecretKeyResult.ErrorResult(dummyThrowable)
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun testClearMessageAction() {
        processor.apply(Observable.just(ClearMessagesAction))
                .subscribe(testObserver)

        testObserver.assertValueSequence(listOf(
                SecretKeyResult.ClearMessagesResult
        ))
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    companion object {

        private const val dummyKeyAlias = "dummyAlias"
        private const val dummyMessageDecrypted = "dummyMessageDecrypted"
        private const val dummyMessageEncrypted = "dummyMessageEncrypted"
        private val dummyThrowable = Throwable("dummy error")
    }
}