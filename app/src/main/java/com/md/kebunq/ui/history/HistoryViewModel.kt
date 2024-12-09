package com.md.kebunq.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.md.kebunq.data.response.PredictionsItem
import com.md.kebunq.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class HistoryViewModel : ViewModel() {

    private val _predictionsHistory = MutableLiveData<List<PredictionsItem>>()
    val predictionsHistory: LiveData<List<PredictionsItem>> = _predictionsHistory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getPredictionsByUserId(userId: String){
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = ApiConfig.getApiService().getPredictionsByUserId(userId)
                if (response.predictions.isNotEmpty()){
                    _predictionsHistory.value = response.predictions.sortedByDescending { it.createdAt }
                }else{
                    _error.value = "Hasil Prediksi Anda Masih Kosong"
                }
            }catch (e: Exception){
                when(e){
                    is IOException -> _error.value = "Internet Anda Bermasalah!"
                    is HttpException -> {
                        val errorBody = e.response()?.errorBody()?.string()
                        _error.value = errorBody ?: "Sedang ada kesalahan pada server"
                    } else -> _error.value = "Sedang ada kesalahan: ${e.message}"
                }
            }finally {
                _isLoading.value = false
            }
        }
    }
}