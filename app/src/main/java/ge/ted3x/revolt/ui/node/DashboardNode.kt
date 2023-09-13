package ge.ted3x.revolt.ui.node

import android.util.Log
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.scaleIn
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
import androidx.compose.runtime.remember
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

class DashboardNode(buildContext: BuildContext) : Node(buildContext) {

    sealed class Target {
        object Dashboard : Target()
        object Servers : Target()
        object Members : Target()
    }

    @Composable
    override fun View(modifier: Modifier) {
        val items = listOf(Target.Servers, Target.Dashboard, Target.Members)
        val spotlight = remember {
            Spotlight(
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
        }
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
}