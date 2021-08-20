package com.theherdonline.ui.profile.mybid;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theherdonline.db.entity.Bid;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.profile.BidRecyclerViewAdapter;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.ui.profile.PagerViewModel;

public class MyBidListFragment extends PagerListFragment<Bid>{

    private MyBidPagerViewModel mMyAdFragmentViewModel;
    private LivestockInterface mLivestockListener;

    public static MyBidListFragment newInstance() {
        Bundle args = new Bundle();
        MyBidListFragment fragment = new MyBidListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CustomRecyclerViewAdapter getAdapter() {
        //return new AdverRecyclerViewAdapter(mMyAdFragmentViewModel.getmLiveDataStreamList().getValue());
        return  new BidRecyclerViewAdapter(getContext(),mMyAdFragmentViewModel.getDataList(), mLivestockListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyBidPagerViewModel.Factory factoryProfile = new MyBidPagerViewModel.Factory(getActivity().getApplication()
               );
        mMyAdFragmentViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(MyBidPagerViewModel.class);
        mMyAdFragmentViewModel.getmLiveDataStreamList().observe(this, mListDataObserver);
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

    @Override
    public PagerViewModel getViewModel() {
        return mMyAdFragmentViewModel;
    }
}
