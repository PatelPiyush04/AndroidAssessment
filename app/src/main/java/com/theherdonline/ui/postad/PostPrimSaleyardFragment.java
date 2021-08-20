package com.theherdonline.ui.postad;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentPostPrimeSaleyardBinding;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.SaleyardCategory;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.PickupSaleyardCategoryDialogFragment;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.poddocksearch.AutoLocationInterface;
import com.theherdonline.ui.makebid.BidDetailFragment;
import com.theherdonline.ui.view.DateTimePicker;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UIUtils;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.Lazy;

public class PostPrimSaleyardFragment extends HerdFragment
        implements PickupSaleyardCategoryDialogFragment.OnSelectedItemListener
        {


    public static String AGR_DATA = "data-livestock";
    public static String AGR_MODE = "data-mode";
    public static Integer AGR_MODE_NEW = 1;
    public static Integer AGR_MODE_UPDATE = 2;
    private Integer mUiMode = AGR_MODE_NEW;
    public Integer[] mDurationList = {1, 2, 3, 4, 5, 6, 7};
    @Inject
    PlacesClient mPlaceClient;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    @Inject
    Lazy<BidDetailFragment> mBidDetailFragmentProvider;
    @Inject
    AppUtil mAppUtil;
    @Inject
    AppDatabase mDatabase;
    SaleyardInterface mListener;
    FragmentPostPrimeSaleyardBinding mBinding;

    MainActivityViewModel mMainActivityViewModel;

    LivestockInterface mLivestockInterface;

    Boolean mIsAgent = false;
    MainActivityViewModel mViewModel;
    AutoLocationInterface mLocationListener;
    private EntitySaleyard mEntitySaleyard = null;
    private EntitySaleyard mOriginalEntitySaleyard = null;

    private Place mPlace;
    private DateTime mAvailableTime;
    private List<SaleyardCategory> mSaleyardCategoryList;

    private SaleyardCategory mSelectSaleyardCategory;
    private List<Media> mMediaListToDelete = new ArrayList<>();



    private String mAttachDrawImage = null;



    @Inject
    public PostPrimSaleyardFragment() {
    }

    public static PostPrimSaleyardFragment newInstance(Lazy<PostPrimSaleyardFragment> lazy, EntitySaleyard entitySaleyard) {
        Bundle args = new Bundle();
        PostPrimSaleyardFragment fragment = lazy.get();
        args.putParcelable(AGR_DATA, entitySaleyard == null ? new EntitySaleyard() : entitySaleyard);
        args.putInt(AGR_MODE, entitySaleyard == null ? AGR_MODE_NEW : AGR_MODE_UPDATE);
        fragment.setArguments(args);
        return fragment;
    }

    public void setmAppUtil(AppUtil mAppUtil) {
        this.mAppUtil = mAppUtil;
    }

    public void setmLazyFactory(Lazy<ViewModelFactory> mLazyFactory) {
        this.mLazyFactory = mLazyFactory;
    }


    @Override
    public Boolean getmShowNavigationBar() {
        return false;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_prime_saleyard, container, false);
        if (getArguments() != null) {
            mUiMode = getArguments().getInt(AGR_MODE);
            mEntitySaleyard = getArguments().getParcelable(AGR_DATA);
            if (mUiMode == AGR_MODE_UPDATE)
            {

            }
            else
            {

            }
        }


        mBinding.list.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mSaleyardCategoryList = mDatabase.saleyardCategoryDao().loadAllExceptAll();
        if (mSaleyardCategoryList != null && mSaleyardCategoryList.size() > 0) {
            SaleyardCategory livestockCategory = mBinding.tvSpinnerCategory.getTag() == null ? mSaleyardCategoryList.get(0) : (SaleyardCategory) mBinding.tvSpinnerCategory.getTag();
            mBinding.tvSpinnerCategory.setText(livestockCategory.getName());
            mBinding.tvSpinnerCategory.setTag(livestockCategory);
        }

        mBinding.tvSpinnerCategory.setOnClickListener(l -> {
            if (mSaleyardCategoryList != null && mSaleyardCategoryList.size() > 0) {
                PickupSaleyardCategoryDialogFragment fragment = PickupSaleyardCategoryDialogFragment.newInstance("add", mSaleyardCategoryList, PostPrimSaleyardFragment.this);
                fragment.show(getChildFragmentManager(), "photoh");
            }
        });



        mBinding.tvStartDate.setOnClickListener(l -> {

            DialogFragment newFragment = DateTimePicker.newInstance(new DateTimePicker.InterfaceTimeDate() {
                public void setTimeDate(DateTime dateTime) {
                    mAvailableTime = dateTime;
                    mBinding.tvStartDate.setTag(TimeUtils.time2ApiString(mAvailableTime));
                    mBinding.tvStartDate.setText(TimeUtils.time2String(mAvailableTime));

                }
            },new DateTime());
            newFragment.show(getActivity().getSupportFragmentManager(),"datePicker");
        });


        mBinding.tvChooseALocation.setOnClickListener(l -> {
            mLocationListener.updateCreateLivestockLocation();
        });



        mBinding.imageDeleteDraw.setOnClickListener(l->{
            ActivityUtils.deleteFile(mAttachDrawImage);
            mBinding.imageDrawPicture.setVisibility(View.GONE);
            mBinding.linearDrawButton.setVisibility(View.VISIBLE);
            mBinding.imageDeleteDraw.setVisibility(View.GONE);
            mViewModel.upDatePostSaleyardPdfPhoto(null);
            mAttachDrawImage = null;
        });

        mBinding.linearDrawButton.setOnClickListener(l->{
            mMainActivity.popupPhotoDialog(getString(R.string.txt_choose_attach_image),AppConstants.ACTIVITY_SALEYEARD_POST_PDF_PHOTO_TAKE);
        });






        mBinding.buttonConfirm.setOnClickListener(l -> {
            EntitySaleyard newLivestock = updateLivestock();
            if (newLivestock == null) {
                return;
            } else {
                EntitySaleyard entitySaleyard = getArguments().getParcelable(AGR_DATA);
                newLivestock.setId(entitySaleyard != null ? entitySaleyard.getId() : null);
                newLivestock.setType(AppConstants.TAG_SALEYARD_TYPE_PRIME);
                mViewModel.updatePostSaleyard(newLivestock);
                if (newLivestock.getId() == null)
                {
                    mListener.onConfirmSaleyardV2(newLivestock,null);
                }
                else
                {
                    mListener.onConfirmSaleyardV2(newLivestock, entitySaleyard);
                }
            }
        });

        return mBinding.getRoot();
    }




    public void initUI(EntitySaleyard entitySaleyard) {
        mBinding.editTitle.setText(ActivityUtils.trimTextEmpty(entitySaleyard.getName()));


        if (entitySaleyard.getStartsAt() != null)
        {
            mBinding.tvStartDate.setText(TimeUtils.BackendUTC2Local(entitySaleyard.getStartsAt()));
            mBinding.tvStartDate.setTag(entitySaleyard.getStartsAt());
        }


        // Location
        mPlace = entitySaleyard.getmPlace();
        if (mPlace != null) {
            mBinding.tvChooseALocation.setText(mPlace.getAddress());
        }
        else
        {
            mBinding.tvChooseALocation.setText(StringUtils.defaultString(entitySaleyard.getAddress()));
        }
        mBinding.editTotalHead.setText(StringUtils.defaultString(entitySaleyard.getHeadcount()!= null ? entitySaleyard.getHeadcount().toString() : ""));




        // entitySaleyard Category
        SaleyardCategory category = null;
        if (entitySaleyard.getSaleyardCategory() == null && entitySaleyard.getSaleyardCategoryId() != null) {
            category = mDatabase.saleyardCategoryDao().getItemById(entitySaleyard.getSaleyardCategoryId());
        }
        else
        {
            category = entitySaleyard.getSaleyardCategory();
        }
        mBinding.tvSpinnerCategory.setTag(category);

        mBinding.tvSpinnerCategory.setText(category == null ? "" : category.getName());




        mBinding.editSaleNumber.setText(StringUtils.defaultString(entitySaleyard.getSale_numbers()));



        HashMap<String, List<Media>> classifyMedias = UIUtils.classifyMedias(ListUtils.emptyIfNull(entitySaleyard.getMedia()));

        try {
            mAttachDrawImage = classifyMedias.get(Media.CoolectionName.pdfs.name()).get(0).getUrl();
        }
        catch (Exception e)
        {
            mAttachDrawImage = null;
        }

        if (mAttachDrawImage == null)
        {
            if (mUiMode == AGR_MODE_UPDATE)
            {
                mBinding.cardDrawImage.setVisibility(View.VISIBLE);
                mBinding.linearDrawButton.setVisibility(View.VISIBLE);

            }
            else {
                mBinding.cardDrawImage.setVisibility(View.VISIBLE);
                mBinding.linearDrawButton.setVisibility(View.VISIBLE);

            }
            mBinding.imageDeleteDraw.setVisibility(View.INVISIBLE);
        }
        else
        {
            mBinding.cardDrawImage.setVisibility(View.VISIBLE);
            if (mUiMode == AGR_MODE_UPDATE)
            {
                mBinding.imageDeleteDraw.setVisibility(View.VISIBLE);
                mBinding.linearDrawButton.setVisibility(View.GONE);
            }
            else
            {
                mBinding.imageDeleteDraw.setVisibility(View.INVISIBLE);
                mBinding.linearDrawButton.setVisibility(View.GONE);

            }
            ActivityUtils.loadImage(getContext(),mBinding.imageDrawPicture,mAttachDrawImage,null);
        }

       //     mBinding.cartNote.setVisibility(StringUtils.isEmpty(entitySaleyard.getDescription()) ? View.GONE : View.VISIBLE);
            mBinding.editNotes.setText(StringUtils.defaultString(entitySaleyard.getDescription()));
        //}



        // Initialize the Media of delete list
        mMediaListToDelete.addAll(ListUtils.emptyIfNull(classifyMedias.get(Media.CoolectionName.others.name())));
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {

        return new CustomerToolbar(getString( mEntitySaleyard == null ? R.string.txt_create_ad : R.string.txt_edit_ad), mExitListener,
                null, null,
                null, null);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        if (mViewModel.getmLiveDataPostSaleyard().getValue() == null) {
            mViewModel.updatePostSaleyard(mEntitySaleyard);
        }
        subscription();
    }

    public void subscription() {

        mViewModel.getmLiveDataPostSaleyardPhoto().observe(this,url->{
            if (url != null)
            {
                ActivityUtils.loadImage(getContext(),mBinding.imageDrawPicture, url,null);
                mBinding.linearDrawButton.setVisibility(View.GONE);
                mBinding.imageDrawPicture.setVisibility(View.VISIBLE);
                mBinding.imageDeleteDraw.setVisibility(View.VISIBLE);
            }
            else
            {
                mBinding.imageDeleteDraw.setVisibility(View.GONE);
                mBinding.imageDrawPicture.setVisibility(View.GONE);
                mBinding.linearDrawButton.setVisibility(View.VISIBLE);

            }
        });

        mViewModel.getmLiveDataSaleyardMediaList().observe(this, new Observer<List<Media>>() {
            @Override
            public void onChanged(@Nullable List<Media> mediaList) {
                if (mediaList != null) {
                    if (mBinding.list.getAdapter() != null) {
                        mBinding.list.getAdapter().notifyDataSetChanged();
                    } else {
                        mBinding.list.setAdapter(new PhotoSaleyardRecyclerViewAdapter((MainActivity) getActivity(), mediaList, new PhotoSaleyardRecyclerViewAdapter.OnClickPhotoInterface() {
                            @Override
                            public void deletePhoto(Media media) {
                                mViewModel.removeSaleyardListPhoto(media);
                            }

                            @Override
                            public void setPrimaryImage(Media media) {
                                mViewModel.setPrimarySaleyardPhoto(media);
                            }
                        }));
                    }
                }
            }
        });

        mViewModel.getmLDPostLivestockLocation().observe(this, new Observer<Place>() {
            @Override
            public void onChanged(@Nullable Place place) {
                mPlace = place;
                if (place != null) {
                    mBinding.tvChooseALocation.setText(place.getAddress());
                } else {
                    mBinding.tvChooseALocation.setText("");
                }
            }
        });

        mViewModel.getmLiveDataPostSaleyard().observe(this, new Observer<EntitySaleyard>() {
            @Override
            public void onChanged(@Nullable EntitySaleyard entitySaleyard) {


                if (entitySaleyard == null)
                {
                    return;
                }

                List<Media> mediaListPdf = new ArrayList<>();
                List<Media> mediaListOthers = new ArrayList<>();


                for(Media media : ListUtils.emptyIfNull(entitySaleyard.getMedia()))
                {
                    if (AppUtil.isPdf(media))
                    {
                        mediaListPdf.add(media);
                    }
                    else
                    {
                        mediaListOthers.add(media);
                    }
                }


                mViewModel.setMediaListSaleyard(ListUtils.emptyIfNull(mediaListOthers));
                if (mediaListPdf.size() == 0)
                {
                    mViewModel.upDatePostSaleyardPdfPhoto(null);
                }
                else {
                    mViewModel.upDatePostSaleyardPdfPhoto(mediaListPdf.get(0).getUrl());
                }








                if (entitySaleyard != null) {
                    initUI(entitySaleyard);
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SaleyardInterface) {
            mListener = (SaleyardInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof AutoLocationInterface) {
            mLocationListener = (AutoLocationInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AutoLocationInterface");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mLocationListener = null;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.updatePostLivestocks(new EntityLivestock());
        // Delete all cached  photo file

        for (Media media : ListUtils.emptyIfNull(mViewModel.getmLiveDataMediaList().getValue())) {
            AppUtil.deleteFile(media.getUrl());
        }
        mViewModel.setMediaListSaleyard(null);
        mViewModel.clearDeleteMediaListSaleyard();
        mViewModel.upDatePostSaleyardPdfPhoto(null);
        mViewModel.updatePostSaleyard(null);
        mMediaListToDelete.clear();
        mEntitySaleyard = null;
    }

    public EntitySaleyard updateLivestock() {
        EntitySaleyard newLivestock = new EntitySaleyard();
        String title = mBinding.editTitle.getText().toString();
        String des = mBinding.editNotes.getText().toString();
        if (!ActivityUtils.checkInput(getContext(), title, getString(R.string.txt_title))) {
            return null;
        }



        newLivestock.setName(title);
        newLivestock.setDescription(StringUtils.isEmpty(des) ? null : des);

        // check start time
        String strAvailableDate = mBinding.tvStartDate.getText().toString();
        if (!StringUtils.isEmpty(strAvailableDate)) {
            newLivestock.setStartsAt((String) mBinding.tvStartDate.getTag());
        }
        if (!ActivityUtils.checkInput(getContext(), strAvailableDate, getString(R.string.txt_start_date))) {
            return null;
        }



        try {
            newLivestock.setQuantity(Integer.valueOf(mBinding.editTotalHead.getText().toString()));

        }
        catch (Exception e)
        {
            newLivestock.setQuantity(null);
        }


        if (mPlace != null) {
            newLivestock.setmPlace(mPlace);
            newLivestock.setAddress(mPlace.getAddress());
            newLivestock.setAddressLineOne(mPlace.getAddress());
            if (mPlace.getLatLng() != null) {
                newLivestock.setLat(mPlace.getLatLng().latitude);
                newLivestock.setLng(mPlace.getLatLng().longitude);
            }
            Geocoder mGeocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = mGeocoder.getFromLocation(mPlace.getLatLng().latitude, mPlace.getLatLng().longitude, 1);
                if (addresses.get(0).getPostalCode() != null) {
                    String zipCode = addresses.get(0).getPostalCode();
                    Log.d(TAG, "updateLivestock: " + zipCode);
                    if (zipCode != null) {
                        newLivestock.setAddressPostcode(zipCode);
                    }
                }

                if (addresses.get(0).getLocality() != null) {
                    String city = addresses.get(0).getLocality();
                    if (city != null) {
                        newLivestock.setAddressCity(city);
                    }
                }

                if (addresses.get(0).getAdminArea() != null) {
                    String state = addresses.get(0).getAdminArea();
                    state = "";
                }

                if (addresses.get(0).getCountryName() != null) {
                    String country = addresses.get(0).getCountryName();

                }
            } catch (Exception e) {

            }
        } else {
            if (mEntitySaleyard != null) {
                newLivestock.setAddress(mEntitySaleyard.getAddress());
                newLivestock.setLat(mEntitySaleyard.getLat());
                newLivestock.setLng(mEntitySaleyard.getLng());
                newLivestock.setAddressPostcode(mEntitySaleyard.getAddressPostcode());
                newLivestock.setAddressLineOne(mEntitySaleyard.getAddressLineOne());
                newLivestock.setAddressLineTwo(mEntitySaleyard.getAddressLineTwo());
            }
        }





        try {
            newLivestock.setHeadcount(Integer.valueOf(mBinding.editTotalHead.getText().toString()));
        }
        catch (NumberFormatException e)
        {
            newLivestock.setHeadcount(null);

        }


        String strAnimal = mBinding.tvSpinnerCategory.getText().toString();
        if (!ActivityUtils.checkInput(getContext(), strAnimal, getString(R.string.txt_saleyard_category))) {
            return null;
        }
        newLivestock.setSaleyardCategoryId(((SaleyardCategory) mBinding.tvSpinnerCategory.getTag()).getId());

        newLivestock.setSale_numbers(StringUtils.defaultString(mBinding.editSaleNumber.getText().toString()));



        List<Media> newSaleyardMediaList = new ArrayList<>();

        List<Media> mediaList = mViewModel.getmLiveDataSaleyardMediaList().getValue();
        if (mediaList != null) {
            //newLivestock.setMedia(mediaList);
            newSaleyardMediaList.addAll(mediaList);
        }

        if (mViewModel.getmLiveDataPostSaleyardPhoto().getValue() != null)
        {
            Media media = new Media();
            media.setCollection_name(Media.CoolectionName.pdfs.toString());
            media.setUrl(mViewModel.getmLiveDataPostSaleyardPhoto().getValue());
            newSaleyardMediaList.add(media);
        }

/*
        for (Media media : mediaList) {
            for (Media mediaOld : mMediaListToDelete) {
                if (mediaOld.getUrl().equals(media.getUrl())) {
                    mMediaListToDelete.remove(mediaOld);
                    break;
                }
            }
        }
*/

        if (newSaleyardMediaList.size() != 0)
        {
            newLivestock.setMedia(newSaleyardMediaList);
        }
        return newLivestock;
    }


    @Override
    public void OnSelectedItem(SaleyardCategory animal) {
        if (!animal.equals(mSelectSaleyardCategory)) {
            mBinding.tvSpinnerCategory.setText(animal.getName());
            mBinding.tvSpinnerCategory.setTag(animal);

        }
        mSelectSaleyardCategory = animal;
    }




}
