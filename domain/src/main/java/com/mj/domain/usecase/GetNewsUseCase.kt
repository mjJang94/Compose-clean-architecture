package com.mj.domain.usecase

import com.mj.domain.Repository
import javax.inject.Inject

class GetNewsUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(query: String, start: Int) =
        repository.getNews(query, start)
}