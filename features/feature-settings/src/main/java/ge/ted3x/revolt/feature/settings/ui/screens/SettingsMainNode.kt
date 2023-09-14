package ge.ted3x.revolt.feature.settings.ui.screens

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
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import ge.ted3x.revolt.feature.settings.R
import ge.ted3x.revolt.feature.settings.ui.SettingsRootNode

class SettingsMainNode(
    buildContext: BuildContext,
    private val onClick: (SettingsRootNode.SettingsTarget) -> Unit
) : Node(buildContext) {

    companion object {
        val image = "https://i.kym-cdn.com/entries/icons/original/000/013/564/doge.jpg"
    }

    @Composable
    override fun View(modifier: Modifier) {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Profile()
            SettingOptions(
                itemsWithTitle = mapOf(
                    stringResource(id = R.string.settings_user_settings) to listOf(
                        SettingItem.Account {
                            onClick.invoke(SettingsRootNode.SettingsTarget.Account)
                        },
                        SettingItem.Profile {

                        },
                        SettingItem.Sessions {

                        }
                    ),
                    stringResource(id = R.string.settings_client_settings) to listOf(
                        SettingItem.VoiceSettings {

                        },
                        SettingItem.Appearance {

                        },
                        SettingItem.Notifications {

                        },
                        SettingItem.Language {

                        },
                        SettingItem.Sync {

                        }
                    ),
                    stringResource(id = R.string.settings_revolt) to listOf(
                        SettingItem.Bots {

                        }
                    )
                )
            )
            GeneralSettingOptions()
        }
    }

    @Composable
    private fun Profile() {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(Color.DarkGray)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                AsyncImage(
                    model = image,
                    contentDescription = "profile image",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                )
                Column {
                    Text(text = "ted3x")
                    Text(text = "ted3x#7383")
                    Text(text = "Offline")
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color.Gray)
            ) {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = "Change your status...")
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
            SettingItem.Feedback {

            },
            SettingItem.Changelog {

            },
            SettingItem.SourceCode {

            },
            SettingItem.Donate {

            }
        )
        generalOptions.forEach { item ->
            SettingItemButton(item)
        }
        val logoutItem = SettingItem.Logout {

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
                    painter = painterResource(id = item.image),
                    contentDescription = item.javaClass.simpleName
                )
                Spacer(modifier = Modifier.width(8.dp)) // Optional: For some spacing between image and text.
                Text(text = stringResource(id = item.text), textAlign = TextAlign.Start)
            }
        }
    }

    sealed class SettingItem {

        @get:StringRes
        abstract val text: Int

        @get:DrawableRes
        abstract val image: Int

        abstract val onClick: () -> Unit

        data class Account(
            override val text: Int = R.string.settings_my_account,
            override val image: Int = R.drawable.ic_account,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Profile(
            override val text: Int = R.string.settings_profile,
            override val image: Int = R.drawable.ic_profile,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Sessions(
            override val text: Int = R.string.settings_sessions,
            override val image: Int = R.drawable.ic_sessions,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class VoiceSettings(
            override val text: Int = R.string.settings_voice_settings,
            override val image: Int = R.drawable.ic_voice_settings,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Appearance(
            override val text: Int = R.string.settings_appearance,
            override val image: Int = R.drawable.ic_appearance,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Notifications(
            override val text: Int = R.string.settings_notifications,
            override val image: Int = R.drawable.ic_notifications,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Language(
            override val text: Int = R.string.settings_language,
            override val image: Int = R.drawable.ic_language,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Sync(
            override val text: Int = R.string.settings_sync,
            override val image: Int = R.drawable.ic_sync,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Bots(
            override val text: Int = R.string.settings_my_bots,
            override val image: Int = R.drawable.ic_bots,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Feedback(
            override val text: Int = R.string.settings_feedback,
            override val image: Int = R.drawable.ic_feedback,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Changelog(
            override val text: Int = R.string.settings_changelogs,
            override val image: Int = R.drawable.ic_changelogs,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class SourceCode(
            override val text: Int = R.string.settings_source_code,
            override val image: Int = R.drawable.ic_source_code,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Donate(
            override val text: Int = R.string.settings_donate,
            override val image: Int = R.drawable.ic_donate,
            override val onClick: () -> Unit
        ) : SettingItem()

        data class Logout(
            override val text: Int = R.string.settings_log_out,
            override val image: Int = R.drawable.ic_logout,
            override val onClick: () -> Unit
        ) : SettingItem()
    }
}