package com.md.kebunq.ui.settings

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    val isDarkMode: LiveData<Boolean> = application.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }.asLiveData()

    fun saveDarkModeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            getApplication<Application>().dataStore.edit { settings ->
                settings[DARK_MODE_KEY] = isDarkMode
            }
        }
    }
}