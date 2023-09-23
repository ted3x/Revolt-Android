package ge.ted3x.core.database

import ge.ted3x.revolt.RevoltConfigurationEntity
import ge.ted3x.revolt.RevoltDatabase
import javax.inject.Inject

class RevoltConfigurationQueries @Inject constructor(private val database: RevoltDatabase) {

    suspend fun insertConfiguration(configurationEntity: RevoltConfigurationEntity) {
        database.transaction {
            database.revoltConfigurationQueries.nukeConfiguration()
            database.revoltConfigurationQueries.insertConfiguration(configurationEntity)
        }
    }

    suspend fun getConfiguration(): RevoltConfigurationEntity {
        return database.revoltConfigurationQueries.selectConfiguration().executeAsOne()
    }
}