package com.theherdonline.ui.main.myad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;

import com.theherdonline.R;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.main.saleyardsearch.SaleyardRecyclerViewAdapter;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.ui.profile.PagerViewModel;

public class PrimeSaleyardPagerListFragment extends PagerListFragment<EntitySaleyard>{



    public static String ARG_search_filter = "arg_search_filter";


    private PrimeSaleyardViewModel mSaleyardFragmentViewModel;
    private SaleyardInterface mSaleyardListener;

    private SearchFilter mSearchFilter;


    public static PrimeSaleyardPagerListFragment newInstance(SearchFilter searchFilter) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_search_filter,searchFilter);
        PrimeSaleyardPagerListFragment fragment = new PrimeSaleyardPagerListFragment();
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
        PrimeSaleyardViewModel.Factory factoryProfile = new PrimeSaleyardViewModel.Factory(getActivity().getApplication(),
               mSearchFilter);
        mSaleyardFragmentViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(PrimeSaleyardViewModel.class);
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
    public int getItemId(EntitySaleyard entitySaleyard) {
        return entitySaleyard.getId();
    }

    @Override
    public PagerViewModel getViewModel() {
        return mSaleyardFragmentViewModel;
    }



    @Override
    public String deleteDescription(EntitySaleyard entitySaleyard) {
        return getString(R.string.txt_are_you_sure_delete_prime_saleyard);
    }
}
