package ge.ted3x.revolt

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.ui.slider.BackStackSlider
import com.bumble.appyx.navigation.integration.ActivityIntegrationPoint
import com.bumble.appyx.navigation.integration.NodeHost
import com.bumble.appyx.navigation.platform.AndroidLifecycle
import ge.ted3x.revolt.feature.dashboard.ui.DashboardRootNode
import ge.ted3x.revolt.feature.settings.ui.SettingsRootNode
import ge.ted3x.revolt.ui.theme.RevoltTheme

class MainActivity : ComponentActivity() {

    private lateinit var appyxV2IntegrationPoint: ActivityIntegrationPoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appyxV2IntegrationPoint = createIntegrationPoint(savedInstanceState)
        setContent {
            RevoltTheme {
                // A surface container using the 'background' color from the theme
                NodeHost(
                    lifecycle = AndroidLifecycle(LocalLifecycleOwner.current.lifecycle),
                    integrationPoint = appyxV2IntegrationPoint,
                ) {
                    SettingsRootNode(
                        it,
                        BackStack(
                            model = BackStackModel(
                                SettingsRootNode.SettingsTarget.Main,
                                null
                            ),
                            motionController = { BackStackSlider(it) }
                        )
                    )
                }
            }
        }
    }

    private fun createIntegrationPoint(savedInstanceState: Bundle?) =
        ActivityIntegrationPoint(
            activity = this,
            savedInstanceState = savedInstanceState
        )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        appyxV2IntegrationPoint.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        appyxV2IntegrationPoint.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        appyxV2IntegrationPoint.onSaveInstanceState(outState)
    }
}