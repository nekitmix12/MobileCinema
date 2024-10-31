package com.example.mobilecinema.di

import com.example.mobilecinema.data.repository.AuthRepositoryImpl
import com.example.mobilecinema.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindUserRepository
                (authRepositoryImpl: AuthRepositoryImpl):
            AuthRepository
/*    @Binds
    abstract fun bindInteractionRepository
                (interactionRepositoryImpl:
                 InteractionRepositoryImpl):
            InteractionRepository*/
}