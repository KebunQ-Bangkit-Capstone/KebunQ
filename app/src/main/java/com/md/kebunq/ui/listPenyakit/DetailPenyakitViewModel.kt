package com.md.kebunq.ui.listPenyakit

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.md.kebunq.data.response.DiseasesItem
import com.md.kebunq.data.response.DiseasesResponse
import com.md.kebunq.data.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPenyakitViewModel(private val apiService: ApiService) : ViewModel() {

    private val _disease = MutableLiveData<DiseasesItem?>()
    val disease: LiveData<DiseasesItem?> = _disease

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun fetchDiseaseById(plantIndex: String, diseaseId: String) {
        _isLoading.value = true
        apiService.getDiseaseById(plantIndex, diseaseId).enqueue(object : Callback<DiseasesResponse> {
            override fun onResponse(call: Call<DiseasesResponse>, response: Response<DiseasesResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val diseaseResponse = response.body()
                    if (diseaseResponse != null && diseaseResponse.diseases.isNotEmpty()) {
                        _disease.value = diseaseResponse.diseases[0] // Ambil item pertama
                        Log.d("API_SUCCESS", "Disease fetched successfully: ${diseaseResponse.diseases[0]}")
                    } else {
                        _errorMessage.value = "No disease data found."
                        _disease.value = null
                        Log.w("API_WARNING", "No diseases found in response")
                    }
                } else {
                    _errorMessage.value = "Failed to fetch disease. Error code: ${response.code()}"
                    _disease.value = null
                    Log.e("API_ERROR", "Error code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<DiseasesResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "Network error: ${t.localizedMessage}"
                _disease.value = null
                Log.e("API_FAILURE", "Failed to fetch disease", t)
            }
        })
    }
}
