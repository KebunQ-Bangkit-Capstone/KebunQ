package com.md.kebunq.data.retrofit

import com.md.kebunq.data.response.DiseasesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("diseases/")
    fun getDiseasesByPlant(
        @Query("plant_index") plantId: String
    ): Call<DiseasesResponse>

    @GET("diseases/{id}")
    fun getDiseaseById(
        @Path("id") diseaseId: String
    ): Call<DiseasesResponse>

    @GET("diseases/all")
    fun getAllDiseases(): Call<List<DiseasesResponse>>
}
