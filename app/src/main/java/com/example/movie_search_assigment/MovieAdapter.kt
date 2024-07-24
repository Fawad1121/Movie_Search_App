package com.example.movie_search_assigment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(private val context: Context, private val movies: List<Movie>, private val onMovieClick: (Movie) -> Unit) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener { onMovieClick(movie) }
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val posterImageView: ImageView = itemView.findViewById(R.id.moviePosterImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.movieTitleTextView)
        private val yearTextView: TextView = itemView.findViewById(R.id.movieYearTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.movieRatingTextView)

        fun bind(movie: Movie) {
            titleTextView.text = movie.Title
            yearTextView.text = movie.Year
            ratingTextView.text = "IMDb: ${movie.imdbRating ?: "N/A"}"
            Glide.with(context).load(movie.Poster).into(posterImageView)
        }
    }
}
