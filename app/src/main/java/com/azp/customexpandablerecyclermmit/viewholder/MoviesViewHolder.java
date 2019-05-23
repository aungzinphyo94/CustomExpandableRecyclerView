package com.azp.customexpandablerecyclermmit.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.azp.customexpandablerecyclermmit.R;
import com.azp.customexpandablerecyclermmit.data.Movie;

public class MoviesViewHolder extends ChildViewHolder{

    private TextView mMoviesTextView;

    public MoviesViewHolder(@NonNull View itemView) {
        super(itemView);
        mMoviesTextView  = itemView.findViewById(R.id.tv_movies);
    }

    public void bind(Movie movies) {
        mMoviesTextView.setText(movies.getmName());
    }
}
