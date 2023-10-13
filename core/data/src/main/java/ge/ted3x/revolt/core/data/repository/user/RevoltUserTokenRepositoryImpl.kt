package ge.ted3x.revolt.core.data.repository.user

import android.content.SharedPreferences
import app.revolt.RevoltApi
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserTokenRepository
import javax.inject.Inject

class RevoltUserTokenRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val revoltApi: RevoltApi
) : RevoltUserTokenRepository {

    override fun retrieveToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, null)
    }

    override fun saveToken(token: String) {
        revoltApi.updateToken(token)
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    companion object {
        private const val TOKEN_KEY = "revolt_token"
    }
}