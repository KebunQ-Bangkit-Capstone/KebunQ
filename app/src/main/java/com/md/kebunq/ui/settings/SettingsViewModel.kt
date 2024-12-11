package com.md.kebunq.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.md.kebunq.DataStoreManager
import kotlinx.coroutines.launch

class SettingsViewModel(
    application: Application,
    private val dataStoreManager: DataStoreManager
) : AndroidViewModel(application) {

    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    init {
        viewModelScope.launch {
            dataStoreManager.isDarkMode.collect { isDarkMode ->
                _isDarkMode.value = isDarkMode
            }
        }
    }

    fun saveDarkModeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            dataStoreManager.saveDarkModeSetting(isDarkMode)
        }
    }
}