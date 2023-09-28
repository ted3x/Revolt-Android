package ge.ted3x.revolt.core.data.mapper

import app.revolt.model.user.RevoltUserPresenceApiType
import app.revolt.model.user.RevoltUserStatusApiModel
import ge.ted3x.revolt.core.domain.models.RevoltUserPresence
import ge.ted3x.revolt.core.domain.models.RevoltUserStatus
import javax.inject.Inject

class RevoltUserStatusMapper @Inject constructor() {

    fun mapApiToDomain(apiModel: RevoltUserStatusApiModel): RevoltUserStatus {
        return RevoltUserStatus(text = apiModel.text, presence = apiModel.presence?.toDomainModel())
    }

    fun mapDomainToApi(domainModel: RevoltUserStatus): RevoltUserStatusApiModel {
        return RevoltUserStatusApiModel(
            text = domainModel.text,
            presence = domainModel.presence?.toApiModel()
        )
    }

    private fun RevoltUserPresence.toApiModel() = when (this) {
        RevoltUserPresence.Online -> RevoltUserPresenceApiType.Online
        RevoltUserPresence.Idle -> RevoltUserPresenceApiType.Idle
        RevoltUserPresence.Focus -> RevoltUserPresenceApiType.Focus
        RevoltUserPresence.Busy -> RevoltUserPresenceApiType.Busy
        RevoltUserPresence.Invisible -> RevoltUserPresenceApiType.Invisible
    }

    private fun RevoltUserPresenceApiType.toDomainModel() = when (this) {
        RevoltUserPresenceApiType.Online -> RevoltUserPresence.Online
        RevoltUserPresenceApiType.Idle -> RevoltUserPresence.Idle
        RevoltUserPresenceApiType.Focus -> RevoltUserPresence.Focus
        RevoltUserPresenceApiType.Busy -> RevoltUserPresence.Busy
        RevoltUserPresenceApiType.Invisible -> RevoltUserPresence.Invisible
    }
}