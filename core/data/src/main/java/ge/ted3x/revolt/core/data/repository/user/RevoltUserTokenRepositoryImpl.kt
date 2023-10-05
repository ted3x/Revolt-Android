package ge.ted3x.revolt.core.data.repository.user

import android.content.SharedPreferences
import ge.ted3x.revolt.core.domain.repository.user.RevoltUserTokenRepository
import javax.inject.Inject

class RevoltUserTokenRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : RevoltUserTokenRepository {

    override fun retrieveToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, "sIqX6x2z9M-BzLvjWSILvjA070CBFD-nJ6wNqwP2OfmO-k3k6CcE_8qMH1sATiCi")
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    companion object {
        private const val TOKEN_KEY = "revolt_token"
    }
}