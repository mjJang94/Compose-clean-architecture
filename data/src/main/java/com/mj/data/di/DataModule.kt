package com.mj.data.di

import android.content.Context
import androidx.room.Room
import com.mj.data.repo.RepositoryImpl
import com.mj.data.repo.local.AppDatabase
import com.mj.data.repo.local.dao.NewsDao
import com.mj.data.repo.remote.RemoteApiService
import com.mj.data.repo.remote.api.NaverApi
import com.mj.data.repo.source.RemoteDataSource
import com.mj.data.repo.source.RemoteDataSourceImpl
import com.mj.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
     * Remote
     */
    @Provides
    @Singleton
    fun provideRemoteApi(): RemoteApiService = RemoteApiService()

    @Provides
    @Singleton
    fun provideNaverApiService(remoteApiService: RemoteApiService): NaverApi =
        remoteApiService.create(NaverApi::class.java)

    /**
     * Local
     */
    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "local-database",
        ).build()

    @Provides
    @Singleton
    fun provideNewsDao(appDatabase: AppDatabase): NewsDao = appDatabase.newsDao()

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        naverRemote: NaverApi,
        newDao: NewsDao,
    ): RemoteDataSource =

        RemoteDataSourceImpl(
            naverRemote = naverRemote,
            newsDao = newDao,
        )

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository =
        RepositoryImpl(remoteDataSource)
}