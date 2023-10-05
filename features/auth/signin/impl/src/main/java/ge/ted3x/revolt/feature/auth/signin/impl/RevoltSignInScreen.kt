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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.core.designsystem.DesignSystemDrawable
import ge.ted3x.revolt.core.designsystem.text.SpannedText
import ge.ted3x.revolt.core.designsystem.textfield.RevoltTextField

@Destination(start = true)
@Composable
fun RevoltSignInScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
    ) {
        Row {
            Image(
                modifier = Modifier.height(30.dp),
                painter = painterResource(id = DesignSystemDrawable.ic_revolt_logo_text),
                contentDescription = "logo text"
            )
        }
        Text(text = "\uD83D\uDC4B Welcome!")
        Text(text = "Sign into Revolt")

        val emailValue = remember { mutableStateOf("") }
        RevoltTextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { emailValue.value = it },
            value = emailValue.value,
            title = "Email",
            hint = "Enter your email"
        )
        val passwordValue = remember { mutableStateOf("") }
        RevoltTextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = { passwordValue.value = it },
            value = passwordValue.value,
            title = "Password",
            hint = "Enter your password"
        )
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Login")
        }

        SpannedText(text = "New to Revolt? ", spannedText = "Create a new account") {
            Log.d("acc", "click")
        }
        SpannedText(text = "Forgot your password? ", spannedText = "Reset password") {

        }
        SpannedText(text = "Didn't receive an email? ", spannedText = "Resend verification") {

        }
    }
}