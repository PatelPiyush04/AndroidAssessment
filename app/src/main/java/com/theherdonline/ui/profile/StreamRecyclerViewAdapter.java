package com.theherdonline.ui.profile;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemStreamBinding;
import com.theherdonline.db.entity.Post;
import com.theherdonline.db.entity.StreamItem;
import com.theherdonline.db.entity.StreamType;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.herdinterface.PostInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.UIUtils;


public class StreamRecyclerViewAdapter extends CustomRecyclerViewAdapter<StreamRecyclerViewAdapter.ViewHolder> {

    private List<StreamItem> mValues;
    private final InterfaceStreamItemListener mListener;

    private final LivestockInterface mLivestockListener;
    private final SaleyardInterface mSaleyardListener;
    private final PostInterface mPostListener;



    private Context mContext;
    private LayoutInflater layoutInflater;
    private Gson mGson = new Gson();
    private Boolean mShowLastLabel = false;
    private Integer mTotalNumber;


    public StreamRecyclerViewAdapter(Context context, List<StreamItem> items, InterfaceStreamItemListener listener,
                                     LivestockInterface livestockInterface,
                                     SaleyardInterface saleyardInterface,
                                     PostInterface postInterface) {
        mContext = context;
        mValues = items;
        mListener = listener;
        mLivestockListener = livestockInterface;
        mSaleyardListener = saleyardInterface;
        mPostListener = postInterface;
    }

    @Override
    public void showNowMoreDataText(Boolean bShow) {
        mShowLastLabel = bShow;
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
        StreamItem item = mValues.get(position);
        StreamType type = ActivityUtils.convertStr2StreamType(item.getType());
        customHolder.binding.setType(StreamType.POST);
        customHolder.binding.includeLivestockItem.getRoot().setVisibility(type == StreamType.LIVESTOCK? View.VISIBLE: View.GONE);
        customHolder.binding.includeSaleyardItem.getRoot().setVisibility(type == StreamType.SALEYARD ? View.VISIBLE: View.GONE);
        customHolder.binding.includePostItem.getRoot().setVisibility(type == StreamType.POST ? View.VISIBLE: View.GONE);

        try {
            JsonObject jsonObject = mGson.toJsonTree(item.getStreamable()).getAsJsonObject();
            switch (type){
                case LIVESTOCK:
                    EntityLivestock livestockItem = mGson.fromJson(jsonObject, EntityLivestock.class);
                    UIUtils.showLivestock(mContext,customHolder.binding.includeLivestockItem,livestockItem, mLivestockListener,true);
                    break;
                case SALEYARD:
                    EntitySaleyard entitySaleyardItem = mGson.fromJson(jsonObject, EntitySaleyard.class);
                    UIUtils.showSaleyard(mContext,customHolder.binding.includeSaleyardItem, entitySaleyardItem, mSaleyardListener);
                    break;
                case POST:
                    Post postItem = mGson.fromJson(jsonObject, Post.class);
                    UIUtils.showPost(mContext,customHolder.binding.includePostItem,postItem, mPostListener);
                    break;
                default:
            }
            customHolder.mItem = item;
            customHolder.binding.getRoot().setOnClickListener(l->{
                mListener.OnClickStreamItem(item);
            });
            Boolean bShowLastLabel = mTotalNumber != null && mTotalNumber == position+1;
            customHolder.binding.tvLastDataLabel.setVisibility(bShowLastLabel ? View.VISIBLE : View.GONE);
        }
        catch (Exception e)
        {
            customHolder.binding.getRoot().setVisibility(View.GONE);
            customHolder.binding.includeLivestockItem.getRoot().setVisibility(View.GONE);
            customHolder.binding.includePostItem.getRoot().setVisibility(View.GONE);
            customHolder.binding.includeSaleyardItem.getRoot().setVisibility(View.GONE);
            customHolder.binding.linearLayoutTop.setVisibility(View.GONE);
            customHolder.binding.tvLastDataLabel.setVisibility(View.GONE);
        }



    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());

        }
        ItemStreamBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_stream, parent, false);
        DataBindingUtil.inflate(layoutInflater, R.layout.item_stream, parent, false);

        return new ViewHolder(binding);
    }

   /* @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        StreamItem item = mValues.get(position);
        StreamType type = ActivityUtils.convertStr2StreamType(item.getType());
        holder.binding.setType(StreamType.POST);
        holder.binding.includeLivestockItem.getRoot().setVisibility(type == StreamType.LIVESTOCK || type == StreamType.POST ? View.VISIBLE: View.GONE);
        holder.binding.includeSaleyardItem.getRoot().setVisibility(type == StreamType.SALEYARD ? View.VISIBLE: View.GONE);
        JsonObject jsonObject = mGson.toJsonTree(item.getStreamable()).getAsJsonObject();
        switch (type){
            case LIVESTOCK:
                EntityLivestock livestockItem = mGson.fromJson(jsonObject, EntityLivestock.class);
                UIUtils.showLivestock(mContext,holder.binding.includeLivestockItem,livestockItem);
                break;
            case SALEYARD:
                EntitySaleyard saleyardItem = mGson.fromJson(jsonObject, EntitySaleyard.class);
                UIUtils.showSaleyard(mContext,holder.binding.includeSaleyardItem,saleyardItem);
                break;
            case POST:
                Post postItem = mGson.fromJson(jsonObject, Post.class);
                UIUtils.showPost(mContext,holder.binding.includeLivestockItem,postItem);
                break;
            case ALL:
                default:
        }
        holder.mItem = item;
        holder.binding.getRoot().setOnClickListener(l->{
            mListener.OnClickStreamItem(item);
        });
    }*/

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public StreamItem mItem;
        private final ItemStreamBinding binding;

        public ViewHolder(final ItemStreamBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
