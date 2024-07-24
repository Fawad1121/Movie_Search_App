package com.example.movie_search_assigment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var moviesRecyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        moviesRecyclerView = findViewById(R.id.moviesRecyclerView)

        moviesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the adapter with an empty list and set it to the RecyclerView
        movieAdapter = MovieAdapter(this, emptyList()) { movie ->
            val intent = Intent(this, MovieDetailsActivity::class.java)
            intent.putExtra("movieId", movie.imdbID)
            startActivity(intent)
        }
        moviesRecyclerView.adapter = movieAdapter

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            hideKeyboard()
            searchMovies(query)
        }
    }

    private fun searchMovies(query: String) {
        val apiClient = ApiClient.getClient().create(OMDBApi::class.java)
        val call = apiClient.searchMovies(query, "6ed859e2")

        call.enqueue(object : Callback<MovieResponse> {
            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.isSuccessful && response.body()?.Response == "True") {
                    val movies = response.body()?.Search ?: emptyList()
                    movies.forEach { movie ->
                        fetchIMDbRating(movie)
                    }
                    movieAdapter = MovieAdapter(this@MainActivity, movies) { movie ->
                        val intent = Intent(this@MainActivity, MovieDetailsActivity::class.java)
                        intent.putExtra("movieId", movie.imdbID)
                        startActivity(intent)
                    }
                    moviesRecyclerView.adapter = movieAdapter
                }
            }

            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun fetchIMDbRating(movie: Movie) {
        val apiClient = ApiClient.getClient().create(OMDBApi::class.java)
        val call = apiClient.getMovieDetails(movie.imdbID, "full", "6ed859e2")

        call.enqueue(object : Callback<MovieDetails> {
            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if (response.isSuccessful) {
                    movie.imdbRating = response.body()?.imdbRating
                    movieAdapter.notifyDataSetChanged() // Update the adapter
                }
            }

            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                // Handle failure
            }
        })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }
}
