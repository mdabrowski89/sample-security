package pl.mobite.sample.security.ui.base.mvi

import io.reactivex.Scheduler


interface SchedulerProvider {

    fun io(): Scheduler

    fun ui(): Scheduler
}