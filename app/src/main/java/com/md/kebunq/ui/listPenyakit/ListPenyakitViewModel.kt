package com.md.kebunq.ui.listPenyakit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.md.kebunq.data.response.DiseasesItem
import com.md.kebunq.data.response.DiseasesResponse
import com.md.kebunq.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPenyakitViewModel(private val apiService: ApiService) : ViewModel() {

    private val _diseases = MutableLiveData<List<DiseasesItem>>()
    val diseases: LiveData<List<DiseasesItem>> = _diseases

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    // Fungsi untuk mengambil data penyakit berdasarkan tanaman
    fun fetchDiseasesByPlant(plantIndex: String) {
        _isLoading.value = true
        apiService.getDiseasesByPlant(plantIndex).enqueue(object : Callback<DiseasesResponse> {
            override fun onResponse(call: Call<DiseasesResponse>, response: Response<DiseasesResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _diseases.value = response.body()?.diseases ?: emptyList()
                } else {
                    _errorMessage.value = "Error fetching diseases: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DiseasesResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Error fetching data: ${t.message}"
            }
        })
    }
}
