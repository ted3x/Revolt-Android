package ge.ted3x.revolt.feature.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.interactions.core.ui.helper.AppyxComponentSetup
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode
import ge.ted3x.revolt.feature.settings.ui.screens.SettingsAccountNode
import ge.ted3x.revolt.feature.settings.ui.screens.SettingsMainNode

class SettingsRootNode(
    buildContext: BuildContext,
    private val backStack: BackStack<SettingsTarget>
) :
    ParentNode<SettingsRootNode.SettingsTarget>(backStack, buildContext) {

    sealed class SettingsTarget {
        data object Main : SettingsTarget()
        data object Account : SettingsTarget()
        data object Profile : SettingsTarget()
        data object Sessions : SettingsTarget()
        data object Voice : SettingsTarget()
        data object Appearance : SettingsTarget()
        data object Notifications : SettingsTarget()
        data object Language : SettingsTarget()
        data object Sync : SettingsTarget()
        data object Experiments : SettingsTarget()
        data object Bots : SettingsTarget()
        data object Feedback : SettingsTarget()
    }

    override fun resolve(interactionTarget: SettingsTarget, buildContext: BuildContext): Node {
        return when (interactionTarget) {
            SettingsTarget.Main -> SettingsMainNode(buildContext) { backStack.push(SettingsTarget.Account) }
            SettingsTarget.Account -> SettingsAccountNode(buildContext)
            SettingsTarget.Appearance -> TODO()
            SettingsTarget.Bots -> TODO()
            SettingsTarget.Experiments -> TODO()
            SettingsTarget.Feedback -> TODO()
            SettingsTarget.Language -> TODO()
            SettingsTarget.Notifications -> TODO()
            SettingsTarget.Profile -> TODO()
            SettingsTarget.Sessions -> TODO()
            SettingsTarget.Sync -> TODO()
            SettingsTarget.Voice -> TODO()
        }
    }

    @Composable
    override fun View(modifier: Modifier) {
        Column {
            AppyxComponent(appyxComponent = backStack)
            Row {
                TextButton(onClick = { backStack.pop() }) {
                    Text(text = "Pop")
                }
            }
        }
    }
}