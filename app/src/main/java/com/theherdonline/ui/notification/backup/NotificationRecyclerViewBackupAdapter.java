package com.theherdonline.ui.notification.backup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.theherdonline.R;
import com.theherdonline.databinding.ItemNotificationBinding;
import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.ui.notification.normal.NotificationListener;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.util.TimeUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;


public class NotificationRecyclerViewBackupAdapter extends CustomRecyclerViewAdapter<NotificationRecyclerViewBackupAdapter.ViewHolder> {

    private List<HerdNotification> mValues;

    private final NotificationListener notificationListener;



    private Context mContext;
    private LayoutInflater layoutInflater;
    private Gson mGson = new Gson();
    private Boolean mShowLastLabel = false;
    private Integer mTotalNumber;

    public NotificationRecyclerViewBackupAdapter(Context context, List<HerdNotification> items,
                                                 NotificationListener saleyardInterface
                                           ) {
        mContext = context;
        mValues = items;
        notificationListener = saleyardInterface;
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
        HerdNotification item = mValues.get(position);
      ViewHolder customHolder = (ViewHolder) holder;
      customHolder.binding.tvTitle.setText(StringUtils.defaultString(item.getTitle()));
      customHolder.binding.tvMessage.setText(StringUtils.defaultString(item.getMessage()));
      customHolder.binding.tvTime.setText(StringUtils.defaultString(TimeUtils.BackendUTC2Local(item.getCreateTime())));
      customHolder.binding.linearNotification.setOnClickListener(l->{
         notificationListener.onClickNotification(item);
      });

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());

        }
        ItemNotificationBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_notification, parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ItemNotificationBinding binding;

        public ViewHolder(final ItemNotificationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setmValues(List<HerdNotification> mValues) {
        this.mValues = mValues;
        notifyDataSetChanged();
    }
}
