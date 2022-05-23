package com.epam.harrypotterspells.util.scheduler

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class DefaultSchedulerProvider : SchedulerProvider {
    override fun getIOScheduler(): Scheduler = Schedulers.io()

    override fun getComputationScheduler(): Scheduler = Schedulers.computation()

    override fun getUIScheduler(): Scheduler = AndroidSchedulers.mainThread()
}