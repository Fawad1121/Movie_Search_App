package com.mdm.final_assingment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var movieId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        firestore = FirebaseFirestore.getInstance()

        val imageViewPosterDetail: ImageView = findViewById(R.id.imageViewPosterDetail)
        val textViewTitleDetail: TextView = findViewById(R.id.textViewTitleDetail)
        val textViewStudioDetail: TextView = findViewById(R.id.textViewStudioDetail)
        val textViewRatingDetail: TextView = findViewById(R.id.textViewRatingDetail)
        val buttonDeleteMovie: Button = findViewById(R.id.buttonDeleteMovie)
        val buttonAddMoreMovies: Button = findViewById(R.id.buttonAddMoreMovies)

        val title = intent.getStringExtra("title")
        val studio = intent.getStringExtra("studio")
        val posterLink = intent.getStringExtra("posterLink")
        val criticsRating = intent.getStringExtra("criticsRating")
        movieId = intent.getStringExtra("movieId") ?: ""

        textViewTitleDetail.text = title
        textViewStudioDetail.text = studio
        textViewRatingDetail.text = criticsRating
        Glide.with(this).load(posterLink).into(imageViewPosterDetail)

        buttonDeleteMovie.setOnClickListener {
            deleteMovie()
        }

        buttonAddMoreMovies.setOnClickListener {
            val intent = Intent(this, MovieUploadActivity::class.java)
            startActivity(intent)
        }
    }

    private fun deleteMovie() {
        if (movieId.isNotEmpty()) {
            firestore.collection("movies").document(movieId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Movie deleted successfully", Toast.LENGTH_SHORT).show()
                    finish() // Go back to the previous screen (MovieListActivity)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error deleting movie: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Movie ID is not valid", Toast.LENGTH_SHORT).show()
        }
    }
}

