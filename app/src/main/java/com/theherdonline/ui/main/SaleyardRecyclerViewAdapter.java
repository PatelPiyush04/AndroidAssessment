package com.theherdonline.ui.main;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemSaleyardBinding;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.view.OnBottomRechedListener;
import com.theherdonline.util.UIUtils;


public class SaleyardRecyclerViewAdapter extends RecyclerView.Adapter<SaleyardRecyclerViewAdapter.ViewHolder> {

    private List<EntitySaleyard> mValues;
    private final SaleyardInterface mListener;

    private OnBottomRechedListener onBottomReachedListener;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private boolean mSelectList = false;

    public SaleyardRecyclerViewAdapter(Context context, List<EntitySaleyard> items, SaleyardInterface listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }


    public SaleyardRecyclerViewAdapter(Context context, List<EntitySaleyard> items, SaleyardInterface listener, boolean bFlag) {
        mContext = context;
        mValues = items;
        mListener = listener;
        mSelectList = bFlag;
    }

    public void setOnBottomReachedListener(OnBottomRechedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemSaleyardBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_saleyard, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        UIUtils.showSaleyard(mContext, holder.binding, holder.mItem, new SaleyardInterface() {
            @Override
            public void OnClickSaleyard(EntitySaleyard entitySaleyard) {
                if (mSelectList)
                {
                    mListener.OnSelectSaleyard(holder.mItem);
                }
                else {
                    mListener.OnClickSaleyard(holder.mItem);
                }
            }

            @Override
            public void OnClickSaleyardList(List<EntitySaleyard> entitySaleyard) {

            }

            @Override
            public void OnSelectSaleyard(EntitySaleyard entitySaleyard) {

            }

            @Override
            public void onClickShareSaleyard(EntitySaleyard entitySaleyard) {

            }

            @Override
            public void onClickSavedSaleyard(EntitySaleyard entitySaleyard, ImageView view) {

            }



            @Override
            public void onUpdateSaleyard(EntitySaleyard entitySaleyard) {

            }

            @Override
            public void onConfirmSaleyardV2(EntitySaleyard newEntitySaleyard, EntitySaleyard oldEntitySaleyard) {

            }
        });

        /*String text = holder.mItem.getName();
        if (!TextUtils.isEmpty(text))
        {
            holder.binding.tvTitle.setText(text);
        }
        text = holder.mItem.getStartsAt();
        if (text != null)
        {
            holder.binding.tvTime.setText(text);
        }
        text = holder.mItem.getAddress();
        if (text != null)
        {
            holder.binding.tvLocation.setText(text);
        }
        holder.binding.getRoot().setOnClickListener(l->{
            if (mSelectList)
            {
                mListener.OnSelectSaleyard(holder.mItem);
            }
            else {
                mListener.OnClickSaleyard(holder.mItem);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EntitySaleyard mItem;


        public final ItemSaleyardBinding binding;

        public ViewHolder(final ItemSaleyardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public List<EntitySaleyard> getmValues() {
        return mValues;
    }
}
