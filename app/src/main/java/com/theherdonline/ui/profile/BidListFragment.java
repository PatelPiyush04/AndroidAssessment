package com.theherdonline.ui.profile;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentGenericListBinding;
import com.theherdonline.db.entity.Bid;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.AppUtil;
import dagger.Lazy;

public class BidListFragment extends HerdFragment{

    public static String ARG_user_id = "user_id";

    private FragmentGenericListBinding mBinding;
    private MainActivityViewModel mMainViewModel;
    private LivestockInterface mLivestockListener;

    private DataObserver<List<Bid>> mBidObserve = new DataObserver<List<Bid>>() {
        @Override
        public void onSuccess(List<Bid> data) {
            //mBinding.progressBar.setVisibility(View.GONE);
            mBinding.swiperefreshContainer.setRefreshing(false);
            mBinding.list.setAdapter(new BidRecyclerViewAdapter(getContext(),data,mLivestockListener));
        }

        @Override
        public void onError(Integer code, String msg) {
            mBinding.swiperefreshContainer.setRefreshing(false);
            //mBinding.progressBar.setVisibility(View.GONE);
            mNetworkListener.onFailed(code,msg);
        }

        @Override
        public void onLoading() {
            //mBinding.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onDirty() {
            mBinding.swiperefreshContainer.setRefreshing(false);
            //mBinding.progressBar.setVisibility(View.GONE);
        }
    };

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    @Inject
    AppUtil mAppUtil;



    private Integer mUserID;


    public static BidListFragment newInstance(Lazy<BidListFragment> bidListFragmentLazy, Integer userId) {
        Bundle args = new Bundle();
        BidListFragment fragment = bidListFragmentLazy.get();
        if (userId != null)
        {
            args.putInt(ARG_user_id, userId);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Inject
    public BidListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mUserID = getArguments().getInt(ARG_user_id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_generic_list, container, false);
        mBinding.frameLayoutToolbar.getRoot().setVisibility(View.GONE);
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.list.addItemDecoration(mAppUtil.provideDividerItemDecoration());
        mBinding.swiperefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mUserID != null)
                {
                    mMainViewModel.refreshBidList(mUserID);
                }
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
       // mMainViewModel.getmLDResponseBid().observe(this,mBidObserve);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserID != null) {
            mMainViewModel.refreshBidList(mUserID);
            mBinding.swiperefreshContainer.setRefreshing(true);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LivestockInterface) {
            mLivestockListener = (LivestockInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LivestockInterface");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLivestockListener = null;
    }

}
