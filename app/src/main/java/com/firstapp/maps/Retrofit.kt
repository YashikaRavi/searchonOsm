package com.firstapp.maps

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit {

    val API: MapsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://trial-22e0c-default-rtdb.firebaseio.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MapsApi::class.java)
    }
}