package ge.ted3x.revolt

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RevoltDataStoreModule {

    private fun getMasterKey(context: Context) =
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()

    @Provides
    @Singleton
    fun providesSharedPreference(@ApplicationContext context: Context) = EncryptedSharedPreferences.create(
        context,
        REVOLT_SHARED_PREFS_FILE_NAME,
        getMasterKey(context),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private const val REVOLT_SHARED_PREFS_FILE_NAME = "revolt_shared_preferences"
}