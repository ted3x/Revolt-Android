package ge.ted3x.revolt.feature.settings.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileCard
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileTab

class SettingsProfileNode(buildContext: BuildContext) : Node(buildContext) {

    private val tabs = listOf<RevoltProfileTab>(
        ProfileTab("Profile", ProfileTabType.Profile),
        ProfileTab("Mutual Friends", ProfileTabType.MutualFriends),
        ProfileTab("Mutual Groups", ProfileTabType.MutualGroups),
        ProfileTab("Mutual Servers", ProfileTabType.MutualServers),
    )

    @Composable
    override fun View(modifier: Modifier) {
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

}

@Preview
@Composable
fun Preview() {
    SettingsProfileNode(BuildContext.root(null)).View(modifier = Modifier)
}