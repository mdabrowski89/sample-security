package pl.mobite.sample.security.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import pl.mobite.sample.security.ui.base.mvi.SchedulerProvider


class ImmediateSchedulerProvider private constructor(): SchedulerProvider {

    override fun io(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()

    companion object {

        val instance = ImmediateSchedulerProvider()
    }
}