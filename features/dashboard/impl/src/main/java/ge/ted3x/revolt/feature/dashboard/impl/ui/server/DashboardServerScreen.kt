package ge.ted3x.revolt.feature.dashboard.impl.ui.server

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ge.ted3x.revolt.feature.dashboard.impl.DashboardScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardServerScreen(
    onScroll: (idx: Int, offset: Int) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val state = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
    val key = remember { derivedStateOf { state.firstVisibleItemScrollOffset } }
    LaunchedEffect(key1 = key.value, block = {
        onScroll.invoke(state.firstVisibleItemIndex, key.value)
    })
    LazyRow(
        modifier = modifier,
        state = state,
        flingBehavior = flingBehavior
    ) {
        item {
            DashboardServers(modifier = Modifier.padding(paddingValues))
        }
        item {
            val screenWidth = LocalConfiguration.current.screenWidthDp
            DashboardScreen(modifier = Modifier.width(screenWidth.dp))
        }
        item {
            DashboardServers()
        }
    }
}

@Composable
fun DashboardServers(
    modifier: Modifier = Modifier,
    viewModel: DashboardServersViewModel = hiltViewModel()
) {
    Row {
        val screenWidth = LocalConfiguration.current.screenWidthDp
        val calculatedWidth = screenWidth - (screenWidth * 0.1)
        val servers = viewModel.servers.collectAsState(listOf()).value
        val selectedServer = remember { mutableStateOf("") }
        LazyRow(
            modifier = modifier
                .width(calculatedWidth.dp)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                LazyColumn {
                    items(servers.size, key = { servers[it].id }) { idx ->
                        val server = servers[idx]
                        val isSelectedServer = selectedServer.value == server.id
                        Row {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(2.dp, 56.dp)
                                    .background(if (isSelectedServer) Color.White else Color.Transparent)
                            )
                            AsyncImage(
                                model = server.icon?.url,
                                contentDescription = "${server.name} icon image",
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(56.dp)
                                    .clip(if (isSelectedServer) RoundedCornerShape(16.dp) else CircleShape)
                                    .clickable { selectedServer.value = server.id },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
            item {
                LazyColumn {
                    val channels = servers.first { it.id == selectedServer.value }.channels
                    items(channels.size, key = { channels[it] }) { idx ->
                        val server = servers[idx]
                        val isSelectedServer = selectedServer.value == server.id
                        Row {
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(2.dp, 56.dp)
                                    .background(if (isSelectedServer) Color.White else Color.Transparent)
                            )
                            AsyncImage(
                                model = server.icon?.url,
                                contentDescription = "${server.name} icon image",
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(56.dp)
                                    .clip(if (isSelectedServer) RoundedCornerShape(16.dp) else CircleShape)
                                    .clickable { selectedServer.value = server.id },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }
}