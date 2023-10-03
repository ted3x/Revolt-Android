package ge.ted3x.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import ge.ted3x.revolt.RevoltRelationEntity
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.RevoltUserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RevoltUserDao @Inject constructor(private val database: RevoltDatabase) {

    fun getUserObservable(): Flow<RevoltUserEntity> {
        return database.revoltUserQueries.selectCurrentUser().asFlow().mapToOne(Dispatchers.IO)
    }

    fun insertUser(user: RevoltUserEntity) {
        database.revoltUserQueries.insertUser(user)
    }

    fun getCurrentUserId(): String? {
        return database.revoltUserQueries.selectCurrentUserId().executeAsOneOrNull()
    }

    fun getCurrentUser(): RevoltUserEntity {
        return database.revoltUserQueries.selectCurrentUser().executeAsOne()
    }

    fun getUser(userId: String): RevoltUserEntity {
        return database.revoltUserQueries.selectUserById(userId).executeAsOne()
    }

    fun getUserOrNull(userId: String): RevoltUserEntity? {
        return database.revoltUserQueries.selectUserById(userId).executeAsOneOrNull()
    }

    fun updateUsername(username: String) {
        database.revoltUserQueries.updateUsername(username)
    }

    fun getRelations(userId: String): List<RevoltRelationEntity> {
        return database.revoltUserQueries.selectRelationsByUserId(userId).executeAsList()
    }
}