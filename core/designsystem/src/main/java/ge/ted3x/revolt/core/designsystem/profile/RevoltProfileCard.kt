package ge.ted3x.revolt.core.designsystem.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun RevoltProfileCard(
    onAddClick: () -> Unit,
    onTabClick: (RevoltProfileTab) -> Unit,
    data: RevoltProfileData,
    selectedTab: RevoltProfileTab,
    tabs: List<RevoltProfileTab>,
    modifier: Modifier = Modifier,
    tabContent: @Composable (RevoltProfileTab) -> Unit
) {
    Card(modifier = modifier) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                AsyncImage(
                    modifier = Modifier.matchParentSize(),
                    model = data.backgroundUrl,
                    contentDescription = "background image",
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                )
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(top = 16.dp, end = 16.dp, start = 16.dp),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(96.dp),
                            model = data.avatarUrl,
                            contentDescription = "profile image",
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Column {
                            Text(text = data.displayName, color = Color.White)
                            Text(text = data.usernameWithDiscriminator, color = Color.LightGray)
                            Text(text = data.statusMessage, color = Color.LightGray)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = onAddClick) {
                            Icon(imageVector = Icons.Filled.Add, contentDescription = "add friend")
                        }
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    Row {
                        // Implement badges
                    }
                    Spacer(modifier = Modifier.padding(16.dp))
                    Row {
                        tabs.forEach { tab ->
                            ProfileTabButton(
                                onClick = { onTabClick.invoke(tab) },
                                text = tab.title,
                                isSelected = selectedTab == tab
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                tabContent.invoke(selectedTab)
            }
        }
    }
}

@Composable
private fun ProfileTabButton(
    onClick: () -> Unit,
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val maxWidth = LocalConfiguration.current.screenWidthDp / 4

    Column(
        modifier = modifier
            .wrapContentHeight()
            .width(maxWidth.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextButton(onClick = onClick) {
            Text(text = text, minLines = 2, textAlign = TextAlign.Center)
        }
        AnimatedVisibility(visible = isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(color = Color.White)
            )
        }
    }
}

data class RevoltProfileData(
    val username: String,
    val displayName: String,
    val discriminator: String,
    val statusMessage: String,
    val avatarUrl: String?,
    val backgroundUrl: String?
) {
    val usernameWithDiscriminator = "$username#$discriminator"
}

interface RevoltProfileTab {
    val title: String
}