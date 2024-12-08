package com.md.kebunq.ui.home

import android.util.Log
import androidx.datastore.core.IOException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.md.kebunq.data.response.PredictionsItem
import com.md.kebunq.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel : ViewModel() {

    private val _predictionsHistory = MutableLiveData<List<PredictionsItem>>()
    val predictionsHistory: LiveData<List<PredictionsItem>> = _predictionsHistory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _latestPredictions = MutableLiveData<List<PredictionsItem>>()
    val latestPredictions: LiveData<List<PredictionsItem>> = _latestPredictions

    fun getLatestPredictionsByUserId(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().getPredictionsByUserId(userId)
                if (response.predictions.isNotEmpty()) {
                    // Sort predictions by creation date in descending order and take the latest 3
                    val latest = response.predictions.sortedByDescending { it.createdAt }.take(3)
                    Log.d("HomeViewModel", "Latest predictions: $latest") // Log for debugging
                    _latestPredictions.value = latest
                } else {
                    _error.value = "Riwayat Anda masih kosong."
                }
            } // Tambahkan log di catch block
            catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}", e)
                when (e) {
                    is IOException -> _error.value = "Internet Anda bermasalah!"
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        Log.e("HomeViewModel", "HTTP Error: $errorBody")
                        _error.value = errorBody ?: "Sedang ada kesalahan pada server"
                    }
                    else -> _error.value = "Sedang ada kesalahan: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

}