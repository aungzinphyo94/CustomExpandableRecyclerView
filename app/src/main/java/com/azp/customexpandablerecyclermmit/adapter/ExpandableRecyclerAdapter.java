package com.azp.customexpandablerecyclermmit.adapter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.azp.customexpandablerecyclermmit.data.ParentListItem;
import com.azp.customexpandablerecyclermmit.data.ParentWrapper;
import com.azp.customexpandablerecyclermmit.viewholder.ChildViewHolder;
import com.azp.customexpandablerecyclermmit.viewholder.ParentViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class ExpandableRecyclerAdapter <PVH extends ParentViewHolder, CVH extends ChildViewHolder>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ParentViewHolder.ParentListItemExpandColapseListener {

    protected List<Object> mItemList;
    private static final int TYPE_PARENT = 0;
    private static final int TYPE_CHILD = 1;
    private static final String EXPANDED_STATE_MAP = "ExpandableRecyclerAdapter.ExpandedStateMap";

    private List<? extends ParentListItem> mParentItemList;
    private List<RecyclerView> mAttachedRecyclerView;
    private ExpandCollapseListener mExpandCollapseListener;

    public interface ExpandCollapseListener {

        void onListItemExpanded(int position);

        void onListItemCollapsed(int position);
    }

    public ExpandableRecyclerAdapter(List<? extends ParentListItem> mParentItemList) {
        super();
        this.mParentItemList = mParentItemList;
        mItemList = ExpandableRecyclerAdapterHelper.generateParentChildItemList(mParentItemList);
        mAttachedRecyclerView = new ArrayList<>();
    }

    public abstract PVH onCreateParentViewHolder (ViewGroup parentViewGroup);

    public abstract CVH onCreateChildViewHolder (ViewGroup childViewGroup);

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_PARENT) {
            PVH pvh = onCreateParentViewHolder(viewGroup);
            pvh.setParentListItemExpandColapseListener(this);
            return pvh;
        } else if (viewType == TYPE_CHILD) {
            return onCreateChildViewHolder(viewGroup);
        } else {
            throw new IllegalStateException("Incorrect ViewType found");
        }
    }

    public abstract void onBindParentViewHolder (PVH parentViewHolder, int position, ParentListItem parentListItem);

    public abstract void onBindChildViewHolder (CVH childViewHolder, int position, Object childListItem);

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            PVH parentViewHolder = (PVH) viewHolder;

            if (parentViewHolder.shouldItemViewClickToggleExpansion()) {
                parentViewHolder.setMainItemClickToExpand();
            }

            ParentWrapper parentWrapper = (ParentWrapper) listItem;
            parentViewHolder.setmExpanded(parentWrapper.ismExpanded());
            onBindParentViewHolder(parentViewHolder, position, parentWrapper.getmParentListItem());
        } else if (listItem == null) {
            throw new IllegalStateException("Incorrect ViewHolder found");
        }else {
            onBindChildViewHolder((CVH) viewHolder, position, listItem);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            return TYPE_PARENT;
        } else if (listItem == null){
            throw new IllegalStateException("Null object added");
        } else {
            return TYPE_CHILD;
        }
    }

    public List<? extends ParentListItem> getParentItemList() {
        return mParentItemList;
    }

    @Override
    public void onParentListItemExpanded(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            expandParentListItem((ParentWrapper) listItem, position, true);
        }
    }

    @Override
    public void onParentListItemCollapsed(int position) {
        Object listItem = getListItem(position);
        if (listItem instanceof ParentWrapper) {
            collapseParentListItem((ParentWrapper) listItem, position, true);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttachedRecyclerView.add(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mAttachedRecyclerView.remove(recyclerView);
    }

    public void setExpandCollapseListener(ExpandCollapseListener expandCollapseListener) {
        mExpandCollapseListener = expandCollapseListener;
    }

    public void expandParent(int parentIndex) {
        int parentWrapperIndex = getParentWrapperIndex(parentIndex);

        Object listItem = getListItem(parentWrapperIndex);
        ParentWrapper parentWrapper;
        if (listItem instanceof ParentWrapper) {
            parentWrapper = (ParentWrapper) listItem;
        } else {
            return;
        }

        expandViews(parentWrapper, parentWrapperIndex);
    }

    public void expandParent(ParentListItem parentListItem) {
        ParentWrapper parentWrapper = getParentWrapper(parentListItem);
        int parentWrapperIndex = mItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        expandViews(parentWrapper, parentWrapperIndex);
    }

    public void expandParentRange(int startParentIndex, int parentCount) {
        int endParentIndex = startParentIndex + parentCount;
        for (int i = startParentIndex; i < endParentIndex; i++) {
            expandParent(i);
        }
    }

    public void expandAllParents() {
        for (ParentListItem parentListItem : mParentItemList) {
            expandParent(parentListItem);
        }
    }

    public void collapseParent(int parentIndex) {
        int parentWrapperIndex = getParentWrapperIndex(parentIndex);

        Object listItem = getListItem(parentWrapperIndex);
        ParentWrapper parentWrapper;
        if (listItem instanceof ParentWrapper) {
            parentWrapper = (ParentWrapper) listItem;
        } else {
            return;
        }

        collapseViews(parentWrapper, parentWrapperIndex);
    }

    public void collapseParent(ParentListItem parentListItem) {
        ParentWrapper parentWrapper = getParentWrapper(parentListItem);
        int parentWrapperIndex = mItemList.indexOf(parentWrapper);
        if (parentWrapperIndex == -1) {
            return;
        }

        collapseViews(parentWrapper, parentWrapperIndex);
    }

    public void collapseParentRange(int startParentIndex, int parentCount) {
        int endParentIndex = startParentIndex + parentCount;
        for (int i = startParentIndex; i < endParentIndex; i++) {
            collapseParent(i);
        }
    }

    public void collapseAllParents() {
        for (ParentListItem parentListItem : mParentItemList) {
            collapseParent(parentListItem);
        }
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(EXPANDED_STATE_MAP, generateExpandedStateMap());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null
                || !savedInstanceState.containsKey(EXPANDED_STATE_MAP)) {
            return;
        }

        HashMap<Integer, Boolean> expandedStateMap = (HashMap<Integer, Boolean>) savedInstanceState.getSerializable(EXPANDED_STATE_MAP);
        if (expandedStateMap == null) {
            return;
        }

        List<Object> parentWrapperList = new ArrayList<>();
        ParentListItem parentListItem;
        ParentWrapper parentWrapper;

        int parentListItemCount = mParentItemList.size();
        for (int i = 0; i < parentListItemCount; i++) {
            parentListItem = mParentItemList.get(i);
            parentWrapper = new ParentWrapper(parentListItem);
            parentWrapperList.add(parentWrapper);

            if (expandedStateMap.containsKey(i)) {
                boolean expanded = expandedStateMap.get(i);
                if (expanded) {
                    parentWrapper.setmExpanded(true);

                    int childListItemCount = parentWrapper.getChildItemList().size();
                    for (int j = 0; j < childListItemCount; j++) {
                        parentWrapperList.add(parentWrapper.getChildItemList().get(j));
                    }
                }
            }
        }

        mItemList = parentWrapperList;

        notifyDataSetChanged();
    }

    protected Object getListItem(int position) {
        boolean indexInRange = position >= 0 && position < mItemList.size();
        if (indexInRange) {
            return mItemList.get(position);
        } else {
            return null;
        }
    }

    private void expandViews(ParentWrapper parentWrapper, int parentIndex) {
        PVH viewHolder;
        for (RecyclerView recyclerView : mAttachedRecyclerView) {
            viewHolder = (PVH) recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null && !viewHolder.isExpanded()) {
                viewHolder.setmExpanded(true);
                viewHolder.onExpansionToggled(false);
            }

            expandParentListItem(parentWrapper, parentIndex, false);
        }
    }

    private void collapseViews(ParentWrapper parentWrapper, int parentIndex) {
        PVH viewHolder;
        for (RecyclerView recyclerView : mAttachedRecyclerView) {
            viewHolder = (PVH) recyclerView.findViewHolderForAdapterPosition(parentIndex);
            if (viewHolder != null && viewHolder.isExpanded()) {
                viewHolder.setmExpanded(false);
                viewHolder.onExpansionToggled(true);
            }

            collapseParentListItem(parentWrapper, parentIndex, false);
        }
    }

    private void expandParentListItem(ParentWrapper parentWrapper, int parentIndex, boolean expansionTriggeredByListItemClick) {
        if (!parentWrapper.ismExpanded()) {
            parentWrapper.setmExpanded(true);

            List<?> childItemList = parentWrapper.getChildItemList();
            if (childItemList != null) {
                int childListItemCount = childItemList.size();
                for (int i = 0; i < childListItemCount; i++) {
                    mItemList.add(parentIndex + i + 1, childItemList.get(i));
                }

                notifyItemRangeInserted(parentIndex + 1, childListItemCount);
            }

            if (expansionTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemExpanded(parentIndex - expandedCountBeforePosition);
            }
        }
    }


    private void collapseParentListItem(ParentWrapper parentWrapper, int parentIndex, boolean collapseTriggeredByListItemClick) {
        if (parentWrapper.ismExpanded()) {
            parentWrapper.setmExpanded(false);

            List<?> childItemList = parentWrapper.getChildItemList();
            if (childItemList != null) {
                int childListItemCount = childItemList.size();
                for (int i = childListItemCount - 1; i >= 0; i--) {
                    mItemList.remove(parentIndex + i + 1);
                }

                notifyItemRangeRemoved(parentIndex + 1, childListItemCount);
            }

            if (collapseTriggeredByListItemClick && mExpandCollapseListener != null) {
                int expandedCountBeforePosition = getExpandedItemCount(parentIndex);
                mExpandCollapseListener.onListItemCollapsed(parentIndex - expandedCountBeforePosition);
            }
        }
    }

    private int getExpandedItemCount(int position) {
        if (position == 0) {
            return 0;
        }

        int expandedCount = 0;
        for (int i = 0; i < position; i++) {
            Object listItem = getListItem(i);
            if (!(listItem instanceof ParentWrapper)) {
                expandedCount++;
            }
        }
        return expandedCount;
    }

    public void notifyParentItemInserted(int parentPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);

        int wrapperIndex;
        if (parentPosition < mParentItemList.size() - 1) {
            wrapperIndex = getParentWrapperIndex(parentPosition);
        } else {
            wrapperIndex = mItemList.size();
        }

        int sizeChanged = addParentWrapper(wrapperIndex, parentListItem);
        notifyItemRangeInserted(wrapperIndex, sizeChanged);
    }

    public void notifyParentItemRangeInserted(int parentPositionStart, int itemCount) {
        int initialWrapperIndex;
        if (parentPositionStart < mParentItemList.size() - itemCount) {
            initialWrapperIndex = getParentWrapperIndex(parentPositionStart);
        } else {
            initialWrapperIndex = mItemList.size();
        }

        int sizeChanged = 0;
        int wrapperIndex = initialWrapperIndex;
        int changed;
        int parentPositionEnd = parentPositionStart + itemCount;
        for (int i = parentPositionStart; i < parentPositionEnd; i++) {
            ParentListItem parentListItem = mParentItemList.get(i);
            changed = addParentWrapper(wrapperIndex, parentListItem);
            wrapperIndex += changed;
            sizeChanged += changed;
        }

        notifyItemRangeInserted(initialWrapperIndex, sizeChanged);
    }

    private int addParentWrapper(int wrapperIndex, ParentListItem parentListItem) {
        int sizeChanged = 1;
        ParentWrapper parentWrapper = new ParentWrapper(parentListItem);
        mItemList.add(wrapperIndex, parentWrapper);
        if (parentWrapper.isInitiallyExpanded()) {
            parentWrapper.setmExpanded(true);
            List<?> childItemList = parentWrapper.getChildItemList();
            mItemList.addAll(wrapperIndex + sizeChanged, childItemList);
            sizeChanged += childItemList.size();
        }
        return sizeChanged;
    }

    public void notifyParentItemRemoved(int parentPosition) {
        int wrapperIndex = getParentWrapperIndex(parentPosition);
        int sizeChanged = removeParentWrapper(wrapperIndex);

        notifyItemRangeRemoved(wrapperIndex, sizeChanged);
    }

    public void notifyParentItemRangeRemoved(int parentPositionStart, int itemCount) {
        int sizeChanged = 0;
        int wrapperIndex = getParentWrapperIndex(parentPositionStart);
        for (int i = 0; i < itemCount; i++) {
            sizeChanged += removeParentWrapper(wrapperIndex);
        }

        notifyItemRangeRemoved(wrapperIndex, sizeChanged);
    }

    private int removeParentWrapper(int parentWrapperIndex) {
        int sizeChanged = 1;
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.remove(parentWrapperIndex);
        if (parentWrapper.ismExpanded()) {
            int childListSize = parentWrapper.getChildItemList().size();
            for (int i = 0; i < childListSize; i++) {
                mItemList.remove(parentWrapperIndex);
                sizeChanged++;
            }
        }
        return sizeChanged;
    }

    public void notifyParentItemChanged(int parentPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);
        int wrapperIndex = getParentWrapperIndex(parentPosition);
        int sizeChanged = changeParentWrapper(wrapperIndex, parentListItem);

        notifyItemRangeChanged(wrapperIndex, sizeChanged);
    }

    public void notifyParentItemRangeChanged(int parentPositionStart, int itemCount) {
        int initialWrapperIndex = getParentWrapperIndex(parentPositionStart);

        int wrapperIndex = initialWrapperIndex;
        int sizeChanged = 0;
        int changed;
        ParentListItem parentListItem;
        for (int j = 0; j < itemCount; j++) {
            parentListItem = mParentItemList.get(parentPositionStart);
            changed = changeParentWrapper(wrapperIndex, parentListItem);
            sizeChanged += changed;
            wrapperIndex += changed;
            parentPositionStart++;
        }
        notifyItemRangeChanged(initialWrapperIndex, sizeChanged);
    }

    private int changeParentWrapper(int wrapperIndex, ParentListItem parentListItem) {
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(wrapperIndex);
        parentWrapper.setmParentListItem(parentListItem);
        int sizeChanged = 1;
        if (parentWrapper.ismExpanded()) {
            List<?> childItems = parentWrapper.getChildItemList();
            int childListSize = childItems.size();
            Object child;
            for (int i = 0; i < childListSize; i++) {
                child = childItems.get(i);
                mItemList.set(wrapperIndex + i + 1, child);
                sizeChanged++;
            }
        }

        return sizeChanged;

    }

    public void notifyParentItemMoved(int fromParentPosition, int toParentPosition) {

        int fromWrapperIndex = getParentWrapperIndex(fromParentPosition);
        ParentWrapper fromParentWrapper = (ParentWrapper) mItemList.get(fromWrapperIndex);

        // If the parent is collapsed we can take advantage of notifyItemMoved otherwise
        // we are forced to do a "manual" move by removing and then adding the parent + children
        // (no notifyItemRangeMovedAvailable)
        boolean isCollapsed = !fromParentWrapper.ismExpanded();
        boolean isExpandedNoChildren = !isCollapsed && (fromParentWrapper.getChildItemList().size() == 0);
        if (isCollapsed || isExpandedNoChildren) {
            int toWrapperIndex = getParentWrapperIndex(toParentPosition);
            ParentWrapper toParentWrapper = (ParentWrapper) mItemList.get(toWrapperIndex);
            mItemList.remove(fromWrapperIndex);
            int childOffset = 0;
            if (toParentWrapper.ismExpanded()) {
                childOffset = toParentWrapper.getChildItemList().size();
            }
            mItemList.add(toWrapperIndex + childOffset, fromParentWrapper);

            notifyItemMoved(fromWrapperIndex, toWrapperIndex + childOffset);
        } else {
            // Remove the parent and children
            int sizeChanged = 0;
            int childListSize = fromParentWrapper.getChildItemList().size();
            for (int i = 0; i < childListSize + 1; i++) {
                mItemList.remove(fromWrapperIndex);
                sizeChanged++;
            }
            notifyItemRangeRemoved(fromWrapperIndex, sizeChanged);


            // Add the parent and children at new position
            int toWrapperIndex = getParentWrapperIndex(toParentPosition);
            int childOffset = 0;
            if (toWrapperIndex != -1) {
                ParentWrapper toParentWrapper = (ParentWrapper) mItemList.get(toWrapperIndex);
                if (toParentWrapper.ismExpanded()) {
                    childOffset = toParentWrapper.getChildItemList().size();
                }
            } else {
                toWrapperIndex = mItemList.size();
            }
            mItemList.add(toWrapperIndex + childOffset, fromParentWrapper);
            List<?> childItemList = fromParentWrapper.getChildItemList();
            sizeChanged = childItemList.size() + 1;
            mItemList.addAll(toWrapperIndex + childOffset + 1, childItemList);
            notifyItemRangeInserted(toWrapperIndex + childOffset, sizeChanged);
        }
    }

    public void notifyChildItemInserted(int parentPosition, int childPosition) {
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);

        if (parentWrapper.ismExpanded()) {
            ParentListItem parentListItem = mParentItemList.get(parentPosition);
            Object child = parentListItem.getChildItemList().get(childPosition);
            mItemList.add(parentWrapperIndex + childPosition + 1, child);
            notifyItemInserted(parentWrapperIndex + childPosition + 1);
        }
    }

    public void notifyChildItemRangeInserted(int parentPosition, int childPositionStart, int itemCount) {
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);

        if (parentWrapper.ismExpanded()) {
            ParentListItem parentListItem = mParentItemList.get(parentPosition);
            List<?> childList = parentListItem.getChildItemList();
            Object child;
            for (int i = 0; i < itemCount; i++) {
                child = childList.get(childPositionStart + i);
                mItemList.add(parentWrapperIndex + childPositionStart + i + 1, child);
            }
            notifyItemRangeInserted(parentWrapperIndex + childPositionStart + 1, itemCount);
        }
    }

    public void notifyChildItemRemoved(int parentPosition, int childPosition) {
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);

        if (parentWrapper.ismExpanded()) {
            mItemList.remove(parentWrapperIndex + childPosition + 1);
            notifyItemRemoved(parentWrapperIndex + childPosition + 1);
        }
    }

    public void notifyChildItemRangeRemoved(int parentPosition, int childPositionStart, int itemCount) {
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);

        if (parentWrapper.ismExpanded()) {
            for (int i = 0; i < itemCount; i++) {
                mItemList.remove(parentWrapperIndex + childPositionStart + 1);
            }
            notifyItemRangeRemoved(parentWrapperIndex + childPositionStart + 1, itemCount);
        }
    }

    public void notifyChildItemChanged(int parentPosition, int childPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);
        parentWrapper.setmParentListItem(parentListItem);
        if (parentWrapper.ismExpanded()) {
            int listChildPosition = parentWrapperIndex + childPosition + 1;
            Object child = parentWrapper.getChildItemList().get(childPosition);
            mItemList.set(listChildPosition, child);
            notifyItemChanged(listChildPosition);
        }
    }

    public void notifyChildItemRangeChanged(int parentPosition, int childPositionStart, int itemCount) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);
        parentWrapper.setmParentListItem(parentListItem);
        if (parentWrapper.ismExpanded()) {
            int listChildPosition = parentWrapperIndex + childPositionStart + 1;
            for (int i = 0; i < itemCount; i++) {
                Object child = parentWrapper.getChildItemList().get(childPositionStart + i);
                mItemList.set(listChildPosition + i, child);

            }
            notifyItemRangeChanged(listChildPosition, itemCount);
        }
    }

    public void notifyChildItemMoved(int parentPosition, int fromChildPosition, int toChildPosition) {
        ParentListItem parentListItem = mParentItemList.get(parentPosition);
        int parentWrapperIndex = getParentWrapperIndex(parentPosition);
        ParentWrapper parentWrapper = (ParentWrapper) mItemList.get(parentWrapperIndex);
        parentWrapper.setmParentListItem(parentListItem);
        if (parentWrapper.ismExpanded()) {
            Object fromChild = mItemList.remove(parentWrapperIndex + 1 + fromChildPosition);
            mItemList.add(parentWrapperIndex + 1 + toChildPosition, fromChild);
            notifyItemMoved(parentWrapperIndex + 1 + fromChildPosition, parentWrapperIndex + 1 + toChildPosition);
        }
    }

    private HashMap<Integer, Boolean> generateExpandedStateMap() {
        HashMap<Integer, Boolean> parentListItemHashMap = new HashMap<>();
        int childCount = 0;

        Object listItem;
        ParentWrapper parentWrapper;
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (mItemList.get(i) != null) {
                listItem = getListItem(i);
                if (listItem instanceof ParentWrapper) {
                    parentWrapper = (ParentWrapper) listItem;
                    parentListItemHashMap.put(i - childCount, parentWrapper.ismExpanded());
                } else {
                    childCount++;
                }
            }
        }

        return parentListItemHashMap;
    }

    private int getParentWrapperIndex(int parentIndex) {
        int parentCount = 0;
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            if (mItemList.get(i) instanceof ParentWrapper) {
                parentCount++;

                if (parentCount > parentIndex) {
                    return i;
                }
            }
        }

        return -1;
    }

    private ParentWrapper getParentWrapper(ParentListItem parentListItem) {
        int listItemCount = mItemList.size();
        for (int i = 0; i < listItemCount; i++) {
            Object listItem = mItemList.get(i);
            if (listItem instanceof ParentWrapper) {
                if (((ParentWrapper) listItem).getmParentListItem().equals(parentListItem)) {
                    return (ParentWrapper) listItem;
                }
            }
        }

        return null;
    }
}
