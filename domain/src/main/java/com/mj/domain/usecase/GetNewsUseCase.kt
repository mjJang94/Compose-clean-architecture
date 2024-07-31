package com.mj.domain.usecase

import com.mj.domain.Repository
import com.mj.domain.model.News
import com.mj.domain.usecase.base.ConsumerUseCase
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: Repository,
) : ConsumerUseCase<GetNewsUseCase.GetNewsParam, News>() {

    override suspend fun execute(param: GetNewsParam): News =
        repository.getNews(param.query, param.start)

    data class GetNewsParam(
        val query: String,
        val start: Int,
    )
}