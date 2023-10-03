package ge.ted3x.revolt.feature.settings.impl.ui.screens.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.core.designsystem.dialog.RevoltDialog
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileCard
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileData
import ge.ted3x.revolt.core.designsystem.profile.RevoltProfileTab
import ge.ted3x.revolt.core.designsystem.textfield.RevoltTextField
import ge.ted3x.revolt.core.domain.models.general.RevoltFileDomain


private val tabs = listOf<RevoltProfileTab>(
    ProfileTab("Profile", ProfileTabType.Profile),
)

@Destination
@Composable
fun SettingsProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsProfileViewModel = hiltViewModel()
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        val uiState = viewModel.state.collectAsState()
        val showImagePicker = remember { mutableStateOf<RevoltFileDomain?>(null) }
        val informationValue =
            remember(key1 = uiState.value.content) {
                mutableStateOf(uiState.value.content)
            }
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
            uri?.let { currentContext.contentResolver.openInputStream(it) }?.let { stream ->
                viewModel.onImageSelected(stream.readBytes(), showImagePicker.value!!)
                stream.close()
            }
            showImagePicker.value = null
        }
        when {
            showImagePicker.value != null -> {
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
            onTabClick = { },
            data = profileData,
            selectedTab = tabs.first(),
            tabs = tabs
        ) {
            ProfileContent(uiState.value.content)
        }
        Button(onClick = {
            showImagePicker.value = RevoltFileDomain.Avatar
        }) {
            Text(text = "Upload new profile image")
        }
        Button(onClick = {
            viewModel.removeImage(RevoltFileDomain.Avatar)
        }, enabled = uiState.value.avatarUrl != null) {
            Text(text = "Remove profile image")
        }
        Button(onClick = {
            showImagePicker.value = RevoltFileDomain.Background
        }) {
            Text(text = "Upload new background image")
        }
        Button(onClick = {
            viewModel.removeImage(RevoltFileDomain.Background)
        }, enabled = uiState.value.backgroundUrl != null) {
            Text(text = "Remove background image")
        }
        Button(onClick = {
            viewModel.controlDisplayNameDialogVisibility(true)
        }) {
            Text(text = "Update display name")
        }

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(120.dp),
            value = informationValue.value,
            onValueChange = {
                informationValue.value = it
            })
        Button(onClick = {
            viewModel.updateProfileContent(informationValue.value)
        }, enabled = informationValue.value != uiState.value.content) {
            Text(text = "Save")
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
    Profile
}