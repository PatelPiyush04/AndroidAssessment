package com.theherdonline.ui.notification.normal;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentSwipeListBinding;
import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.notification.backup.NotificationRecyclerViewBackupAdapter;
import com.theherdonline.util.AppUtil;

import org.apache.commons.collections4.ListUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;


public class NotificationFragment extends HerdFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    private NotificationListener mNotificationListener;


    // TODO: Rename and change types of parameters
    private Integer mUserId;
    private FragmentSwipeListBinding mBinding;
    MainActivityViewModel mMainViewModel;
    NotificationPagerViewModel mProfileViewModel;



    @Inject
    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
            return new CustomerToolbar(getString(R.string.txt_notifications), null,
                    null,null, null, null);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_swipe_list, container, false);
        mUserId = mAppUtil.getUserId();
        mBinding.swiperefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mProfileViewModel.refresh();
            }
        });




        return mBinding.getRoot();
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

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        mProfileViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(NotificationPagerViewModel.class);
        mProfileViewModel.refresh();
        mProfileViewModel.getLiveDataHerdNotificationList().observe(this, new DataObserver<List<HerdNotification>>(){
            @Override
            public void onSuccess(List<HerdNotification> data) {
                mBinding.swiperefreshView.setRefreshing(false);
                mBinding.textNotification.setVisibility(ListUtils.emptyIfNull(data).size() == 0 ? View.VISIBLE : View.INVISIBLE);
                RecyclerView.Adapter adapter = mBinding.list.getAdapter();
                if (adapter == null)
                {
                    mBinding.list.setAdapter(new NotificationRecyclerViewBackupAdapter(getContext(), ListUtils.emptyIfNull(data), mNotificationListener));
                }
                else
                {
                    ((NotificationRecyclerViewBackupAdapter)adapter).setmValues(ListUtils.emptyIfNull(data));
                }
            }

            @Override
            public void onError(Integer code, String msg) {
                mBinding.swiperefreshView.setRefreshing(false);
                mNetworkListener.onFailed(code,msg);
                mBinding.textNotification.setVisibility(View.GONE);
            }

            @Override
            public void onLoading() {
                if (mBinding.list.getAdapter() == null) {
                    mBinding.swiperefreshView.setRefreshing(true);
                }
                mBinding.textNotification.setVisibility(View.GONE);

            }

            @Override
            public void onDirty() {

            }
        });
    }





    @Override
    public void onResume() {
        super.onResume();
        if (mUserId == null || mUserId == 0) {
            // might enter this screen from main screen without login
            mUserId = mAppUtil.getUserId();
        }
    }


}
