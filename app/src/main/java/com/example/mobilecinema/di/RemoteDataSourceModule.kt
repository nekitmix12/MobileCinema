package com.example.mobilecinema.di

import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSource
import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindAuthDataSource(authRemoteDataSourceImpl:
                                    AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource


}