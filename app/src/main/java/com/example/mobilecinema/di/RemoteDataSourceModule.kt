package com.example.mobilecinema.di

import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSource
import com.example.mobilecinema.data.datasource.remote.data_source.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
//очень бы хотелось использовать di фраемвкри ,потому что приходится писать очень много зависимосте :(
//оставлю на память...
@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSourceModule {

    @Binds
    abstract fun bindAuthDataSource(authRemoteDataSourceImpl:
                                    AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource


}