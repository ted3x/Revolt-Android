package ge.ted3x.revolt.core.designsystem.textfield

import androidx.compose.material.Text
import androidx.compose.material.TextField
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
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            if (hint != null) {
                Text(text = hint)
            }
        },
        label = {
            if (title != null) {
                Text(text = title)
            }
        },
        singleLine = singleLine,
        modifier = modifier
    )
}