package ge.ted3x.revolt.feature.dashboard.ui

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.bumble.appyx.components.spotlight.Spotlight
import com.bumble.appyx.components.spotlight.SpotlightModel
import com.bumble.appyx.components.spotlight.ui.slider.SpotlightSlider
import com.bumble.appyx.components.spotlight.ui.sliderscale.SpotlightSliderScale
import com.bumble.appyx.interactions.core.ui.gesture.GestureSettleConfig
import com.bumble.appyx.interactions.core.ui.helper.AppyxComponentSetup
import com.bumble.appyx.interactions.sample.Children
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode

class DashboardRootNode(
    buildContext: BuildContext,
    items: List<DashboardTarget> = listOf(
        DashboardTarget.Servers,
        DashboardTarget.Channel,
        DashboardTarget.Members
    ),
    private val spotlight: Spotlight<DashboardTarget> = Spotlight(
        model = SpotlightModel(items, savedStateMap = null),
        motionController = { SpotlightSliderScale(it) },
        gestureFactory = { SpotlightSlider.Gestures(it, Orientation.Horizontal, false) },
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow / 4),
        gestureSettleConfig = GestureSettleConfig(
            completionThreshold = 0.2f,
            completeGestureSpec = spring(),
            revertGestureSpec = spring(),
        ),
    )
) : ParentNode<DashboardRootNode.DashboardTarget>(spotlight, buildContext) {

    sealed class DashboardTarget {
        data object Servers : DashboardTarget()
        data object Channel : DashboardTarget()
        data object Members : DashboardTarget()
    }

    override fun resolve(interactionTarget: DashboardTarget, buildContext: BuildContext): Node {
        return when (interactionTarget) {
            DashboardTarget.Channel -> ChannelNode(buildContext)
            DashboardTarget.Members -> MembersNode(buildContext)
            DashboardTarget.Servers -> ServersNode(buildContext)
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        AppyxComponentSetup(appyxComponent = spotlight)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Cyan)
        ) {
            Children(
                appyxComponent = spotlight,
                screenWidthPx = 1200,
                1200,
                modifier = Modifier.fillMaxSize(),
                childContent = {
                    Box(
                        modifier = Modifier
                            .background(Color.Red)
                            .fillMaxWidth()
                            .height(100.dp)
                            .pointerInput(it.element.id) {
                                detectDragGestures(
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        spotlight.onDrag(dragAmount, this)
                                    },
                                    onDragEnd = {
                                        Log.d("drag", "end")
                                        spotlight.onDragEnd()
                                    }
                                )
                            }
                    ) {
                        Text(text = it.element.id)
                    }
                }
            )
        }
    }

    class ServersNode(buildContext: BuildContext) : Node(buildContext = buildContext) {
        @Composable
        override fun View(modifier: Modifier) {
            Text("Servers")
        }
    }

    class ChannelNode(buildContext: BuildContext) : Node(buildContext = buildContext) {
        @Composable
        override fun View(modifier: Modifier) {
            Text("Channel")
        }
    }

    class MembersNode(buildContext: BuildContext) : Node(buildContext = buildContext) {
        @Composable
        override fun View(modifier: Modifier) {
            Text("Members")
        }
    }
}