package com.example.movie_search_assigment

data class Movie(
    val Title: String,
    val Year: String,
    val imdbID: String,
    val Type: String,
    val Poster: String,
    var imdbRating: String? = null // Add this field
)

data class MovieResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String
)
