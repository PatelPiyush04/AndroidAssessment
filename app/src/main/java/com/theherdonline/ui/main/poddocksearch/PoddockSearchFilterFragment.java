package com.theherdonline.ui.main.poddocksearch;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.theherdonline.R;
import com.theherdonline.databinding.FragmentSearchFilterLivestockBinding;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.entity.GenderEntity;
import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.LivestockSubCategory;
import com.theherdonline.db.entity.PregnancyEntity;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.SearchFilterPublicLivestock;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class PoddockSearchFilterFragment extends HerdFragment {


    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    @Inject
    AppDatabase mDatabase;


    @Inject
    PlacesClient mPlaceClient;
    @Inject
    AppUtil mAppUtil;
    MainActivityViewModel mViewModel;
    FragmentSearchFilterLivestockBinding mBinding;
    LocationPermissionListener mGpsLocationListener;
    AutoLocationInterface mLocationListener;
    private Boolean mUsingCurrentLocation = false;
    private Place mPlace;

    private LatLng mCurLocation;


    @Inject
    public PoddockSearchFilterFragment() {
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(getString(R.string.txt_filter_paddock_sales),
                mExitListener, null, null, null, null);
    }

    public void setCurrentLocation(LatLng latLng) {
        SearchFilterPublicLivestock filterPublicLivestock = mViewModel.getmLDSearchFilterLivestock().getValue();
        mPlace = null;
        filterPublicLivestock.setmPlace(null);
        filterPublicLivestock.setLatLng(latLng);
        mBinding.tvChooseALocation.setText(getString(R.string.txt_current_location));
        mBinding.tvRadius.setVisibility(View.VISIBLE);
        mBinding.editDistance.setVisibility(View.VISIBLE);
    }

    @Override
    public Boolean getmShowNavigationBar() {
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_filter_livestock, container, false);

        mBinding.linearUseCurrentLocation.setOnClickListener(l -> {
            final SearchFilterPublicLivestock filterPublicLivestock = mViewModel.getmLDSearchFilterLivestock().getValue();
            mUsingCurrentLocation = true;
            if (mGpsLocationListener.isHasLocationPermission()) {

                LatLng curLocation = mViewModel.getmLiveDataCurrentLocation().getValue();
                if (curLocation != null) {
                    setCurrentLocation(curLocation);
                } else {
                    mBinding.tvUseCurrentLocation.setText(getString(R.string.txt_use_current_location) + " " + getString(R.string.txt_locating));
                }
            } else {
                mGpsLocationListener.requestLocationPermission();
            }
        });




/*        mBinding.spinnerSelectSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sex = (String) parent.getItemAtPosition(position);
                if (sex.toLowerCase().equals(getString(R.string.txt_female).toLowerCase())) {
                    mBinding.tvPregnancy.setVisibility(View.VISIBLE);
                    mBinding.frameLayoutPregnancy.setVisibility(View.VISIBLE);
                } else {
                    mBinding.tvPregnancy.setVisibility(View.GONE);
                    mBinding.frameLayoutPregnancy.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/


        //initSpinner();
        mBinding.buttonFilter.setOnClickListener(l -> {
            GenderEntity genderEntity;
            PregnancyEntity pregnancyEntity;


            SearchFilterPublicLivestock searchFilter = mViewModel.getmLDSearchFilterLivestock().getValue();


            LivestockCategory category = (LivestockCategory) mBinding.spinnerSelectAnimal.getSelectedItem();
            if (category.getName().equals(getString(R.string.txt_all))) {
                category = null;
                genderEntity = null;
                pregnancyEntity = null;
            } else {
                if (mBinding.frameLayoutPregnancy.getVisibility() != View.VISIBLE) {
                    pregnancyEntity = null;
                } else {
                    pregnancyEntity = (PregnancyEntity) mBinding.spinnerPregnancy.getSelectedItem();
                    if (pregnancyEntity.getId() == 0) {
                        // select all
                        pregnancyEntity = null;
                    }
                }


                if (mBinding.frameLayoutGender.getVisibility() != View.VISIBLE) {
                    genderEntity = null;
                } else {
                    genderEntity = (GenderEntity) mBinding.spinnerSelectSex.getSelectedItem();
                    if (genderEntity.getId() == 0) {
                        // select all
                        genderEntity = null;
                    }
                }
            }


            LivestockSubCategory subCategory = (LivestockSubCategory) mBinding.spinnerSelectBreed.getSelectedItem();
            if (subCategory.getName().equals(getString(R.string.txt_all))) {
                subCategory = null;
            }

            String stringMaxPrice = mBinding.editPriceMax.getText().toString();
            String stringMinPrice = mBinding.editPriceMin.getText().toString();
            Integer minPrice = null;
            Integer maxPrice = null;
            if (!StringUtils.isEmpty(stringMinPrice)) {
                minPrice = Math.round(Float.valueOf(stringMinPrice) * 100);
            }
            if (!StringUtils.isEmpty(stringMaxPrice)) {
                maxPrice = Math.round(Float.valueOf(stringMaxPrice) * 100);
            }
            if (minPrice != null && maxPrice != null && maxPrice <= minPrice) {
                Toast.makeText(getContext(), getString(R.string.txt_cannot_more_than,
                        getString(R.string.txt_min) + " " + getString(R.string.txt_price),
                        getString(R.string.txt_max) + " " + getString(R.string.txt_price)), Toast.LENGTH_LONG).show();
                return;
            }
            String stringMinWeight = mBinding.editWeightMin.getText().toString();
            String stringMaxWeight = mBinding.editWeightMax.getText().toString();
            Integer minWeight = null;
            Integer maxWeight = null;
            if (!StringUtils.isEmpty(stringMinWeight)) {
                minWeight = Integer.valueOf(stringMinWeight);

            }
            if (!StringUtils.isEmpty(stringMaxWeight)) {
                maxWeight = Integer.valueOf(stringMaxWeight);
            }
            if (minWeight != null && maxWeight != null && maxWeight <= minWeight) {
                Toast.makeText(getContext(), getString(R.string.txt_cannot_more_than,
                        getString(R.string.txt_min) + " " + getString(R.string.txt_weight),
                        getString(R.string.txt_max) + " " + getString(R.string.txt_weight)), Toast.LENGTH_LONG).show();
                return;
            }

            String stringMinAge = mBinding.editAgeMin.getText().toString();
            String stringMaxAge = mBinding.editAgeMax.getText().toString();
            Integer minAge = null;
            Integer maxAge = null;


            if (!StringUtils.isEmpty(stringMinAge)) {
                minAge = Integer.valueOf(mBinding.editAgeMin.getText().toString());
            }
            if (!StringUtils.isEmpty(stringMaxAge)) {
                maxAge = Integer.valueOf(mBinding.editAgeMax.getText().toString());
            }
            if (minAge != null && maxAge != null && maxAge <= minAge) {
                Toast.makeText(getContext(), getString(R.string.txt_cannot_more_than,
                        getString(R.string.txt_min) + " " + getString(R.string.txt_age),
                        getString(R.string.txt_max) + " " + getString(R.string.txt_age)), Toast.LENGTH_LONG).show();
                return;
            }


            Float distance = null;

            if (!TextUtils.isEmpty(mBinding.editDistance.getText().toString())) {
                distance = Float.valueOf(mBinding.editDistance.getText().toString());
            }

            if (mUsingCurrentLocation == false) {
                searchFilter.setLatLng(null);
            }


            String ageUnit;
            if ((!StringUtils.isEmpty(stringMaxAge)) || (!StringUtils.isEmpty(stringMaxAge))) {
                ageUnit = mBinding.spinnerSelectAgeUnit.getSelectedItem() != null ? (String) mBinding.spinnerSelectAgeUnit.getSelectedItem() : null;
            } else {
                ageUnit = null;
            }

            if (!TextUtils.isEmpty(mBinding.tvChooseALocation.getText()) && TextUtils.isEmpty(mBinding.editDistance.getText().toString().trim())) {
                //Piyush
                Toast.makeText(mMainActivity, "Please enter search radius.", Toast.LENGTH_SHORT).show();
                return;
            }
            SearchFilterPublicLivestock newFilter = new SearchFilterPublicLivestock();
            if (mPlace == null) {
                newFilter.setmPlace(searchFilter.getmPlace());
            } else {
                newFilter.setmPlace(mPlace);
            }

            newFilter.setLatLng(searchFilter.getLatLng());
            newFilter.setRadius(distance);
            newFilter.setLat(searchFilter.getLat());
            newFilter.setLng(searchFilter.getLng());
            //newFilter.setmPlace(searchFilter.getmPlace());
            newFilter.setLivestockCategory(category);
            newFilter.setLivestockSubCategory(subCategory);
            newFilter.setMaxPrice(maxPrice);
            newFilter.setMinPrice(minPrice);
            newFilter.setMaxWeight(maxWeight);
            newFilter.setMinWeight(minWeight);
            newFilter.setMinAge(minAge);
            newFilter.setMaxAge(maxAge);
            newFilter.setGenderEntity(genderEntity);
            newFilter.setPregnancyEntity(pregnancyEntity);
            newFilter.setAgeUnit(ageUnit);
            if (!newFilter.equals(searchFilter)) {
                mViewModel.setLivestockSearchFilterNew(newFilter);
            }
            mGobackListener.OnClickGoBackButton();
        });

        mBinding.tvChooseALocation.setOnClickListener(l -> {
            mLocationListener.updateLivestockSearchLocation();
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
    public void onDetach() {
        super.onDetach();
        mLocationListener = null;
        mGobackListener = null;
    }

    public void initSpinner() {

        // category
        List<LivestockCategory> livestockCategories = mDatabase.livestockCategoryDao().loadAll();
        ArrayAdapter<LivestockCategory> animalAdapter = new ArrayAdapter<LivestockCategory>(
                getContext(), R.layout.item_spinner_dropdown, livestockCategories);
        animalAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBinding.spinnerSelectAnimal.setAdapter(animalAdapter);

        // sub category
        List<LivestockSubCategory> livestockSubCategoryList = mDatabase.livestockSubCategoryDao().loadAll();
        ArrayAdapter<LivestockSubCategory> breedAdapter = new ArrayAdapter<LivestockSubCategory>(
                getContext(), R.layout.item_spinner_dropdown, livestockSubCategoryList);
        breedAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBinding.spinnerSelectBreed.setAdapter(breedAdapter);


        // sub category
        // List<LivestockSubCategory> livestockSubCategoryList = mDatabase.livestockSubCategoryDao().loadAll();
        ArrayAdapter<String> ageUnitAdpter = new ArrayAdapter<String>(
                getContext(), R.layout.item_spinner_dropdown, getResources().getStringArray(R.array.array_age_unit));
        ageUnitAdpter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBinding.spinnerSelectAgeUnit.setAdapter(ageUnitAdpter);
        // mBinding.s.setAdapter(breedAdapter);




/*        // sex spinner
        List<GenderEntity> genderEntityList = mDatabase.genderEntityDao().loadAll();
        ArrayAdapter<GenderEntity> sexAdapter = new ArrayAdapter<>(
                getContext(), R.layout.item_spinner_dropdown, genderEntityList);
        sexAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBinding.spinnerSelectSex.setAdapter(sexAdapter);

        // pregnancy spinner
        List<PregnancyEntity> pregnancyEntityList = mDatabase.pregnancyEntityDao().loadAll();
        ArrayAdapter<PregnancyEntity> pregnancyAdapter = new ArrayAdapter<>(
                getContext(), R.layout.item_spinner_dropdown, pregnancyEntityList);
        pregnancyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mBinding.spinnerPregnancy.setAdapter(pregnancyAdapter);*/

    }


    public void setGenderSpinner(LivestockCategory item, GenderEntity entity) {

        if (item == null || item.getId() == 0 || null == mDatabase.genderEntityDao().isExisted(item.getId())) {
            // not set, or set all, or no data
            mBinding.tvGender.setVisibility(View.GONE);
            mBinding.frameLayoutGender.setVisibility(View.GONE);
        } else {
            mBinding.tvGender.setVisibility(View.VISIBLE);
            mBinding.frameLayoutGender.setVisibility(View.VISIBLE);
            Integer categoryID = item.getId();
            List<GenderEntity> genderEntityList = mDatabase.genderEntityDao().loadAllByCategory(categoryID);
            ArrayAdapter<GenderEntity> sexAdapter = new ArrayAdapter<>(
                    getContext(), R.layout.item_spinner_dropdown, genderEntityList);
            sexAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mBinding.spinnerSelectSex.setAdapter(sexAdapter);

            if (entity == null) {
                mBinding.spinnerSelectSex.setSelection(0);
            } else {
                mBinding.spinnerSelectSex.setSelection(0);
                for (int index = 0; index < genderEntityList.size(); index++) {
                    if (genderEntityList.get(index).getId() == entity.getId()) {
                        mBinding.spinnerSelectSex.setSelection(index);
                        break;
                    }
                }
            }

        }
    }

    public void setPregnancySpinner(LivestockCategory item, PregnancyEntity entity) {

        if (item == null || item.getId() == 0 || null == mDatabase.pregnancyEntityDao().isExisted(item.getId())) {
            // not set, or set all, or no data
            mBinding.tvPregnancy.setVisibility(View.GONE);
            mBinding.frameLayoutPregnancy.setVisibility(View.GONE);
        } else {
            mBinding.tvPregnancy.setVisibility(View.VISIBLE);
            mBinding.frameLayoutPregnancy.setVisibility(View.VISIBLE);
            Integer categoryID = item.getId();
            List<PregnancyEntity> pregnancyEntityList = mDatabase.pregnancyEntityDao().loadAllByCategory(categoryID);
            ArrayAdapter<PregnancyEntity> pregnancyAdapter = new ArrayAdapter<>(
                    getContext(), R.layout.item_spinner_dropdown, pregnancyEntityList);
            pregnancyAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            mBinding.spinnerPregnancy.setAdapter(pregnancyAdapter);
            if (entity == null) {
                mBinding.spinnerPregnancy.setSelection(0);
            } else {
                mBinding.spinnerPregnancy.setSelection(0);
                for (int index = 0; index < pregnancyEntityList.size(); index++) {
                    if (pregnancyEntityList.get(index).getId() == entity.getId()) {
                        mBinding.spinnerPregnancy.setSelection(index);
                        break;
                    }
                }

            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.setFilterPlace(null);
    }

    void subscription() {


        mViewModel.getmLiveDataCurrentLocation().observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(@Nullable LatLng latLng) {
                mCurLocation = latLng;
                if (mUsingCurrentLocation == true) {
                    if (latLng != null) {
                        mBinding.tvUseCurrentLocation.setText(getString(R.string.txt_use_current_location));
                        setCurrentLocation(latLng);
                    }
                }
            }
        });

        mViewModel.getmLiveDataIsLocating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (mUsingCurrentLocation == true && aBoolean) {
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

        mViewModel.getmLDSearchFilterLivestock().observe(this, new Observer<SearchFilterPublicLivestock>() {
            @Override
            public void onChanged(@Nullable SearchFilterPublicLivestock searchFilter) {

                if (searchFilter.latLng == null && searchFilter.mPlace == null) {
                    mBinding.tvRadius.setVisibility(View.GONE);
                    mBinding.editDistance.setVisibility(View.GONE);
                } else {
                    mBinding.tvRadius.setVisibility(View.VISIBLE);
                    mBinding.editDistance.setVisibility(View.VISIBLE);
                }

                initSpinner();


                // Livestock Category
                ArrayAdapter<LivestockCategory> adapter = (ArrayAdapter<LivestockCategory>) mBinding.spinnerSelectAnimal.getAdapter();
                if (searchFilter.livestockCategory != null) {
                    LivestockCategory a = searchFilter.livestockCategory;
                    int len = adapter.getCount();
                    for (int i = 0; i < len; i++) {
                        if (adapter.getItem(i).equals(a)) {
                            mBinding.spinnerSelectAnimal.setSelection(i);
                            break;
                        }
                    }
                } else {
                    mBinding.spinnerSelectAnimal.setSelection(0);
                }

                setGenderSpinner(searchFilter.livestockCategory, searchFilter.genderEntity);
                setPregnancySpinner(searchFilter.livestockCategory, searchFilter.pregnancyEntity);


                // Livestock Subcategory
                ArrayAdapter<LivestockSubCategory> adapterBreed = (ArrayAdapter<LivestockSubCategory>) mBinding.spinnerSelectBreed.getAdapter();
                if (searchFilter.livestockSubCategory != null) {
                    LivestockSubCategory a = searchFilter.livestockSubCategory;
                    int len = adapterBreed.getCount();
                    for (int i = 0; i < len; i++) {
                        if (adapterBreed.getItem(i).equals(a)) {
                            mBinding.spinnerSelectBreed.setSelection(i);
                            break;
                        }
                    }
                } else {
                    mBinding.spinnerSelectBreed.setSelection(0);
                }


/*                // AgeUnit
                ArrayAdapter<String> adapterAgeUnit = (ArrayAdapter<String>) mBinding.spinnerSelectAgeUnit.getAdapter();
                if (searchFilter.ageUnit != null) {
                    String a = searchFilter.ageUnit;
                    int len = adapterAgeUnit.getCount();
                    for (int i = 0; i < len; i++) {
                        if (adapterAgeUnit.getItem(i).equals(a)) {
                            mBinding.spinnerSelectAgeUnit.setSelection(i);
                            break;
                        }
                    }
                } else {
                    mBinding.spinnerSelectAgeUnit.setSelection(0);
                }*/


                /*// Gender
                ArrayAdapter<GenderEntity> adapterSex = (ArrayAdapter<GenderEntity>) mBinding.spinnerSelectSex.getAdapter();
                if (searchFilter.genderEntity != null) {
                    GenderEntity a = searchFilter.genderEntity;
                    int len = adapterSex.getCount();
                    for (int i = 0; i < len; i++) {
                        if (adapterSex.getItem(i).equals(a)) {
                            mBinding.spinnerSelectSex.setSelection(i);
                            break;
                        }
                    }
                } else {
                    mBinding.spinnerSelectSex.setSelection(0);
                }

                // Pregnancy status
                ArrayAdapter<PregnancyEntity> adapterPregnancy = (ArrayAdapter<PregnancyEntity>) mBinding.spinnerPregnancy.getAdapter();
                if (searchFilter.pregnancyEntity != null) {
                    PregnancyEntity a = searchFilter.pregnancyEntity;
                    int len = adapterPregnancy.getCount();
                    for (int i = 0; i < len; i++) {
                        if (adapterPregnancy.getItem(i).equals(a)) {
                            mBinding.spinnerPregnancy.setSelection(i);
                            break;
                        }
                    }
                } else {
                    mBinding.spinnerPregnancy.setSelection(0);
                }*/


                mBinding.spinnerSelectAnimal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        LivestockCategory livestockCategory = (LivestockCategory) parent.getItemAtPosition(position);
                        // sub category
                        List<LivestockSubCategory> livestockSubCategoryList;
                        if (position != 0) {
                            livestockSubCategoryList = mDatabase.livestockSubCategoryDao().loadSpecificArrayExtend(livestockCategory.getId());
                        } else {
                            livestockSubCategoryList = mDatabase.livestockSubCategoryDao().loadAll();
                        }
                        ArrayAdapter<LivestockSubCategory> breedAdapter = new ArrayAdapter<LivestockSubCategory>(
                                getContext(), R.layout.item_spinner_dropdown, livestockSubCategoryList);
                        breedAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                        mBinding.spinnerSelectBreed.setAdapter(breedAdapter);
                        mBinding.spinnerSelectBreed.setSelection(0);
                        // set gender spinner, and set pregnancy spinner
                        setGenderSpinner(livestockCategory, (GenderEntity) mBinding.spinnerSelectSex.getSelectedItem());
                        setPregnancySpinner(livestockCategory, (PregnancyEntity) mBinding.spinnerPregnancy.getSelectedItem());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


                if (searchFilter.minPrice != null) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    String s = df.format(searchFilter.minPrice / 100.0f);
                    mBinding.editPriceMin.setText(s);
                }
                if (searchFilter.maxPrice != null) {
                    DecimalFormat df = new DecimalFormat("0.00");
                    String s = df.format(searchFilter.maxPrice / 100.0f);
                    mBinding.editPriceMax.setText(s);
                }

                if (searchFilter.maxWeight != null) {
                    mBinding.editWeightMax.setText(String.valueOf(searchFilter.maxWeight));
                }

                if (searchFilter.minWeight != null) {
                    mBinding.editWeightMin.setText(String.valueOf(searchFilter.minWeight));
                }

                if (searchFilter.minAge != null) {
                    mBinding.editAgeMin.setText(String.valueOf(searchFilter.minAge));
                }

                if (searchFilter.maxAge != null) {
                    mBinding.editAgeMax.setText(String.valueOf(searchFilter.maxAge));
                }
                if (searchFilter.getmPlace() != null) {
                    // must be using the auto complete location
                    mUsingCurrentLocation = false;
                    mBinding.tvChooseALocation.setText(ActivityUtils.trimTextEmpty(ActivityUtils.formatPlace(searchFilter.getmPlace())));
                } else if (searchFilter.getLatLng() != null) {
                    // must be using the current location
                    mUsingCurrentLocation = true;
                    mBinding.tvChooseALocation.setText(ActivityUtils.trimTextEmpty(getString(R.string.txt_current_location)));
                } else {
                    // not yet set
                }

                if (searchFilter.getRadius() != null) {
                    mBinding.editDistance.setText(ActivityUtils.trimTextEmpty(String.valueOf(searchFilter.getRadius())));
                }

            }
        });
    }
}
