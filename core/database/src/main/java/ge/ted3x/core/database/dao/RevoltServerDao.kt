package ge.ted3x.core.database.dao

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import ge.ted3x.core.database.models.RevoltServerCollection
import ge.ted3x.revolt.GetServer
import ge.ted3x.revolt.RevoltDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class RevoltServerDao @Inject constructor(private val database: RevoltDatabase) {

    private val dao = database.revoltServerQueries

    fun insertServer(collection: RevoltServerCollection) {
        database.transaction {
            dao.insertServer(collection.server)
            collection.categories?.forEach { dao.insertCategory(it) }
            collection.roles?.forEach { dao.insertRole(it) }
        }
    }

    fun getServer(id: String): GetServer {
        return dao.getServer(id).executeAsOne()
    }

    fun getServers(): Flow<List<RevoltServerCollection>> {
        val fileDao = database.revoltFileQueries
        return dao.getServers().asFlow().mapToList(Dispatchers.IO)
            .mapLatest { list ->
                list.map { serverEntity ->
                    val categories = dao.getCategories(serverEntity.id).executeAsList()
                    val roles = dao.getRoles(serverEntity.id).executeAsList()
                    val icon = serverEntity.icon_id?.let { fileDao.selectFileById(it).executeAsOne() }
                    val banner = serverEntity.banner_id?.let { fileDao.selectFileById(it).executeAsOne() }
                    RevoltServerCollection(
                        server = serverEntity,
                        iconEntity = icon,
                        bannerEntity = banner,
                        categories = categories,
                        roles = roles
                    )
                }
            }
    }
}