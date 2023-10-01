package ge.ted3x.core.database

import ge.ted3x.revolt.MemberEntity
import ge.ted3x.revolt.RevoltDatabase
import javax.inject.Inject

class RevoltMemberQueries @Inject constructor(private val database: RevoltDatabase) {

    fun insertMember(member: MemberEntity) {
        database.revoltMemberQueries.insertMember(member)
    }
}