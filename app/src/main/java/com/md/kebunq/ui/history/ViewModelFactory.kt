package com.md.kebunq.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.md.kebunq.data.retrofit.ApiService

class ViewModelFactory(
    private val apiService: ApiService
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailAnalisisViewModel::class.java)) {
            return DetailAnalisisViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(apiService: ApiService): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(apiService).also { instance = it }
            }
    }
}
