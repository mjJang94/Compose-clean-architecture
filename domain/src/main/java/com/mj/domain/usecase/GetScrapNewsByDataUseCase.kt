package com.mj.domain.usecase

import com.mj.domain.Repository
import com.mj.domain.model.News
import com.mj.domain.usecase.base.ConsumerUseCase
import javax.inject.Inject

class GetScrapNewsByDataUseCase @Inject constructor(
    private val repository: Repository
) : ConsumerUseCase<GetScrapNewsByDataUseCase.GetScrapNewsParam, News.Content?>() {

    override suspend fun execute(param: GetScrapNewsParam): News.Content? =
        repository.getScrapNewsByData(param.title, param.date)

    data class GetScrapNewsParam(
        val title: String,
        val date: String,
    )
}