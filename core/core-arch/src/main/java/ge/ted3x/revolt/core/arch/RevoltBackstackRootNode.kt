package ge.ted3x.revolt.core.arch

import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.ui.slider.BackStackSlider
import com.bumble.appyx.interactions.core.plugin.Plugin
import com.bumble.appyx.navigation.modality.BuildContext
import com.bumble.appyx.navigation.node.ParentNode

abstract class RevoltBackstackRootNode<InteractionTarget : Any>(
    buildContext: BuildContext,
    private val defaultTarget: InteractionTarget,
    protected val backstack: BackStack<InteractionTarget> = BackStack(
        model = BackStackModel(defaultTarget, buildContext.savedStateMap),
        motionController = { BackStackSlider(it) }
    ),
    plugins: List<Plugin> = listOf()
) : ParentNode<InteractionTarget>(backstack, buildContext, plugins = plugins)