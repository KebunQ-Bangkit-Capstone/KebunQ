package com.md.kebunq.data.retrofit

import com.md.kebunq.data.response.DiseasesResponse
import com.md.kebunq.data.User
import com.md.kebunq.data.response.CreateUserResponse
import retrofit2.http.Body
import com.md.kebunq.data.response.DetailPredictionResponse
import com.md.kebunq.data.response.GetOneUserResponse
import com.md.kebunq.data.response.ListPredictionResponse
import com.md.kebunq.data.response.PredictionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @GET("diseases/")
    fun getDiseasesByPlant(
        @Query("plant_index") plantId: String
    ): Call<DiseasesResponse>
    @POST("users")
    suspend fun createUser(@Body user: User): CreateUserResponse
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): GetOneUserResponse

    @GET("diseases/{id_plant}_{id_disease}")
    fun getDiseaseById(
        @Path("id_plant") plantId: String,
        @Path("id_disease") diseaseId: String
    ): Call<DiseasesResponse>
    @GET("predictions")
    suspend fun getPredictionsByUserId(@Query("user_id") userId: String): ListPredictionResponse

    @GET("diseases/all")
    fun getAllDiseases(): Call<List<DiseasesResponse>>
    @GET("predictions/{id}")
    suspend fun getDetailPrediction(@Path("id") id: String): DetailPredictionResponse

    @Multipart
    @POST("predictions/predict")
    fun predict(
        @Query("user_id") userId: String,
        @Query("plant_index") plantIndex: String,
        @Part image: MultipartBody.Part
    ): Call<PredictionResponse>
}