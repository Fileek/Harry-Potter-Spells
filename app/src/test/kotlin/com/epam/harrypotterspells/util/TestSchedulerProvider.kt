package com.epam.harrypotterspells.util

import com.epam.harrypotterspells.util.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class TestSchedulerProvider : SchedulerProvider {
    override fun getIOScheduler(): Scheduler = Schedulers.trampoline()

    override fun getComputationScheduler(): Scheduler = Schedulers.trampoline()

    override fun getUIScheduler(): Scheduler = Schedulers.trampoline()
}