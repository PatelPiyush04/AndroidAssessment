package com.theherdonline.ui.main.saleyardsearch;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.ui.profile.PagerViewModel;

public class SaleyardPagerListFragment extends PagerListFragment<EntitySaleyard>{


    public static String ARG_user_id = "user_id";
    public static String ARG_Ad_type = "data_ad_type";
    public static String ARG_is_for_public = "is_for_public";
    public static String ARG_search_filter = "arg_search_filter";


    private SaleyardPagerViewModel mSaleyardFragmentViewModel;
    private SaleyardInterface mSaleyardListener;

    private SearchFilter mSearchFilter;


    public static SaleyardPagerListFragment newInstance(SearchFilter searchFilter) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_search_filter,searchFilter);
        SaleyardPagerListFragment fragment = new SaleyardPagerListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public CustomRecyclerViewAdapter getAdapter() {
        //return new AdverRecyclerViewAdapter(mSaleyardFragmentViewModel.getmLiveDataStreamList().getValue());
        return  new SaleyardRecyclerViewAdapter(getContext(),mSaleyardFragmentViewModel.getDataList(), mSaleyardListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null)
        {
            mSearchFilter = getArguments().getParcelable(ARG_search_filter);
        }
        SaleyardPagerViewModel.Factory factoryProfile = new SaleyardPagerViewModel.Factory(getActivity().getApplication(),
               mSearchFilter);
        mSaleyardFragmentViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(SaleyardPagerViewModel.class);
        mSaleyardFragmentViewModel.getmLiveDataStreamList().observe(this, mListDataObserver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SaleyardInterface) {
            mSaleyardListener = (SaleyardInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SaleyardInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSaleyardListener = null;
    }

    @Override
    public PagerViewModel getViewModel() {
        return mSaleyardFragmentViewModel;
    }
}
