package com.theherdonline.ui.main.saleyardsearch;

import android.app.DatePickerDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.apache.commons.lang3.BooleanUtils;
import org.joda.time.DateTime;

import java.util.List;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentSearchFilterSaleyardBinding;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.entity.SaleyardCategory;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DatePickerFragment;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.SearchFilterPublicLivestock;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.poddocksearch.AutoLocationInterface;
import com.theherdonline.ui.main.poddocksearch.LocationPermissionListener;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import dagger.Lazy;

public class SaleyardSearchFilterFragment extends HerdFragment {


    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    @Inject
    AppDatabase mDatabase;

    @Inject
    PlacesClient mPlaceClient;

    @Inject
    AppUtil mAppUtil;

    MainActivityViewModel mViewModel;

    AutoLocationInterface mLocationListener;

    private Place mPlace;


    private Boolean mUsingCurrentLocation = false;

    DateTime mStartTime;
    DateTime mEndTime;


    FragmentSearchFilterSaleyardBinding mBinding;
    LocationPermissionListener mGpsLocationListener;
    private LatLng mCurLocation;

    @Inject
    public SaleyardSearchFilterFragment() {
    }

    @Override
    public Boolean getmShowNavigationBar() {
        return false;
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(getString(R.string.txt_filter_saleyard_auctions),
                mExitListener,null,null,null,null);
    }



    public Boolean mIsPrimeFilter = false;


    public void setmIsPrimeFilter(Boolean mIsPrimeFilter) {
        this.mIsPrimeFilter = mIsPrimeFilter;
    }

    public void setCurrentLocation(LatLng latLng)
    {
        SearchFilterPublicSaleyard filterPublicLivestock = mViewModel.getmLDSearchFilterSaleyard().getValue();
        mPlace = null;
        filterPublicLivestock.setmPlace(null);
        filterPublicLivestock.setLatLng(latLng);
        mBinding.tvChooseALocation.setText(getString(R.string.txt_current_location));
        mBinding.tvRadius.setVisibility(View.VISIBLE);
        mBinding.editDistance.setVisibility(View.VISIBLE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_filter_saleyard, container, false);

        if (BooleanUtils.isTrue(mIsPrimeFilter))
        {
            mBinding.frameLayoutType.setVisibility(View.GONE);
            mBinding.tvTypeLabel.setVisibility(View.GONE);
        }

        mBinding.linearUseCurrentLocation.setOnClickListener(l -> {
            final SearchFilterPublicLivestock filterPublicLivestock = mViewModel.getmLDSearchFilterLivestock().getValue();
            mUsingCurrentLocation = true;
            if (mGpsLocationListener.isHasLocationPermission()) {

                LatLng curLocation = mViewModel.getmLiveDataCurrentLocation().getValue();
                if (curLocation != null)
                {
                    setCurrentLocation(curLocation);
                }
                else
                {
                    mBinding.tvUseCurrentLocation.setText(getString(R.string.txt_use_current_location) + " " + getString(R.string.txt_locating));
                }
            } else {
                mGpsLocationListener.requestLocationPermission();
            }
        });



        /*mBinding.linearUseCurrentLocation.setOnClickListener(l->{
            final SearchFilterPublicLivestock filterPublicLivestock = mViewModel.getmLDSearchFilterLivestock().getValue();
            if (mGpsLocationListener.isHasLocationPermission()
                    )
            {
                if (mAppUtil.getmCurrentLocation() != null) {
                    mPlace = null;
                    filterPublicLivestock.setmPlace(null);
                    filterPublicLivestock.setLatLng(mAppUtil.getmCurrentLocation());
                    //mViewModel.setLivestockSearchFilterNew(filterPublicLivestock);
                    mBinding.tvChooseALocation.setText(R.string.txt_current_location);
                    mUseingCurrentLocation = true;
                }
            }
            else
            {
                mGpsLocationListener.requestLocationPermission();
            }
        });*/

        //initSpinner();
        mBinding.buttonFilter.setOnClickListener(l -> {
            SearchFilterPublicSaleyard searchFilter = mViewModel.getmLDSearchFilterSaleyard().getValue();
            SaleyardCategory animalType = (SaleyardCategory) mBinding.spinnerSelectSaleyardAnimal.getSelectedItem();
            if (animalType.getName().equals(getString(R.string.txt_all))) {
                animalType = null;
            }



            String type = (String) mBinding.spinnerSelectSaleyardType.getSelectedItem();
            if (type.equals(getString(R.string.txt_all))) {
                type = null;
            }
            else
            {
                type = type.split(" ")[0];
            }




            if (mStartTime != null && mEndTime != null && mStartTime.isAfter(mEndTime)) {
                Toast.makeText(getContext(), getString(R.string.txt_start_later_than_end), Toast.LENGTH_LONG).show();
                return;
            }


            Float distance = null;

            if (!TextUtils.isEmpty(mBinding.editDistance.getText().toString()))
            {
                distance = Float.valueOf(mBinding.editDistance.getText().toString());
            }

            if (mUsingCurrentLocation == false)
            {
                searchFilter.setLatLng(null);
            }



            SearchFilterPublicSaleyard newFilter = new SearchFilterPublicSaleyard();
            if (mPlace == null)
            {
                newFilter.setmPlace(searchFilter.getmPlace());
            }
            else
            {
                newFilter.setmPlace(mPlace);
            }

          //  newFilter.setmPlace(searchFilter.getmPlace());
            newFilter.setLatLng(searchFilter.getLatLng());
            newFilter.setRadius(distance);
            newFilter.setSaleyard_category(animalType);
            newFilter.setType(type);
            newFilter.setStartDateTime(mStartTime);
            newFilter.setEndDateTime(mEndTime);
            if (!newFilter.equals(searchFilter)) {
                mViewModel.setSaleyardSearchFilterNew(newFilter);
            }
            mGobackListener.OnClickGoBackButton();
        });

        mBinding.editStartTime.setOnClickListener(l -> {
            FragmentManager fragmentManager = getChildFragmentManager();
           /* DateTimePicker newFragment = DateTimePicker.newInstance(new DateTimePicker.InterfaceTimeDate() {
                @Override
                public void setTimeDate(DateTime dateTime) {
                    mStartTime = dateTime;
                    mBinding.editStartTime.setText(ActivityUtils.postDateTimeString(mStartTime));
                }
            },mStartTime);
            newFragment.show(fragmentManager, "dialog");*/

            DialogFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mStartTime = new DateTime(year, month + 1, dayOfMonth, 0, 0);
                    mBinding.editStartTime.setText(ActivityUtils.postDateTimeString(mStartTime, "dd MMM yy"));
                }
            }, mStartTime);
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

        });

        mBinding.editEndTime.setOnClickListener(l -> {
            FragmentManager fragmentManager = getChildFragmentManager();
            /*DateTimePicker newFragment = DateTimePicker.newInstance(new DateTimePicker.InterfaceTimeDate() {
                @Override
                public void setTimeDate(DateTime dateTime) {
                    mEndTime = dateTime;
                    mBinding.editEndTime.setText(ActivityUtils.postDateTimeString(mEndTime));

                }
            },mEndTime);
            newFragment.show(fragmentManager, "dialog");*/

            DialogFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    mEndTime = new DateTime(year, month + 1, dayOfMonth, 0, 0);
                    mBinding.editEndTime.setText(ActivityUtils.postDateTimeString(mEndTime, "dd MMM yy"));
                }
            }, mEndTime);
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
        });

        mBinding.tvChooseALocation.setOnClickListener(l->{
            mLocationListener.updateSaleyardSearchLocation();
        });

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        subscription();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AutoLocationInterface) {
            mLocationListener = (AutoLocationInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AutoLocationInterface");
        }
        if (context instanceof LocationPermissionListener) {
            mGpsLocationListener = (LocationPermissionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LocationPermissionListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.setFilterPlace(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLocationListener = null;
        mGobackListener = null;

    }

    public void initSpinner() {
        // animal
        List<SaleyardCategory> saleyardCategories = mDatabase.saleyardCategoryDao().loadAll();
        ArrayAdapter<SaleyardCategory> animalAdapter = new ArrayAdapter<SaleyardCategory>(
                getContext(), R.layout.item_spinner_dropdown, saleyardCategories);
        animalAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBinding.spinnerSelectSaleyardAnimal.setAdapter(animalAdapter);

        // saleyard type
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.saleyard_type, R.layout.item_spinner_dropdown);
        typeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBinding.spinnerSelectSaleyardType.setAdapter(typeAdapter);

    }


    void subscription() {

        mViewModel.getmLiveDataCurrentLocation().observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(@Nullable LatLng latLng) {
                mCurLocation = latLng;
                if (mUsingCurrentLocation == true)
                {
                    if (latLng != null)
                    {
                        mBinding.tvUseCurrentLocation.setText(getString(R.string.txt_use_current_location));
                        setCurrentLocation(latLng);
                    }
                }
            }
        });

        mViewModel.getmLiveDataIsLocating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (mUsingCurrentLocation == true && aBoolean)
                {
                    mBinding.tvChooseALocation.setText(getString(R.string.txt_locating));
                }
            }
        });




        mViewModel.getmFilterPlaceLivedata().observe(this, new Observer<Place>() {
            @Override
            public void onChanged(@Nullable Place place) {
                if (place != null) {
                    mUsingCurrentLocation = false;
                    mBinding.tvChooseALocation.setText(ActivityUtils.trimTextEmpty(ActivityUtils.formatPlace(place)));
                    mPlace = place;
                }
                mBinding.tvRadius.setVisibility(place == null ? View.GONE : View.VISIBLE);
                mBinding.editDistance.setVisibility(place == null ? View.GONE : View.VISIBLE);

            }
        });

        mViewModel.getmLDSearchFilterSaleyard().observe(this, new Observer<SearchFilterPublicSaleyard>() {
            @Override
            public void onChanged(@Nullable SearchFilterPublicSaleyard searchFilter) {
                if (searchFilter.latLng == null && searchFilter.mPlace == null) {
                    mBinding.tvRadius.setVisibility(View.GONE);
                    mBinding.editDistance.setVisibility(View.GONE);
                } else {
                    mBinding.tvRadius.setVisibility(View.VISIBLE);
                    mBinding.editDistance.setVisibility(View.VISIBLE);
                }



                mStartTime = searchFilter.getStartDateTime();
                mEndTime = searchFilter.getEndDateTime();
                initSpinner();
                ArrayAdapter<SaleyardCategory> adapter = (ArrayAdapter<SaleyardCategory>) mBinding.spinnerSelectSaleyardAnimal.getAdapter();
                if (searchFilter.getSaleyard_category() == null) {
                    mBinding.spinnerSelectSaleyardAnimal.setSelection(0);
                } else {
                    // calculate the index of selected one
                    SaleyardCategory a = searchFilter.getSaleyard_category();
                    int len = adapter.getCount();
                    for (int i = 0; i < len; i++) {
                        if (adapter.getItem(i).equals(a)) {
                            mBinding.spinnerSelectSaleyardAnimal.setSelection(i);
                            break;
                        }
                    }
                }

                ArrayAdapter<String> typeAdapter = (ArrayAdapter<String>) mBinding.spinnerSelectSaleyardType.getAdapter();
                if (searchFilter.getType() == null) {
                    mBinding.spinnerSelectSaleyardType.setSelection(0);
                } else {
                    // calculate the index of selected one
                    String a = searchFilter.getType();
                    int len = typeAdapter.getCount();
                    for (int i = 0; i < len; i++) {
                        String  stringDropdown = typeAdapter.getItem(i);
                        if (stringDropdown.toLowerCase().contains(a.toLowerCase())) {
                            mBinding.spinnerSelectSaleyardType.setSelection(i);
                            break;
                        }
                    }
                }

                if (searchFilter.getStartDateTime() != null) {
                    mBinding.editStartTime.setText(ActivityUtils.postDateTimeString(mStartTime, "yyyy-MM-dd"));
                }

                if (searchFilter.getEndDateTime() != null) {
                    mBinding.editEndTime.setText(ActivityUtils.postDateTimeString(mEndTime, "yyyy-MM-dd"));
                }

                if (searchFilter.getmPlace() != null)
                {
                    mUsingCurrentLocation = false;
                    mBinding.tvChooseALocation.setText(ActivityUtils.trimTextEmpty(ActivityUtils.formatPlace(searchFilter.getmPlace())));
                }else if (searchFilter.getLatLng() != null)
                {
                    // must be using the current location
                    mUsingCurrentLocation = true;
                    mBinding.tvChooseALocation.setText(ActivityUtils.trimTextEmpty(getString(R.string.txt_current_location)));
                }
                else
                {
                    // not yet set
                }

                if (searchFilter.getRadius() != null)
                {
                    mBinding.editDistance.setText(ActivityUtils.trimTextEmpty(String.valueOf(searchFilter.getRadius())));
                }

            }
        });
    }
}
