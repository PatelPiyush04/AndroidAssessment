package com.theherdonline.ui.profile;

import androidx.recyclerview.widget.RecyclerView;

public abstract class CustomRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    public abstract void showNowMoreDataText(Boolean bShow);
    public abstract void setTotalItemNumber(Integer number);

}
