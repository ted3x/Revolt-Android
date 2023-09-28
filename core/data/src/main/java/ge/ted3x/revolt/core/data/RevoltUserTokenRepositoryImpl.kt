package ge.ted3x.revolt.core.data

import android.content.SharedPreferences
import ge.ted3x.revolt.core.domain.user.RevoltUserTokenRepository
import javax.inject.Inject

class RevoltUserTokenRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : RevoltUserTokenRepository {

    override fun retrieveToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, "n0_vYtkgvN0v3ZCGwyTeJ_r5Z6nRrM72sOgskbnFHAFLyPANChlC7jv5SVXEZmHf")
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    companion object {
        private const val TOKEN_KEY = "revolt_token"
    }
}