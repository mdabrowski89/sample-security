package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.`when`
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.*
import pl.mobite.sample.security.utils.ImmediateSchedulerProvider
import pl.mobite.sample.security.utils.lazyMock


class SecretKeyActionProcessorTest {

    private val secretKeyRepositoryMock: SecretKeyRepository by lazyMock()

    @Test
    fun testCheckKeyActionProcessorHasKey() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.just(true))

        val action = CheckKeyAction(dummyKeyAlias)
        val expectedResults = listOf(
                InFlightResult,
                HasValidKeyResult(dummyKeyAlias)
        )

        test(action, expectedResults)
    }

    @Test
    fun testCheckKeyActionProcessorDoesNotHaveKey() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.just(false))

        val action = CheckKeyAction(dummyKeyAlias)
        val expectedResults = listOf(
                InFlightResult,
                NoValidKeyResult
        )

        test(action, expectedResults)
    }

    @Test
    fun testCheckKeyActionProcessorFailure() {
        `when`(secretKeyRepositoryMock.checkKey(dummyKeyAlias)).thenReturn(Single.error(dummyThrowable))

        val action = CheckKeyAction(dummyKeyAlias)
        val expectedResults = listOf(
                InFlightResult,
                ErrorResult(dummyThrowable)
        )

        test(action, expectedResults)
    }

    @Test
    fun testGenerateNewKeyActionProcessorSuccess() {
        `when`(secretKeyRepositoryMock.generateKey(dummyKeyAlias)).thenReturn(Completable.complete())

        val action = GenerateNewKeyAction(dummyKeyAlias)
        val expectedResults = listOf(
                InFlightResult,
                HasValidKeyResult(dummyKeyAlias)
        )

        test(action, expectedResults)
    }

    @Test
    fun testGenerateNewKeyActionProcessorFailure() {
        `when`(secretKeyRepositoryMock.generateKey(dummyKeyAlias)).thenReturn(Completable.error(dummyThrowable))

        val action = GenerateNewKeyAction(dummyKeyAlias)
        val expectedResults = listOf(
                InFlightResult,
                ErrorResult(dummyThrowable)
        )

        test(action, expectedResults)
    }

    @Test
    fun testRemoveKeyActionProcessorSuccess() {
        `when`(secretKeyRepositoryMock.removeKey(dummyKeyAlias)).thenReturn(Completable.complete())

        val action = RemoveKeyAction(dummyKeyAlias)
        val expectedResults = listOf(
                InFlightResult,
                NoValidKeyResult
        )

        test(action, expectedResults)
    }

    @Test
    fun testRemoveActionProcessorFailure() {
        `when`(secretKeyRepositoryMock.removeKey(dummyKeyAlias)).thenReturn(Completable.error(dummyThrowable))

        val action = RemoveKeyAction(dummyKeyAlias)
        val expectedResults = listOf(
                InFlightResult,
                ErrorResult(dummyThrowable)
        )

        test(action, expectedResults)
    }

    @Test
    fun testEncryptMessageActionSuccess() {
        `when`(secretKeyRepositoryMock.encrypt(dummyKeyAlias, dummyMessageDecrypted)).thenReturn(Single.just(dummyMessageEncrypted))

        val action = EncryptMessageAction(dummyKeyAlias, dummyMessageDecrypted)
        val expectedResults = listOf(
                InFlightResult,
                EncryptMessageResult(dummyKeyAlias, dummyMessageEncrypted)
        )

        test(action, expectedResults)
    }

    @Test
    fun testEncryptMessageActionFailure() {
        `when`(secretKeyRepositoryMock.encrypt(dummyKeyAlias, dummyMessageDecrypted)).thenReturn(Single.error(dummyThrowable))

        val action = EncryptMessageAction(dummyKeyAlias, dummyMessageDecrypted)
        val expectedResults = listOf(
                InFlightResult,
                ErrorResult(dummyThrowable)
        )

        test(action, expectedResults)
    }

    @Test
    fun testDecryptMessageActionSuccess() {
        `when`(secretKeyRepositoryMock.decrypt(dummyKeyAlias, dummyMessageEncrypted)).thenReturn(Single.just(dummyMessageDecrypted))

        val action = DecryptMessageAction(dummyKeyAlias, dummyMessageEncrypted)
        val expectedResults = listOf(
                InFlightResult,
                DecryptMessageResult(dummyKeyAlias, dummyMessageEncrypted, dummyMessageDecrypted)
        )

        test(action, expectedResults)
    }

    @Test
    fun testDecryptMessageActionFailure() {
        `when`(secretKeyRepositoryMock.decrypt(dummyKeyAlias, dummyMessageEncrypted)).thenReturn(Single.error(dummyThrowable))

        val action = DecryptMessageAction(dummyKeyAlias, dummyMessageEncrypted)
        val expectedResults = listOf(
                InFlightResult,
                ErrorResult(dummyThrowable)
        )

        test(action, expectedResults)
    }

    @Test
    fun testClearMessageAction() {
        val action = ClearMessagesAction
        val expectedResults = listOf(ClearMessagesResult)

        test(action, expectedResults)
    }

    private fun test(action: SecretKeyAction, expectedResults: List<SecretKeyResult>) {
        val processor = SecretKeyActionProcessor(secretKeyRepositoryMock, ImmediateSchedulerProvider.instance)
        val testObserver = TestObserver<SecretKeyResult>()

        processor.apply(Observable.just(action))
                .subscribe(testObserver)

        testObserver.assertValueCount(expectedResults.size)

        testObserver.values().forEachIndexed { i, tested ->
            Assert.assertEquals(expectedResults[i], tested)
        }

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