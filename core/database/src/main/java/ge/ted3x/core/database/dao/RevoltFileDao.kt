package ge.ted3x.core.database.dao

import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.RevoltFileEntity
import javax.inject.Inject

class RevoltFileDao @Inject constructor(private val database: RevoltDatabase) {

    fun insertFile(file: RevoltFileEntity) {
        database.revoltFileQueries.insertFile(file)
    }

    fun getFile(fileId: String): RevoltFileEntity? {
        return database.revoltFileQueries.selectFileById(fileId).executeAsOneOrNull()
    }

    fun deleteFile(fileId: String) {
        return database.revoltFileQueries.deleteFileById(fileId)
    }
}