package com.azp.customexpandablerecyclermmit.viewholder;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.azp.customexpandablerecyclermmit.R;
import com.azp.customexpandablerecyclermmit.data.MovieCategory;

public class MovieCategoryViewHolder extends ParentViewHolder {

    private static final float INITIAL_POSITION = 0.0f;
    private static final float ROTATION_POSITION = 180f;

    private final ImageView mArrowExpanded;
    private TextView mMovieTextView;

    public MovieCategoryViewHolder(@NonNull View itemView) {
        super(itemView);
        mMovieTextView = itemView.findViewById(R.id.tv_movie_category);
        mArrowExpanded = itemView.findViewById(R.id.iv_arrow_expand);
    }

    public void bind(MovieCategory movieCategory) {

        mMovieTextView.setText(movieCategory.getmName());
    }

    @Override
    public void setmExpanded(boolean expanded) {
        super.setmExpanded(expanded);

        if (expanded) {
            mArrowExpanded.setRotation(ROTATION_POSITION);
        } else {
            mArrowExpanded.setRotation(INITIAL_POSITION);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        super.onExpansionToggled(expanded);

        RotateAnimation rotateAnimation;
        if (expanded) {
            rotateAnimation = new RotateAnimation(ROTATION_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        } else {
            rotateAnimation = new RotateAnimation(-1*ROTATION_POSITION,
                    INITIAL_POSITION,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                    RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnimation.setDuration(200);
        rotateAnimation.setFillAfter(true);
        mArrowExpanded.startAnimation(rotateAnimation);
    }
}
