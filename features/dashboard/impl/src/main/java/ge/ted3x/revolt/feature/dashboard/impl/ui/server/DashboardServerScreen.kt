package ge.ted3x.revolt.feature.dashboard.impl.ui.server

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import ge.ted3x.revolt.feature.dashboard.impl.DashboardScreen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DashboardServerScreen(onScroll: (idx:Int, offset: Int) -> Unit, modifier: Modifier = Modifier) {
    val state = rememberLazyListState()
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = state)
    LaunchedEffect(key1 = state.firstVisibleItemScrollOffset, block = {
        onScroll.invoke(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset)
    })
    LazyRow(
        state = state,
        flingBehavior = flingBehavior
    ) {
        item {
            DashboardServers(idx = 0)
        }
        item {
            val screenWidth = LocalConfiguration.current.screenWidthDp
            DashboardScreen(modifier = Modifier.width(screenWidth.dp))
        }
        item {
            DashboardServers(idx = 2)
        }
    }
}

@Composable
fun DashboardServers(idx: Int) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val calculatedWidth = screenWidth - (screenWidth * 0.1)
    LazyColumn(
        modifier = Modifier
            .width(calculatedWidth.dp)
            .rotate(if (idx == 0) 0f else 180f)
            .background(if (idx == 0) Color.Cyan else Color.Magenta)
    ) {
        items(50) {
            Text(text = "Server")
        }
    }
}