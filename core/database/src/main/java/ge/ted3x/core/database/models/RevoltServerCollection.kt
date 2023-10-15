package ge.ted3x.core.database.models

import ge.ted3x.revolt.RevoltFileEntity
import ge.ted3x.revolt.RevoltServerCategoryEntity
import ge.ted3x.revolt.RevoltServerEntity
import ge.ted3x.revolt.RevoltServerRoleEntity

data class RevoltServerCollection(
    val server: RevoltServerEntity,
    val iconEntity: RevoltFileEntity?,
    val bannerEntity: RevoltFileEntity?,
    val categories: List<RevoltServerCategoryEntity>?,
    val roles: List<RevoltServerRoleEntity>?,
)
