package com.theherdonline.ui.main;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.theherdonline.R;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.databinding.ItemLivestockBinding;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.view.OnBottomRechedListener;
import com.theherdonline.util.UIUtils;

import java.util.List;


public class AdverRecyclerViewAdapter extends RecyclerView.Adapter<AdverRecyclerViewAdapter.ViewHolder> {

    private List<EntityLivestock> mValues;
    private final LivestockInterface mListener;

    private OnBottomRechedListener onBottomReachedListener;
    private Context mContext;

    private LayoutInflater layoutInflater;



    public AdverRecyclerViewAdapter(Context context, List<EntityLivestock> items, LivestockInterface listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    public void setOnBottomReachedListener(OnBottomRechedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        UIUtils.showLivestock(mContext,holder.binding, holder.mItem, mListener,false);

        /*holder.binding.linearLayoutPoster.setVisibility(View.GONE);
        holder.binding.tvTitle.setText(holder.mItem.getId().toString() + "   " + holder.mItem.getName());


        if (!TextUtils.isEmpty(holder.mItem.getAddress()))
        {
            holder.binding.tvLocation.setText(holder.mItem.getAddress());
        }
        else
        {
            holder.binding.tvLocation.setText(mContext.getString(R.string.txt_no_address));
        }

        holder.binding.tvQuantity.setText(holder.mItem.getQuantity().toString());




        *//*AppUtil.loadImage(mContext,
                holder.binding.imageViewSymbol,
                holder.mItem.getCategory().getCategory_image_path(),
                R.drawable.ic_default_animal_symbol);
        *//*
        if (holder.binding.listImage.getAdapter() == null)
        {
            ImagePagerAdapter imagePagerAdapter;
            imagePagerAdapter = new ImagePagerAdapter(mContext,holder.mItem.getMediaArray());
            holder.binding.listImage.setAdapter(imagePagerAdapter);
        }
        holder.binding.getRoot().setOnClickListener(l->{
            mListener.onClickLivestock(holder.mItem, mEntryPoint);
        });*/

    }

    public List<EntityLivestock> getmValues() {
        return mValues;
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
