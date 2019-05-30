package com.iapptm.coreapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.data.api.model.MovieModel
import com.iapptm.coreapp.R
import kotlinx.android.synthetic.main.row_movie.view.*
import java.math.RoundingMode

class MoviesAdapters(private val movieClickedListener: (MovieModel) -> Unit) :
    RecyclerView.Adapter<MoviesAdapters.Holder>() {
    private var movies: MutableList<MovieModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_movie, parent, false),
            movieClickedListener
        )
    }

    fun submitList(movieList: List<MovieModel>) {
        movies.addAll(movieList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val movie: MovieModel = movies[position]
        holder.movieModel = movie
        holder.tvTitle.text = movie.title
        holder.tvDescription.text = movie.storyline
        holder.tvRate.text = movie.run { ratings.average().toBigDecimal().setScale(1,RoundingMode.HALF_EVEN).toString() }

        Glide.with(holder.img)
            .load(movie.posterurl)
            .into(holder.img)
    }


    class Holder(itemView: View, private val movieClickedListener: (MovieModel) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        lateinit var movieModel: MovieModel

        val tvTitle: TextView = itemView.tv_title
        val tvDescription: TextView = itemView.tv_description
        val tvRate: TextView = itemView.tv_rate
        val img: ImageView = itemView.iv_image

        init {
            itemView.setOnClickListener {
                movieClickedListener(movieModel)
            }
        }

    }

}