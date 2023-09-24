package ge.ted3x.core.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import ge.ted3x.revolt.BotEntity
import ge.ted3x.revolt.ProfileEntity
import ge.ted3x.revolt.RelationEntity
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RevoltUserQueries @Inject constructor(private val database: RevoltDatabase) {

    fun getUserObservable(): Flow<UserEntity> {
        return database.revoltUserQueries.selectCurrentUser().asFlow().mapToOne(Dispatchers.IO)
    }

    fun insertUser(user: UserEntity) {
        database.revoltUserQueries.insertUser(user)
    }

    fun getUser(userId: String): UserEntity {
        return database.revoltUserQueries.selectUserById(userId).executeAsOne()
    }

    fun getProfile(userId: String): ProfileEntity? {
        return database.revoltUserQueries.selectProfileByUserId(userId).executeAsOneOrNull()
    }

    fun getBot(userId: String): BotEntity? {
        return database.revoltUserQueries.selectBotByUserId(userId).executeAsOneOrNull()
    }

    fun getRelations(userId: String): List<RelationEntity> {
        return database.revoltUserQueries.selectRelationByUserId(userId).executeAsList()
    }

    fun updateUsername(userId: String, username: String) {
        database.revoltUserQueries.updateUsername(username, userId)
    }

}