package ge.ted3x.core.database.dao

import ge.ted3x.core.database.models.RevoltServerCollection
import ge.ted3x.revolt.GetServer
import ge.ted3x.revolt.RevoltDatabase
import javax.inject.Inject

class RevoltServerDao @Inject constructor(private val database: RevoltDatabase) {

    fun insertServer(collection: RevoltServerCollection) {
        database.transaction {
            database.revoltServerQueries.insertServer(collection.server)
            collection.categories?.forEach { database.revoltServerQueries.insertCategory(it) }
            collection.roles?.forEach { database.revoltServerQueries.insertRole(it) }
        }
    }

    fun getServer(id: String): GetServer {
        return database.revoltServerQueries.getServer(id).executeAsOne()
    }
}