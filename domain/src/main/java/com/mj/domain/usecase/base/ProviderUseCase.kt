package com.mj.domain.usecase.base

import android.util.Log
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class ProviderUseCase<out R> {
    suspend operator fun invoke(dispatcher: CoroutineDispatcher): R {
        val name = this::class.java.simpleName
        return withContext(dispatcher) {
            Log.d(name, "invoke()")
            execute()
        }
    }

    protected abstract suspend fun execute(): R
}