package pl.mobite.sample.security.ui.base.mvi

import io.reactivex.Scheduler


interface SchedulersProvider {

    fun subscriptionScheduler(): Scheduler

    fun observationScheduler(): Scheduler
}