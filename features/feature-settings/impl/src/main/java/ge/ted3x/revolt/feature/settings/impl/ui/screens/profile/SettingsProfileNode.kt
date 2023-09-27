package ge.ted3x.revolt.feature.settings.impl.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileCard
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileData
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileTab


private val tabs = listOf<RevoltProfileTab>(
    ProfileTab("Profile", ProfileTabType.Profile),
//    ProfileTab("Mutual Friends", ProfileTabType.MutualFriends),
//    ProfileTab("Mutual Groups", ProfileTabType.MutualGroups),
//    ProfileTab("Mutual Servers", ProfileTabType.MutualServers),
)

@Destination
@Composable
fun SettingsProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsProfileViewModel = hiltViewModel()
) {
    Column {
        val selectedTab = remember { mutableStateOf(tabs.first()) }
        val showImagePicker = remember { mutableStateOf(false) }
        val uiState = viewModel.state.collectAsState()
        val profileData = RevoltProfileData(
            username = uiState.value.username,
            displayName = uiState.value.displayName,
            discriminator = uiState.value.discriminator,
            statusMessage = uiState.value.statusMessage,
            avatarUrl = uiState.value.avatarUrl,
            backgroundUrl = uiState.value.backgroundUrl,
        )
        val currentContext = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            val stream = uri?.let { currentContext.contentResolver.openInputStream(it) }?.readBytes()
            viewModel.onImageSelected(stream!!)
        }
        if (showImagePicker.value) {
            showImagePicker.value = false
            SideEffect {
                launcher.launch(
                    PickVisualMediaRequest(
                        //Here we request only photos. Change this to .ImageAndVideo if
                        //you want videos too.
                        //Or use .VideoOnly if you only want videos.
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        }
        Text(text = "Preview")
        RevoltProfileCard(
            onAddClick = {},
            onTabClick = { selectedTab.value = it },
            data = profileData,
            selectedTab = selectedTab.value,
            tabs = tabs
        ) { tab ->
            when ((tab as ProfileTab).type) {
                ProfileTabType.Profile -> ProfileContent(uiState.value.content)
//                ProfileTabType.MutualFriends -> MutualFriendsContent()
//                ProfileTabType.MutualGroups -> MutualGroupsContent()
//                ProfileTabType.MutualServers -> MutualServersContent()
            }
        }
        Button(onClick = {
            showImagePicker.value = true
        }) {
            Text(text = "Upload new profile image")
        }
    }
}

@Composable
private fun ProfileContent(content: String) {
    Column {
        Text(text = "Information")
        Text(text = content)
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
//    MutualFriends,
//    MutualGroups,
//    MutualServers
}