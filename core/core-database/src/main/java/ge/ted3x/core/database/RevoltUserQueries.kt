package ge.ted3x.core.database

import ge.ted3x.revolt.BotEntity
import ge.ted3x.revolt.ProfileEntity
import ge.ted3x.revolt.RelationEntity
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.UserEntity
import javax.inject.Inject

class RevoltUserQueries @Inject constructor(private val database: RevoltDatabase) {

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
//
//    fun generateRandomUser(): UserEntity {
//        val id = List(10) { Random.nextInt(0, 10) }.joinToString("")
//        val username = "user${Random.nextInt(1000, 9999)}"
//        val discriminator = List(4) { Random.nextInt(0, 10) }.joinToString("")
//
//        val displayNames = listOf("Alice", "Bob", "Charlie", "Dave", "Eva", null)
//        val display_name = displayNames.random()
//
//        val badges = Random.nextLong(0, 100)
//        val statusTexts = listOf("Happy", "Busy", "At work", "Away", null)
//        val status_text = statusTexts.random()
//
//        val statusPresences = listOf("Online", "Offline", "Idle", null)
//        val status_presence = statusPresences.random()
//
//        val flags = Random.nextLong(0, 100)
//        val privileged = Random.nextBoolean()
//        val relationships = listOf("Friend", "Blocked", "Pending", null)
//        val relationship = relationships.random()
//        val online = Random.nextBoolean()
//
//        return UserEntity(
//            id = id,
//            username = username,
//            discriminator = discriminator,
//            display_name = display_name,
//            badges = badges,
//            status_text = status_text,
//            status_presence = status_presence,
//            flags = flags,
//            privileged = privileged,
//            relationship = relationship,
//            online = online
//        )
//    }

}