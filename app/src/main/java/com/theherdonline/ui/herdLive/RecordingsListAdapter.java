package com.theherdonline.ui.herdLive;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemRecordingBinding;
import com.theherdonline.util.TimeUtils;

import java.util.List;

public class RecordingsListAdapter extends RecyclerView.Adapter<RecordingsListAdapter.ViewHolder> {

    private final List<ResVods> mData;
    private final Context mContext;
    //HerdLiveFragment.OnHerdLiveInterface mOnHerdLiveInterface;
    private LayoutInflater layoutInflater;

    public RecordingsListAdapter(FragmentActivity requireActivity, List<ResVods> data) {
        this.mContext = requireActivity;
        this.mData = data;
        //this.mOnHerdLiveInterface = (HerdLiveFragment.OnHerdLiveInterface) requireActivity;
    }


    @Override
    public RecordingsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ItemRecordingBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_recording, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        ResVods item = mData.get(position);

        holder.binding.tvRecordTitle.setText(item.getName());
        holder.binding.tvRecordDate.setText("Started: " + TimeUtils.BackendUTC2LocalUTCOnlyTime(TimeUtils.UTC2Local(item.getDateRecorded())));

        holder.binding.llHerdLiveItem.setOnClickListener(l -> {
            //mOnHerdLiveInterface.onClickHerdLiveItem(item.getId());
            Log.e("Data", "Click url Path=>" + item.getUrl());
            //ActivityUtils.playVideo(mContext, item.getUrl());
            Intent intent = new Intent(mContext, VideoPlayer.class);
            intent.putExtra("path", item.getUrl());
            mContext.startActivity(intent);
        });

        //holder.mItem = item;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //public SaleyardArea mItem;
        private final ItemRecordingBinding binding;

        public ViewHolder(final ItemRecordingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
