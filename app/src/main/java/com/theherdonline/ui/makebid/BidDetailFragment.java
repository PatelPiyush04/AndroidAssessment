package com.theherdonline.ui.makebid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
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
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentLivestockDetaiBinding;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.DataConverter;
import com.theherdonline.db.entity.Bid;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.GenderEntity;
import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.LivestockSubCategory;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.MyChat;
import com.theherdonline.db.entity.Organisation;
import com.theherdonline.db.entity.PregnancyEntity;
import com.theherdonline.db.entity.User;
import com.theherdonline.db.entity.extraattributes.AuctionsPlus;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.repository.DataRepository;
import com.theherdonline.ui.general.ADType;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.DialogAcceptOfferFragment;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.InterfaceBidOffer;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.general.MapMarkerBitmap;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.MapMarkerFragment;
import com.theherdonline.ui.main.SaleyardDetailFragment;
import com.theherdonline.ui.view.ImagePagerAdapter;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UIUtils;
import com.theherdonline.util.UserType;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

import static com.theherdonline.app.AppConstants.SERVER_ERROR_401;


public class BidDetailFragment extends HerdFragment {

    public static String ARG_DATA_LIVESTOCK = "arg-data";
    public static String ARG_DATA_LIVESTOCK_ID = "arg-data-id";

    public static String ARG_DATA_MEDIA_TO_DELETE = "arg-data-media-to-delete";

    public static String ARG_DATA_UI_MODE = "arg-ui-mode";
    public static String ARG_DATA_LIVESTOCK_NEED_UPDATE = "arg-data-livestock-need-update";

    public static String ARG_DATA_ENABLE_EDIT = "arg-ui-enable-edit";


    public static Integer ARG_DATA_UI_MODE_DETAIL = 1;
    public static Integer ARG_DATA_UI_MODE_POST_CONFIRM = 2;


    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    @Inject
    Lazy<MapMarkerFragment> mMapMarkerFragmentLazy;
    MainActivityViewModel mViewModel;
    FragmentLivestockDetaiBinding mBinding;
    @Inject
    AppUtil mAppUtil;
    DataObserver<EntityLivestock> mUpdateObserver = new DataObserver<EntityLivestock>() {
        @Override
        public void onSuccess(EntityLivestock data) {
            mNetworkListener.onLoading(false);
            mNetworkListener.onLoading(false);
            /*ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_submit_done), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });*/

            ActivityUtils.showWarningDialogWithDismiss(getContext(), getString(R.string.app_name), getString(R.string.txt_submit_done),
                    new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
/*
                                getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().getSupportFragmentManager().popBackStack();
*/

                                ((MainActivity) getActivity()).mBinding.navigation.setCurrentItem(2);

                            }

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
            mNetworkListener.onLoading(false);
        }
    };
    DateTime nowTime;
    DataObserver<EntityLivestock> mCreateObserver = new DataObserver<EntityLivestock>() {
        @Override
        public void onSuccess(EntityLivestock data) {
            // long l = DateTime.now().getMillis() - nowTime.getMillis();
            //  Log.d(TAG, "take time: " + (DateTime.now().getMillis() - nowTime.getMillis()));
            // System.out.println("take time: " + (DateTime.now().getMillis() - nowTime.getMillis()));

            mNetworkListener.onLoading(false);
            ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_submit_done), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // getActivity().getSupportFragmentManager().popBackStack();
                    // getActivity().getSupportFragmentManager().popBackStack();
                    //getActivity().onBackPressed();
                    ((MainActivity) getActivity()).onBackPressedTwice();
                    //((MainActivity)getActivity()).mBinding.navigation.setCurrentItem(2);
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
            nowTime = DateTime.now();
            mNetworkListener.onLoading(true);
        }

        @Override
        public void onDirty() {
            mNetworkListener.onLoading(false);
        }
    };
    @Inject
    DataRepository mRepository;
    @Inject
    AppDatabase mAppDatabase;
    private InterfaceBidOffer mBidOfferListener;
    private InterfaceContactCard mContactCardListener;
    private LivestockInterface mLivestockInterfaceListener;
    private Integer mLiveStockId;
    private Boolean mEnableEdit = false;
    private SaleyardDetailFragment.OnFragmentInteractionListener mMediaListener;
    private EntityLivestock mLivestock;
    View.OnClickListener mSaveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLivestockInterfaceListener.onClickSavedLivestock(mLivestock, (ImageView) v);
        }
    };
    View.OnClickListener mShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLivestockInterfaceListener.onClickShareLivestock(mLivestock);
        }
    };
    DialogAcceptOfferFragment.AcceptOfferInterface mListener = new DialogAcceptOfferFragment.AcceptOfferInterface() {
        @Override
        public void OnClickAcceptOffer(Integer userId, Integer livestockId, Float price) {
            mRepository.makeBid(null, livestockId, Math.round(price * 100)).observe(BidDetailFragment.this, new DataObserver<Bid>() {
                @Override
                public void onSuccess(Bid data) {


                    mRepository.updateBidId(livestockId, data.getId()).observe(BidDetailFragment.this, new DataObserver<EntityLivestock>() {
                        @Override
                        public void onSuccess(EntityLivestock data) {
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(BidDetailFragment.this.getView().getWindowToken(), 0);
                            mNetworkListener.onLoading(false);
                            ActivityUtils.showWarningDialog(getContext(), null,
                                    getString(R.string.format_mark_as_sold, price));
                            CustomerToolbar customerToolbar = new CustomerToolbar(true, null, null, null, R.drawable.ic_button_edit, mEditListener,
                                    R.drawable.ic_button_share, mShareListener,
                                    mLivestock.getHas_liked() ? R.drawable.ic_button_save : R.drawable.ic_saved, mSaveListener);
                            setToolBar(customerToolbar);

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
                            mNetworkListener.onLoading(false);
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
                    mNetworkListener.onLoading(false);
                }
            });
        }
    };
    View.OnClickListener mEditListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            PopupMenu popup = new PopupMenu(v.getContext(), v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_ad_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.action_mark_as_sold) {
                        DialogAcceptOfferFragment newFragment = DialogAcceptOfferFragment.newInstance(null, mLivestock.getId(),
                                mListener, getString(R.string.txt_mark_as_sold), null);
                        newFragment.show(getActivity().getSupportFragmentManager(), "accept dialog");
                        return true;
                    } else if (item.getItemId() == R.id.action_edit) {
                        mLivestockInterfaceListener.onUpdateLivestock(mLivestock);
                        return true;
                    } else if (item.getItemId() == R.id.action_delete) {

                        String msg = getString(R.string.txt_are_you_sure_delete_paddock_sale);
                        ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), msg,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mViewModel.deleteLivestock(mLivestock.getId()).observe(getViewLifecycleOwner(),
                                                new DataObserver<Void>() {
                                                    @Override
                                                    public void onSuccess(Void data) {
                                                        mGobackListener.OnClickGoBackButton();
                                                    }

                                                    @Override
                                                    public void onError(Integer code, String msg) {

                                                    }

                                                    @Override
                                                    public void onLoading() {

                                                    }

                                                    @Override
                                                    public void onDirty() {

                                                    }
                                                }
                                        );


                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });


                        return true;
                    } else {
                        return true;
                    }
                }
            });
            popup.show();

            //mLivestockInterfaceListener.onUpdateLivestock(mLivestock);
        }
    };
    private final View.OnClickListener mClickImageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMediaListener.OnClickSaleyardFragmentMediaButton(mLivestock.getMedia());
        }
    };
    private Boolean mNeedUpdateLivestockData = true;
    private Integer mMode = ARG_DATA_UI_MODE_DETAIL;
    private List<Media> mMediaListToDelete;

    View.OnClickListener mConfirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mLivestock.getId() == null) {
                // for Create
                mViewModel.postNewLivestock(mLivestock).observe(BidDetailFragment.this, mCreateObserver);
            } else {
                // for Update
                mViewModel.updateLivestock(mLivestock.getId(), mLivestock, mMediaListToDelete, mNeedUpdateLivestockData).observe(BidDetailFragment.this, mUpdateObserver);
            }
        }
    };


    @Inject
    public BidDetailFragment() {
    }

    public static BidDetailFragment newInstance(Lazy<BidDetailFragment> lazy, EntityLivestock advertisement) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA_LIVESTOCK, advertisement);
        BidDetailFragment fragment = lazy.get();
        fragment.setArguments(args);
        return fragment;
    }

    public static BidDetailFragment newInstance(Lazy<BidDetailFragment> lazy, EntityLivestock advertisement, Integer mode, List<Media> mediaListToDelete, Boolean needUpdate) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA_LIVESTOCK, advertisement);
        args.putInt(ARG_DATA_UI_MODE, mode);
        args.putBoolean(ARG_DATA_LIVESTOCK_NEED_UPDATE, advertisement.getId() == null || needUpdate);
        args.putParcelableArrayList(ARG_DATA_MEDIA_TO_DELETE, new ArrayList<>(mediaListToDelete));
        BidDetailFragment fragment = lazy.get();
        fragment.setArguments(args);
        return fragment;
    }

    public Lazy<ViewModelFactory> getmLazyFactory() {
        return mLazyFactory;
    }

    public void setmLazyFactory(Lazy<ViewModelFactory> mLazyFactory) {
        this.mLazyFactory = mLazyFactory;
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        if (mMode == ARG_DATA_UI_MODE_DETAIL) {
            if (mEnableEdit) {
                CustomerToolbar customerToolbar = new CustomerToolbar(true, null, null, null, R.drawable.ic_menu, mEditListener,
                        R.drawable.ic_button_share, mShareListener,
                        mLivestock.getHas_liked() ? R.drawable.ic_button_save : R.drawable.ic_saved, mSaveListener);
                /*customerToolbar.setmImageLeft(R.drawable.ic_button_acceptbid);
                customerToolbar.setmListenerImageLeft(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogAcceptOfferFragment newFragment = DialogAcceptOfferFragment.newInstance(null,mLivestock.getId(),
                                mListener,getString(R.string.txt_mark_as_sold), null);
                        newFragment.show(getActivity().getSupportFragmentManager(), "accept dialog");
                    }
                });*/
                return customerToolbar;
            } else {
                if (mLivestock.getAuctions_plus_id() == null) {
                    return new CustomerToolbar("", mExitListener,
                            R.drawable.ic_button_share, mShareListener,
                            mLivestock.getHas_liked() ? R.drawable.ic_button_save : R.drawable.ic_saved, mSaveListener);
                } else {
                    return new CustomerToolbar("", mExitListener,
                            null, null,
                            null, null);

                }
            }
        } else {
            //    return new CustomerToolbar(getString(R.string.txt_confirm_ad),
            //            mExitListener, R.drawable.ic_button_confirm, mConfirmListener,
            //            null, null);
            return new CustomerToolbar(true, getString(R.string.txt_confirm_ad),
                    getString(R.string.txt_publish), mConfirmListener,
                    null, null,
                    null, null,
                    null, null);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLivestock != null && mMode == ARG_DATA_UI_MODE_DETAIL) {
            ((MainActivity) getActivity()).setSavedButton(mLivestock.getHas_liked(), mLivestock);
        }
    }

    public Boolean enableEdit(EntityLivestock livestock) {
        ADType adType = DataConverter.Int2ADType(livestock.getAdvertisementStatusId());

        boolean b = mAppUtil.getUserId() == livestock.getAgentId();

        return b && livestock.getAuctions_plus_id() == null && mAppUtil.getUserType() == UserType.AGENT && (adType == ADType.SCHEDULED || adType == ADType.PUBLISHED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_livestock_detai, container, false);
        if (getArguments() != null) {
            mLivestock = getArguments().getParcelable(ARG_DATA_LIVESTOCK);
            mMode = getArguments().getInt(ARG_DATA_UI_MODE, ARG_DATA_UI_MODE_DETAIL);
            mNeedUpdateLivestockData = getArguments().getBoolean(ARG_DATA_LIVESTOCK_NEED_UPDATE);
            mEnableEdit = enableEdit(mLivestock);
            if (mMode == ARG_DATA_UI_MODE_POST_CONFIRM) {
                mMediaListToDelete = getArguments().getParcelableArrayList(ARG_DATA_MEDIA_TO_DELETE);
            }
            initUI(mLivestock);
        }

        if (mLivestock.getLat() == null || mLivestock.getLng() == null) {
            mBinding.cardViewMap.setVisibility(View.GONE);
        } else {
            mBinding.cardViewMap.setVisibility(View.VISIBLE);

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (mLivestock.getLat() != null && mLivestock.getLng() != null) {
                        LatLngBounds.Builder LatLngBoundsBuilder = new LatLngBounds.Builder();
                        LatLng latLng = new LatLng(mLivestock.getLat(), mLivestock.getLng());
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng)); //.title("Marker in Sydney"));
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(new MapMarkerBitmap(getContext(), 0).asBitmap()));
                        LatLngBoundsBuilder.include(latLng);
                        Integer padding = -200;/*
                    //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(LatLngBoundsBuilder.build(), padding);
                    LatLngBounds livestoceLocation = new LatLngBounds(
                            latLng);*/

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLngBoundsBuilder.build().getCenter(), 10);

                        googleMap.moveCamera(cameraUpdate);
                        googleMap.getUiSettings().setAllGesturesEnabled(false);
                        mBinding.tvLocationNotAvailable.setVisibility(View.INVISIBLE);

                        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(LatLng latLng) {

                                ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                                        MapMarkerFragment.newInstance(mMapMarkerFragmentLazy.get(), Double.valueOf(mLivestock.getLat()), Double.valueOf(mLivestock.getLng())), R.id.frameLayout_container);
                            }
                        });

                    } else {
                        mBinding.tvLocationNotAvailable.setVisibility(View.VISIBLE);
                    }
                }
            });
        }


        return mBinding.getRoot();
    }

    private void updateSavedIcon(Boolean saved) {
        Drawable savedDrawable = ContextCompat.getDrawable(getContext(), saved ? R.drawable.ic_button_save : R.drawable.ic_button_unsave);
        // mBinding.toolbar.imageRight1.setImageDrawable(savedDrawable);
        // mBinding.toolbar.imageRight1.setTag(saved);
        mLivestock.setHas_liked(saved);
    }

    private void initUI(EntityLivestock livestock) {
       /* if (mMode == ARG_DATA_UI_MODE_DETAIL) {

            UIUtils.showToolbar(getContext(), mBinding.toolbar, getString(R.string.txt_paddock_details),
                    mExitListener, R.drawable.ic_button_save, mSaveListener,
                    null, null);
            Boolean isSaved = livestock.getHas_liked() == null || livestock.getHas_liked() == false ? false : true;
            updateSavedIcon(isSaved);
        }
        else
        {
            UIUtils.showToolbar(getContext(), mBinding.toolbar, getString(R.string.txt_paddock_details),
                    mExitListener, R.drawable.ic_button_confirm, mConfirmListener,
                    null, null);
            mBinding.cardViewMakeABid.setVisibility(View.GONE);
        }*/

        // mBinding.cardAgent.setVisibility(mMode == ARG_DATA_UI_MODE_DETAIL ? View.VISIBLE : View.GONE);  //piyush

        if (livestock.getMedia() != null && livestock.getMedia().size() != 0) {
            ImagePagerAdapter imagePagerAdapter;// = (ImagePagerAdapter)mBinding.listImage.getAdapter();

            //if (imagePagerAdapter == null) {
            imagePagerAdapter = new ImagePagerAdapter(getContext(), livestock.getMedia());
            imagePagerAdapter.setmListener(mClickImageListener);
            mBinding.listImage.setAdapter(imagePagerAdapter);
            //}
            //imagePagerAdapter.updateList(livestock.getMedia());
            //  mBinding.listImage.setCurrentItem(0);
            //  imagePagerAdapter.notifyDataSetChanged();

          /* else
           {
           }

                ImagePagerAdapter imagePagerAdapter;
                imagePagerAdapter = new ImagePagerAdapter(getContext(), livestock.getMedia());
                imagePagerAdapter.setmListener(mClickImageListener);
                mBinding.listImage.setAdapter(imagePagerAdapter);
                mBinding.listImage.setCurrentItem(0);*/


            //}
            mBinding.listImage.setVisibility(View.VISIBLE);
            mBinding.imageNoAvailablePicture.setVisibility(View.INVISIBLE);
        } else {
            mBinding.listImage.setVisibility(View.INVISIBLE);
            mBinding.imageNoAvailablePicture.setVisibility(View.VISIBLE);
        }


        mBinding.tvTitle.setText(livestock.getName());
        mBinding.tvQuantity.setText(getString(R.string.txt_number_head, livestock.getQuantity() == null ? 0 : livestock.getQuantity()));
        if (livestock.getLivestockCategoryId() != null) {
            LivestockCategory item = mAppDatabase.livestockCategoryDao().getItemById(livestock.getLivestockCategoryId());
            if (item != null && item.getPath() != null) {
                ActivityUtils.loadImage(getContext(), mBinding.imageViewAnimalSymbol,
                        ActivityUtils.getImageAbsoluteUrlFromWeb(item.getPath()), R.drawable.animal_defaul_photo);
            }
        }
        // show breed
        String breedName = getString(R.string.txt_n_a);
        if (livestock.getLivestockSubCategoryId() != null) {
            LivestockSubCategory subCategory = mAppDatabase.livestockSubCategoryDao().getItemById(livestock.getLivestockSubCategoryId());
            if (subCategory != null) {
                breedName = StringUtils.defaultString(subCategory.getName(), breedName);
            }
        }
        mBinding.tvAnimal.setText(breedName);


        if (StringUtils.isEmpty(livestock.getAddress())) {
            mBinding.tvLocation.setText(getString(R.string.txt_n_a));
        } else {
            mBinding.tvLocation.setText(livestock.getAddress());

        }


        LatLng currentLocation = mAppUtil.getmCurrentLocation();

        if (currentLocation != null && livestock.getLat() != null && livestock.getLng() != null) {
            float[] result = {1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
            Location.distanceBetween(currentLocation.latitude, currentLocation.longitude, livestock.getLat(), livestock.getLng(), result);
            mBinding.tvDistance.setVisibility(View.VISIBLE);
            mBinding.tvDistance.setText(String.format("%.2fKM", result[0] / 1000));
        } else {
            mBinding.tvDistance.setVisibility(View.GONE);
        }


        if (livestock.getAuctions_plus_id() == null) {
            mBinding.linearExpires.setVisibility(View.VISIBLE);
            mBinding.tvExpires.setText(getString(R.string.txt_expires_on,
                    TimeUtils.BackendUTC2Local(
                            ActivityUtils.trimTextEmpty(livestock.getEnd_date()))));
        } else {
            mBinding.linearExpires.setVisibility(View.GONE);
        }

        mBinding.tvDescription.setText(ActivityUtils.htmlText2Spanned(getContext(),
                ActivityUtils.trimTextEmpty(livestock.getDescription(), getString(R.string.txt_n_a))
        ));


        mBinding.tvDescription.setText(StringUtils.defaultString(livestock.getDescription(), getString(R.string.txt_n_a)));

        Log.e("Data", "dates==>" + livestock.getAvailable_at() + "==>" + livestock.getEarliest_at() + "==>" + livestock.getLatest_at());

        if (StringUtils.isEmpty(livestock.getAvailable_at())) {
            // mBinding.tvInfoAvailable.setText(R.string.txt_n_a);
            mBinding.llAvailableInfoView.setVisibility(View.GONE);
        } else {
            mBinding.tvInfoAvailable.setText(TimeUtils.BackendUTC2LocalDate_AvailableTime(livestock.getAvailable_at()));
        }

        try {
            if (livestock.getAsap() == 1 || livestock.getAsap().equals("1")) {
                mBinding.tvPickUp.setText(getResources().getString(R.string.txt_asap_c));
            } else {
                //livestock.setEarliest_at(TextUtils.isEmpty(livestock.getEarliest_at())? "" : TimeUtils.dateTimeSpiltGetDate(livestock.getEarliest_at()) );
                livestock.setEarliest_at(TimeUtils.dateTimeSpiltGetDate(livestock.getEarliest_at()));
                //livestock.setLatest_at(TextUtils.isEmpty(livestock.getLatest_at())? "" : TimeUtils.dateTimeSpiltGetDate(livestock.getLatest_at()));
                livestock.setLatest_at(TimeUtils.dateTimeSpiltGetDate(livestock.getLatest_at()));
                mBinding.tvPickUp.setText(TimeUtils.BackendUTC2LocalDate_AvailableTime(livestock.getEarliest_at()) + " - " + TimeUtils.BackendUTC2LocalDate_AvailableTime(livestock.getLatest_at()));
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


        if (livestock.getUnder_offer() == 1) {
            mBinding.llOfferViews.setVisibility(View.VISIBLE);
        } else {
            mBinding.llOfferViews.setVisibility(View.GONE);
        }

        if (livestock.getGender_id() == null) {
            mBinding.linearLayoutSex.setVisibility(View.GONE);
        } else {
            GenderEntity genderEntity = mAppDatabase.genderEntityDao().getItemById(livestock.getGender_id());
            if (genderEntity != null) {
                mBinding.tvSex.setText(genderEntity.getName());
            } else {
                // mBinding.tvSex.setText(getString(R.string.txt_n_a));
                mBinding.linearLayoutSex.setVisibility(View.GONE);
            }
        }

        if (livestock.getPregnancy_status_id() == null) {
            mBinding.linearLayoutPregnant.setVisibility(View.GONE);
        } else {
            PregnancyEntity pregnancyEntity = mAppDatabase.pregnancyEntityDao().getItemById(livestock.getPregnancy_status_id());
            if (pregnancyEntity != null && !pregnancyEntity.getName().contains("N/A")) {
                mBinding.tvPregnancy.setText(pregnancyEntity.getName());
            } else {
                //mBinding.tvPregnancy.setText(R.string.txt_n_a);
                mBinding.linearLayoutPregnant.setVisibility(View.GONE);
            }
        }


        if (!livestock.getPrice().equals("0.00")) {
            mBinding.linearPrice.setVisibility(View.VISIBLE);
            String print_price = StringUtils.isEmpty(livestock.getDollar_price()) ? getString(R.string.txt_n_a) : UIUtils.str2CurrencyStr(livestock.getDollar_price());


            try {
                String price_type = livestock.getPrice_type().toLowerCase();
                if (!price_type.contains("head")) {
                    print_price = print_price + " " + StringUtils.defaultString(livestock.getPrice_type());
                }
            } catch (Exception ignored) {

            }


            try {

                String offerType = livestock.getOffer_type().toLowerCase();
                if (offerType.contains("fixed")) {
                    if (new Integer(1).equals(livestock.getOno())) {
                        print_price += " " + getString(R.string.txt_ono);
                    }
                } else if (offerType.contains("offer ")) {
                    print_price = "Offers Over " + print_price;
                    //print_price = "Offers Between $" + livestock.getPriceLow() + " - $" + livestock.getPriceHigh() + " " + print_price;
                }
            } catch (Exception ignored) {

            }
            mBinding.tvBidPrice.setText(print_price);
        } else if (livestock.getOffer_type().toLowerCase().contains("offers between") && livestock.getPriceLow() != null || livestock.getPriceHigh() != null) {
            mBinding.tvBidPrice.setText("Offers Between $" + livestock.getPriceLow() + " - $" + livestock.getPriceHigh() + " " + livestock.getPrice_type());
        } else {
            mBinding.linearPrice.setVisibility(View.GONE);

        }


        String ageUnit = "";
        if (!StringUtils.isEmpty(livestock.getAge_type())) {
            ageUnit = ActivityUtils.capFirstLetter(ActivityUtils.trimTextEmpty(livestock.getAge_type()));
        }


        try {
            mBinding.llMinAgeView.setVisibility(checkViewHide(livestock.getAge()) ? View.GONE : View.VISIBLE);
            mBinding.llMaxAgeView.setVisibility(checkViewHide(livestock.getMax_age()) ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (livestock.getAge() != null) {
            mBinding.tvMinAge.setText(getString(R.string.txt_str_years_old, ActivityUtils.trimTextEmpty(livestock.getAge()) + " " + ageUnit));
        } else {
            //mBinding.tvMinAge.setText(getString(R.string.txt_n_a));
            mBinding.llMinAgeView.setVisibility(View.GONE);
        }
        if (livestock.getMax_age() != null) {
            mBinding.tvMaxAge.setText(getString(R.string.txt_str_years_old, ActivityUtils.trimTextEmpty(livestock.getMax_age()) + " " + ageUnit));
        } else {
            // mBinding.tvMaxAge.setText(getString(R.string.txt_n_a));
            mBinding.llMaxAgeView.setVisibility(View.GONE);
        }
        if (livestock.getAverage_age() != null) {
            mBinding.tvAverageAge.setText(getString(R.string.txt_str_years_old, ActivityUtils.trimTextEmpty(livestock.getAverage_age()) + " " + ageUnit));
        } else {
            //mBinding.tvAverageAge.setText(getString(R.string.txt_n_a));
            mBinding.llAverageAgeView.setVisibility(View.GONE);
        }

        if (livestock.getLowestWeight() == null) {
            //mBinding.tvWeightMin.setText(getString(R.string.txt_n_a));
            mBinding.llWeightMinView.setVisibility(View.GONE);
        } else {
            mBinding.tvWeightMin.setText(getString(R.string.txt_str_kg, ActivityUtils.trimTextEmpty(livestock.getLowestWeight(), getString(R.string.txt_n_a))));
        }
        if (livestock.getHighestWeight() == null) {
            //mBinding.tvWeightMax.setText(getString(R.string.txt_n_a));
            mBinding.llWeightMaxView.setVisibility(View.GONE);
        } else {
            mBinding.tvWeightMax.setText(getString(R.string.txt_str_kg, ActivityUtils.trimTextEmpty(livestock.getHighestWeight(), getString(R.string.txt_n_a))));
        }
        if (livestock.getAverageWeight() == null) {
            //mBinding.tvWeightAverage.setText(getString(R.string.txt_n_a));
            mBinding.llWeightAverage.setVisibility(View.GONE);
        } else {
            mBinding.tvWeightAverage.setText(getString(R.string.txt_str_kg, ActivityUtils.trimTextEmpty(livestock.getAverageWeight(), getString(R.string.txt_n_a))));
        }
        if (livestock.getAssessedBy() == null) {
            mBinding.llAccessbyView.setVisibility(View.GONE);
        } else {
            mBinding.tvAccesedBy.setText(livestock.getAssessedBy());
        }
/*
        if (livestock.getAgent() != null) {
            UIUtils.showContactCard(getContext(), mBinding.includeContactCardAgent, livestock.getAgent(), mContactCardListener);
        }
        else
        {
            mBinding.includeContactCardAgent.getRoot().setVisibility(View.GONE);
        }*/

        RequestCommaValue strings = new RequestCommaValue();
        /*if (livestock.getDentition() != null && livestock.getDentition() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_dentition)));
        }*/
        if (livestock.getWeighed() != null && livestock.getWeighed() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_weighed)));
        }
        if (livestock.getEu() != null && livestock.getEu() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_eu)));
        }
        if (livestock.getMouthed() != null && livestock.getMouthed() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_mouthed)));
        }
        if (livestock.getMsa() != null && livestock.getMsa() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_msa)));
        }
        if (livestock.getLpa() != null && livestock.getLpa() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_lpa)));
        }
        if (livestock.getOrganic() != null && livestock.getOrganic() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_organic)));
        }
        if (livestock.getVendorBred() != null && livestock.getVendorBred() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_vendorbred_c)));
        }
        if (livestock.getPcas() != null && livestock.getPcas() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_pcas)));
        }
        if (livestock.getGrassFed() != null && livestock.getGrassFed() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_grass_fed_c)));
        }
        if (livestock.getAntibioticFree() != null && livestock.getAntibioticFree() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_antibiotic_free_c)));
        }
        if (livestock.getLifeTimeTraceable() != null && livestock.getLifeTimeTraceable() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_life_time_traceable_c)));
        }
        if (livestock.getOneMark() != null && livestock.getOneMark() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_one_mark_c)));
        }
        if (livestock.getHgpFree() != null && livestock.getHgpFree() != 0) {
            strings.add(" " + ActivityUtils.capFirstLetter(getString(R.string.txt_hgp_free_c)));
        }


        if (strings.size() == 0) {
            //   mBinding.
            mBinding.linearLayoutOtherInfo.setVisibility(View.GONE);
        } else {
            mBinding.tvOtherInformation.setText(StringUtils.isAllEmpty(strings.filterValue()) ? getString(R.string.txt_none) : strings.filterValue());
        }


        User agentUser = livestock.getAgent();


      /*  if ((mAppUtil.getUserId() != null && (mAppUtil.getUserId() == livestock.getAgentId() || mAppUtil.getUserId() == livestock.getProducerId()))
                || (livestock.getAdvertisementStatusId() != null && livestock.getAdvertisementStatusId() != 4
                || mMode == ARG_DATA_UI_MODE_POST_CONFIRM)
        ) {
            mBinding.cardViewChat.setVisibility(View.GONE);
        } else {
            mBinding.cardViewChat.setVisibility(View.VISIBLE);
        }*/

        try {
            if (mAppUtil.getUserId() == agentUser.getId()) {
                mBinding.cardViewChat.setVisibility(View.GONE);
            } else {
                mBinding.cardViewChat.setVisibility(View.VISIBLE);
            }

            if (agentUser != null) {

                Organisation organisation = agentUser.getOrganisation();
                if (organisation != null) {
                    ActivityUtils.loadImage(getActivity(), mBinding.imageOrganisation, ActivityUtils.getImageAbsoluteUrl(organisation.getAvatar_location_full_url()),
                            R.drawable.ic_default_person);
                }
                mBinding.tvAgentName.setText(agentUser.getFullName());
                mBinding.buttonCallOrganisation.setOnClickListener(l -> {
                    mContactCardListener.OnClickPhoneCall(agentUser);
                });
                mBinding.buttonMsgOrganisation.setOnClickListener(l -> {
                    mContactCardListener.OnClickSendMessage(agentUser);
                });
                mBinding.imageOrganisation.setOnClickListener(l -> {
                    mContactCardListener.OnClickUserAvatar(agentUser);
                });


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBinding.buttonBidToOffer.setOnClickListener(l -> {
            mBidOfferListener.OnClickBidOffer(mLivestock);
        });


        if (livestock.getAuctions_plus_id() != null) {

            mBinding.cardViewAuctionDetail.setVisibility(View.VISIBLE);
            mBinding.cardViewAuctionLink.setVisibility(View.VISIBLE);
            mBinding.cardViewLivestockDetail.setVisibility(View.GONE);
            mBinding.cardViewChat.setVisibility(View.GONE);
            mBinding.cardAgent.setVisibility(View.GONE);

            AuctionsPlus auctionPlus = livestock.getExtra_attributes().getAuctionsPlus();
            mBinding.tvSaleType.setText(getString(R.string.txt_auctions_plus));
            mBinding.linearAnimalIcon.setVisibility(View.GONE);
            // animal
            try {
                mBinding.tvAuctionAnimal.setText(StringUtils.defaultString(auctionPlus.getSpecies()));
                mBinding.linearAuctionAnimal.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                mBinding.linearAuctionAnimal.setVisibility(View.GONE);
            }

            // breed
            try {
                mBinding.tvAuctionBreed.setText(auctionPlus.getBreeds().get(0).getDam());
                mBinding.linearLayoutAuctionBreed.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                mBinding.linearLayoutAuctionBreed.setVisibility(View.GONE);
            }

            // sex
            try {
                mBinding.tvAuctionSex.setText(auctionPlus.getSex());
                mBinding.linearLayoutAuctionSex.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                mBinding.linearLayoutAuctionSex.setVisibility(View.GONE);
            }

            // average weight
            try {
                mBinding.tvAuctionAverageWeight.setText(String.format("%.02fKg", auctionPlus.getAvgWeight()));
                mBinding.linearAuctionAverageWeight.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                mBinding.linearAuctionAverageWeight.setVisibility(View.GONE);
            }

            // average age
            try {
                mBinding.tvAuctionAge.setText(auctionPlus.getAge());
                mBinding.linearAuctionAge.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                mBinding.linearAuctionAge.setVisibility(View.GONE);
            }


            // Certifiaction
            try {
                mBinding.tvAuctionCertifications.setText(auctionPlus.getAccreditations().get(0).getDescription());
                mBinding.linearLayoutAuctionCertifications.setVisibility(View.VISIBLE);
            } catch (NullPointerException e) {
                mBinding.linearLayoutAuctionCertifications.setVisibility(View.GONE);
            } catch (IndexOutOfBoundsException e) {
                mBinding.linearLayoutAuctionCertifications.setVisibility(View.GONE);
            }

            try {


                mBinding.cardViewAuctionLink.setVisibility(View.VISIBLE);

                ActivityUtils.loadImage(getContext(), mBinding.imageAngentLogo, auctionPlus.getAgentLogoUrl(), null);
                mBinding.buttonGoToAuction.setOnClickListener(l -> {
                    String auctionPlus_url = auctionPlus.getAuctionDetails().getAuctionLink();
                    if ("Upcoming".equals(auctionPlus.getAuctionDetails().getAuctionStatus())) {
                        auctionPlus_url = auctionPlus.getAuctionDetails().getAuctionLink();
                    } else {
                        auctionPlus_url = StringUtils.defaultString(auctionPlus.getLotLink(), livestock.getAuctions_plus_url()); // : auctionPlus.getLotLink() ;
                    }
                    ActivityUtils.openWebPage(getActivity(), auctionPlus_url);
                });
            } catch (NullPointerException e) {
                mBinding.cardViewAuctionLink.setVisibility(View.GONE);
            }

        } else {
            mBinding.tvSaleType.setText(getString(R.string.txt_paddock_sale));
            mBinding.linearAnimalIcon.setVisibility(View.VISIBLE);

            mBinding.cardViewAuctionDetail.setVisibility(View.GONE);
            mBinding.cardViewAuctionLink.setVisibility(View.GONE);
            mBinding.cardViewLivestockDetail.setVisibility(View.VISIBLE);
            //mBinding.cardViewChat.setVisibility(View.VISIBLE); //piyush
            //mBinding.cardAgent.setVisibility(View.VISIBLE);  // piyush
            try {
                if (agentUser.getId() == mAppUtil.getUserId()) {
                    mBinding.cardViewChat.setVisibility(View.GONE);
                    mBinding.cardAgent.setVisibility(View.GONE);
                } else {
                    mBinding.cardViewChat.setVisibility(View.VISIBLE);
                    mBinding.cardAgent.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            mBinding.buttonAskAQuestion.setOnClickListener(l -> {

                if (AppConstants.defaultContactByEmail) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    String sub = "Enquiry: " + StringUtils.defaultString(livestock.getName());
                    emailIntent.setData(Uri.parse("mailto:" + StringUtils.defaultString(mLivestock.getAgent().getEmail()) + "?&subject=" + sub));
                    startActivity(Intent.createChooser(emailIntent, sub));
                } else {

                    if (mAppUtil.getAccessToken() == null) {
                        mNetworkListener.onFailed(SERVER_ERROR_401, "");
                        return;
                    }

                    if (mLivestock.getMyChat() != null) {
                        mBidOfferListener.OnClickMessage(mLivestock.getMyChat(), mLivestock);
                    } else {
                        mViewModel.createChat(livestock.getId()).observe(this, new DataObserver<MyChat>() {
                            @Override
                            public void onSuccess(MyChat data) {
                                mNetworkListener.onLoading(false);
                                mBidOfferListener.OnClickMessage(data, mLivestock);
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
                                mNetworkListener.onLoading(false);
                            }
                        });
                    }
                }
            });

        }

    }

    private boolean checkViewHide(Integer age) {
        if (age.equals(null))
            age = 0;
        return age == 0;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
       /* mViewModel.getmLiveDataLivestock().observe(this, new Observer<EntityLivestock>() {
            @Override
            public void onChanged(@Nullable EntityLivestock advertisement) {
                initUI(advertisement);
            }
        });*/

    }


    @Override
    public void onDestroyView() {
        mBinding.listImage.setCurrentItem(0);
        mBinding.listImage.removeAllViews();
        mBinding.listImage.removeAllViewsInLayout();

        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceBidOffer) {
            mBidOfferListener = (InterfaceBidOffer) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof InterfaceContactCard) {
            mContactCardListener = (InterfaceContactCard) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InterfaceContactCard");
        }


        if (context instanceof SaleyardDetailFragment.OnFragmentInteractionListener) {
            mMediaListener = (SaleyardDetailFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SaleyardDetailFragment.OnFragmentInteractionListener");
        }


        if (context instanceof LivestockInterface) {
            mLivestockInterfaceListener = (LivestockInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LivestockInterface");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBidOfferListener = null;
        mContactCardListener = null;
        mMediaListener = null;
        mLivestockInterfaceListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnClickBidToOffer(int id);

        void OnClickContactAgent(int id);
    }


}
