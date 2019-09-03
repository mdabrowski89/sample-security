package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.data.repositories.SecretKeyRepositoryApi23Impl
import pl.mobite.sample.security.data.repositories.SecretKeyRepositoryImpl
import pl.mobite.sample.security.utils.hasMarshmallow


val repositoriesModule = module {

    factory<SecretKeyRepository> {
        if (hasMarshmallow()) {
            SecretKeyRepositoryApi23Impl()
        } else {
            SecretKeyRepositoryImpl()
        }
    }

}