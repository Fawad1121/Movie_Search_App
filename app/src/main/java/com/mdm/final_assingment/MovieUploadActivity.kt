package com.mdm.final_assingment

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MovieUploadActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val TAG = "MovieUploadActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_upload)

        // Enable ActionBar back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val editTextMovieTitle: EditText = findViewById(R.id.editTextMovieTitle)
        val editTextStudio: EditText = findViewById(R.id.editTextStudio)
        val editTextPosterLink: EditText = findViewById(R.id.editTextPosterLink)
        val editTextCriticsRating: EditText = findViewById(R.id.editTextCriticsRating)
        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)
        val buttonBack: Button = findViewById(R.id.buttonBack)

        buttonSubmit.setOnClickListener {
            val movieTitle = editTextMovieTitle.text.toString()
            val studio = editTextStudio.text.toString()
            val posterLink = editTextPosterLink.text.toString()
            val criticsRatingStr = editTextCriticsRating.text.toString()
            val userId = auth.currentUser?.uid

            Log.d(TAG, "Movie Title: $movieTitle")
            Log.d(TAG, "Studio: $studio")
            Log.d(TAG, "Poster Link: $posterLink")
            Log.d(TAG, "Critics Rating: $criticsRatingStr")
            Log.d(TAG, "User ID: $userId")

            // Validate input
            if (movieTitle.isEmpty() || studio.isEmpty() || posterLink.isEmpty() || criticsRatingStr.isEmpty() || userId.isNullOrEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Validation failed: Some fields are empty or user not logged in")
                return@setOnClickListener
            }

            // Validate critics rating
            val criticsRating = criticsRatingStr.toFloatOrNull()
            if (criticsRating == null || criticsRating < 0.0 || criticsRating > 10.0) {
                Toast.makeText(this, "Critics Rating must be a number between 0 and 10", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "Validation failed: Invalid critics rating")
                return@setOnClickListener
            }

            // Generate a new document ID
            val newMovieRef = firestore.collection("movies").document()
            val movieId = newMovieRef.id

            // Create a new movie record
            val movie = Movie(
                id = movieId,
                title = movieTitle,
                studio = studio,
                posterLink = posterLink,
                criticsRating = criticsRatingStr,
                userId = userId
            )

            // Add a new document with the generated ID
            newMovieRef.set(movie)
                .addOnSuccessListener {
                    Log.d(TAG, "Movie added with ID: $movieId")
                    showSuccessDialog() // Show the success dialog
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding movie", e)
                    Toast.makeText(this, "Error adding movie: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }

        buttonBack.setOnClickListener {
            finish() // Navigate back to the previous screen (MovieListActivity)
        }
    }

    // Handle the ActionBar back button
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Movie added successfully")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                clearInputFields() // Clear input fields after dialog is dismissed
            }
        val alert = builder.create()
        alert.show()
    }

    private fun clearInputFields() {
        findViewById<EditText>(R.id.editTextMovieTitle).text.clear()
        findViewById<EditText>(R.id.editTextStudio).text.clear()
        findViewById<EditText>(R.id.editTextPosterLink).text.clear()
        findViewById<EditText>(R.id.editTextCriticsRating).text.clear()
    }
}









