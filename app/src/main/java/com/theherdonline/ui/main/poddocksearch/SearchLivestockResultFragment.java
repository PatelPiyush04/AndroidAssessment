package com.theherdonline.ui.main.poddocksearch;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentLivestockSearchResultBinding;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.SearchFilterPublicLivestock;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.MapLivestockFragment;
import com.theherdonline.ui.main.myad.LivestockListFragment;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.util.ActivityUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class SearchLivestockResultFragment extends HerdFragment {

    final static public String ARG_is_auction_plus_screen = "arg-is-auction-plus";
    protected FragmentLivestockSearchResultBinding mBinding;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    MainActivityViewModel mMainActivityViewModel;
    @Inject
    Lazy<PoddockSearchFilterFragment> poddockSearchFilterFragmentLazy;
    public View.OnClickListener mClickFilterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null) {
                ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                        poddockSearchFilterFragmentLazy.get(), R.id.frameLayout_container);
            }
        }
    };


    @Inject
    Lazy<MapLivestockFragment> mMapLivestockFragmentProvider;
    private SearchFilterPublicLivestock mSearchFilter;
    private Boolean mIsFirstLaunch = true;
    private Boolean mCurrentIsMap = false;
    public View.OnClickListener mClickMapListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean isMapView = (Boolean) mBinding.getRoot().getTag();
            mBinding.frameLayoutList.setVisibility(isMapView ? View.VISIBLE : View.INVISIBLE);
            mBinding.mainCollapsingTab.setVisibility(isMapView ? View.VISIBLE : View.GONE);
            mBinding.frameLayoutMap.setVisibility(!isMapView ? View.VISIBLE : View.INVISIBLE);
            mCurrentIsMap = !isMapView;
            mBinding.getRoot().setTag(mCurrentIsMap);
            mBinding.cardViewFilter.setVisibility(mCurrentIsMap ? View.GONE : View.VISIBLE);
            Drawable icon = ContextCompat.getDrawable(getContext(), !mCurrentIsMap ? R.drawable.ic_button_map : R.drawable.ic_button_list);
            ((MainActivity) getActivity()).setMapButtonImage(icon);
        }
    };
    private Boolean mIsAuctionScreen = false;


    @Inject
    public SearchLivestockResultFragment() {
    }

    public static SearchLivestockResultFragment newInstance(SearchLivestockResultFragment fragment, Boolean isAuctionSection) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_is_auction_plus_screen, isAuctionSection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: mIsFirstLaunch" + mIsFirstLaunch);
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        if (!mIsAuctionScreen) {
            return new CustomerToolbar(getString(R.string.txt_paddock_sales),
                    mExitListener,
                    R.drawable.ic_button_filter, mClickFilterListener,
                    !mCurrentIsMap ? R.drawable.ic_button_map : R.drawable.ic_button_list, mClickMapListener);
        } else {
            return new CustomerToolbar(getString(R.string.txt_auctions_plus),
                    mExitListener,
                    null, null,
                    !mCurrentIsMap ? R.drawable.ic_button_map : R.drawable.ic_button_list, mClickMapListener);

        }
    }

    public void refreshMap(List<EntityLivestock> list) {
        Fragment map = MapLivestockFragment.newInstance(mMapLivestockFragmentProvider, list);
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout_map, map).addToBackStack(null).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_livestock_search_result, container, false);
        if (getArguments() != null) {
            mIsAuctionScreen = getArguments().getBoolean(ARG_is_auction_plus_screen);
        }

        //mBinding.mainCollapsingTab.setVisibility(!mIsAuctionScreen ? View.VISIBLE : View.GONE);

        mBinding.getRoot().setTag(mCurrentIsMap);
        mBinding.frameLayoutList.setVisibility(!mCurrentIsMap ? View.VISIBLE : View.INVISIBLE);
        mBinding.frameLayoutMap.setVisibility(mCurrentIsMap ? View.VISIBLE : View.INVISIBLE);
        mBinding.cardViewFilter.setOnClickListener(mClickFilterListener);

        mBinding.linearRadioGroup.setOnCheckedChangeListener((radioGroup, id) -> {

            try {
                //SearchFilterPublicLivestock mSearchFilter = mMainActivityViewModel.getmLDSearchFilterLivestock().getValue().clone();
                SearchFilterPublicLivestock mSearchFilter = mMainActivityViewModel.getmLDSearchFilterLivestock().getValue().clone();
                switch (id) {
                    case R.id.radio_option_2: {
                        mSearchFilter.setLivestockCategory(new LivestockCategory(1));
                        break;
                    }
                    case R.id.radio_option_3: {
                        mSearchFilter.setLivestockCategory(new LivestockCategory(2));
                        break;
                    }
                    default: {
                        mSearchFilter.setLivestockCategory(null);
                    }
                }

                mMainActivityViewModel.setLivestockSearchFilterNew(mSearchFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivityViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        subscription();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsFirstLaunch = true;
        mSearchFilter = null;
        mCurrentIsMap = false;
    }

    public Boolean isSetFilter(SearchFilterPublicLivestock filter) {
        if (filter == null)
            return false;
        SearchFilterPublicLivestock livestock = new SearchFilterPublicLivestock();
        //livestock.equals(filter);
        //return !livestock.equals(filter);
        return !livestock.equalsFromPaddock(filter);
    }

    protected Boolean isAuctionsPlus() {
        return mIsAuctionScreen;
    }

    public void subscription() {

        mMainActivityViewModel.getmLDSearchFilterLivestock().observe(this, new Observer<SearchFilterPublicLivestock>() {
            @Override
            public void onChanged(@Nullable SearchFilterPublicLivestock filter) {
                if (isSetFilter(filter)) {
                    mBinding.mainCollapsing.setVisibility(View.VISIBLE);
                    if ((filter.getmPlace() != null || filter.getLatLng() != null) && filter.getRadius() != null) {
                        mBinding.tvFilterApplied.setVisibility(View.INVISIBLE);
                        mBinding.linerFilterDetail.setVisibility(View.VISIBLE);
                        //ActivityUtils.loadImage(getContext(), mBinding.imageAnimal, ActivityUtils.getImageAbsoluteUrlFromWeb(filter.getLivestockCategory().getPath()), R.drawable.animal_defaul_photo);
                        // mBinding.tvAnimal.setText(filter.getLivestockCategory().getName());
                        mBinding.tvAddress.setText(filter.getmPlace() != null ? filter.getmPlace().getAddress() : getString(R.string.txt_current_location));
                        mBinding.tvDistance.setText(getString(R.string.txt_positive_distance_km, Math.round(filter.getRadius())));
                    } else {
                        mBinding.tvFilterApplied.setVisibility(View.VISIBLE);
                        mBinding.linerFilterDetail.setVisibility(View.INVISIBLE);
                    }
                } else {
                    mBinding.mainCollapsing.setVisibility(View.GONE);
                }
                if (filter.equals(mSearchFilter) && !mIsFirstLaunch) {
                    return;
                }
                mIsFirstLaunch = false;
                mSearchFilter = filter;
                Fragment f = LivestockListFragment.newInstance(filter, true, isAuctionsPlus());
                ((LivestockListFragment) f).setmOnDataChangedListener(new PagerListFragment.OnDataChangedListener() {
                    @Override
                    public void onChanged(ArrayList dataTypeList) {

                        List<EntityLivestock> advertisementList = new ArrayList<>(dataTypeList);
                        Fragment f = getChildFragmentManager().findFragmentById(R.id.frameLayout_map);
                        if (f instanceof MapLivestockFragment) {
                            ((MapLivestockFragment) f).refreshData(advertisementList);
                        } else {
                            refreshMap(advertisementList);
                        }
                    }
                });
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout_list, f).commit();
            }
        });
    }


}
