package ge.ted3x.revolt

import app.revolt.RevoltApi
import app.revolt.RevoltApiConfig
import app.revolt.revoltApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.ted3x.revolt.core.data.repository.user.RevoltAccountRepositoryImpl
import ge.ted3x.revolt.core.data.repository.general.RevoltConfigurationRepositoryImpl
import ge.ted3x.revolt.core.data.repository.general.RevoltFileRepositoryImpl
import ge.ted3x.revolt.core.data.repository.general.RevoltGatewayRepositoryImpl
import ge.ted3x.revolt.core.data.repository.channel.RevoltMessagingRepositoryImpl
import ge.ted3x.revolt.core.data.repository.server.RevoltServerRepositoryImpl
import ge.ted3x.revolt.core.data.repository.user.RevoltSessionsRepositoryImpl
import ge.ted3x.revolt.core.data.repository.user.RevoltUserRepositoryImpl
import ge.ted3x.revolt.core.data.repository.user.RevoltUserTokenRepositoryImpl
import ge.ted3x.revolt.core.domain.repository.general.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltAccountRepository
import ge.ted3x.revolt.core.domain.repository.general.RevoltFileRepository
import ge.ted3x.revolt.core.domain.repository.general.RevoltGatewayRepository
import ge.ted3x.revolt.core.domain.repository.channel.RevoltMessagingRepository
import ge.ted3x.revolt.core.domain.repository.server.RevoltServerRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltSessionsRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserRepository
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserTokenRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RevoltRepositoryModule {

    @Provides
    @Singleton
    fun providesRevoltApi(tokenRepository: RevoltUserTokenRepository): RevoltApi {
        val config = RevoltApiConfig(
            token = tokenRepository.retrieveToken()
        )
        return revoltApi(config)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class Repository {
        @Binds
        @Singleton
        abstract fun bindsRevoltUserRepository(impl: RevoltUserRepositoryImpl): RevoltUserRepository

        @Binds
        @Singleton
        abstract fun bindsRevoltUserTokenRepository(impl: RevoltUserTokenRepositoryImpl): RevoltUserTokenRepository

        @Binds
        @Singleton
        abstract fun bindsRevoltConfigurationRepository(impl: RevoltConfigurationRepositoryImpl): RevoltConfigurationRepository

        @Binds
        @Singleton
        abstract fun bindsRevoltAccountRepository(impl: RevoltAccountRepositoryImpl): RevoltAccountRepository

        @Binds
        @Singleton
        abstract fun bindsRevoltGatewayRepository(impl: RevoltGatewayRepositoryImpl): RevoltGatewayRepository

        @Binds
        @Singleton
        abstract fun bindsFileRepository(impl: RevoltFileRepositoryImpl): RevoltFileRepository


        @Binds
        @Singleton
        abstract fun bindsRevoltSessionsRepository(impl: RevoltSessionsRepositoryImpl): RevoltSessionsRepository

        @Binds
        @Singleton
        abstract fun bindsRevoltMessagingRepository(impl: RevoltMessagingRepositoryImpl): RevoltMessagingRepository

        @Binds
        @Singleton
        abstract fun bindsRevoltServerRepository(impl: RevoltServerRepositoryImpl): RevoltServerRepository
    }
}