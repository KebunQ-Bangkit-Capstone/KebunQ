package com.md.kebunq.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.md.kebunq.data.response.DetailPredictionResponse
import com.md.kebunq.data.retrofit.ApiService
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class DetailAnalisisViewModel (private val apiService: ApiService) : ViewModel() {

    private val _detailPrediction = MutableLiveData<DetailPredictionResponse>()
    val detailPrediction: LiveData<DetailPredictionResponse> = _detailPrediction

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun getDetailPrediction(id: String){
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val response = apiService.getDetailPrediction(id).execute()
                if (response.isSuccessful){
                    response.body()?.let {
                        _detailPrediction.postValue(it)
                    }?: run {
                        _error.postValue("Tidak Ada Hasil")
                    }
                }else{
                    _error.postValue("Error: ${response.code()} - ${response.message()}")
                }
            }catch (e: HttpException){
                _error.postValue("HttpException: ${e.message}")
            }catch (e: IOException){
                _error.postValue("IOException: ${e.message}")
            }finally {
                _isLoading.postValue(false)
            }
        }
    }
}