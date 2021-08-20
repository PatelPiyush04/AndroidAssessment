package com.theherdonline.ui.notification.backup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.ui.notification.normal.NotificationListener;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.ui.profile.PagerViewModel;

public class NotificationListBackupFragment extends PagerListFragment<HerdNotification>{





    private NotificationPagerViewBackupModel mNotificationViewModel;
    private NotificationListener mNotificationListener;
    public static NotificationListBackupFragment newInstance() {
        Bundle args = new Bundle();
        NotificationListBackupFragment fragment = new NotificationListBackupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CustomRecyclerViewAdapter getAdapter() {
        return  new NotificationRecyclerViewBackupAdapter(getContext(), mNotificationViewModel.getDataList(), mNotificationListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NotificationPagerViewBackupModel.Factory factoryProfile = new NotificationPagerViewBackupModel.Factory(getActivity().getApplication());
        mNotificationViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(NotificationPagerViewBackupModel.class);
        mNotificationViewModel.getmLiveDataStreamList().observe(this, mListDataObserver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NotificationListener) {
            mNotificationListener = (NotificationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NotificationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNotificationListener = null;
    }

    @Override
    public PagerViewModel getViewModel() {
        return mNotificationViewModel;
    }
}
