package com.mj.domain.usecase.base

import android.util.Log
import com.mj.domain.model.News
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

abstract class ActionUseCase<in P> {
    suspend operator fun invoke(dispatcher: CoroutineDispatcher, param: P){
        val name = this::class.java.simpleName
        return withContext(dispatcher) {
            Log.d(name, "invoke($param)")
            execute(param)
        }
    }

    protected abstract suspend fun execute(param: P)
}