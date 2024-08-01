package com.mj.domain.usecase.base

import android.util.Log
import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<in P, out R> {
    operator fun invoke(param: P): Flow<R> {
        val name = this::class.java.simpleName
        Log.d(name, "invoke($param)")
        return execute(param)
    }

    protected abstract fun execute(param: P): Flow<R>

}
