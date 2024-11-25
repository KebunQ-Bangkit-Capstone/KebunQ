package com.md.kebunq.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.md.kebunq.data.retrofit.ApiService
import com.md.kebunq.ui.listPenyakit.ListPenyakitViewModel

@Suppress("UNCHECKED_CAST")
class PenyakitViewModelFactory(private val apiService: ApiService) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListPenyakitViewModel(apiService) as T
    }
}
