package ge.ted3x.core.database

import ge.ted3x.revolt.FileEntity
import ge.ted3x.revolt.RevoltDatabase
import javax.inject.Inject

class RevoltFileQueries @Inject constructor(private val database: RevoltDatabase) {

    fun insertFile(file: FileEntity) {
        database.revoltFileQueries.insertFile(file)
    }

    fun getFile(fileId: String): FileEntity? {
        return database.revoltFileQueries.selectFileById(fileId).executeAsOneOrNull()
    }

    fun deleteFile(fileId: String) {
        return database.revoltFileQueries.deleteFileById(fileId)
    }
}