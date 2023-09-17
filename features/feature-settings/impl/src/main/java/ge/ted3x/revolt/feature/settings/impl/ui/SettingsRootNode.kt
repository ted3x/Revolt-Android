package ge.ted3x.revolt.feature.settings.impl.ui

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.bumble.appyx.components.backstack.activeElement
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.interactions.core.asElement
import com.bumble.appyx.navigation.composable.AppyxComponent
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.Node
import ge.ted3x.revolt.core.arch.RevoltBackstackRootNode
import ge.ted3x.revolt.core.designsystem.appbar.RevoltAppBar
import ge.ted3x.revolt.feature.settings.impl.ui.screens.account.SettingsAccountNode
import ge.ted3x.revolt.feature.settings.impl.ui.screens.main.SettingsMainNode
import ge.ted3x.revolt.feature.settings.impl.ui.screens.profile.SettingsProfileNode

class SettingsRootNode(buildContext: BuildContext) :
    RevoltBackstackRootNode<SettingsRootNode.SettingsTarget>(buildContext, SettingsTarget.Main) {

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
            SettingsTarget.Main -> SettingsMainNode(buildContext) { target -> backstack.push(target) }
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
            val backstackState = backstack.model.elements.collectAsState()
            val currentElementTitle =
                backstack.model.activeElement.asElement().interactionTarget.interactionTarget.title
            RevoltAppBar(
                onBackClick = { backstack.pop() },
                isBackVisible = backstackState.value.size > 1,
                title = currentElementTitle
            )
        }) {
            AppyxComponent(appyxComponent = backstack)
        }
    }
}