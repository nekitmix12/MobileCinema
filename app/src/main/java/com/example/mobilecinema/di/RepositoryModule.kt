package com.example.mobilecinema.di

import com.example.mobilecinema.domain.repository.UserRepository
import com.example.mobilecinema.data.UserRepositoryImpl
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
            UserRepository
/*    @Binds
    abstract fun bindInteractionRepository
                (interactionRepositoryImpl:
                 InteractionRepositoryImpl):
            InteractionRepository*/
}