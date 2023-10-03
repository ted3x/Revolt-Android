package ge.ted3x.core.database.dao

import ge.ted3x.revolt.RevoltMemberEntity
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.SelectMembers
import javax.inject.Inject

class RevoltMemberDao @Inject constructor(private val database: RevoltDatabase) {

    fun insertMember(member: RevoltMemberEntity) {
        database.revoltMemberQueries.insertMember(member)
    }

    fun getMembers(memberIds: List<String>): List<SelectMembers> {
        return database.revoltMemberQueries.selectMembers(memberIds).executeAsList()
    }
}