package com.md.kebunq.data.retrofit

import com.md.kebunq.data.response.DetailPredictionResponse
import com.md.kebunq.data.response.ListPredictionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("predictions")
    suspend fun getPredictionsByUserId(@Query("user_id") userId: String): ListPredictionResponse

    @GET("predictions/{id}")
    suspend fun getDetailPrediction(@Path("id") id: String): DetailPredictionResponse
}