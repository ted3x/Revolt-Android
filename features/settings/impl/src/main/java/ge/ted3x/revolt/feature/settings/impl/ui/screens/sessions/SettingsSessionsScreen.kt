package ge.ted3x.revolt.feature.settings.impl.ui.screens.sessions

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import ge.ted3x.revolt.feature.settings.impl.R

@Composable
@Destination
fun SettingsSessionsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsSessionsViewModel = hiltViewModel()
) {
    val uiState = viewModel.state.collectAsState().value
    LazyColumn(modifier = modifier
        .padding(8.dp)
        .fillMaxSize(), content = {
        item {
            Text(text = "Active Sessions")
            Spacer(modifier = Modifier.height(8.dp))
            uiState.currentSession?.let {
                CurrentSession(
                    name = it.name,
                    created = it.createdAt
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        items(uiState.sessions) {
            Session(
                id = it.id,
                name = it.name,
                created = it.createdAt,
                onLogOut = { id ->
                    viewModel.revokeSession(id)
                })
        }
        item {
            Button(onClick = { viewModel.revokeSessions() }) {
                Text(text = "Log out of all other sessions")
            }
        }
    })
}

@Composable
private fun CurrentSession(
    name: String,
    created: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Cyan)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "This Device")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = R.drawable.ic_unknown_session),
                    contentDescription = "session image"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = name)
                    Text(text = "Created $created")
                }
            }
        }
    }
}

@Composable
private fun Session(
    id: String,
    name: String,
    created: String,
    onLogOut: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    modifier = Modifier.size(36.dp),
                    painter = painterResource(id = R.drawable.ic_unknown_session),
                    contentDescription = "session image"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = name)
                    Text(text = "Created $created")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onLogOut.invoke(id)
                }) {
                Text(text = "Log Out")
            }
        }
    }
}