package com.theherdonline.ui.marketReport;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theherdonline.R;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.ui.profile.InterfaceStreamItemListener;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.ui.profile.PagerViewModel;

import org.apache.commons.lang3.StringUtils;

public class MarketReportListFragment extends PagerListFragment<MarketReport>{

    public static String ARG_user_id = "user_id";
    public static String ARG_stream_type = "stream_type";

    private Integer mUserID;
    private MarketReportPagerViewModel mMarketReportViewModel;
    private InterfaceMarketReportListener mMarketReportListener;

    public static MarketReportListFragment newInstance(Integer userId) {
        Bundle args = new Bundle();
        MarketReportListFragment fragment = new MarketReportListFragment();
        if (userId != null)
        {
            args.putInt(ARG_user_id,userId);
            fragment.setArguments(args);
        }
        return fragment;
    }


    @Override
    public int getItemId(MarketReport marketReport) {
        return marketReport.getId();
    }

    @Override
    public CustomRecyclerViewAdapter getAdapter() {
        //return new AdverRecyclerViewAdapter(mMarketReportViewModel.getmLiveDataStreamList().getValue());
        return  new MarketReportRecyclerViewAdapter(getContext(),mMarketReportViewModel.getDataList(), mMarketReportListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public String deleteDescription(MarketReport marketReport) {
        return getString(R.string.txt_are_you_sure_delete_market_report,StringUtils.defaultString(marketReport.getTitle()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null)
        {
            mUserID = getArguments().getInt(ARG_user_id);
        }

        MarketReportPagerViewModel.Factory factoryProfile = new MarketReportPagerViewModel.Factory(getActivity().getApplication(),
                mUserID);
        mMarketReportViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(MarketReportPagerViewModel.class);
        mMarketReportViewModel.getmLiveDataStreamList().observe(this, mListDataObserver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceStreamItemListener) {
            mMarketReportListener = (InterfaceMarketReportListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMarketReportListener = null;
    }

    @Override
    public PagerViewModel getViewModel() {
        return mMarketReportViewModel;
    }


}
