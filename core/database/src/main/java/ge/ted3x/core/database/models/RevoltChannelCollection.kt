package ge.ted3x.core.database.models

import ge.ted3x.revolt.RevoltChannelEntity
import ge.ted3x.revolt.RevoltChannelRolePermissionsEntity
import ge.ted3x.revolt.RevoltFileEntity

data class RevoltChannelCollection(
    val channel: RevoltChannelEntity,
    val rolePermissions: List<RevoltChannelRolePermissionsEntity>?,
    val icon: RevoltFileEntity?
)
