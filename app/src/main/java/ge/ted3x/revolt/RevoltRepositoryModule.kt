package ge.ted3x.revolt

import androidx.security.crypto.EncryptedSharedPreferences
import app.revolt.RevoltApi
import app.revolt.RevoltApiConfig
import app.revolt.revoltApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.ted3x.revolt.core.data.RevoltAccountRepositoryImpl
import ge.ted3x.revolt.core.data.RevoltConfigurationRepositoryImpl
import ge.ted3x.revolt.core.data.RevoltFileRepositoryImpl
import ge.ted3x.revolt.core.data.RevoltGatewayRepositoryImpl
import ge.ted3x.revolt.core.data.RevoltUserRepositoryImpl
import ge.ted3x.revolt.core.data.RevoltUserTokenRepositoryImpl
import ge.ted3x.revolt.core.domain.core.RevoltConfigurationRepository
import ge.ted3x.revolt.core.domain.user.RevoltAccountRepository
import ge.ted3x.revolt.core.domain.user.RevoltFileRepository
import ge.ted3x.revolt.core.domain.user.RevoltGatewayRepository
import ge.ted3x.revolt.core.domain.user.RevoltUserRepository
import ge.ted3x.revolt.core.domain.user.RevoltUserTokenRepository
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
    }
}