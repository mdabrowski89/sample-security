package pl.mobite.sample.security.ui.components.secretkey

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyAction.*
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyResult.*
import pl.mobite.sample.security.utils.SchedulerProvider


class SecretKeyActionProcessor(
        secretKeyRepository: SecretKeyRepository,
        schedulerProvider: SchedulerProvider
): ObservableTransformer<SecretKeyAction, SecretKeyResult> {

    override fun apply(actions: Observable<SecretKeyAction>): ObservableSource<SecretKeyResult> {
        return actions.publish { shared ->
            Observable.merge(listOf(
                    shared.ofType(CheckKeyAction::class.java).compose(checkKeyProcessor),
                    shared.ofType(GenerateNewKeyAction::class.java).compose(generateNewKeyProcessor),
                    shared.ofType(RemoveKeyAction::class.java).compose(removeKeyProcessor),
                    shared.ofType(EncryptMessageAction::class.java).compose(encryptMessageProcessor),
                    shared.ofType(DecryptMessageAction::class.java).compose(decryptMessageProcessor),
                    shared.ofType(ClearMessagesAction::class.java).compose(clearMessagesProcessor)
            ))
        }
    }

    private val checkKeyProcessor = ObservableTransformer { actions: Observable<CheckKeyAction> ->
        actions.switchMap { action ->
            secretKeyRepository.checkKey(action.keyAlias)
                    .toObservable()
                    .map { hasKey -> if (hasKey) {
                        HasValidKeyResult(action.keyAlias)
                    } else {
                        NoValidKeyResult
                    }}
                    .cast(SecretKeyResult::class.java)
                    .onErrorReturn { t -> ErrorResult(t) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(InFlightResult)
        }
    }

    private val generateNewKeyProcessor = ObservableTransformer { actions: Observable<GenerateNewKeyAction> ->
        actions.switchMap { action ->
            secretKeyRepository.generateKey(action.keyAlias)
                    .toSingleDefault(HasValidKeyResult(action.keyAlias))
                    .toObservable()
                    .cast(SecretKeyResult::class.java)
                    .onErrorReturn { t -> ErrorResult(t) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(InFlightResult)
        }
    }

    private val removeKeyProcessor = ObservableTransformer { actions: Observable<RemoveKeyAction> ->
        actions.switchMap { action ->
            secretKeyRepository.removeKey(action.keyAlias)
                    .toSingleDefault(NoValidKeyResult)
                    .toObservable()
                    .cast(SecretKeyResult::class.java)
                    .onErrorReturn { t -> ErrorResult(t) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(InFlightResult)
        }
    }

    private val encryptMessageProcessor = ObservableTransformer { actions: Observable<EncryptMessageAction> ->
        actions.switchMap { action ->
            secretKeyRepository.encrypt(action.keyAlias, action.messageToEncrypt)
                    .toObservable()
                    .map { messageEncrypted -> EncryptMessageResult(action.keyAlias, messageEncrypted) }
                    .cast(SecretKeyResult::class.java)
                    .onErrorReturn { t -> ErrorResult(t) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(InFlightResult)
        }
    }

    private val decryptMessageProcessor = ObservableTransformer { actions: Observable<DecryptMessageAction> ->
        actions.switchMap { action ->
            secretKeyRepository.decrypt(action.keyAlias, action.messageToDecrypt)
                    .toObservable()
                    .map { messageDecrypted -> DecryptMessageResult(action.keyAlias, action.messageToDecrypt, messageDecrypted) }
                    .cast(SecretKeyResult::class.java)
                    .onErrorReturn { t -> ErrorResult(t) }
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .startWith(InFlightResult)
        }
    }

    private val clearMessagesProcessor = ObservableTransformer { actions: Observable<ClearMessagesAction> ->
        actions.switchMap { Observable.just(ClearMessagesResult) }
    }
}