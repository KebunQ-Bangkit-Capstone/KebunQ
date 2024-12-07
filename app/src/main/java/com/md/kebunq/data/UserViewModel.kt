package com.md.kebunq.data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.md.kebunq.data.response.CreateUserResponse

import kotlinx.coroutines.launch
import kotlin.Result

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _user = MutableLiveData<Result<CreateUserResponse>>()
    val user: LiveData<Result<CreateUserResponse>> get() =  _user

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

    fun signOut() {
        Firebase.auth.signOut()
    }
}