package com.mj.domain.usecase

import com.mj.domain.Repository
import com.mj.domain.model.News
import com.mj.domain.usecase.base.ActionUseCase
import javax.inject.Inject

class DeleteScrapNewsUseCase @Inject constructor(
    private val repository: Repository,
) : ActionUseCase<News.Content>() {

    override suspend fun execute(param: News.Content) {
        repository.deleteScrapNews(param)
    }

}