package com.mj.domain.usecase

import com.mj.domain.Repository
import com.mj.domain.model.News
import com.mj.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetScrapNewsUseCase @Inject constructor(
    private val repository: Repository,
) : FlowUseCase<List<News.Content>>() {

    override fun execute(): Flow<List<News.Content>> =
        repository.getScrapNews()
}