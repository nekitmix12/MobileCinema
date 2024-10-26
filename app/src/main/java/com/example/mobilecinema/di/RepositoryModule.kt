package com.example.mobilecinema.di

import com.example.mobilecinema.data.repository.UserRepositoryImpl
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
                (userRepositoryImpl: UserRepositoryImpl):
            AuthRepository
/*    @Binds
    abstract fun bindInteractionRepository
                (interactionRepositoryImpl:
                 InteractionRepositoryImpl):
            InteractionRepository*/
}