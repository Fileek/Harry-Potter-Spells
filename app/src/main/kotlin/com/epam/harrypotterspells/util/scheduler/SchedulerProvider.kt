package com.epam.harrypotterspells.util.scheduler

import io.reactivex.rxjava3.core.Scheduler

/**
 * Provides schedulers for three types of work: IO-bound, computational and [getUIScheduler].
 */
interface SchedulerProvider {
    /**
     * Provides scheduler intended for IO-bound work.
     */
    fun getIOScheduler(): Scheduler

    /**
     * Provides scheduler intended for computational work.
     */
    fun getComputationScheduler(): Scheduler

    /**
     * Provides scheduler intended for UI work.
     */
    fun getUIScheduler(): Scheduler
}