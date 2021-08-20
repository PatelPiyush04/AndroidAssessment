package com.theherdonline.ui.profile.mypost;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemPostBinding;
import com.theherdonline.db.entity.Post;
import com.theherdonline.ui.herdinterface.PostInterface;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.util.UIUtils;


public class MyPostRecyclerViewAdapter extends CustomRecyclerViewAdapter<MyPostRecyclerViewAdapter.ViewHolder> {

    private List<Post> mValues;
    private final PostInterface mListener;
    //private final LivestockInterface mLivestockListener;

    private Context mContext;
    private LayoutInflater layoutInflater;
    private Gson mGson = new Gson();


    public MyPostRecyclerViewAdapter(Context context, List<Post> items, PostInterface listener) {
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
        Post item = mValues.get(position);
        UIUtils.showPost(mContext,((ViewHolder) viewHolder).binding,item,mListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemPostBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_post, parent, false);
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
        public Post mItem;
        private final ItemPostBinding binding;

        public ViewHolder(final ItemPostBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
