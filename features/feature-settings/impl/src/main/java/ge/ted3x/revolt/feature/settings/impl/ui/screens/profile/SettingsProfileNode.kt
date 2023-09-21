package ge.ted3x.revolt.feature.settings.impl.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileCard
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileTab
import ge.ted3x.revolt.feature.settings.impl.ui.SettingsNavGraph


private val tabs = listOf<RevoltProfileTab>(
    ProfileTab("Profile", ProfileTabType.Profile),
    ProfileTab("Mutual Friends", ProfileTabType.MutualFriends),
    ProfileTab("Mutual Groups", ProfileTabType.MutualGroups),
    ProfileTab("Mutual Servers", ProfileTabType.MutualServers),
)

@SettingsNavGraph
@Destination
@Composable
fun SettingsProfileScreen(modifier: Modifier = Modifier) {
    Column {
        val selectedTab = remember { mutableStateOf(tabs.first()) }
        Text(text = "Preview")
        RevoltProfileCard(
            onAddClick = {},
            onTabClick = { selectedTab.value = it },
            selectedTab.value,
            tabs
        ) { tab ->
            when ((tab as ProfileTab).type) {
                ProfileTabType.Profile -> ProfileContent()
                ProfileTabType.MutualFriends -> MutualFriendsContent()
                ProfileTabType.MutualGroups -> MutualGroupsContent()
                ProfileTabType.MutualServers -> MutualServersContent()
            }
        }
    }
}

@Composable
private fun ProfileContent() {
    Column {
        Text(text = "Information")
        Text(text = "Some information")
    }
}

@Composable
private fun MutualFriendsContent() {
    Column {
        Text(text = "Information")
        Text(text = "Some MutualFriendsContent")
    }
}

@Composable
private fun MutualGroupsContent() {
    Column {
        Text(text = "Information")
        Text(text = "Some MutualGroupsContent")
    }
}

@Composable
private fun MutualServersContent() {
    Column {
        Text(text = "Information")
        Text(text = "Some MutualServersContent")
    }
}

private data class ProfileTab(
    override val title: String,
    val type: ProfileTabType
) : RevoltProfileTab

private enum class ProfileTabType {
    Profile,
    MutualFriends,
    MutualGroups,
    MutualServers
}