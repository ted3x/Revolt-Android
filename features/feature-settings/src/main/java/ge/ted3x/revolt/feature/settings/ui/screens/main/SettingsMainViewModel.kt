package ge.ted3x.revolt.feature.settings.ui.screens.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ge.ted3x.core.database.RevoltUserQueries
import javax.inject.Inject

@HiltViewModel
class SettingsMainViewModel @Inject constructor(val userQueries: RevoltUserQueries) : ViewModel() {
}