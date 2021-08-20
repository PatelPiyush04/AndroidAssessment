package com.theherdonline.ui.main.myad;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemLivestockBinding;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.util.UIUtils;


public class MyAdRecyclerViewAdapter extends CustomRecyclerViewAdapter<MyAdRecyclerViewAdapter.ViewHolder> {

    private List<EntityLivestock> mValues;
    private final LivestockInterface mListener;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private Boolean mShowLastLabel = false;
    private Integer mTotalNumber;
    private Integer mViewType;


    public MyAdRecyclerViewAdapter(Context context, List<EntityLivestock> items, LivestockInterface listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
        mViewType = LivestockListFragment.ARG_VIEW_TYPE_PUBLIC;
    }


    public MyAdRecyclerViewAdapter(Context context, List<EntityLivestock> items, LivestockInterface listener, Integer viewType) {
        mContext = context;
        mValues = items;
        mListener = listener;
        mViewType = viewType;

    }

    @Override
    public void showNowMoreDataText(Boolean bShow) {

        if (mValues.size() != 0)
        {
            notifyItemChanged(mValues.size() - 1);

        }
    }

    @Override
    public void setTotalItemNumber(Integer number) {
        mTotalNumber = number;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder customHolder = (ViewHolder) holder;
        EntityLivestock item = mValues.get(position);
        ((ViewHolder) holder).mItem = item;
        Boolean showLabel = mViewType !=  LivestockListFragment.ARG_VIEW_TYPE_PUBLIC;
        UIUtils.showLivestock(mContext,customHolder.binding,item,mListener,showLabel);
        customHolder.binding.imageSaved.setVisibility(mViewType == LivestockListFragment.ARG_VIEW_TYPE_REQUEST ? View.INVISIBLE : View.VISIBLE);
        customHolder.binding.executePendingBindings();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemLivestockBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_livestock, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EntityLivestock mItem;
        private final ItemLivestockBinding binding;

        public ViewHolder(final ItemLivestockBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
