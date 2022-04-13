package com.firstapp.maps

import retrofit2.Response
import retrofit2.http.GET

interface MapsApi {
    @GET( value = "/osm.json")
    suspend fun getTodos(): Response<ArrayList<MapDataformat>>

}
