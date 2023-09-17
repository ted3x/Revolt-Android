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
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ge.ted3x.core.database.RevoltUserQueries
import ge.ted3x.revolt.feature.settings.R
import ge.ted3x.revolt.feature.settings.impl.ui.SettingsRootNode

class SettingsMainNode(
    buildContext: BuildContext,
    private val onClick: (SettingsRootNode.SettingsTarget) -> Unit,
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
                        SettingItem(SettingType.ACCOUNT) {
                            //userQueries.insertUser()
                            onClick.invoke(SettingsRootNode.SettingsTarget.Account)
                        },
                        SettingItem(SettingType.PROFILE) {
                            onClick.invoke(SettingsRootNode.SettingsTarget.Profile)
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

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SettingsMainNodeEntryPoint {
        fun userQueries(): RevoltUserQueries
    }

}