package ge.ted3x.revolt.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination

@Composable
@Destination(route = "profile", start = true)
fun BottomSheet(id: String?) {
    Text(id ?: " empty")
}