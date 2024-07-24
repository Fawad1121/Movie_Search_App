package com.example.movie_search_assigment

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var posterImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var productionTextView: TextView
    private lateinit var ratingsTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var actorsTextView: TextView
    private lateinit var plotTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        // Initialize the back button (Button)
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Initialize other views
        posterImageView = findViewById(R.id.moviePosterImageView)
        titleTextView = findViewById(R.id.movieTitleTextView)
        yearTextView = findViewById(R.id.movieYearTextView)
        productionTextView = findViewById(R.id.movieProductionTextView)
        ratingsTextView = findViewById(R.id.movieRatingsTextView)
        genreTextView = findViewById(R.id.movieGenreTextView)
        actorsTextView = findViewById(R.id.movieActorsTextView)
        plotTextView = findViewById(R.id.moviePlotTextView)

        val movieId = intent.getStringExtra("movieId")
        if (movieId != null) {
            getMovieDetails(movieId)
        }
    }

    private fun getMovieDetails(movieId: String) {
        val apiClient = ApiClient.getClient().create(OMDBApi::class.java)
        val call = apiClient.getMovieDetails(movieId, "full", "6ed859e2")

        call.enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.isSuccessful) {
                    val movie = response.body()
                    movie?.let {
                        titleTextView.text = it.Title
                        yearTextView.text = it.Year
                        productionTextView.text = it.Production ?: "N/A"
                        ratingsTextView.text = it.Ratings.joinToString("\n") { rating ->
                            val source = when (rating.Source) {
                                "Internet Movie Database" -> "IMDB"
                                "Rotten Tomatoes" -> "RT"
                                "Metacritic" -> "MC"
                                else -> rating.Source
                            }
                            "$source: ${rating.Value}"
                        }
                        genreTextView.text = it.Genre
                        actorsTextView.text = it.Actors
                        plotTextView.text = it.Plot
                        Glide.with(this@MovieDetailsActivity).load(it.Poster).into(posterImageView)
                    }
                }
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                // Handle failure
            }
        })
    }
}
