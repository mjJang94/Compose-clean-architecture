package com.mj.domain.usecase.base

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class ConsumerUseCase<in P, out R> {
    suspend operator fun invoke(dispatcher: CoroutineDispatcher, param: P): R {
        val name = this::class.java.simpleName
        return withContext(dispatcher) {
            Log.d(name, "invoke($param)")
            execute(param)
        }
    }

    protected abstract suspend fun execute(param: P): R
}