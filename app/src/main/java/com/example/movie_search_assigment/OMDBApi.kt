package com.example.movie_search_assigment

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OMDBApi {
    @GET("/")
    fun searchMovies(@Query("s") query: String, @Query("apikey") apiKey: String): Call<MovieResponse>

    @GET("/")
    fun getMovieDetails(@Query("i") imdbID: String, @Query("plot") plot: String = "short", @Query("apikey") apiKey: String): Call<MovieDetails>
}
