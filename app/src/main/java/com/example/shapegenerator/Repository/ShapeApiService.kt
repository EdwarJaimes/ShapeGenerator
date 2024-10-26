package com.example.shapegenerator.Repository

import com.example.shapegenerator.Model.Shape
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ShapeApiService {
    @GET("polygons")
    suspend fun getShapes(): List<Shape>
}

object RetrofitInstance {
    private const val BASE_URL = "https://gca.traces.com.co/pruebamovil/api/"

    val api: ShapeApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShapeApiService::class.java)
    }
}
