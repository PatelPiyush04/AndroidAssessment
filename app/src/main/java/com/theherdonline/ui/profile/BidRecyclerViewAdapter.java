package com.theherdonline.ui.profile;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemBidBinding;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.Bid;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.util.UIUtils;


public class BidRecyclerViewAdapter extends CustomRecyclerViewAdapter<BidRecyclerViewAdapter.ViewHolder> {

    private List<Bid> mValues;
    private final LivestockInterface mListener;
    //private final LivestockInterface mLivestockListener;

    private Context mContext;
    private LayoutInflater layoutInflater;
    private Gson mGson = new Gson();


    public BidRecyclerViewAdapter(Context context, List<Bid> items, LivestockInterface listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public void showNowMoreDataText(Boolean bShow) {

    }

    @Override
    public void setTotalItemNumber(Integer number) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Bid item = mValues.get(position);
        JsonObject jsonObject = mGson.toJsonTree(item.getBiddable()).getAsJsonObject();
        EntityLivestock livestockItem = mGson.fromJson(jsonObject, EntityLivestock.class);
        ((BidRecyclerViewAdapter.ViewHolder) viewHolder).mItem = item;
        UIUtils.showLivestock(mContext,((ViewHolder) viewHolder).binding.includeLivestockItem,livestockItem, mListener,false);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemBidBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_bid, parent, false);
        return new ViewHolder(binding);
    }

/*    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Bid item = mValues.get(position);
        JsonObject jsonObject = mGson.toJsonTree(item.getBiddable()).getAsJsonObject();
        EntityLivestock livestockItem = mGson.fromJson(jsonObject, EntityLivestock.class);
        UIUtils.showLivestock(mContext,holder.binding.includeLivestockItem,livestockItem, mListener);
    }*/

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Bid mItem;
        private final ItemBidBinding binding;

        public ViewHolder(final ItemBidBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
