package ge.ted3x.revolt.feature.settings.impl.ui.screens.account

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.core.designsystem.dialog.RevoltDialog
import ge.ted3x.revolt.core.designsystem.textfield.RevoltTextField
import ge.ted3x.revolt.feature.settings.impl.R

@Destination
@Composable
fun SettingsAccountScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsAccountViewModel = hiltViewModel()
) {
    val uiState = viewModel.state.collectAsState()
    val usernameWithDiscriminator = "${uiState.value.username}#${uiState.value.discriminator}"
    val openChangeUsernameDialog = remember { mutableStateOf(false) }
    val openChangeEmailDialog = remember { mutableStateOf(false) }
    val openChangePasswordDialog = remember { mutableStateOf(false) }

    when {
        openChangeUsernameDialog.value -> {
            ChangeUsernameDialog(
                onDismissRequest = { openChangeUsernameDialog.value = false },
                onConfirmation = { username, password ->
                    viewModel.updateUsername(
                        username,
                        password
                    )
                }
            )
        }

        openChangeEmailDialog.value -> {
            ChangeEmailDialog(
                onDismissRequest = { openChangeEmailDialog.value = false },
                onConfirmation = {}
            )
        }

        openChangePasswordDialog.value -> {
            ChangePasswordDialog(
                onDismissRequest = { openChangePasswordDialog.value = false },
                onConfirmation = {}
            )
        }
    }
    Column(modifier = modifier) {
        Row {
            AsyncImage(
                model = uiState.value.imageUrl,
                contentDescription = "profile image",
                modifier = Modifier
                    .clip(
                        CircleShape
                    )
                    .size(128.dp)
            )
            Column {
                Text(text = usernameWithDiscriminator)
                Text(text = uiState.value.userId)
            }
        }
        ActionButton(
            title = "username",
            value = usernameWithDiscriminator,
            icon = painterResource(id = R.drawable.ic_tag)
        ) {
            openChangeUsernameDialog.value = true
        }
        ActionButton(
            title = "email",
            value = "ted3x",
            icon = painterResource(id = R.drawable.ic_email)
        ) {
            openChangeEmailDialog.value = true
        }
        ActionButton(
            title = "password",
            value = "********",
            icon = painterResource(id = R.drawable.ic_password)
        ) {
            openChangePasswordDialog.value = true
        }
    }
}

@Composable
private fun ActionButton(
    title: String,
    value: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(Color.DarkGray)
            .fillMaxWidth()
    ) {
        Image(painter = icon, contentDescription = title)
        Column {
            Text(text = title)
            Text(text = value)
        }
        Image(
            painter = painterResource(id = R.drawable.ic_edit),
            contentDescription = "edit action"
        )
    }
}

@Composable
fun ChangeUsernameDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: (username: String, password: String) -> Unit
) {
    val usernameValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    RevoltDialog(
        onDismissRequest = onDismissRequest,
        onConfirmation = { onConfirmation.invoke(usernameValue.value, passwordValue.value) },
        content = {
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { usernameValue.value = it },
                value = usernameValue.value,
                hint = "Enter your preferred username",
                title = "Username"
            )
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { passwordValue.value = it },
                value = passwordValue.value,
                hint = "Enter your current password",
                title = "Current Password"
            )
        },
        title = "Change your username",
        negativeText = "Cancel",
        positiveText = "Update"
    )
}

@Composable
fun ChangeEmailDialog(onDismissRequest: () -> Unit, onConfirmation: () -> Unit) {
    RevoltDialog(
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        content = {
            val emailValue = remember { mutableStateOf("") }
            val passwordValue = remember { mutableStateOf("") }
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { emailValue.value = it },
                value = emailValue.value,
                hint = "Please enter your email",
                title = "Email"
            )
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { passwordValue.value = it },
                value = passwordValue.value,
                hint = "Enter your current password",
                title = "Current Password"
            )
        },
        title = "Change your email",
        negativeText = "Cancel",
        positiveText = "Send Email"
    )
}

@Composable
fun ChangePasswordDialog(onDismissRequest: () -> Unit, onConfirmation: () -> Unit) {
    RevoltDialog(
        onDismissRequest = onDismissRequest,
        onConfirmation = onConfirmation,
        content = {
            val currentPasswordValue = remember { mutableStateOf("") }
            val passwordValue = remember { mutableStateOf("") }
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { currentPasswordValue.value = it },
                value = currentPasswordValue.value,
                hint = "Please enter your password",
                title = "Password"
            )
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = { passwordValue.value = it },
                value = passwordValue.value,
                hint = "Enter your current password",
                title = "Current Password"
            )
        },
        title = "Change your password",
        negativeText = "Cancel",
        positiveText = "Update"
    )
}
