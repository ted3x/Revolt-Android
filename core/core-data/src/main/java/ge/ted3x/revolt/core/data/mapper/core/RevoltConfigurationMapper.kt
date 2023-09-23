package ge.ted3x.revolt.core.data.mapper.core

import app.revolt.model.RevoltConfigurationApiModel
import ge.ted3x.revolt.RevoltConfigurationEntity
import ge.ted3x.revolt.core.domain.models.core.RevoltConfiguration
import javax.inject.Inject

class RevoltConfigurationMapper @Inject constructor() {

    fun mapApiToEntity(apiModel: RevoltConfigurationApiModel): RevoltConfigurationEntity {
        return with(apiModel) {
            RevoltConfigurationEntity(
                revolt = revolt,
                captcha_enabled = apiModel.features.captcha.enabled,
                captcha_key = apiModel.features.captcha.key,
                email_verification_enabled = apiModel.features.email,
                invite_only = apiModel.features.inviteOnly,
                autumn_enabled = apiModel.features.autumn.enabled,
                autumn_url = apiModel.features.autumn.url,
                january_enabled = apiModel.features.january.enabled,
                january_url = apiModel.features.january.url,
                voso_enabled = apiModel.features.voso.enabled,
                voso_url = apiModel.features.voso.url,
                voso_ws = apiModel.features.voso.ws,
                ws = apiModel.ws,
                vapid = apiModel.vapid,
                app = apiModel.app,
                build_commit_sha = apiModel.build.commitSHA,
                build_commit_timestamp = apiModel.build.commitTimestamp,
                build_semver = apiModel.build.semver,
                build_origin_url = apiModel.build.originUrl,
                build_timestamp = apiModel.build.timestamp
            )
        }
    }

    fun mapEntityToDomain(entity: RevoltConfigurationEntity): RevoltConfiguration {
        return with(entity) {
            RevoltConfiguration(
                revolt = revolt,
                features = RevoltConfiguration.Features(
                    captcha = RevoltConfiguration.Features.HCaptchaConfiguration(
                        enabled = entity.captcha_enabled,
                        key = entity.captcha_key,
                    ),
                    email = email_verification_enabled,
                    inviteOnly = invite_only,
                    autumn = RevoltConfiguration.Features.GeneralServerConfiguration(
                        enabled = autumn_enabled,
                        url = autumn_url
                    ),
                    january = RevoltConfiguration.Features.GeneralServerConfiguration(
                        enabled = january_enabled,
                        url = january_url
                    ),
                    voso = RevoltConfiguration.Features.VoiceServerConfiguration(
                        enabled = voso_enabled,
                        url = voso_url,
                        ws = voso_ws
                    )
                ),
                ws = ws,
                app = app,
                vapid = vapid,
                build = RevoltConfiguration.Build(
                    commitSHA = build_commit_sha,
                    commitTimestamp = build_commit_timestamp,
                    semver = build_semver,
                    originUrl = build_origin_url,
                    timestamp = build_timestamp
                )
            )
        }
    }
}