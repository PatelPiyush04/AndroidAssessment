package com.theherdonline.ui.main;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.ItemStockItemBinding;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.SaleyardAsset;
import com.theherdonline.ui.herdinterface.MediaListInterface;
import com.theherdonline.util.ActivityUtils;


public class SaleyardAssetRecyclerViewAdapter extends RecyclerView.Adapter<SaleyardAssetRecyclerViewAdapter.ViewHolder> {

    private List<SaleyardAsset> mValues;
    private final MediaListInterface mListener;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public SaleyardAssetRecyclerViewAdapter(Context context, List<SaleyardAsset> items, MediaListInterface listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemStockItemBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_stock_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SaleyardAsset item = mValues.get(position);
        holder.mItem = item;
        holder.binding.tvAddress.setText(ActivityUtils.trimTextEmpty(item.getDescription()));
        Boolean hasMedia = item.getMedia() != null && item.getMedia().size() != 0;
        holder.binding.imageViewMedia.setVisibility(hasMedia ? View.VISIBLE: View.GONE);
        if (hasMedia)
        {
            Media firstMedia  = item.getMedia().get(0);
            if (firstMedia.getName().equals(AppConstants.TAG_images))
            {
                ActivityUtils.loadImage(mContext,holder.binding.imageViewMedia,ActivityUtils.getImageAbsoluteUrl(firstMedia.getUrl()),R.drawable.tho_logo_small);
            }
            else
            {

            }
        }
        holder.binding.imageViewMedia.setOnClickListener(l->{
            mListener.onClickOpenMediaList(item.getMedia());
        });
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public SaleyardAsset mItem;
        private final ItemStockItemBinding binding;

        public ViewHolder(final ItemStockItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
