package ge.ted3x.revolt

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import ge.ted3x.revolt.core.arch.RevoltBackstackRootNode
import ge.ted3x.revolt.feature.settings.impl.ui.SettingsRootNode

class RootNode(buildContext: BuildContext) : RevoltBackstackRootNode<RootNode.RootTarget>(
    buildContext,
    RootTarget.Root
) {

    sealed class RootTarget {
        data object Root : RootTarget()
        data object Settings : RootTarget()
    }

    override fun resolve(interactionTarget: RootTarget, buildContext: BuildContext): Node {
        return when (interactionTarget) {
            RootTarget.Root -> Node(buildContext)
            RootTarget.Settings -> SettingsRootNode(buildContext)
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Box {
            AppyxComponent(appyxComponent = backstack)
            TextButton(onClick = { backstack.push(RootTarget.Settings) }) {
                Text("click me")
            }
        }
    }
}
