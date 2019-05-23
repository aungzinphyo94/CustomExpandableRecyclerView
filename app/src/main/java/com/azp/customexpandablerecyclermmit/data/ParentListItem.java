package com.azp.customexpandablerecyclermmit.data;

import java.util.List;

public interface ParentListItem {

    List<?> getChildItemList();

    boolean isInitiallyExpanded();

}
