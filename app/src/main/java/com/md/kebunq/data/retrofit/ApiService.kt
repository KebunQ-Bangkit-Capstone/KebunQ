package com.md.kebunq.data.retrofit

import com.md.kebunq.data.User
import com.md.kebunq.data.response.CreateUserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("users")
    suspend fun createUser(@Body user: User): CreateUserResponse

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

}