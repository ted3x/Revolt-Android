package ge.ted3x.revolt.core.designsystem.textfield

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RevoltTextField(
    onValueChange: (String) -> Unit,
    value: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    hint: String? = null,
    singleLine: Boolean = true,
) {
    title?.let { Text(text = it) }
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            if (hint != null) {
                Text(text = hint)
            }
        },
        singleLine = singleLine,
        modifier = modifier
    )
}