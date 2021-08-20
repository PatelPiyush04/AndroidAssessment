package com.theherdonline.ui.marketReport;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.gson.Gson;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemMarketReportBinding;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.TimeUtils;


public class MarketReportRecyclerViewAdapter extends CustomRecyclerViewAdapter<MarketReportRecyclerViewAdapter.ViewHolder> {

    private List<MarketReport> mValues;
    private final InterfaceMarketReportListener mListener;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private Gson mGson = new Gson();
    private Boolean mShowLastLabel = false;
    private Integer mTotalNumber;
    public MarketReportRecyclerViewAdapter(Context context, List<MarketReport> items, InterfaceMarketReportListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
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
        MarketReport item = mValues.get(position);
        ((ViewHolder) holder).mItem = item;
        ((ViewHolder) holder).binding.tvTitle.setText(ActivityUtils.trimText(mContext, item.getTitle()));
        ((ViewHolder) holder).binding.tvDescription.setText(ActivityUtils.trimText(mContext, item.getDescription()));

        // sale at
        if (item.getSale_at() != null) {
            ((ViewHolder) holder).binding.linearSaleDate.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).binding.tvSaleDate.setText(mContext.getString(R.string.txt_sale_date_s,ActivityUtils.trimText(mContext, TimeUtils.BackendUTC2Local(item.getSale_at()))));
        }
        else
        {
            ((ViewHolder) holder).binding.linearSaleDate.setVisibility(View.GONE);
        }
        // address
        if (item.getEntitySaleyard() != null)
        {
            ((ViewHolder) holder).binding.linearSaleAddress.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).binding.tvSaleAddress.setText(ActivityUtils.trimText(mContext, item.getEntitySaleyard().getAddress()));
        }
        else
        {
            ((ViewHolder) holder).binding.linearSaleAddress.setVisibility(View.GONE);
        }
        // report date
        ((ViewHolder) holder).binding.tvVoiceReport.setText(mContext.getString(R.string.txt_report_date,ActivityUtils.trimText(mContext, TimeUtils.BackendUTC2Local(item.getUpdated_at()))));
        // report by
        if (item.getUser() != null)
        {
            ((ViewHolder) holder).binding.tvReportBy.setText(mContext.getString(R.string.txt_report_by,ActivityUtils.trimText(mContext, item.getUser().getFullName())));
        }
        else
        {
            ((ViewHolder) holder).binding.tvReportBy.setText(mContext.getString(R.string.txt_report_by,ActivityUtils.trimText(mContext, null)));
        }
        ((ViewHolder) holder).binding.getRoot().setOnClickListener(l->{
            mListener.OnClickMarketReports(item);
        });
        ((ViewHolder) holder).binding.executePendingBindings();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemMarketReportBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_market_report, parent, false);
        return new ViewHolder(binding);
    }



    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public MarketReport mItem;
        private final ItemMarketReportBinding binding;

        public ViewHolder(final ItemMarketReportBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
