package com.azp.customexpandablerecyclermmit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.azp.customexpandablerecyclermmit.adapter.ExpandableRecyclerAdapter;
import com.azp.customexpandablerecyclermmit.adapter.MovieCategoryAdapter;
import com.azp.customexpandablerecyclermmit.data.Movie;
import com.azp.customexpandablerecyclermmit.data.MovieCategory;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MovieCategoryAdapter mAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Movie  movie_one = new Movie("The Shawshank Redemption");
        Movie movie_two  = new Movie("The Godfather");
        Movie  movie_three = new Movie("The Dark Knight");
        Movie movie_four  = new Movie("Schindler's List ");
        Movie movie_five = new Movie("12 Angry Men ");
        Movie movie_six = new Movie("Pulp Fiction");
        Movie movie_seven = new Movie("The Lord of the Rings: The Return of the King");
        Movie movie_eight = new Movie("The Good, the Bad and the Ugly");
        Movie movie_nine = new Movie("Fight Club");
        Movie movie_ten = new Movie("Star Wars: Episode V - The Empire Strikes");
        Movie movie_eleven = new Movie("Forrest Gump");
        Movie movie_tweleve = new Movie("Inception");

        MovieCategory molvie_category_one = new MovieCategory("Drama", Arrays.asList(movie_one, movie_two, movie_three, movie_four));
        MovieCategory molvie_category_two = new MovieCategory("Action", Arrays.asList(movie_five, movie_six, movie_seven,movie_eight));
        MovieCategory molvie_category_three = new MovieCategory("History", Arrays.asList(movie_nine, movie_ten, movie_eleven,movie_tweleve));
        MovieCategory molvie_category_four = new MovieCategory("Thriller", Arrays.asList(movie_one, movie_five, movie_nine,movie_tweleve));

        final List<MovieCategory> movieCategories = Arrays.asList(molvie_category_one,  molvie_category_two, molvie_category_three,molvie_category_four);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mAdapter = new MovieCategoryAdapter(this, movieCategories);
        mAdapter.setExpandCollapseListener(new ExpandableRecyclerAdapter.ExpandCollapseListener() {
            @Override
            public void onListItemExpanded(int position) {
                MovieCategory expandedMovieCategory = movieCategories.get(position);

                String toastMsg = getResources().getString(R.string.expanded, expandedMovieCategory.getmName());
                Toast.makeText(MainActivity.this,
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onListItemCollapsed(int position) {
                MovieCategory collapsedMovieCategory = movieCategories.get(position);

                String toastMsg = getResources().getString(R.string.collapsed, collapsedMovieCategory.getmName());
                Toast.makeText(MainActivity.this,
                        toastMsg,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mAdapter.onRestoreInstanceState(savedInstanceState);
    }

}
