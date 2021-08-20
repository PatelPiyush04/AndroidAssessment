package com.theherdonline.ui.main.saleyardsearch;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemSaleyardBinding;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.util.UIUtils;


public class SaleyardRecyclerViewAdapter extends CustomRecyclerViewAdapter<SaleyardRecyclerViewAdapter.ViewHolder> {

    private List<EntitySaleyard> mValues;
    private final SaleyardInterface mListener;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private Boolean mShowLastLabel = false;
    private Integer mTotalNumber;
    public SaleyardRecyclerViewAdapter(Context context, List<EntitySaleyard> items, SaleyardInterface listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
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
        EntitySaleyard item = mValues.get(position);
        ((ViewHolder) holder).mItem = item;
        UIUtils.showSaleyard(mContext,customHolder.binding,item,mListener);
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
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public EntitySaleyard mItem;
        private final ItemSaleyardBinding binding;

        public ViewHolder(final ItemSaleyardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
