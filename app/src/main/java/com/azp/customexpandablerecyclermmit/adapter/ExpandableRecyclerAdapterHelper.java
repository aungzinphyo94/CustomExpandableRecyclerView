package com.azp.customexpandablerecyclermmit.adapter;

import com.azp.customexpandablerecyclermmit.data.ParentListItem;
import com.azp.customexpandablerecyclermmit.data.ParentWrapper;

import java.util.ArrayList;
import java.util.List;

public class ExpandableRecyclerAdapterHelper {

    public static List<Object> generateParentChildItemList(List<? extends ParentListItem> parentListItems) {

        List<Object> parentWrapperList = new ArrayList<>();
        ParentListItem parentListItem;
        ParentWrapper parentWrapper;

        int parentListItemCount = parentListItems.size();
        for (int i = 0; i < parentListItemCount; i++) {
            parentListItem = parentListItems.get(i);
            parentWrapper = new ParentWrapper(parentListItem);
            parentWrapperList.add(parentWrapper);

            if (parentWrapper.isInitiallyExpanded()) {
                parentWrapper.setmExpanded(true);

                int childListItemCount = parentWrapper.getChildItemList().size();
                for (int j = 0; j < childListItemCount; j++) {
                    parentWrapperList.add(parentWrapper.getChildItemList().get(j));
                }
            }

        }
        return parentWrapperList;
    }


}
