package com.md.kebunq.ui.listPenyakit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailPenyakitViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Detail Penyakit Fragment"
    }
    val text: LiveData<String> = _text
}