package ge.ted3x.core.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import ge.ted3x.revolt.RelationEntity
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RevoltUserQueries @Inject constructor(private val database: RevoltDatabase) {

    fun getUserObservable(): Flow<UserEntity> {
        return database.revoltUserQueries.selectCurrentUser().asFlow().mapToOne(Dispatchers.IO)
    }

    fun insertUser(user: UserEntity) {
        database.revoltUserQueries.insertUser(user)
    }

    fun getCurrentUserId(): String? {
        return database.revoltUserQueries.selectCurrentUserId().executeAsOneOrNull()
    }

    fun getCurrentUser(): UserEntity {
        return database.revoltUserQueries.selectCurrentUser().executeAsOne()
    }

    fun getUser(userId: String): UserEntity {
        return database.revoltUserQueries.selectUserById(userId).executeAsOne()
    }

    fun getUserOrNull(userId: String): UserEntity? {
        return database.revoltUserQueries.selectUserById(userId).executeAsOneOrNull()
    }

    fun updateUsername(username: String) {
        database.revoltUserQueries.updateUsername(username)
    }

    fun getRelations(userId: String): List<RelationEntity> {
        return database.revoltUserQueries.selectRelationsByUserId(userId).executeAsList()
    }
}