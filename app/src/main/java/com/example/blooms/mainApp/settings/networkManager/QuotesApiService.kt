package com.example.blooms.mainApp.settings.networkManager

import Quote
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface QuotesApiService {
    @GET("quote")
    suspend fun getQuote(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String,
        @Query("token") token: String
    ): Quote
}
