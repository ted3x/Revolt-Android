package ge.ted3x.revolt.feature.dashboard.impl

import ge.ted3x.revolt.core.domain.models.general.RevoltMessage

data class DashboardScreenUiState(
    val messages: List<RevoltMessage> = listOf()
)
