package com.theherdonline.ui.main;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentSaleyardDetailBinding;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.db.entity.User;
import com.theherdonline.di.MyApplication;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.general.MapMarkerBitmap;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.view.ImagePagerAdapter;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UIUtils;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import dagger.Lazy;

public class SaleyardDetailFragment extends HerdFragment {

    public static String ARG_TAG_saleyard_id = "saleyard id";
    public static String ARG_TAG_saleyard_new_item = "new_saleyard";
    public static String ARG_TAG_saleyard_old_item = "old_saleyard";
    public static String ARG_TAG_ui_mode = "saleyard data";

    public static Integer UI_normal = 1;
    public static Integer UI_confirm = 2;


    public static Integer mUIMode;


    @Inject
    Lazy<MapMarkerFragment> mMapMarkerFragmentLazy;


    MainActivityViewModel mViewModel;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    @Inject
    AppUtil mAppUtil;
    EntitySaleyard mNewEntitySaleyard;
    EntitySaleyard mOldEntitySaleyard;
    EntitySaleyard mPresentEntitySaleyard;
    FragmentSaleyardDetailBinding mBinding;
    private SaleyardInterface mSaleyardInterface;

    private SaleyardDetailFragment.OnFragmentInteractionListener mMediaListener;

    View.OnClickListener mSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSaleyardInterface.onClickSavedSaleyard(mNewEntitySaleyard,(ImageView)v);
        }
    };
    View.OnClickListener  mShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSaleyardInterface.onClickShareSaleyard(mNewEntitySaleyard);
        }
    };
    private SaleyardDetailFragment.OnFragmentInteractionListener mListener;
    private NetworkInterface mNetworkInterface;
    private InterfaceContactCard mInterfaceContactCard;
    private Integer mSaleyardId;
    private Boolean mEnableEdit = false;
    private DataObserver<EntitySaleyard> mObserverList = new DataObserver<EntitySaleyard>() {
        @Override
        public void onSuccess(EntitySaleyard data) {
            mNetworkInterface.onLoading(false);
            if (data != null) {
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


    @Inject
    public SaleyardDetailFragment() {
    }

    public static SaleyardDetailFragment newInstance(Lazy<SaleyardDetailFragment> lazy, Integer saleyardId, EntitySaleyard newEntitySaleyard, EntitySaleyard oldEntitySaleyard) {
        Bundle args = new Bundle();
        SaleyardDetailFragment fragment = new SaleyardDetailFragment();
        args.putInt(ARG_TAG_saleyard_id, saleyardId);
        if (newEntitySaleyard != null)
        {
            args.putParcelable(ARG_TAG_saleyard_new_item, newEntitySaleyard);
        }
        if (oldEntitySaleyard != null)
        {
            args.putParcelable(ARG_TAG_saleyard_old_item, oldEntitySaleyard);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public List<Media> mediaListNeedDeleted(EntitySaleyard newData, EntitySaleyard oldData)
    {
        List<Media> newMediaList = ListUtils.emptyIfNull(newData.getMedia());
        List<Media> mediaListToDelete = new ArrayList<>();

        for (Media oldMedia : ListUtils.emptyIfNull(oldData.getMedia()))
        {
            for (Media newMedia : newMediaList)
            {
                if (!newMedia.getUrl().equals(oldMedia))
                {
                    mediaListToDelete.add(newMedia);
                    break;
                }
            }
        }
        return mediaListToDelete;
    }

    public List<Media> mediaListNeedUpload(EntitySaleyard newData, EntitySaleyard oldData)
    {

        List<Media> list = new ArrayList<>();
        for (Media media: ListUtils.emptyIfNull(newData.getMedia()))
        {
            if(new File(media.getUrl()).isFile())
            {
                list.add(media);
            }
        }
        return list;
    }

    public EntitySaleyard compareSaleyard(EntitySaleyard newData, EntitySaleyard oldData)
    {

        if (oldData == null)
        {
            // this is for creating a new saleyard
            return newData;
        }
        EntitySaleyard updateEntitySaleyard = new EntitySaleyard();
        if (!newData.getName().equals(oldData.getName()))
        {
            updateEntitySaleyard.setName(newData.getName());
        }
        if (!newData.getType().equals(oldData.getType()))
        {
            updateEntitySaleyard.setType(newData.getType());
        }
        if (!newData.getDescription().equals(oldData.getDescription()))
        {
            updateEntitySaleyard.setDescription(newData.getDescription());
        }
        if (!newData.getSale_numbers().equals(oldData.getSale_numbers()))
        {
            updateEntitySaleyard.setSale_numbers(newData.getSale_numbers());
        }
        if (!newData.getStartsAt().equals(oldData.getStartsAt()))
        {
            updateEntitySaleyard.setStartsAt(newData.getStartsAt());
        }
        if (!newData.getSaleyardStatusId().equals(oldData.getSaleyardStatusId()))
        {
            updateEntitySaleyard.setSaleyardStatusId(newData.getSaleyardStatusId());
        }
        if (!newData.getSaleyardCategoryId().equals(oldData.getSaleyardCategoryId()))
        {
            updateEntitySaleyard.setSaleyardCategoryId(newData.getSaleyardCategoryId());
        }
        if (!newData.getAddressLineOne().equals(newData.getAddressLineOne()))
        {
            updateEntitySaleyard.setAddressLineOne(newData.getAddressLineOne());
        }


        if (!newData.getAddressLineTwo().equals(newData.getAddressLineTwo()))
        {
            updateEntitySaleyard.setAddressLineTwo(newData.getAddressLineTwo());
        }


        if (!newData.getAddressSuburb().equals(newData.getAddressSuburb()))
        {
            updateEntitySaleyard.setAddressSuburb(newData.getAddressSuburb());
        }


        if (!newData.getAddressCity().equals(newData.getAddressCity()))
        {
            updateEntitySaleyard.setAddressCity(newData.getAddressCity());
        }


        if (!newData.getAddressPostcode().equals(newData.getAddressPostcode()))
        {
            updateEntitySaleyard.setAddressPostcode(newData.getAddressPostcode());
        }

        if (!newData.getLat().equals(newData.getLat()))
        {
            updateEntitySaleyard.setLat(newData.getLat());
        }

        if (!newData.getLng().equals(newData.getLng()))
        {
            updateEntitySaleyard.setLng(newData.getLng());
        }

        if (newData.equals(updateEntitySaleyard))
        {
            return null;
        }
        else
        {
            return updateEntitySaleyard;
        }



    }

    private void presenter(EntitySaleyard data)
    {




        CustomerToolbar toolbar = null;
        if (mUIMode == UI_confirm)
        {
            // For prime saleyard
            View.OnClickListener mConfirmListener = v->{
                if (mNewEntitySaleyard.getId() == null) {
                    // create a new saleyard
                    mViewModel.postCreateSaleyard(mNewEntitySaleyard).observe(SaleyardDetailFragment.this, new DataObserver<EntitySaleyard>(){
                        @Override
                        public void onSuccess(EntitySaleyard data) {
                            mNetworkListener.onLoading(false);
                            ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_submit_done), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().onBackPressed();
                                }
                            });
                        }

                        @Override
                        public void onError(Integer code, String msg) {
                            mNetworkListener.onLoading(false);
                            mNetworkListener.onFailed(code, msg);
                        }

                        @Override
                        public void onLoading() {

                            mNetworkListener.onLoading(true);
                        }

                        @Override
                        public void onDirty() {

                        }
                    } );
                } else {
                    // for Update
                    EntitySaleyard updateEntitySaleyard = compareSaleyard(mNewEntitySaleyard, mOldEntitySaleyard);
                    List<Media> mediaListUpload = mediaListNeedUpload(mNewEntitySaleyard, mOldEntitySaleyard);
                    List<Media> mediaListDelete = mediaListNeedDeleted(mNewEntitySaleyard, mOldEntitySaleyard);

                    if (updateEntitySaleyard == null && mediaListDelete.size() == 0  && mediaListDelete.size() == 0)
                    {
                        // nothing needed to be updated
                        ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_submit_done), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // getActivity().getSupportFragmentManager().popBackStack();
                                // getActivity().getSupportFragmentManager().popBackStack();
                                getActivity().onBackPressed();
                                //    ((MainActivity)getActivity()).mBinding.navigation.setCurrentItem(2);
                            }
                        });
                        return;
                    }
                    else
                    {
                        // need to update something

                        mViewModel.updateSaleyard(mNewEntitySaleyard.getId(), updateEntitySaleyard,mediaListDelete,mediaListUpload).observe(this,
                                new DataObserver<EntitySaleyard>(){
                                    @Override
                                    public void onSuccess(EntitySaleyard data) {
                                        mNetworkListener.onLoading(false);
                                        ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_saleyard_update_done), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                getActivity().onBackPressed();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(Integer code, String msg) {
                                        mNetworkListener.onLoading(false);
                                        mNetworkListener.onFailed(code, msg);
                                    }

                                    @Override
                                    public void onLoading() {
                                        mNetworkListener.onLoading(true);
                                    }

                                    @Override
                                    public void onDirty() {
                                    }
                                });
                    }
                }
            };

            toolbar =  new CustomerToolbar(true, getString(R.string.txt_confirm_ad),
                    getString(R.string.txt_publish), mConfirmListener,
                    null, null,
                    null, null,
                    null, null);

        }
        else
        {
            if (mAppUtil.getUserId().equals(mPresentEntitySaleyard.getUserId()) || AppConstants.TAG_SALEYARD_TYPE_PRIME.equals(mPresentEntitySaleyard.getType()))
            {
                // For update prime saleyard
                View.OnClickListener mEditListener = v -> {
                    //mLivestockInterfaceListener.onUpdateLivestock(mLivestock);
                    mSaleyardInterface.onUpdateSaleyard(mPresentEntitySaleyard);
                };

                toolbar  = new CustomerToolbar(true, null, null,null, R.drawable.ic_button_edit, mEditListener,
                        R.drawable.ic_button_share, mShareListener,
                        mPresentEntitySaleyard.getHas_liked() ? R.drawable.ic_button_save : R.drawable.ic_saved, mSaveListener);
                }
            else
            {
                // Normal view
                toolbar = new CustomerToolbar("",
                        mExitListener,
                        R.drawable.ic_button_share, mShareListener,
                        mPresentEntitySaleyard != null && mPresentEntitySaleyard.getHas_liked() ? R.drawable.ic_button_save : R.drawable.ic_button_unsave,mSaveListener);

            }
        }





        mMainActivity.setToolBar(toolbar);
        ((MainActivity) getActivity()).setSavedButton(data.getHas_liked(), data);
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

        String str = String.format("%s - %s", ActivityUtils.trimText(getContext(), data.getName()),
                ActivityUtils.trimText(getContext(), data.getSaleyardCategory().getName()));
        mBinding.tvTitle.setText(str);
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
            imagePagerAdapter.setmListener(l->mMediaListener.OnClickSaleyardFragmentMediaButton(mediaListOther));
            mBinding.listImage.setAdapter(imagePagerAdapter);
            mBinding.listImage.setVisibility(View.VISIBLE);
        } else {
            mBinding.listImage.setVisibility(View.GONE);
        }
        // show Pdf
        List<Media> mediaListPdf = hashMap.get(Media.CoolectionName.pdfs.name());
        if (ListUtils.emptyIfNull(mediaListPdf).size() != 0)
        {
            mBinding.buttonViewSaleDraw.setOnClickListener(l->mMediaListener.OnClickSaleyardFragmentMediaButton(mediaListPdf));
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
        mBinding.tvAnimalType.setText(ActivityUtils.trimTextEmpty(data.getSaleyardCategory().getName()));
        ActivityUtils.loadImage(getContext(),mBinding.imageViewAnimalType,ActivityUtils.getImageAbsoluteUrlFromWeb(data.getSaleyardCategory().getPath()),R.drawable.animal_defaul_photo);

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
           mBinding.listStock.setAdapter(new SaleyardStockRecyclerViewAdapter(getContext(), data.getSaleyardAreas(), mMediaListInterface));
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



        if  (data.getLat() == null || data.getLng() == null) {
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





        return new CustomerToolbar("",
                mExitListener,
                R.drawable.ic_button_share, mShareListener,
                R.drawable.ic_button_unsave,mSaveListener
                );


    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        if (mNewEntitySaleyard == null)
        {
            mViewModel.refreshSaleyardDetail(mSaleyardId, mPresentEntitySaleyard);
        }
        else
        {
            mViewModel.refreshSaleyardDetail(null, mPresentEntitySaleyard);
        }
        mViewModel.getmLDResponseSpecificSaleyard().observe(this, mObserverList);

    }

    public void setmLazyFactory(Lazy<ViewModelFactory> mLazyFactory) {
        this.mLazyFactory = mLazyFactory;
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
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_saleyard_detail, container, false);
        if (getArguments() != null) {
            mSaleyardId = getArguments().getInt(ARG_TAG_saleyard_id);
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
        }


        /*UIUtils.showToolbar(getContext(),mBinding.toolbar,getString(R.string.txt_saleyard),
                mExitListener,R.drawable.ic_button_save,mSaveListener,
                null, null);*/
        mBinding.listAgent.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.listStock.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SaleyardDetailFragment.OnFragmentInteractionListener) {
            mListener = (SaleyardDetailFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SaleyardDetailFragment.OnFragmentInteractionListener");
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

        if (context instanceof SaleyardDetailFragment.OnFragmentInteractionListener) {
            mMediaListener = (SaleyardDetailFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SaleyardDetailFragment.OnFragmentInteractionListener");
        }




    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mSaleyardInterface = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.resetSpecificSaleyard();
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /*
    public HashMap<String, Object> trigger(Integer saleyardId, EntitySaleyard saleyard)
    {
         HashMap<String, Object> hashMap = new HashMap<String, Object>();
         if (saleyard != null)
         {
             hashMap.put("id",saleyard);
         }
         if (saleyard != null)
         {
             hashMap.put("data", saleyard);
         }
         return hashMap;
    }*/

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnClickSaleyardFragmentMediaButton(List<Media> mediaList);
        void onClickSaleyardFragmentContactAgentButton(User user);
    }
}
