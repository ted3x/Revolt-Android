package ge.ted3x.revolt.core.designsystem.appbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RevoltAppBar(
    onBackClick: () -> Unit,
    isBackVisible: Boolean,
    title: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(Color.DarkGray)
            .height(56.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isBackVisible) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Filled.ArrowBack, "back", tint = Color.White)
            }
        } else {
            Spacer(modifier = Modifier.width(48.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, color = Color.White)
    }
}