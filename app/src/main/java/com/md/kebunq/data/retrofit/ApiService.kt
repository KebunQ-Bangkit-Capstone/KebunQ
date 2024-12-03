package com.md.kebunq.data.retrofit

import com.md.kebunq.data.response.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @Multipart
    @POST("predictions/predict")
    fun predict(
        @Query("user_id") userId: String,
        @Query("plant_index") plantIndex: String,
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>
}