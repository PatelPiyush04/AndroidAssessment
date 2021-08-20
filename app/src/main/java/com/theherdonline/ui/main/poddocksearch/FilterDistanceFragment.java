package com.theherdonline.ui.main.poddocksearch;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentSearchDistanceBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.general.SearchFilterPublicLivestock;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.saleyardsearch.SearchResultSaleyardFragment;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import dagger.Lazy;


public class FilterDistanceFragment extends HerdFragment {

    private static final String TAG = FilterDistanceFragment.class.getName();
    public static String ARG_SEARCH_TYPE = "data-search-type";
    public static String ARG_SEARCH_TYPE_SEALYARD = "saleyard-search";
    public static String ARG_SEARCH_TYPE_PODDOCK = "poddock-search";
    FragmentSearchDistanceBinding mBinding;
    SearchFilter mSearchFilter;
    LocationPermissionListener mLocationPermissionListener;
    @Inject
    PlacesClient mPlaceClient;

    @Inject
    AppUtil mAppUtil;

    @Inject
    Lazy<SearchResultSaleyardFragment> mSearchResultSaleyardFragmentProvider;

    @Inject
    Lazy<SearchLivestockResultFragment> mSearchLivestockResultFragmentProvider;


    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    MainActivityViewModel mViewModel;

    private String mSearchType;

    private Boolean mIsForLivestock = true;
    private LatLng mLatlng = null;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).showDismissProgressDialogFragment(false);
            }
            if (mAppUtil.getmCurrentLocation() == null && location != null) {
                double longval = location.getLongitude();
                double latval = location.getLatitude();
                mAppUtil.setmCurrentLocation(new LatLng(latval, longval));
                startSearch();
            } else {
                double longval = location.getLongitude();
                double latval = location.getLatitude();
                mAppUtil.setmCurrentLocation(new LatLng(latval, longval));
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).showDismissProgressDialogFragment(false);
            }

            if (provider != null) {

            }

        }

        @Override
        public void onProviderEnabled(String provider) {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).showDismissProgressDialogFragment(false);
            }
            if (provider != null) {

            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (getActivity() != null) {
                ((MainActivity) getActivity()).showDismissProgressDialogFragment(false);

            }
            if (provider != null) {

            }
        }
    };

    @Inject
    public FilterDistanceFragment() {
    }

    public static FilterDistanceFragment newInstance(Lazy<FilterDistanceFragment> lazy, String searchType) {
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_TYPE, searchType);
        FilterDistanceFragment fragment = lazy.get();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_distance, container, false);
        if (getArguments() != null) {
            mSearchType = getArguments().getString(ARG_SEARCH_TYPE);
            mIsForLivestock = ARG_SEARCH_TYPE_PODDOCK.equals(mSearchType);
        }
        mBinding.imageViewBackbutton.setOnClickListener(l -> {
            mGobackListener.OnClickGoBackButton();
        });

        mBinding.buttonSearch.setOnClickListener(l -> {
            if (mAppUtil.getmCurrentLocation() == null && mLatlng == null) {

                /*ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_setting_location),
                        getString(R.string.txt_skip),
                        getString(R.string.txt_allow),
                        (ll, a) -> {
                            startSearch();
                        },
                        (ll, a) -> {
                            if (mLocationPermissionListener.isHasLocationPermission()) {
                                if (mAppUtil.getmCurrentLocation() == null) {
                                    ((MainActivity) getActivity()).showDismissProgressDialogFragment(true, getString(R.string.txt_locating));
                                    mLocationPermissionListener.requestLocation(mLocationListener);
                                } else {
                                    startSearch();
                                }
                            } else {
                                mLocationPermissionListener.requestLocationPermission();
                            }
                        });*/
                startSearch();
            } else {
                startSearch();
            }
        });

        mBinding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBinding.tvDistance.setText(getString(R.string.txt_distance_km, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (mSearchFilter == null)
        {
            mBinding.seekBar.setProgress(mBinding.seekBar.getMax());
        }
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                // mBinding.
                //mBinding.tvAddress.setText("Place: " + place.getName() + ", " + place.getId() + "LAT:" + place.getLatLng().latitude + "LNG:" + place.getLatLng().longitude);
                //  Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                mLatlng = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                // mBinding.tvAddress.setText("An error occurred: " + status);
                Toast.makeText(getContext(), "An error occurred: " + status, Toast.LENGTH_LONG).show();
                ;
                mLatlng = null;
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        return mBinding.getRoot();
    }

    public void startSearch() {
        LatLng currentLocation = mLatlng == null ? mAppUtil.getmCurrentLocation() : mLatlng;
        if (mIsForLivestock) {
            SearchFilterPublicLivestock newSearchFilter = new SearchFilterPublicLivestock();
            if (mBinding.seekBar.getProgress() != mBinding.seekBar.getMax()) {
                newSearchFilter.setRadius((float) mBinding.seekBar.getProgress());
            }
            newSearchFilter.setLatLng(currentLocation);
            mViewModel.setLivestockSearchFilterNew(newSearchFilter);
            ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(), mSearchLivestockResultFragmentProvider.get(), R.id.frameLayout_container);
        } else {
            SearchFilterPublicSaleyard newSearchFilter = new SearchFilterPublicSaleyard();
            newSearchFilter.setLatLng(currentLocation);
            if (mBinding.seekBar.getProgress() != mBinding.seekBar.getMax()) {
                newSearchFilter.setRadius((float) mBinding.seekBar.getProgress());
            }
            mViewModel.setSaleyardSearchFilterNew(newSearchFilter);
            ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(), mSearchResultSaleyardFragmentProvider.get(), R.id.frameLayout_container);

        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        subscription();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.setSaleyardSearchFilterNew(null);
        mViewModel.setLivestockSearchFilterNew(null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void subscription() {
        if (mIsForLivestock) {
            mViewModel.getmLDSearchFilterLivestock().observe(this, new Observer<SearchFilterPublicLivestock>() {
                @Override
                public void onChanged(@Nullable SearchFilterPublicLivestock searchFilter) {
                    mSearchFilter = searchFilter;
                    if (searchFilter != null && searchFilter.getRadius() != null) {
                        mBinding.seekBar.setProgress(Math.round(searchFilter.getRadius()));
                    } else {
                        mBinding.seekBar.setProgress(mBinding.seekBar.getMax());
                    }
                }
            });
        } else {
            mViewModel.getmLDSearchFilterSaleyard().observe(this, new Observer<SearchFilterPublicSaleyard>() {
                @Override
                public void onChanged(@Nullable SearchFilterPublicSaleyard searchFilterSaleyard) {
                    mSearchFilter = searchFilterSaleyard;
                    if (searchFilterSaleyard != null && searchFilterSaleyard.getRadius() != null) {
                        mBinding.seekBar.setProgress(Math.round(searchFilterSaleyard.getRadius()));
                    } else {
                        mBinding.seekBar.setProgress(mBinding.seekBar.getMax());
                    }
                }
            });
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LocationPermissionListener) {
            mLocationPermissionListener = (LocationPermissionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LocationPermissionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLocationPermissionListener = null;
    }


}
