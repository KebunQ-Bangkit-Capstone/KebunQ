package com.md.kebunq.data

import com.md.kebunq.data.response.CreateUserResponse
import com.md.kebunq.data.retrofit.ApiService


class UserRepository(private val apiService: ApiService) {

    suspend fun createUser(user: User): CreateUserResponse {
        return apiService.createUser(user)
    }
}
