package com.theherdonline.ui.profile.mysavedlivestock;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.main.myad.MyAdRecyclerViewAdapter;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.ui.profile.PagerViewModel;

public class MySavedLivestockPagerFragment extends PagerListFragment<EntityLivestock>{


    public static String ARG_user_id = "user_id";
    public static String ARG_Ad_type = "data_ad_type";
    public static String ARG_is_for_public = "is_for_public";
    public static String ARG_search_filter = "arg_search_filter";


    private MySavedLivestockViewModel mLivestockFragmentViewModel;
    private LivestockInterface mLivestockListener;

    private SearchFilter mSearchFilter;


    public static MySavedLivestockPagerFragment newInstance(SearchFilter searchFilter) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_search_filter,searchFilter);
        MySavedLivestockPagerFragment fragment = new MySavedLivestockPagerFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public CustomRecyclerViewAdapter getAdapter() {
        //return new AdverRecyclerViewAdapter(mLivestockFragmentViewModel.getmLiveDataStreamList().getValue());
        return  new MyAdRecyclerViewAdapter(getContext(),mLivestockFragmentViewModel.getDataList(), mLivestockListener);
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
        MySavedLivestockViewModel.Factory factoryProfile = new MySavedLivestockViewModel.Factory(getActivity().getApplication(),
               mSearchFilter);
        mLivestockFragmentViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(MySavedLivestockViewModel.class);
        mLivestockFragmentViewModel.getmLiveDataStreamList().observe(this, mListDataObserver);
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
        return mLivestockFragmentViewModel;
    }
}
