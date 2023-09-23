package ge.ted3x.revolt.core.data.mapper

import app.revolt.model.user.RevoltUserProfileApiModel
import ge.ted3x.revolt.FileEntity
import ge.ted3x.revolt.ProfileEntity
import ge.ted3x.revolt.core.domain.models.RevoltUserProfile
import javax.inject.Inject

class RevoltProfileMapper @Inject constructor(private val fileMapper: RevoltFileMapper) {

    fun mapApiToEntity(userId: String, apiModel: RevoltUserProfileApiModel): ProfileEntity {
        return with(apiModel) {
            ProfileEntity(
                content = content,
                background_id = apiModel.background?.id,
                user_id = userId
            )
        }
    }

    fun mapEntityToDomain(entityModel: ProfileEntity, backgroundModel: FileEntity): RevoltUserProfile {
        return with(entityModel) {
            RevoltUserProfile(
                content = content,
                background = fileMapper.mapEntityToDomain(backgroundModel)
            )
        }
    }
}