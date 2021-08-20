package com.theherdonline.ui.main.saleyardsearch;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentLivestockSearchResultBinding;
import com.theherdonline.db.entity.SaleyardType;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.MapLivestockFragment;
import com.theherdonline.ui.main.MapSaleyardFragment;
import com.theherdonline.ui.herdinterface.SearchFilterInterface;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.util.ActivityUtils;

import org.apache.commons.lang3.StringUtils;

import dagger.Lazy;

public class SearchResultSaleyardFragment extends HerdFragment {

    private SearchFilterInterface mSearchListener;
    protected FragmentLivestockSearchResultBinding mBinding;
    private SearchFilterPublicSaleyard mSearchFilter;
    private Boolean mIsFirstLaunch = true;
    private Boolean mCurrentIsMap = false;
    private String mSaleyardType;


    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    MainActivityViewModel mMainActivityViewModel;


    public void setmSaleyardType(String mSaleyardType) {
        this.mSaleyardType = mSaleyardType;
    }

    @Inject
    Lazy<SaleyardSearchFilterFragment> saleyardSearchFilterFragmentLazy;
    @Inject
    Lazy<MapSaleyardFragment> mMapLivestockFragmentProvider;




    public View.OnClickListener mClickMapListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean isMapView = (Boolean) mBinding.getRoot().getTag();
            mBinding.frameLayoutList.setVisibility(isMapView ? View.VISIBLE : View.INVISIBLE);
            mBinding.frameLayoutMap.setVisibility(!isMapView ? View.VISIBLE : View.INVISIBLE);
            mCurrentIsMap = !isMapView;

            mBinding.cardViewFilter.setVisibility(mCurrentIsMap ? View.VISIBLE : View.GONE);
            mBinding.getRoot().setTag(mCurrentIsMap);
            Drawable icon =  ContextCompat.getDrawable(getContext(),!mCurrentIsMap ? R.drawable.ic_button_map : R.drawable.ic_button_list);
            ((MainActivity)getActivity()).setMapButtonImage(icon);
        }
    };


    @Override
    public CustomerToolbar getmCustomerToolbar() {

        String title =  mSaleyardType.equals(SaleyardType.Prime.name()) ? getString(R.string.txt_prime_saleyards) : getString(R.string.txt_saleyard_auctions);
        return new CustomerToolbar(StringUtils.defaultString(title),
                mExitListener,
                R.drawable.ic_button_filter,mClickFilterListener,
                !mCurrentIsMap ? R.drawable.ic_button_map : R.drawable.ic_button_list,mClickMapListener);
    }


    public View.OnClickListener mClickFilterListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getActivity() != null)
            {
                SaleyardSearchFilterFragment filterFragment = saleyardSearchFilterFragmentLazy.get();
                filterFragment.setmIsPrimeFilter(mSaleyardType.equals(SaleyardType.Prime.name()));
                ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                        saleyardSearchFilterFragmentLazy.get(),R.id.frameLayout_container);
            }

        }
    };

    @Inject
    public SearchResultSaleyardFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: mIsFirstLaunch" + String.valueOf(mIsFirstLaunch));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_livestock_search_result, container, false);
        mBinding.getRoot().setTag(mCurrentIsMap);
        mBinding.frameLayoutList.setVisibility(!mCurrentIsMap ? View.VISIBLE : View.INVISIBLE);
        mBinding.frameLayoutMap.setVisibility(mCurrentIsMap ? View.VISIBLE : View.INVISIBLE);
        mBinding.mainCollapsingTab.setVisibility(View.GONE);
        mBinding.cardViewFilter.setOnClickListener(mClickFilterListener);

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SearchFilterInterface) {
            mSearchListener = (SearchFilterInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SearchFilterInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSearchListener = null;
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
        mSearchFilter = null;
        mIsFirstLaunch = true;
        mCurrentIsMap = false;
    }

    public void refreshMap(List<EntitySaleyard> list)
    {
        Fragment map =  MapSaleyardFragment.newInstance(mMapLivestockFragmentProvider, list);
        getChildFragmentManager().beginTransaction().replace(R.id.frameLayout_map,map).addToBackStack(null).commit();
    }


    public Boolean isSetFilter(SearchFilterPublicSaleyard filter)
    {
        if (filter == null)
            return false;
        SearchFilterPublicSaleyard emptyFilter = new SearchFilterPublicSaleyard();
        emptyFilter.setType(mSaleyardType);

        emptyFilter.equals(filter);

        if (emptyFilter.equals(filter))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void subscription()
    {
         mMainActivityViewModel.getmLDSearchFilterSaleyard().observe(this, new Observer<SearchFilterPublicSaleyard>() {
            @Override
            public void onChanged(@Nullable SearchFilterPublicSaleyard filter) {

                if (isSetFilter(filter))
                {
                    mBinding.mainCollapsing.setVisibility(View.VISIBLE);
                    if ((filter.getmPlace() != null || filter.getLatLng() != null) && filter.getRadius() != null && filter.getSaleyard_category() != null)
                    {
                        mBinding.tvFilterApplied.setVisibility(View.INVISIBLE);
                        mBinding.linerFilterDetail.setVisibility(View.VISIBLE);
                        ActivityUtils.loadImage(getContext(),mBinding.imageAnimal,ActivityUtils.getImageAbsoluteUrlFromWeb(filter.getSaleyard_category().getPath()),R.drawable.animal_defaul_photo);
                        mBinding.tvAnimal.setText(filter.getSaleyard_category().getName());
                        mBinding.tvAddress.setText(filter.getmPlace() != null ? filter.getmPlace().getAddress() : getString(R.string.txt_current_location));
                        mBinding.tvDistance.setText(getString(R.string.txt_positive_distance_km,Math.round(filter.getRadius())));
                    }
                    else
                    {
                        mBinding.tvFilterApplied.setVisibility(View.VISIBLE);
                        mBinding.linerFilterDetail.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    mBinding.mainCollapsing.setVisibility(View.GONE);
                }


                if (filter.equals(mSearchFilter) && (!mIsFirstLaunch)){
                    return;
                }
                mSearchFilter = filter;
                mIsFirstLaunch = false;

                Boolean  hasSetFilter = false;
                if (mSearchFilter.getSaleyard_category() != null)
                {
                    hasSetFilter = true;
                    mBinding.tvAnimal.setText(mSearchFilter.getSaleyard_category().getName());
                    ActivityUtils.loadImage(getContext(),mBinding.imageAnimal,mSearchFilter.getSaleyard_category().getPath(),R.drawable.animal_defaul_photo);
                }
                else
                {
                    mBinding.tvAnimal.setText(getString(R.string.txt_all));
                    Drawable d = ContextCompat.getDrawable(getContext(),R.drawable.ic_filter);
                    mBinding.imageAnimal.setImageDrawable(d);
                }
                if (mSearchFilter.getmPlace() != null)
                {
                    hasSetFilter = true;
                    mBinding.tvAddress.setText(mSearchFilter.getmPlace().getAddress());
                }
                else
                {
                    mBinding.tvAddress.setText(getString(R.string.txt_all));
                }
                if (mSearchFilter.getRadius() != null)
                {
                    hasSetFilter = true;
                    mBinding.tvDistance.setText(getString(R.string.txt_distance_km, Math.round(mSearchFilter.getRadius())));
                }
                else
                {
                    mBinding.tvDistance.setText(getString(R.string.txt_all));
                }
              //  mBinding.cardViewFilter.setVisibility(hasSetFilter == false ? View.GONE : View.VISIBLE);



                Fragment f =  SaleyardPagerListFragment.newInstance(filter);
                ((SaleyardPagerListFragment) f).setmOnDataChangedListener(new PagerListFragment.OnDataChangedListener() {
                    @Override
                    public void onChanged(ArrayList dataTypeList) {
                        List<EntitySaleyard> advertisementList = new ArrayList<>(dataTypeList);
                        Fragment f = getChildFragmentManager().findFragmentById(R.id.frameLayout_map);
                        if (f instanceof MapLivestockFragment)
                        {
                           // ((MapLivestockFragment)f).refreshData(advertisementList);
                        }
                        else
                        {
                            refreshMap(advertisementList);
                        }
                    }
                });
                getChildFragmentManager().beginTransaction().replace(R.id.frameLayout_list,f).commit();



            }
        });



    }

}
