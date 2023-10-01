package ge.ted3x.core.database

import ge.ted3x.revolt.MemberEntity
import ge.ted3x.revolt.RevoltDatabase
import ge.ted3x.revolt.SelectMembers
import javax.inject.Inject

class RevoltMemberQueries @Inject constructor(private val database: RevoltDatabase) {

    fun insertMember(member: MemberEntity) {
        database.revoltMemberQueries.insertMember(member)
    }

    fun getMembers(memberIds: List<String>): List<SelectMembers> {
        return database.revoltMemberQueries.selectMembers(memberIds).executeAsList()
    }
}