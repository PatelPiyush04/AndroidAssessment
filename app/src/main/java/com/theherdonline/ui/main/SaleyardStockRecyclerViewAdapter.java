package com.theherdonline.ui.main;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemStockBinding;
import com.theherdonline.db.entity.SaleyardArea;
import com.theherdonline.ui.herdinterface.MediaListInterface;
import com.theherdonline.util.ActivityUtils;


public class SaleyardStockRecyclerViewAdapter extends RecyclerView.Adapter<SaleyardStockRecyclerViewAdapter.ViewHolder> {

    private List<SaleyardArea> mValues;
    private final MediaListInterface mListener;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public SaleyardStockRecyclerViewAdapter(Context context, List<SaleyardArea> items, MediaListInterface listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemStockBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_stock, parent, false);
        return new ViewHolder(binding);
    }




    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SaleyardArea item = mValues.get(position);
        holder.mItem = item;
        holder.binding.tvTitle.setText(item.getName());
        holder.binding.listStock.setLayoutManager(new LinearLayoutManager(mContext));
        holder.binding.listStock.setAdapter(new SaleyardAssetRecyclerViewAdapter(mContext,item.getSaleyardAssets(),
                mListener));
        holder.binding.listStock.addItemDecoration(ActivityUtils.provideDividerItemDecoration(mContext,R.drawable.list_divider));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SaleyardArea mItem;
        private final ItemStockBinding binding;

        public ViewHolder(final ItemStockBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
