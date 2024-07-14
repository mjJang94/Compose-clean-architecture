package com.mj.data.di

import android.util.Log
import com.mj.data.repo.RepositoryImpl
import com.mj.data.repo.remote.api.Endpoints
import com.mj.data.repo.remote.api.NaverApi
import com.mj.data.repo.source.RemoteDataSource
import com.mj.data.repo.source.RemoteDataSourceImpl
import com.mj.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Endpoints.BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor { message ->
                        Log.d("Retrofit", message)
                    }.apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): NaverApi =
        retrofit.create(NaverApi::class.java)

    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: NaverApi): RemoteDataSource =
        RemoteDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource): Repository =
        RepositoryImpl(remoteDataSource)
}