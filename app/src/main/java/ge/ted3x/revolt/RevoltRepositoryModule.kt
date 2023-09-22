package ge.ted3x.revolt

import app.revolt.RevoltApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.ted3x.revolt.core.data.RevoltUserRepositoryImpl
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RevoltRepositoryModule {

    @Provides
    @Singleton
    fun providesRevoltApi(): RevoltApi {
        return RevoltApi()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class Repository {
        @Binds
        @Singleton
        abstract fun bindsRevoltUserRepository(impl: RevoltUserRepositoryImpl): RevoltUserRepository
    }
}