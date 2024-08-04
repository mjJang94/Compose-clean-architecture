package com.mj.domain.usecase.base

import android.util.Log
import kotlinx.coroutines.flow.Flow

abstract class FlowUseCase<out R> {
    operator fun invoke(): Flow<R> {
        val name = this::class.java.simpleName
        Log.d(name, "invoke()")
        return execute()
    }

    protected abstract fun execute(): Flow<R>

}
