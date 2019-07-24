package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.data.repositories.SecretKeyRepository
import pl.mobite.sample.security.data.repositories.SecretKeyRepositoryImpl


val repositoriesModule = module {

    factory<SecretKeyRepository> { SecretKeyRepositoryImpl() }

}