package ge.ted3x.revolt.feature.dashboard.impl

import androidx.paging.PagingData
import ge.ted3x.revolt.core.domain.models.general.RevoltMessage
import ge.ted3x.revolt.core.domain.models.user.RevoltUser

data class DashboardScreenUiState(
    val messages: PagingData<RevoltMessage>,
    val user: RevoltUser
) {
    companion object {
        val Empty = DashboardScreenUiState(
            messages = PagingData.empty(),
            user = RevoltUser.Empty
        )
    }
}
