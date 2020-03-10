package com.pd.pokerdom.worker

import android.content.Context
import android.util.Log
import androidx.work.*

const val WORK_MY = "MyWorker"

class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        Log.d(WORK_MY, "Performing long running task in scheduled job")
        return Result.success()
    }
}

fun initMyWorker(ctx: Context) {
        val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .build()

        WorkManager.getInstance(ctx).beginUniqueWork(
            WORK_MY,
            ExistingWorkPolicy.REPLACE,
            workRequest).enqueue()
}

fun cancelMyWorker(ctx: Context) {
//    WorkManager.getInstance(ctx).cancelAllWorkByTag(WORK_MY)
    WorkManager.getInstance(ctx).cancelUniqueWork(WORK_MY)
}