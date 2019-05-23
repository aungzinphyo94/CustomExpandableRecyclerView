package com.azp.customexpandablerecyclermmit.data;

import java.util.List;

public class MovieCategory implements ParentListItem{

    private String mName;
    private List<Movie> mMovies;

    public MovieCategory(String mName, List<Movie> mMovies) {
        this.mName = mName;
        this.mMovies = mMovies;
    }

    public String getmName() {
        return mName;
    }

    @Override
    public List<?> getChildItemList() {
        return mMovies;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
