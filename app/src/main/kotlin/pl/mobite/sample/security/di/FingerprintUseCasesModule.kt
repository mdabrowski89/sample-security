package pl.mobite.sample.security.di

import org.koin.dsl.module
import pl.mobite.sample.security.uscases.CheckFingerprintEnrolledUseCase
import pl.mobite.sample.security.uscases.CheckFingerprintEnrolledUseCaseImpl
import pl.mobite.sample.security.uscases.CheckFingerprintHardwareUseCase
import pl.mobite.sample.security.uscases.CheckFingerprintHardwareUseCaseImpl


val fingerprintUseCasesModule = module {

    factory<CheckFingerprintHardwareUseCase> { CheckFingerprintHardwareUseCaseImpl(get()) }

    factory<CheckFingerprintEnrolledUseCase> { CheckFingerprintEnrolledUseCaseImpl(get()) }
}