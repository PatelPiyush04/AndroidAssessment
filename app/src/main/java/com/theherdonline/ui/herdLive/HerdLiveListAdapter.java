package com.theherdonline.ui.herdLive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemHerdLiveListBinding;
import com.theherdonline.util.TimeUtils;

import java.util.List;

public class HerdLiveListAdapter extends RecyclerView.Adapter<HerdLiveListAdapter.ViewHolder> {

    private final List<ResHerdLive> mData;
    private final Context mContext;
    HerdLiveFragment.OnHerdLiveInterface mOnHerdLiveInterface;
    private LayoutInflater layoutInflater;

    public HerdLiveListAdapter(FragmentActivity requireActivity, List<ResHerdLive> data) {
        this.mContext = requireActivity;
        this.mData = data;
        this.mOnHerdLiveInterface = (HerdLiveFragment.OnHerdLiveInterface) requireActivity;
    }


    @Override
    public HerdLiveListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemHerdLiveListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_herd_live_list, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ResHerdLive item = mData.get(position);

        holder.binding.tvTitle.setText(item.getName());
        holder.binding.tvDate.setText(TimeUtils.BackendUTC2LocalUTC(TimeUtils.UTC2Local(item.getStreamDate())));

        holder.binding.llHerdLiveItem.setOnClickListener(l -> {
            mOnHerdLiveInterface.onClickHerdLiveItem(item.getId());
        });

        //holder.mItem = item;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public SaleyardArea mItem;
        private final ItemHerdLiveListBinding binding;

        public ViewHolder(final ItemHerdLiveListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
