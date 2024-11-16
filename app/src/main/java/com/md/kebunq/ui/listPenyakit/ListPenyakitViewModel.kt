package com.md.kebunq.ui.listPenyakit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListPenyakitViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is List Penyakit Fragment"
    }
    val text: LiveData<String> = _text
}