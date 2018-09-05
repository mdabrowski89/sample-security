package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import pl.mobite.sample.security.data.repositories.CryptoRepository
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.*
import pl.mobite.sample.security.utils.SchedulerProvider


class SecretKeyActionProcessor(
    cryptoRepository: CryptoRepository,
    schedulerProvider: SchedulerProvider
): ObservableTransformer<SecretKeyAction, SecretKeyResult> {

    override fun apply(actions: Observable<SecretKeyAction>): ObservableSource<SecretKeyResult> {
        return actions.publish { shared ->
            Observable.merge(listOf(
                    shared.ofType(LoadKeyAction::class.java).compose(loadKeyFromKeystoreProcessor),
                    shared.ofType(GenerateNewKeyAction::class.java).compose(generateNewKeyProcessor),
                    shared.ofType(ClearKeyAction::class.java).compose(clearKeyProcessor),
                    shared.ofType(EncryptMessageAction::class.java).compose(encryptMessageProcessor),
                    shared.ofType(DecryptMessageAction::class.java).compose(decryptMessageProcessor),
                    shared.ofType(ClearMessagesAction::class.java).compose(clearMessagesProcessor)
            ))
        }
    }

    private val loadKeyFromKeystoreProcessor = ObservableTransformer { actions: Observable<LoadKeyAction> ->
        // TODO: read key from repository
        actions.switchMap { action -> Observable.just(LoadKeyResult(action.keyAlias)) }
    }

    private val generateNewKeyProcessor = ObservableTransformer { actions: Observable<GenerateNewKeyAction> ->
        // TODO: generate key in repository
        actions.switchMap { action -> Observable.just(GenerateNewKeyResult(action.keyAlias)) }
    }

    private val clearKeyProcessor = ObservableTransformer { actions: Observable<ClearKeyAction> ->
        // TODO: remove key in repository
        actions.switchMap { Observable.just(ClearKeyResult) }
    }

    private val encryptMessageProcessor = ObservableTransformer { actions: Observable<EncryptMessageAction> ->
        // TODO: encrypt message in repository
        actions.switchMap { action -> Observable.just(EncryptMessageResult("encryptedMEssage")) }
    }

    private val decryptMessageProcessor = ObservableTransformer { actions: Observable<DecryptMessageAction> ->
        // TODO: decrypt message in repository
        actions.switchMap { Observable.just(DecryptMessageResult("decrypted message")) }
    }

    private val clearMessagesProcessor = ObservableTransformer { actions: Observable<ClearMessagesAction> ->
        actions.switchMap { Observable.just(ClearMessagesResult) }
    }
}