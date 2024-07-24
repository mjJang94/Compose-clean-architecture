package com.mj.domain.usecase

import com.mj.domain.Repository
import com.mj.domain.model.News
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class GetNewsUseCase @Inject constructor(
    private val repository: Repository,
) : BaseUseCase<GetNewsUseCase.GetNewsParam, News>() {

    override suspend fun execute(param: GetNewsParam): News =
        repository.getNews(param.query, param.start)

    data class GetNewsParam(
        val query: String,
        val start: Int,
    )
}