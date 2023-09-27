package ge.ted3x.revolt.feature.settings.impl.ui.screens.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import ge.ted3x.revolt.core.designsystem.dialog.RevoltDialog
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileCard
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileData
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileTab
import ge.ted3x.revolt.core.designsystem.textfield.RevoltTextField


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
        val uiState = viewModel.state.collectAsState()
        val selectedTab = remember { mutableStateOf(tabs.first()) }
        val showImagePicker = remember { mutableStateOf(false) }
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
            val stream =
                uri?.let { currentContext.contentResolver.openInputStream(it) }?.readBytes()
            viewModel.onImageSelected(stream!!)
        }
        when {
            showImagePicker.value -> {
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

            uiState.value.showChangeDisplayNameDialog -> {
                ChangeDisplayNameDialog(
                    onDismissRequest = {
                        viewModel.controlDisplayNameDialogVisibility(false)
                    }, onConfirmation = {
                        viewModel.changeDisplayName(it)
                    })
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
        Button(onClick = {
            viewModel.controlDisplayNameDialogVisibility(true)
        }) {
            Text(text = "Update display name")
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
fun ChangeDisplayNameDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (displayName: String) -> Unit
) {
    val displayNameValue = remember { mutableStateOf("") }
    RevoltDialog(
        onDismissRequest = onDismissRequest,
        onConfirmation = { onConfirmation.invoke(displayNameValue.value) },
        content = {
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { displayNameValue.value = it },
                value = displayNameValue.value,
                hint = "Enter your preferred display name",
                title = "Display Name"
            )
        },
        title = "Change your display name",
        negativeText = "Cancel",
        positiveText = "Save"
    )
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