package com.mdm.final_assingment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MovieListActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerViewMovies: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private val movieList = mutableListOf<Movie>()
    private val TAG = "MovieListActivity"
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        recyclerViewMovies = findViewById(R.id.recyclerViewMovies)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        recyclerViewMovies.layoutManager = LinearLayoutManager(this)

        movieAdapter = MovieAdapter(this, movieList)
        recyclerViewMovies.adapter = movieAdapter

        swipeRefreshLayout.setOnRefreshListener {
            fetchMovies()
        }

        fetchMovies()

        val buttonAddMovie: Button = findViewById(R.id.buttonAddMovie)
        buttonAddMovie.setOnClickListener {
            val intent = Intent(this, MovieUploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchMovies() {
        val userId = auth.currentUser?.uid

        if (userId != null) {
            firestore.collection("movies")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { result ->
                    movieList.clear() // Clear the list before adding new data
                    for (document in result) {
                        val movie = document.toObject(Movie::class.java)
                        movieList.add(movie)
                    }
                    movieAdapter.notifyDataSetChanged()
                    swipeRefreshLayout.isRefreshing = false // Stop the refresh animation
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting movies: ", exception)
                    swipeRefreshLayout.isRefreshing = false // Stop the refresh animation in case of error
                }
        } else {
            Log.w(TAG, "User not logged in")
            swipeRefreshLayout.isRefreshing = false // Stop the refresh animation if user is not logged in
        }
    }
}















