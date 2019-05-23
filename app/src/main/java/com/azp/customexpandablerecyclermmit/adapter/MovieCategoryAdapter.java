package com.azp.customexpandablerecyclermmit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.azp.customexpandablerecyclermmit.R;
import com.azp.customexpandablerecyclermmit.data.Movie;
import com.azp.customexpandablerecyclermmit.data.MovieCategory;
import com.azp.customexpandablerecyclermmit.data.ParentListItem;
import com.azp.customexpandablerecyclermmit.viewholder.MovieCategoryViewHolder;
import com.azp.customexpandablerecyclermmit.viewholder.MoviesViewHolder;

import java.util.List;

public class MovieCategoryAdapter extends ExpandableRecyclerAdapter<MovieCategoryViewHolder, MoviesViewHolder> {

    private LayoutInflater mInflator;

    public MovieCategoryAdapter(Context context,List<? extends ParentListItem> mParentItemList) {
        super(mParentItemList);
        mInflator = LayoutInflater.from(context);
    }

    @Override
    public MovieCategoryViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View movieCategoryView = mInflator.inflate(R.layout.movie_category_view, parentViewGroup, false);
        return new MovieCategoryViewHolder(movieCategoryView);
    }

    @Override
    public MoviesViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View moviesView = mInflator.inflate(R.layout.movies_view, childViewGroup, false);
        return new MoviesViewHolder(moviesView);
    }

    @Override
    public void onBindParentViewHolder(MovieCategoryViewHolder parentViewHolder, int position, ParentListItem parentListItem) {
        MovieCategory movieCategory = (MovieCategory) parentListItem;
        parentViewHolder.bind(movieCategory);

    }

    @Override
    public void onBindChildViewHolder(MoviesViewHolder childViewHolder, int position, Object childListItem) {
        Movie movie = (Movie) childListItem;
        childViewHolder.bind(movie);

    }
}
