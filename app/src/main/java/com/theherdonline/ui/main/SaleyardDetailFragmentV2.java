package com.theherdonline.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentSaleyardDetailBinding;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.SaleyardCategory;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.di.MyApplication;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.GeneralFragment;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.general.MapMarkerBitmap;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.herdinterface.MediaListInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.view.ImagePagerAdapter;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UIUtils;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

import dagger.Lazy;

public class SaleyardDetailFragmentV2 extends GeneralFragment {

    public static String ARG_TAG_saleyard_id = "saleyard id";
    public static String ARG_TAG_saleyard_new_item = "new_saleyard";
    public static String ARG_TAG_saleyard_old_item = "old_saleyard";
    public static String ARG_TAG_ui_mode = "saleyard data";

    public static Integer UI_normal = 1;
    public static Integer UI_confirm = 2;


    public static Integer mUIMode;




    MainActivityViewModel mViewModel;


    public Lazy<ViewModelFactory> mLazyFactory;
    public Lazy<MapMarkerFragment> mMapMarkerFragmentLazy;
    public AppUtil mAppUtil;







    EntitySaleyard mNewEntitySaleyard;
    EntitySaleyard mOldEntitySaleyard;
    EntitySaleyard mPresentEntitySaleyard;
    FragmentSaleyardDetailBinding mBinding;
    private SaleyardInterface mSaleyardInterface;


    MediaListInterface mStockMediaListener;

    View.OnClickListener mSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSaleyardInterface.onClickSavedSaleyard(mPresentEntitySaleyard,(ImageView)v);
        }
    };
    View.OnClickListener  mShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSaleyardInterface.onClickShareSaleyard(mPresentEntitySaleyard);
        }
    };
    private NetworkInterface mNetworkInterface;
    private InterfaceContactCard mInterfaceContactCard;
    protected Integer mSaleyardId;
    private Boolean mEnableEdit = false;
    protected DataObserver<EntitySaleyard> mObserverSaleyard = new DataObserver<EntitySaleyard>() {
        @Override
        public void onSuccess(EntitySaleyard data) {
            mNetworkInterface.onLoading(false);
            if (data != null) {
                mPresentEntitySaleyard = data;
                presenter(data);
                // mBinding.listStock.addItemDecoration(mAppUtil.provideDividerItemDecoration());
            }
        }

        @Override
        public void onError(Integer code, String msg) {
            mNetworkInterface.onLoading(false);
            mNetworkInterface.onFailed(code, msg);
        }

        @Override
        public void onLoading() {
            mNetworkInterface.onLoading(true);
        }

        @Override
        public void onDirty() {
            mNetworkInterface.onLoading(false);
        }
    };


    public SaleyardDetailFragmentV2() {
    }

    public static SaleyardDetailFragmentV2 newInstance(Lazy<ViewModelFactory> mLazyFactory,
                                                       Lazy<MapMarkerFragment> mMapMarkerFragmentLazy,
                                                       EntitySaleyard newEntitySaleyard, EntitySaleyard oldEntitySaleyard) {
        Bundle args = new Bundle();
        SaleyardDetailFragmentV2 fragment = new SaleyardDetailFragmentV2();
        if (newEntitySaleyard != null)
        {
            args.putParcelable(ARG_TAG_saleyard_new_item, newEntitySaleyard);
        }
        if (oldEntitySaleyard != null)
        {
            args.putParcelable(ARG_TAG_saleyard_old_item, oldEntitySaleyard);
        }
        fragment.setArguments(args);
        fragment.mLazyFactory = mLazyFactory;
        fragment.mMapMarkerFragmentLazy = mMapMarkerFragmentLazy;
        return fragment;
    }



    private void presenter(EntitySaleyard data)
    {

        mBinding.tvTime.setText(TimeUtils.BackendUTC2LocalUTC(data.getStartsAt()));
        mBinding.linearQuantity.setVisibility(data.getHeadcount() == null ? View.GONE : View.VISIBLE);
        if (data.getHeadcount() != null)
        {
            mBinding.linearQuantity.setVisibility(View.VISIBLE);
            mBinding.tvQuantity.setText(getString(R.string.txt_number_head,data.getHeadcount())); //   ActivityUtils.trimTextEmpty(data.getHeadcount()));
        }
        else
        {
            mBinding.linearQuantity.setVisibility(View.GONE);
        }





        SaleyardCategory caName = null;
        if (data.getSaleyardCategory() == null)
        {
            caName =  ((MyApplication)getActivity().getApplication()).getmAppDatabase().saleyardCategoryDao().getItemById(data.getSaleyardCategoryId());

        }
        else
        {
            caName = data.getSaleyardCategory();
        }




        String str = String.format("%s - %s", ActivityUtils.trimText(getContext(), data.getName()),
                ActivityUtils.trimText(getContext(), caName.getName()));





        mBinding.tvTitle.setText(str);


        if (!StringUtils.isEmpty(data.getAddress()))
        {
            mBinding.linearAddress.setVisibility(View.VISIBLE);
            mBinding.tvLocation.setText(ActivityUtils.trimText(getContext(), data.getAddress()));
            MyApplication application = (MyApplication) getActivity().getApplicationContext();
            LatLng currentLocation = application.getmAppUtil().getmCurrentLocation();
            if (currentLocation != null && data.getLat() != null && data.getLng() != null)
            {
                float [] result = {1.0f,1.0f,1.0f,1.0f,1.0f};
                Location.distanceBetween(currentLocation.latitude,currentLocation.longitude, data.getLat(), data.getLng(),result);
                mBinding.tvDistance.setVisibility(View.VISIBLE);
                mBinding.tvDistance.setText(String.format ("%.2fKM", result[0] / 1000));
            }
            else
            {
                mBinding.tvDistance.setVisibility(View.GONE);
            }
        }
        else
        {
            mBinding.linearAddress.setVisibility(View.GONE);
        }





        if (!StringUtils.isEmpty(data.getDescription()))
        {
            mBinding.linearLayoutNote.setVisibility(View.VISIBLE);
            mBinding.tvNotes.setVisibility(View.VISIBLE);
            mBinding.tvDescription.setVisibility(View.VISIBLE);
            mBinding.cardviewDescription.setVisibility(View.VISIBLE);
            mBinding.tvDescription.setText(ActivityUtils.htmlText2Spanned(getContext(), data.getDescription()));
        }
        else
        {
            mBinding.cardviewDescription.setVisibility(View.GONE);
            mBinding.tvDescription.setVisibility(View.GONE);
            mBinding.tvNotes.setVisibility(View.GONE);
            mBinding.linearLayoutNote.setVisibility(View.GONE);
        }
        // show Media
        HashMap<String,List<Media>> hashMap = UIUtils.classifyMedias(ListUtils.emptyIfNull(data.getMedia()));
        List<Media> mediaListOther = hashMap.get(Media.CoolectionName.others.name());
        if (ListUtils.emptyIfNull(mediaListOther).size() != 0){
            ImagePagerAdapter imagePagerAdapter;
            imagePagerAdapter = new ImagePagerAdapter(getContext(), mediaListOther);
            imagePagerAdapter.setmListener(l->mStockMediaListener.onClickOpenMediaList(mediaListOther));
            mBinding.listImage.setAdapter(imagePagerAdapter);
            mBinding.listImage.setVisibility(View.VISIBLE);
            mBinding.cardViewImagePager.setVisibility(View.VISIBLE);
        } else {
            mBinding.listImage.setVisibility(View.GONE);
            mBinding.cardViewImagePager.setVisibility(View.GONE);
        }
        // show Pdf
        List<Media> mediaListPdf = hashMap.get(Media.CoolectionName.pdfs.name());
        if (ListUtils.emptyIfNull(mediaListPdf).size() != 0)
        {
            mBinding.buttonViewSaleDraw.setOnClickListener(l->mStockMediaListener.onClickOpenMediaList(mediaListPdf));
            mBinding.cardViewViewSaleDraw.setVisibility(View.VISIBLE);
        }
        else
        {
            mBinding.cardViewViewSaleDraw.setVisibility(View.GONE);
        }
        if (!StringUtils.isEmpty(data.getSale_numbers()))
        {
            mBinding.tvSellNumber.setVisibility(View.VISIBLE);
            mBinding.cardviewSellNumber.setVisibility(View.VISIBLE);
            mBinding.tvSellNumber.setText(data.getSale_numbers());
        }
        else
        {
            mBinding.tvSellNumber.setVisibility(View.GONE);
            mBinding.cardviewSellNumber.setVisibility(View.GONE);
        }
        mBinding.tvSaleyardType.setText(getString(R.string.txt_str_sale, ActivityUtils.trimTextEmpty(data.getType())));
        mBinding.tvAnimalType.setText(ActivityUtils.trimTextEmpty(caName.getName()));
        ActivityUtils.loadImage(getContext(),mBinding.imageViewAnimalType,ActivityUtils.getImageAbsoluteUrlFromWeb(caName.getPath()),R.drawable.animal_defaul_photo);
       if (ListUtils.emptyIfNull(data.getSaleyardAreas()).size() == 0)
       {
           mBinding.linearLayoutStock.setVisibility(View.GONE);
           mBinding.listStock.setVisibility(View.GONE);
           mBinding.tvStock.setVisibility(View.GONE);
       }
       else
       {
           mBinding.linearLayoutStock.setVisibility(View.VISIBLE);
           mBinding.listStock.setVisibility(View.VISIBLE);
           mBinding.tvStock.setVisibility(View.VISIBLE);
           mBinding.listStock.setAdapter(new SaleyardStockRecyclerViewAdapter(getContext(), data.getSaleyardAreas(), mStockMediaListener));
           mBinding.listStock.addItemDecoration(ActivityUtils.provideDividerItemDecoration(getContext(),R.drawable.divider_stock));
       }

       if (ListUtils.emptyIfNull(data.getUsers()).size() == 0)
       {
           mBinding.linearLayoutSellAgents.setVisibility(View.GONE);
           mBinding.tvSellAgents.setVisibility(View.GONE);
           mBinding.listAgent.setVisibility(View.GONE);
       }
       else
       {
           mBinding.linearLayoutSellAgents.setVisibility(View.VISIBLE);
           mBinding.tvSellAgents.setVisibility(View.VISIBLE);
           mBinding.listAgent.setVisibility(View.VISIBLE);
           mBinding.listAgent.setAdapter(new SaleyardAgendRecyclerViewAdapter(getContext(), data.getUsers(), mInterfaceContactCard));
           mBinding.listAgent.addItemDecoration(ActivityUtils.provideDividerItemDecoration(getContext(),R.drawable.list_divider));
       }

        Boolean isSaved = data.getHas_liked() == null || data.getHas_liked() == false ? false : true;
        updateSavedIcon(isSaved ,data);
        if  (data.getLat() == null || new Double(0.0f).equals(data.getLat()) || data.getLng() == null || new Double(0.0f).equals(data.getLng())) {
            mBinding.linearLayoutLocationMap.setVisibility(View.GONE);
            mBinding.tvLocationMap.setVisibility(View.GONE);
            mBinding.cardViewMap.setVisibility(View.GONE);
        }
        else
        {
            mBinding.linearLayoutLocationMap.setVisibility(View.VISIBLE);
            mBinding.tvLocationMap.setVisibility(View.VISIBLE);
            mBinding.cardViewMap.setVisibility(View.VISIBLE);
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (data.getLat() != null && data.getLng() != null) {
                        LatLngBounds.Builder LatLngBoundsBuilder = new LatLngBounds.Builder();
                        LatLng latLng = new LatLng(data.getLat(), data.getLng());
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng)); //.title("Marker in Sydney"));
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(new MapMarkerBitmap(getContext(), 0).asBitmap()));
                        LatLngBoundsBuilder.include(latLng);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLngBoundsBuilder.build().getCenter(), 10);
                        googleMap.moveCamera(cameraUpdate);
                        googleMap.getUiSettings().setAllGesturesEnabled(false);
                        mBinding.tvLocationNotAvailable.setVisibility(View.INVISIBLE);
                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {
                                ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                                        MapMarkerFragment.newInstance(mMapMarkerFragmentLazy.get(), data.getLat(), data.getLng()),R.id.frameLayout_container);
                            }
                        });
                    } else {
                        mBinding.tvLocationNotAvailable.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }



    @Override
    public CustomerToolbar getmCustomerToolbar() {
        if (mAppUtil.getUserId() != null &&  mAppUtil.getUserId().equals(mPresentEntitySaleyard.getUserId()) && AppConstants.TAG_SALEYARD_TYPE_PRIME.equals(mPresentEntitySaleyard.getType()))
        {
            return new CustomerToolbar(true, null, null,null,
                    R.drawable.ic_button_edit, l->mSaleyardInterface.onUpdateSaleyard(mPresentEntitySaleyard),
                    R.drawable.ic_button_share, mShareListener,
                    mPresentEntitySaleyard.getHas_liked() ? R.drawable.ic_button_save : R.drawable.ic_saved, mSaveListener);
        }
        else
        {
            // Normal view
            return new CustomerToolbar("",
                    mExitListener,
                    R.drawable.ic_button_share, mShareListener,
                    mPresentEntitySaleyard != null && mPresentEntitySaleyard.getHas_liked() ? R.drawable.ic_button_save : R.drawable.ic_button_unsave,mSaveListener);
        }
    }


    protected void initViewModel()
    {
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        mViewModel.refreshSaleyardDetail(mSaleyardId, null);
        mViewModel.getmLDResponseSpecificSaleyard().observe(this, mObserverSaleyard);

    }

    @Override
    public void onResume() {
        super.onResume();
        initViewModel();
    }



    private void updateSavedIcon(Boolean saved, EntitySaleyard entitySaleyard)
    {
        //Drawable savedDrawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_button_save);
        //Integer color = ContextCompat.getColor(getContext(),!saved ? R.color.colorToolBarIconTint :R.color.colorSavedIconTint);
        //savedDrawable.setTint(color);
        Drawable savedDrawable = ContextCompat.getDrawable(getContext(),saved ? R.drawable.ic_button_save : R.drawable.ic_button_unsave);
        //mBinding.toolbar.imageRight1.setImageDrawable(savedDrawable);
        //mBinding.toolbar.imageRight1.setTag(saved);
        entitySaleyard.setHas_liked(saved);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_saleyard_detail, container, false);
        mAppUtil = ((MyApplication)getActivity().getApplication()).getmAppUtil();
        if (getArguments() != null) {
            mNewEntitySaleyard = getArguments().getParcelable(ARG_TAG_saleyard_new_item);
            mOldEntitySaleyard = getArguments().getParcelable(ARG_TAG_saleyard_old_item);
            if (mNewEntitySaleyard != null)
            {
                mUIMode = UI_confirm;
                mPresentEntitySaleyard = mNewEntitySaleyard;
            }
            else
            {
                mUIMode = UI_normal;
                mPresentEntitySaleyard = mOldEntitySaleyard;
            }
            mSaleyardId = mPresentEntitySaleyard.getId();
        }
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MediaListInterface) {
            mStockMediaListener = (MediaListInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MediaListInterface");
        }

        if (context instanceof NetworkInterface) {
            mNetworkInterface = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NetworkInterface");
        }

        if (context instanceof InterfaceContactCard) {
            mInterfaceContactCard = (InterfaceContactCard) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InterfaceContactCard");
        }


        if (context instanceof SaleyardInterface) {
            mSaleyardInterface = (SaleyardInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SaleyardInterface");
        }






    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSaleyardInterface = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.resetSpecificSaleyard();
    }



}
