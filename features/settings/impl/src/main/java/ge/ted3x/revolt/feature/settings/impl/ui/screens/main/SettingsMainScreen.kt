package ge.ted3x.revolt.feature.settings.impl.ui.screens.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import ge.ted3x.revolt.core.designsystem.dialog.RevoltDialog
import ge.ted3x.revolt.core.designsystem.gif.RevoltGifImage
import ge.ted3x.revolt.core.designsystem.textfield.RevoltTextField
import ge.ted3x.revolt.feature.settings.impl.R
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsAccountScreenDestination
import ge.ted3x.revolt.feature.settings.impl.ui.screens.destinations.SettingsProfileScreenDestination

@Destination(start = true)
@Composable
fun SettingsMainScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: SettingsMainViewModel = hiltViewModel()
) {
    val uiState = viewModel.state.collectAsState()
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Profile(viewModel, uiState)
        SettingOptions(
            itemsWithTitle = mapOf(
                stringResource(id = R.string.settings_user_settings) to listOf(
                    SettingItem(SettingType.ACCOUNT) {
                        //userQueries.insertUser()
                        navigator.navigate(SettingsAccountScreenDestination)
                    },
                    SettingItem(SettingType.PROFILE) {
                        navigator.navigate(SettingsProfileScreenDestination)
                    },
                    SettingItem(SettingType.SESSIONS) {
                    }
                ),
                stringResource(id = R.string.settings_client_settings) to listOf(
                    SettingItem(SettingType.VOICE_SETTINGS) {
                    },
                    SettingItem(SettingType.APPEARANCE) {
                    },
                    SettingItem(SettingType.NOTIFICATIONS) {
                    },
                    SettingItem(SettingType.LANGUAGE) {
                    },
                    SettingItem(SettingType.SYNC) {
                    }
                ),
                stringResource(id = R.string.settings_revolt) to listOf(
                    SettingItem(SettingType.BOTS) {
                    }
                )
            )
        )
        GeneralSettingOptions()
    }
}

@Composable
private fun Profile(
    viewModel: SettingsMainViewModel,
    uiState: State<SettingsMainUiState>
) {
    if (uiState.value.showStatusChangeDialog) {
        ChangeStatusDialog(
            status = uiState.value.status,
            onDismissRequest = { viewModel.controlStatusChangeDialog(false) },
            onConfirmation = {
                viewModel.changeStatus(it)
                viewModel.controlStatusChangeDialog(false)
            }
        )
    }
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.DarkGray)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            RevoltGifImage(
                model = uiState.value.profileImage,
                contentDescription = "profile image",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Column {
                Text(text = uiState.value.displayName)
                Text(text = "${uiState.value.username}#${uiState.value.discriminator}")
                Text(text = "Offline")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(Color.Gray)
        ) {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.controlStatusChangeDialog(true)
                }) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = uiState.value.status,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}

@Composable
private fun SettingOptions(itemsWithTitle: Map<String, List<SettingItem>>) {
    itemsWithTitle.forEach { (title, items) ->
        Column {
            Text(text = title)
            items.forEach { item ->
                SettingItemButton(item)
            }
        }
    }
}

@Composable
private fun GeneralSettingOptions() {
    val generalOptions = listOf(
        SettingItem(SettingType.FEEDBACK) {

        },
        SettingItem(SettingType.CHANGELOG) {

        },
        SettingItem(SettingType.SOURCE_CODE) {

        },
        SettingItem(SettingType.DONATE) {

        }
    )
    generalOptions.forEach { item ->
        SettingItemButton(item)
    }
    val logoutItem = SettingItem(SettingType.LOGOUT) {

    }
    SettingItemButton(item = logoutItem)
}

@Composable
private fun SettingItemButton(item: SettingItem) {
    TextButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = item.onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = item.type.imageRes),
                contentDescription = item.javaClass.simpleName
            )
            Spacer(modifier = Modifier.width(8.dp)) // Optional: For some spacing between image and text.
            Text(text = stringResource(id = item.type.textRes), textAlign = TextAlign.Start)
        }
    }
}

@Composable
fun ChangeStatusDialog(
    status: String,
    onDismissRequest: () -> Unit,
    onConfirmation: (displayName: String) -> Unit
) {
    val statusValue = remember { mutableStateOf(status) }
    RevoltDialog(
        onDismissRequest = onDismissRequest,
        onConfirmation = { onConfirmation.invoke(statusValue.value) },
        content = {
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { statusValue.value = it },
                value = statusValue.value,
                hint = "Enter your new status",
                title = "Status"
            )
        },
        title = "Change your status",
        negativeText = "Cancel",
        positiveText = "Save"
    )
}

enum class SettingType(
    @StringRes val textRes: Int,
    @DrawableRes val imageRes: Int
) {
    ACCOUNT(R.string.settings_my_account, R.drawable.ic_account),
    PROFILE(R.string.settings_profile, R.drawable.ic_profile),
    SESSIONS(R.string.settings_sessions, R.drawable.ic_sessions),
    VOICE_SETTINGS(R.string.settings_voice_settings, R.drawable.ic_voice_settings),
    APPEARANCE(R.string.settings_appearance, R.drawable.ic_appearance),
    NOTIFICATIONS(R.string.settings_notifications, R.drawable.ic_notifications),
    LANGUAGE(R.string.settings_language, R.drawable.ic_language),
    SYNC(R.string.settings_sync, R.drawable.ic_sync),
    BOTS(R.string.settings_my_bots, R.drawable.ic_bots),
    FEEDBACK(R.string.settings_feedback, R.drawable.ic_feedback),
    CHANGELOG(R.string.settings_changelogs, R.drawable.ic_changelogs),
    SOURCE_CODE(R.string.settings_source_code, R.drawable.ic_source_code),
    DONATE(R.string.settings_donate, R.drawable.ic_donate),
    LOGOUT(R.string.settings_log_out, R.drawable.ic_logout)
}

data class SettingItem(
    val type: SettingType,
    val onClick: () -> Unit
)