package com.azp.customexpandablerecyclermmit.data;

import java.util.List;

public class ParentWrapper {

    private boolean mExpanded;
    private ParentListItem mParentListItem;

    public ParentWrapper(ParentListItem mParentListItem) {
        this.mParentListItem = mParentListItem;
        mExpanded = false;
    }

    public ParentListItem getmParentListItem() {
        return mParentListItem;
    }

    public void setmParentListItem(ParentListItem mParentListItem) {
        this.mParentListItem = mParentListItem;
    }

    public boolean ismExpanded() {
        return mExpanded;
    }

    public void setmExpanded(boolean mExpanded) {
        this.mExpanded = mExpanded;
    }

    public boolean isInitiallyExpanded() {
        return mParentListItem.isInitiallyExpanded();
    }

    public List<?> getChildItemList() {
        return mParentListItem.getChildItemList();
    }
}
