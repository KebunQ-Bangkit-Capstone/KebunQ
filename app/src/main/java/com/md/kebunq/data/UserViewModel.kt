package com.md.kebunq.data
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.md.kebunq.DataStoreManager
import com.md.kebunq.data.response.CreateUserResponse
import com.md.kebunq.data.response.GetOneUserResponse

import kotlinx.coroutines.launch
import kotlin.Result

class UserViewModel(
    private val userRepository: UserRepository,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _user = MutableLiveData<Result<CreateUserResponse>>()
    val user: LiveData<Result<CreateUserResponse>> get() =  _user

    private val _detailUser = MutableLiveData<Result<GetOneUserResponse>>()
    val detailUser: LiveData<Result<GetOneUserResponse>> get() = _detailUser

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun createUser(user: User) {
        _error.value = null
        viewModelScope.launch {
            try {
                val response = userRepository.createUser(user)
                _user.value = Result.success(response)
            } catch (e: Exception) {
                _error.value = e.message
                _user.value = Result.failure(e)
            }
        }
    }

    fun getUserById(id: String) {
        _error.value = null
        viewModelScope.launch {
            try {
                val response = userRepository.getDetailUser(id)
                _detailUser.value = Result.success(response) // Assign the GetOneUserResponse
            } catch (e: Exception) {
                _error.value = e.message
                _detailUser.value = Result.failure(e)
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            try {
                // Logout dari Firebase Auth
                Firebase.auth.signOut()
                Log.d("AuthState", "User signed out")

                // Bersihkan DataStore
                dataStoreManager.clearDataStore()

                // Bersihkan LiveData
                clearLocalUserData()

                _detailUser.value = Result.failure(Exception("User logged out"))
                _user.value = Result.failure(Exception("User logged out"))
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    private fun clearLocalUserData() {
        _detailUser.value = null
        _user.value = null
        _error.value = null
    }
}