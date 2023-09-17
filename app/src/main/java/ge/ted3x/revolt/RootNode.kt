package ge.ted3x.revolt

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.components.backstack.operation.replace
import com.bumble.appyx.interactions.core.plugin.Plugin
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import ge.ted3x.revolt.core.arch.RevoltBackstackRootNode
import ge.ted3x.revolt.core.arch.RevoltNavigator
import ge.ted3x.revolt.feature.dashboard.ui.DashboardRootNode
import ge.ted3x.revolt.feature.settings.impl.ui.SettingsRootNode

class RootNode(
    buildContext: BuildContext,
    plugins: List<Plugin>,
    private val navigator: RevoltNavigator
) :
    RevoltBackstackRootNode<RootNode.RootTarget>(
        buildContext,
        RootTarget.Root,
        plugins = plugins
    ) {

    sealed class RootTarget {
        data object Root : RootTarget()
        data object Settings : RootTarget()
        data object Dashboard : RootTarget()
    }

    override fun resolve(interactionTarget: RootTarget, buildContext: BuildContext): Node {
        return when (interactionTarget) {
            RootTarget.Root -> Node(buildContext)
            RootTarget.Settings -> SettingsRootNode(buildContext)
            RootTarget.Dashboard -> DashboardRootNode(buildContext)
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Box {
            AppyxComponent(appyxComponent = backstack)
            Column {
                TextButton(onClick = { navigator.navigateToDashboard() }) {
                    Text("Dashboard")
                }
                Spacer(modifier = Modifier.padding(top = 40.dp))
                TextButton(onClick = { navigator.navigateToSettings() }) {
                    Text("Settings")
                }
            }
        }
    }

    suspend fun attachSettings(): SettingsRootNode {
        return attachChild {
            backstack.replace(RootTarget.Settings)
        }
    }

    suspend fun attachDashboard(): DashboardRootNode {
        return attachChild {
            backstack.replace(RootTarget.Dashboard)
        }
    }
}
