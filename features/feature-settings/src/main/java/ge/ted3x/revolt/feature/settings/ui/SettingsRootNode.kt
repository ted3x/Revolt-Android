package ge.ted3x.revolt.feature.settings.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.activeElement
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.interactions.core.asElement
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.ParentNode
import ge.ted3x.revolt.core.designsystem.appbar.RevoltAppBar
import ge.ted3x.revolt.feature.settings.ui.screens.account.SettingsAccountNode
import ge.ted3x.revolt.feature.settings.ui.screens.main.SettingsMainNode
import ge.ted3x.revolt.feature.settings.ui.screens.profile.SettingsProfileNode

class SettingsRootNode(
    private val appContext: Context,
    buildContext: BuildContext,
    private val backStack: BackStack<SettingsTarget>
) :
    ParentNode<SettingsRootNode.SettingsTarget>(backStack, buildContext) {

    sealed class SettingsTarget {

        abstract val title: String

        data object Main : SettingsTarget() {
            override val title: String
                get() = "Settings"
        }

        data object Account : SettingsTarget() {
            override val title: String
                get() = "My Account"
        }

        data object Profile : SettingsTarget() {
            override val title: String
                get() = "Profile"
        }

        data object Sessions : SettingsTarget() {
            override val title: String
                get() = "Sessions"
        }

        data object Voice : SettingsTarget() {
            override val title: String
                get() = "Voice Settings"
        }

        data object Appearance : SettingsTarget() {
            override val title: String
                get() = "Appearance"
        }

        data object Notifications : SettingsTarget() {
            override val title: String
                get() = "Notifications"
        }

        data object Language : SettingsTarget() {
            override val title: String
                get() = "Language"
        }

        data object Sync : SettingsTarget() {
            override val title: String
                get() = "Sync"
        }

        data object Bots : SettingsTarget() {
            override val title: String
                get() = "My Bots"
        }

        data object Feedback : SettingsTarget() {
            override val title: String
                get() = "Feedback"
        }
    }

    override fun resolve(interactionTarget: SettingsTarget, buildContext: BuildContext): Node {
        return when (interactionTarget) {
            SettingsTarget.Main -> SettingsMainNode(appContext, buildContext) { target -> backStack.push(target) }
            SettingsTarget.Account -> SettingsAccountNode(buildContext)
            SettingsTarget.Profile -> SettingsProfileNode(buildContext)
            SettingsTarget.Appearance -> TODO()
            SettingsTarget.Bots -> TODO()
            SettingsTarget.Feedback -> TODO()
            SettingsTarget.Language -> TODO()
            SettingsTarget.Notifications -> TODO()
            SettingsTarget.Sessions -> TODO()
            SettingsTarget.Sync -> TODO()
            SettingsTarget.Voice -> TODO()
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    override fun View(modifier: Modifier) {
        Scaffold(topBar = {
            val backstackState = backStack.model.elements.collectAsState()
            val currentElementTitle =
                backStack.model.activeElement.asElement().interactionTarget.interactionTarget.title
            RevoltAppBar(
                onBackClick = { backStack.pop() },
                isBackVisible = backstackState.value.size > 1,
                title = currentElementTitle
            )
        }) {
            AppyxComponent(appyxComponent = backStack)
        }
    }
}