package com.tensoriot.di

import com.google.firebase.auth.FirebaseAuth
import com.tensoriot.auth.AuthRepository
import com.tensoriot.auth.FirebaseRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class AuthModule {

    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


    @Singleton
    @Provides
    fun providesAuthRepository(firebaseRepository: FirebaseRepository): AuthRepository{
        return firebaseRepository
    }
}