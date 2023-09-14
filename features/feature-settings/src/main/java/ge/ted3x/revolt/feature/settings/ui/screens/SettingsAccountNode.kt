package ge.ted3x.revolt.feature.settings.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import ge.ted3x.revolt.feature.settings.R
import ge.ted3x.revolt.feature.settings.ui.screens.SettingsMainNode.Companion.image

class SettingsAccountNode(buildContext: BuildContext) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {

        val openChangeUsernameDialog = remember { mutableStateOf(false) }

        when {
            openChangeUsernameDialog.value -> {
                ChangeUsernameDialog(
                    onDismissRequest = { openChangeUsernameDialog.value = false },
                    onConfirmation = {

                    })
            }
        }
        Column {
            Row {
                AsyncImage(
                    model = image, contentDescription = "profile image", modifier = Modifier
                        .clip(
                            CircleShape
                        )
                        .size(128.dp)
                )
                Column {
                    Text(text = "ted3x#7383")
                    Text(text = "Unique identificator")
                }
            }
            ActionButton(
                title = "username",
                value = "ted3x",
                icon = painterResource(id = R.drawable.ic_tag)
            ) {
                openChangeUsernameDialog.value = true
            }
            ActionButton(
                title = "email",
                value = "ted3x",
                icon = painterResource(id = R.drawable.ic_email)
            ) {

            }
            ActionButton(
                title = "password",
                value = "********",
                icon = painterResource(id = R.drawable.ic_password)
            ) {

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
        onConfirmation: () -> Unit,
    ) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            // Draw a rectangle shape with rounded corners inside the dialog
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(375.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "Change your username")
                    val usernameValue = remember { mutableStateOf("") }
                    val passwordValue = remember { mutableStateOf("") }
                    TextField(
                        value = usernameValue.value,
                        onValueChange = { usernameValue.value = it },
                        singleLine = true
                    )
                    TextField(
                        value = passwordValue.value,
                        onValueChange = { passwordValue.value = it },
                        singleLine = true
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        TextButton(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Dismiss")
                        }
                        TextButton(
                            onClick = { onConfirmation() },
                            modifier = Modifier.padding(8.dp),
                        ) {
                            Text("Confirm")
                        }
                    }

                }
            }
        }
    }
}