package com.md.kebunq.ui.listPenyakit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.md.kebunq.data.retrofit.ApiService

@Suppress("UNCHECKED_CAST")
class PenyakitViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailPenyakitViewModel::class.java)) {
            return DetailPenyakitViewModel(apiService) as T
        }
        if (modelClass.isAssignableFrom(ListPenyakitViewModel::class.java)) {
            return ListPenyakitViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
