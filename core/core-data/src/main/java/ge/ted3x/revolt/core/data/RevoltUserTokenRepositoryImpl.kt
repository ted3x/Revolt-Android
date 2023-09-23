package ge.ted3x.revolt.core.data

import android.content.SharedPreferences
import ge.ted3x.revolt.core.domain.user.RevoltUserTokenRepository
import javax.inject.Inject

class RevoltUserTokenRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : RevoltUserTokenRepository {

    override fun retrieveToken(): String? {
        return sharedPreferences.getString(TOKEN_KEY, "KKx3FtQQlg4z5h3O7tzUZ9yyxD12r2ZWkLO47JtkD3WN0sqPHXOwQV3Fch6b2IzV")
    }

    override fun saveToken(token: String) {
        sharedPreferences.edit().putString(TOKEN_KEY, token).apply()
    }

    companion object {
        private const val TOKEN_KEY = "revolt_token"
    }
}