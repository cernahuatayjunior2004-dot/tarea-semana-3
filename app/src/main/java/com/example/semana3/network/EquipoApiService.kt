package com.example.semana3.network

import com.example.semana3.data.Equipo
import retrofit2.http.*

interface EquipoApiService {
    @GET("equipo")
    suspend fun getEquipos(): List<Equipo>

    @POST("equipo")
    suspend fun createEquipo(@Body equipo: Equipo): Equipo

    @PUT("equipo/{id}")
    suspend fun updateEquipo(@Path("id") id: String, @Body equipo: Equipo): Equipo

    @DELETE("equipo/{id}")
    suspend fun deleteEquipo(@Path("id") id: String)
}
