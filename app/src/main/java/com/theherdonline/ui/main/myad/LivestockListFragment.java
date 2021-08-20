package com.theherdonline.ui.main.myad;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.Media;
import com.theherdonline.ui.general.SearchFilterPublicLivestock;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.ui.profile.PagerViewModel;

public class LivestockListFragment extends PagerListFragment<EntityLivestock>{


    public static String ARG_user_id = "user_id";
    public static String ARG_Ad_type = "data_ad_type";
    public static Integer ARG_VIEW_TYPE_REQUEST = 1;
    public static Integer ARG_VIEW_TYPE_CURRENT = 2;
    public static Integer ARG_VIEW_TYPE_POST = 3;
    public static Integer ARG_VIEW_TYPE_PUBLIC = 4;



    public static String ARG_is_for_public = "is_for_public";
    public static String ARG_is_auction_plus = "is_auction_plus";

    public static String ARG_search_filter = "arg_search_filter";


    private Boolean mIsForPublicSearch;
    private Boolean mIsAuctionPlus;


    private MyAdPagerViewModel mMyAdFragmentViewModel;
    private LivestockInterface mLivestockListener;

    private SearchFilterPublicLivestock mSearchFilter;

    private Integer mViewType = ARG_VIEW_TYPE_REQUEST;


    private LivestockInterface mEditLivestockListener = new LivestockInterface() {
         @Override
        public void onClickLivestock(EntityLivestock livestock) {
            mLivestockListener.onUpdateLivestock(livestock);
        }

        @Override
        public void onClickLivestockList(List<EntityLivestock> livestock) {

        }

        @Override
        public void onUpdateLivestock(EntityLivestock livestock) {

        }

        @Override
        public void onClickShareLivestock(EntityLivestock livestock) {


        }

        @Override
        public void onConfirmLivestock(EntityLivestock livestock, List<Media> mediaListToDelete, Boolean hasChanged) {

        }

        @Override
        public void onClickSavedLivestock(EntityLivestock livestock, ImageView view) {

        }
    };



    public static LivestockListFragment newInstance(SearchFilterPublicLivestock searchFilter, Boolean isForPublic, Boolean isAuctionPlus) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_search_filter,searchFilter);
        args.putBoolean(ARG_is_for_public, isForPublic);
        args.putBoolean(ARG_is_auction_plus, isAuctionPlus);
        LivestockListFragment fragment = new LivestockListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static LivestockListFragment newInstance(SearchFilterPublicLivestock searchFilter, Integer viewType) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_search_filter,searchFilter);
        args.putBoolean(ARG_is_for_public, false);
        args.putInt(ARG_Ad_type,viewType);
        LivestockListFragment fragment = new LivestockListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public CustomRecyclerViewAdapter getAdapter() {
        //return new AdverRecyclerViewAdapter(mMyAdFragmentViewModel.getmLiveDataStreamList().getValue());
        if (mIsForPublicSearch) {
            return new MyAdRecyclerViewAdapter(getContext(), mMyAdFragmentViewModel.getDataList(), mLivestockListener);
        }
        else
        {
            if (mViewType == LivestockListFragment.ARG_VIEW_TYPE_REQUEST)
            {
                return new MyAdRecyclerViewAdapter(getContext(), mMyAdFragmentViewModel.getDataList(), mEditLivestockListener,mViewType);
            }
            else
            {
                return new MyAdRecyclerViewAdapter(getContext(), mMyAdFragmentViewModel.getDataList(), mLivestockListener,mViewType);
            }
        }
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
            mIsForPublicSearch = getArguments().getBoolean(ARG_is_for_public);
            mSearchFilter = getArguments().getParcelable(ARG_search_filter);
            mIsAuctionPlus = getArguments().getBoolean(ARG_is_auction_plus);
            if(!mIsForPublicSearch)
            {
                mViewType = getArguments().getInt(ARG_Ad_type);
            }
            else
            {
                mViewType = ARG_VIEW_TYPE_PUBLIC;
            }
        }
        MyAdPagerViewModel.Factory factoryProfile = new MyAdPagerViewModel.Factory(getActivity().getApplication(),
               mSearchFilter,mIsForPublicSearch, mIsAuctionPlus);
        mMyAdFragmentViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(MyAdPagerViewModel.class);
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

    @Override
    public int getItemId(EntityLivestock entityLivestock) {
        return entityLivestock.getId();
    }



    @Override
    public String deleteDescription(EntityLivestock entityLivestock) {
        return getString(R.string.txt_are_you_sure_delete_paddock_sale);
    }
}
