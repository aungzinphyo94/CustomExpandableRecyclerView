package com.azp.customexpandablerecyclermmit.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ParentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ParentListItemExpandColapseListener mParentListItemExpandColapseListener;
    private boolean mExpanded;

    public ParentViewHolder(@NonNull View itemView) {
        super(itemView);
        mExpanded = false;
    }

    @Override
    public void onClick(View view) {

        if (mExpanded) {
            collapseView();
        }else {
            expandView();
        }

    }

    public boolean shouldItemViewClickToggleExpansion() {
        return true;
    }

    public interface ParentListItemExpandColapseListener {

        void onParentListItemExpanded(int position);

        void onParentListItemCollapsed(int position);
    }

    public void setMainItemClickToExpand(){
        itemView.setOnClickListener(this);
    }

    public void onExpansionToggled(boolean expanded) {

    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setmExpanded(boolean expanded) {
        mExpanded = expanded;
    }

    public ParentListItemExpandColapseListener getParentListItemExpandColapseListener() {
        return mParentListItemExpandColapseListener;
    }

    public void setParentListItemExpandColapseListener(ParentListItemExpandColapseListener mParentListItemExpandColapseListener) {
        this.mParentListItemExpandColapseListener = mParentListItemExpandColapseListener;
    }

    //Expansion of the parent view
    protected void expandView(){
        setmExpanded(true);
        onExpansionToggled(false);

        if (mParentListItemExpandColapseListener != null) {
            mParentListItemExpandColapseListener.onParentListItemExpanded(getAdapterPosition());
        }
    }

    //Collapse of the parent view
    protected void collapseView() {
        setmExpanded(false);
        onExpansionToggled(true);

        if (mParentListItemExpandColapseListener != null){
            mParentListItemExpandColapseListener.onParentListItemCollapsed(getAdapterPosition());
        }
    }
}
