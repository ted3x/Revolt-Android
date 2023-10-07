package ge.ted3x.revolt.feature.auth.signin.impl

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.feature.auth.signin.api.SIGN_IN_FEATURE_SCREEN_ROUTE
import ge.ted3x.feature.auth.signin.api.SignInFeatureScreen
import ge.ted3x.revolt.core.designsystem.DesignSystemDrawable
import ge.ted3x.revolt.core.designsystem.text.SpannedText
import ge.ted3x.revolt.core.designsystem.textfield.RevoltTextField
import kotlinx.coroutines.launch

@Destination(start = true, route = SIGN_IN_FEATURE_SCREEN_ROUTE)
@Composable
fun RevoltSignInScreen(
    modifier: Modifier = Modifier,
    viewModel: RevoltSignInViewModel = hiltViewModel()
) {
    val uiState = viewModel.state.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Row {
                Image(
                    modifier = Modifier.height(30.dp),
                    painter = painterResource(id = DesignSystemDrawable.ic_revolt_logo_text),
                    contentDescription = "logo text",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }
            Text(text = "\uD83D\uDC4B Welcome!")
            Text(text = "Sign into Revolt")

            val emailValue = remember { mutableStateOf("") }
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    emailValue.value = it
                    viewModel.onFieldUpdate(RevoltSignInViewModel.FieldUpdate.Email)
                },
                value = emailValue.value,
                title = "Email",
                hint = "Enter your email",
                isError = uiState.emailError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            val passwordValue = remember { mutableStateOf("") }
            var passwordVisible by rememberSaveable { mutableStateOf(false) }
            RevoltTextField(
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    passwordValue.value = it
                    viewModel.onFieldUpdate(RevoltSignInViewModel.FieldUpdate.Password)
                },
                value = passwordValue.value,
                title = "Password",
                hint = "Enter your password",
                isError = uiState.passwordError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) DesignSystemDrawable.ic_visibility_off
                    else DesignSystemDrawable.ic_visibility

                    // Please provide localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = painterResource(image), description)
                    }
                }
            )
            Button(onClick = { viewModel.signIn(emailValue.value, passwordValue.value) }) {
                Text(text = "Login")
            }

            SpannedText(text = "New to Revolt? ", spannedText = "Create a new account") {
                Log.d("acc", "click")
            }
            SpannedText(text = "Forgot your password? ", spannedText = "Reset password") {

            }
            SpannedText(text = "Didn't receive an email? ", spannedText = "Resend verification") {

            }
            uiState.message?.let { message ->
                LaunchedEffect(key1 = message) {
                    snackbarHostState.showSnackbar(message.message)
                    viewModel.clearMessage(message.id)
                }
            }
        }
    }
}