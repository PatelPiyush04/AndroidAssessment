package com.theherdonline.ui.main;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.test.espresso.IdlingResource;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.theherdonline.BuildConfig;
import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.ActivityMainBinding;
import com.theherdonline.db.entity.Bid;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.MyChat;
import com.theherdonline.db.entity.Post;
import com.theherdonline.db.entity.SaleyardType;
import com.theherdonline.db.entity.StreamItem;
import com.theherdonline.db.entity.User;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.espresso.SimpleIdlingResource;
import com.theherdonline.ui.Flash.FlashActivity;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.ImageFilePath;
import com.theherdonline.ui.general.InterfaceBidOffer;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.general.InterfacePermissionCheck;
import com.theherdonline.ui.general.PickupPhotoDialogFragment;
import com.theherdonline.ui.general.ProgressDialogFragment;
import com.theherdonline.ui.general.SearchFilterPublicLivestock;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.herdLive.HerdLiveDetailFragment;
import com.theherdonline.ui.herdLive.HerdLiveFragment;
import com.theherdonline.ui.herdLive.ResSaleyardStreamDetails;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.herdinterface.LogoutInterface;
import com.theherdonline.ui.herdinterface.MapMarkListener;
import com.theherdonline.ui.herdinterface.MediaListInterface;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.herdinterface.PostInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.herdinterface.SearchFilterInterface;
import com.theherdonline.ui.home.PermissionStatus;
import com.theherdonline.ui.home.ReadStoragePermissionDialogFragment;
import com.theherdonline.ui.login.LoginActivity;
import com.theherdonline.ui.login.LoginFragment;
import com.theherdonline.ui.main.myad.AdTopFragment;
import com.theherdonline.ui.main.myad.MyAdFragment;
import com.theherdonline.ui.main.myad.PrimeSaleyardsFragment;
import com.theherdonline.ui.main.poddocksearch.AutoLocationInterface;
import com.theherdonline.ui.main.poddocksearch.FilterDistanceFragment;
import com.theherdonline.ui.main.poddocksearch.LivestockListFragment;
import com.theherdonline.ui.main.poddocksearch.LocationPermissionListener;
import com.theherdonline.ui.main.poddocksearch.SearchLivestockResultFragment;
import com.theherdonline.ui.main.saleyardsearch.SaleyardListFragment;
import com.theherdonline.ui.main.saleyardsearch.SearchResultSaleyardFragment;
import com.theherdonline.ui.makebid.BidDetailFragment;
import com.theherdonline.ui.makebid.MakeBidActivity;
import com.theherdonline.ui.makebid.MakeBidFragment;
import com.theherdonline.ui.marketReport.CreateMarketReportFragment;
import com.theherdonline.ui.marketReport.InterfaceMarketReportListener;
import com.theherdonline.ui.marketReport.MarketReportDetailFragment;
import com.theherdonline.ui.marketReport.MarketReportFragment;
import com.theherdonline.ui.marketReport.SelectSyleyardFragment;
import com.theherdonline.ui.notification.backup.NotificationBackupFragment;
import com.theherdonline.ui.notification.normal.NotificationFragment;
import com.theherdonline.ui.notification.normal.NotificationListener;
import com.theherdonline.ui.photoview.GalleryMainActivity;
import com.theherdonline.ui.postad.PostLivestockFragment;
import com.theherdonline.ui.postad.PostPrimSaleyardFragment;
import com.theherdonline.ui.profile.InterfaceBidItemListener;
import com.theherdonline.ui.profile.InterfaceStreamItemListener;
import com.theherdonline.ui.profile.UserProfileFragment;
import com.theherdonline.ui.stream.StreamFragment;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UIUtils;
import com.theherdonline.util.UserType;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

import static com.theherdonline.app.AppConstants.AUTOCOMPLETE_REQUEST_CODE_CREATE_LIVESTOCK;
import static com.theherdonline.app.AppConstants.AUTOCOMPLETE_REQUEST_CODE_LIVESTOCK;
import static com.theherdonline.app.AppConstants.AUTOCOMPLETE_REQUEST_CODE_SALEYARD;
import static com.theherdonline.app.AppConstants.SERVER_ERROR_401;

public class MainActivity extends DaggerAppCompatActivity
        implements
        RequestLoginFragment.OnFragmentInteractionListener,
        NetworkErrorFragment.OnFragmentInteractionListener,
        NetworkInterface,
        HomeTopFragment.OnFragmentInteractionListener,
        HerdLiveFragment.OnHerdLiveInterface,
        SaleyardDetailFragment.OnFragmentInteractionListener,
        AdTopFragment.AdTabListener,
        LivestockInterface,
        SaleyardInterface,
        LocationListener,
        InterfaceStreamItemListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        MapMarkListener,
        BackPressInterface,
        InterfaceBidItemListener,
        InterfaceMarketReportListener,
        InterfacePermissionCheck,
        InterfaceContactCard,
        InterfaceBidOffer,
        LocationPermissionListener,
        PostInterface,
        SearchFilterInterface,
        AutoLocationInterface,
        LogoutInterface,
        MediaListInterface,
        NotificationListener {

    public static String PAGE_TYPE = "page_type";
    public static String PAGE_TYPE_saleyards = "saleyards";
    public static String PAGE_TYPE_poddock = "doddock";
    public static String ARG_START_FROM_NOTIFICATION = "arg-start-from-notification";
    private final Boolean mTriggerFromSearchPage = false;
    private final String TAG = MainActivity.class.getName();
    public ActivityMainBinding mBinding;

    @Inject
    Lazy<HomeTopFragment> mHomeTopFragmentProvider;
    @Inject
    Lazy<SearchResultSaleyardFragment> mSearchResultSaleyardFragmentProvider;
    @Inject
    Lazy<MapLivestockFragment> mMapLivestockFragmentProvider;
    @Inject
    Lazy<MapSaleyardFragment> mMapSaleyardFragmentProvider;
    @Inject
    Lazy<ReportFragment> mReportFragmentProvider;
    @Inject
    Lazy<StreamFragment> mStreamFragmentProvider;
    @Inject
    Lazy<NotificationBackupFragment> mNotificationFragmentProvider;
    @Inject
    Lazy<NotificationFragment> mNotificationFragmentProviderV1;
    @Inject
    Lazy<MeFragment> mMeFragmentProvider;
    @Inject
    Lazy<RequestLoginFragment> mRequestLoginProvider;
    @Inject
    Lazy<NetworkErrorFragment> mNetworkErrorProvider;
    @Inject
    Lazy<MarketReportDetailFragment> mMarketReportDetailFragmentProvider;
    @Inject
    Lazy<UserProfileFragment> mUserProfileFragmentProvider;
    @Inject
    Lazy<MarketReportFragment> mMarketReportFragmentProvider;
    @Inject
    Lazy<HerdLiveFragment> mHerdLiveFragmentProvider;


    @Inject
    Lazy<PrimeSaleyardsFragment> mPrimeSaleyardsFragmentProvider;
    @Inject
    Lazy<PostPrimSaleyardFragment> mPostPrimSaleyardFragmentProvider;
    @Inject
    Lazy<AdTopFragment> mAdTopFragmentFragmentProvider;
    @Inject
    Lazy<BidDetailFragment> mBidDetailFragmentProvider;
    @Inject
    Lazy<CreateMarketReportFragment> mCreateMarketReportFragmentProvider;
    @Inject
    Lazy<SaleyardListFragment> mSaleyardListFragmentProvider;
    @Inject
    Lazy<SelectSyleyardFragment> mSelectSyleyardFragmentProvider;
    @Inject
    Lazy<MakeBidFragment> mMakeBidFragmentProvider;
    @Inject
    Lazy<LivestockListFragment> mLivestockListFragmentProvider;
    @Inject
    Lazy<SearchLivestockResultFragment> mSearchLivestockResultFragmentProvider;
    @Inject
    Lazy<MyAdFragment> mMyAdFragmentProvider;
    @Inject
    Lazy<FilterDistanceFragment> mFilterDistanceFragmentProvider;
    @Inject
    Lazy<SaleyardDetailFragment> mSaleyardDetailFragmentProvider;
    @Inject
    Lazy<PostLivestockFragment> mPostLivestockFragmentFragmentProvider;

    @Inject
    Lazy<HerdLiveDetailFragment> mHerdLiveDetailFragmentProvider;
    // @Inject
    // Lazy<UserProfileFragment> mUserProfileFragmentProvider;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    @Inject
    AppUtil mAppUtil;
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            clearFragmentStack();
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mHomeTopFragmentProvider.get(), R.id.frameLayout_container);
                    return true;
                case R.id.navigation_myad:
                    //ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mAdTopFragmentFragmentProvider.get(), R.id.frameLayout_container);
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mMyAdFragmentProvider.get(), R.id.frameLayout_container);
                    return true;
                case R.id.navigation_stream:
                    ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mStreamFragmentProvider.get(), R.id.frameLayout_container);
                    return true;
                case R.id.navigation_me:
                    if (mAppUtil.getUserId() == null) {
                        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mRequestLoginProvider.get(), R.id.frameLayout_container);
                    } else {
                        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), UserProfileFragment.newInstance(mUserProfileFragmentProvider, mAppUtil.getUserId(), true), R.id.frameLayout_container);
                    }
                    return true;
                case R.id.navigation_notification:
                    if (mAppUtil.getUserId() == null) {
                        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mRequestLoginProvider.get(), R.id.frameLayout_container);
                    } else {
                        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mNotificationFragmentProviderV1.get(), R.id.frameLayout_container);
                    }
                    return true;
            }
            return false;
        }
    };
    @Inject
    PlacesClient mPlaceClient;
    String mAudioFileName;
    LocationManager mLocationManager;
    boolean bDestoryRequestFragment = false;
    @Inject
    Lazy<MapMarkerFragment> mMapMarkerFragmentLazy;
    @Nullable
    private IdlingResource mIdlingResource;
    private FirebaseAnalytics mFirebaseAnalytics;
    private boolean isStartFromNotification = false;
    private boolean isShowDontshowAgainDialog = true;
    private String mCurrentVideoPath;
    private String mCurrentMarketReportVideoPath;
    private String mCurrentPhotoPath;
    private ProgressDialogFragment mProgressDialogFragment = null;
    private LocationListener mLocationListener = null;
    private MediaRecorder mAudioRecorder;
    private GoogleMap mMap;
    private MainActivityViewModel mViewModel;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    public void showDismissProgressDialogFragment(final boolean isShow, String info) {
        showDismissProgressDialogFragment(isShow, null, info);
    }

    public void showDismissProgressDialogFragment(final boolean isShow) {
        showDismissProgressDialogFragment(isShow, null, null);
    }

    public void showDismissProgressDialogFragment(final boolean isShow, @SuppressWarnings("SameParameterValue") final DialogInterface.OnDismissListener listener, final String info) {
        //  if (isActive()){
        if (mProgressDialogFragment != null) {
            mProgressDialogFragment.dismiss();
        }
        if (isShow) {
            mProgressDialogFragment = ProgressDialogFragment.newInstance(listener, info);
            mProgressDialogFragment.show(getSupportFragmentManager(), "dialog");
        }
        //  }
    }

    public void setSavedButton(Boolean b, EntityLivestock item) {
        UIUtils.updateSavedIcon(this, mBinding.includeToolbar.imageRight2, b, item);
    }

    public void setSavedButton(Boolean b, EntitySaleyard item) {
        UIUtils.updateSavedIcon(this, mBinding.includeToolbar.imageRight2, b, item);
    }

    public void logFirebaseEvent(final String eventName, final String paramName, final Integer id) {
        Bundle params = new Bundle();
        params.putString(paramName, id.toString());
        mFirebaseAnalytics.logEvent(eventName, params);
    }

    public void logFirebaseEventMarketReport(MarketReport report) {
        if (report != null) {
            logFirebaseEvent("view_market_report", "report_id", report.getId());
        }
    }

    public void logFirebaseEventLivestock(EntityLivestock livestock) {
        if (livestock != null) {
            logFirebaseEvent("view_paddock_sale", "livestock_id", livestock.getId());
        }
    }

    public void logFirebaseEventSaleyard(EntitySaleyard entitySaleyard) {
        if (entitySaleyard != null) {
            logFirebaseEvent("view_saleyard", "saleyard_id", entitySaleyard.getId());
        }
    }

    public void logFirebaseEventPlayMarketReport(MarketReport report) {
        if (report != null) {
            logFirebaseEvent("play_market_report", "report_id", report.getId());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }


        isShowDontshowAgainDialog = mAppUtil.getShowDontshowAgainDialogFlag();

        if (!BooleanUtils.toBoolean(GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this))) {
            GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this); // .makeGooglePlayServicesAvailable()

        }

        final ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    // Nice one, Google
                    decorView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                Log.d(TAG, "onGlobalLayout: " + getStatusBarHeight()); // This is the height of the status bar
            }
        });


        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        isStartFromNotification = getIntent().getBooleanExtra(ARG_START_FROM_NOTIFICATION, false);


        UserType userType = mAppUtil.getUserType();
        if (userType != null && (userType == UserType.AGENT || userType == UserType.PRODUCER)) {
            mBinding.navigation.inflateMenu(R.menu.navigation);
        } else {
            mBinding.navigation.inflateMenu(R.menu.navigation_unloged);
        }


        mBinding.navigation.setTextVisibility(true);
        mBinding.navigation.enableAnimation(false);
        mBinding.navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        if (mAppUtil.getAccessToken() != null) {
            mViewModel.initChatkit(mAppUtil.getAccessToken());
        }

        subscription();
        mBinding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);



/*        // If this Activity is killed by OS, EntryCount should be more than 0. In this case, simply back to the homepage.
        Integer n = getSupportFragmentManager().getBackStackEntryCount();
        for (int i = 0 ; i < n ; i ++) {
            getSupportFragmentManager().popBackStackImmediate();
        }*/
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mHomeTopFragmentProvider.get(), R.id.frameLayout_container);

        if (isStartFromNotification) {
            mBinding.navigation.setSelectedItemId(R.id.navigation_notification);
        }

        if (mAppUtil.getUserId() != null) {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "getInstanceId failed", task.getException());
                                return;
                            }

                            // Get new Instance ID token
                            String token = task.getResult().getToken();
                            mViewModel.postFcmToken(mAppUtil.getUserId(), token);
                            // Log and toast
                            //String msg = getString(R.string.msg_token_fmt, token);
                            //Log.d(TAG, msg);
                            //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    public int getStatusBarHeight() {
        int statusBarHeight = 0;

        if (!hasOnScreenSystemBar()) {
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            }
        }

        return statusBarHeight;
    }

    private boolean hasOnScreenSystemBar() {
        Display display = getWindowManager().getDefaultDisplay();
        int rawDisplayHeight = 0;
        try {
            Method getRawHeight = Display.class.getMethod("getRawHeight");
            rawDisplayHeight = (Integer) getRawHeight.invoke(display);
        } catch (Exception ex) {
        }

        int UIRequestedHeight = display.getHeight();

        return rawDisplayHeight - UIRequestedHeight > 0;
    }

    public void subscription() {


        mViewModel.getmToolbarMutableLiveData().observe(this, toolbar -> {
                    if (toolbar == null) {
                        mBinding.includeToolbar.getRoot().setVisibility(View.GONE);

                    } else {
                        mBinding.includeToolbar.getRoot().setVisibility(View.VISIBLE);
                        UIUtils.showToolbar(this, mBinding.includeToolbar, toolbar.mTitle, toolbar.mHasBackButton == false ? null :
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                onBackPressed();
                                            }
                                        },
                                toolbar.mCommand1, toolbar.mCommandListener1,
                                toolbar.mImageLeft, toolbar.mListenerImageLeft,
                                toolbar.mImage0, toolbar.mListenerImage0,
                                toolbar.mImage1, toolbar.mListenerImage1,
                                toolbar.mImage2, toolbar.mListenerImage2);
                    }
                }

        );

        mViewModel.getmShowNavigationBar().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                mBinding.navigation.setVisibility(aBoolean ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // ActivityUtils.showToast(this,BuildConfig.DEBUG ? "Debug" : "Release");

        if (!isHasLocationPermission(false)) {
            mAppUtil.updateCurrentLocation(null);
            requestLocationPermission();
        } else {
            requestLocation(this);
        }

        //   requestStoragePermission();

        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "firebadse";
                        Log.d(TAG, msg);
                        //   Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isShowDontshowAgainDialog) {
            (new RequestAccessFragment()).show(getSupportFragmentManager(), "dismiss");
            isShowDontshowAgainDialog = false;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationManager != null && mLocationListener != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mAppUtil.shutdownCleanUp();
                return null;
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == AppConstants.ACTIVITY_PERMISSION_AUDIO) {
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                    //denied
                    Log.e("denied", "deeiiee");
                    mAppUtil.setAudioRecordPermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", "e");
                        mAppUtil.setAudioRecordPermissionTag(PermissionStatus.GRANT);
                        //startRecord();
                        //popupPhotoDialog(AppConstants.ACTIVITY_PICK_IMAGE_CONTENT);

                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", "e");
                        mAppUtil.setAudioRecordPermissionTag(PermissionStatus.DONOTASK);
                        //do something here.
                    }
                }
            }
            return;
        } else if (requestCode == AppConstants.ACTIVITY_PERMISSION_LOCATION) {
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Log.e("denied", "deeiiee");
                    mAppUtil.setLocationPermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.e("allowed", "e");
                        mAppUtil.setLocationPermissionTag(PermissionStatus.GRANT);
                        requestLocation(this);
                    } else {
                        Log.e("set to never ask again", "e");
                        mAppUtil.setLocationPermissionTag(PermissionStatus.DONOTASK);
                    }
                }
            }
            return;
        } else if (requestCode == AppConstants.PICKUP_PHOTO_AVATAR_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //denied
                    Log.e("denied", "deeiiee");
                    mAppUtil.setStoragePermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", "e");
                        mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
                        chooseSinglePhotoFromGallary(AppConstants.PICKUP_PHOTO_AVATAR_ACTIVITY_CODE);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", "e");
                        mAppUtil.setStoragePermissionTag(PermissionStatus.DONOTASK);
                        //do something here.
                    }
                }
            }
            return;
        } else if (requestCode == AppConstants.ACTIVITY_SALEYEARD_POST_PDF_PHOTO_TAKE) {
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //denied
                    Log.e("denied", "deeiiee");
                    mAppUtil.setStoragePermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", "e");
                        mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
                        chooseSinglePhotoFromGallary(AppConstants.ACTIVITY_SALEYEARD_POST_PDF_PHOTO_PICK);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", "e");
                        mAppUtil.setStoragePermissionTag(PermissionStatus.DONOTASK);
                        //do something here.
                    }
                }
            }
            return;
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_LIVESTOCK_CHOOSE_MEDIA_PERMISSION) {
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    mAppUtil.setStoragePermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
                        //popupMediaDialog(AppConstants.PICKUP_PHOTO_POST_ACTIVITY_CODE);
                        chooseMultiplePhotoFromGallary(AppConstants.ACTIVITY_CODE_POST_LIVESTOCK_CHOOSE_MEDIA);
                    } else {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.DONOTASK);
                    }
                }
            }
            return;
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_SALEYARD_CHOOSE_MEDIA_PERMISSION) {
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    mAppUtil.setStoragePermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
                        //popupMediaDialog(AppConstants.PICKUP_PHOTO_POST_ACTIVITY_CODE);
                        chooseMultiplePhotoFromGallary(AppConstants.ACTIVITY_CODE_POST_SALEYARD_CHOOSE_MEDIA);
                    } else {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.DONOTASK);
                    }
                }
            }
            return;
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_CHOOSE_PHOTO_PERMISSION ||
                requestCode == AppConstants.ACTIVITY_CODE_AVARTAR_CHOOSE_PHOTO_PERMISSION) {
            // choolse photo on the post page
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    mAppUtil.setStoragePermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
                        // popupMediaDialog(AppConstants.PICKUP_PHOTO_POST_ACTIVITY_CODE);
                        chooseSinglePhotoFromGallery(requestCode - 1, requestCode);
                    } else {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.DONOTASK);
                    }
                }
            }
            return;
        } else if (requestCode == AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_VIDEO_PERMISSION) {
            // choolse photo on the post page
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    mAppUtil.setStoragePermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
                        // popupMediaDialog(AppConstants.PICKUP_PHOTO_POST_ACTIVITY_CODE);
                        chooseSingleVideoFromGallery(AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_VIDEO, AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_VIDEO_PERMISSION);
                    } else {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.DONOTASK);
                    }
                }
            }
            return;
        } else if (requestCode == AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_PHOTO_PERMISSION) {
            // choolse photo on the post page
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    mAppUtil.setStoragePermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
                        // popupMediaDialog(AppConstants.PICKUP_PHOTO_POST_ACTIVITY_CODE);
                        chooseSinglePhotoFromGallery(AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_PHOTO, AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_PHOTO_PERMISSION);
                    } else {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.DONOTASK);
                    }
                }
            }
            return;
        } else if (requestCode == AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_FILE_PERMISSION) {
            // choolse photo on the post page
            if (grantResults.length > 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    mAppUtil.setStoragePermissionTag(PermissionStatus.DENIED);
                } else {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
                        // popupMediaDialog(AppConstants.PICKUP_PHOTO_POST_ACTIVITY_CODE);
                        choosePdfFromGallery(AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_PDF, AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_FILE_PERMISSION);
                    } else {
                        mAppUtil.setStoragePermissionTag(PermissionStatus.DONOTASK);
                    }
                }
            }
            return;
        }


    }

    public void popupUploadVideoDialog(final int actionType, int resOptionId) {
        String[] options = getResources().getStringArray(resOptionId);
        final PickupPhotoDialogFragment fragment = PickupPhotoDialogFragment.newInstance(null, getResources().getStringArray(resOptionId), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selectOption = options[i];
                if (selectOption.equals(getString(R.string.option_take_photo))) {
                    dispatchTakePictureIntent(actionType + 1);
                } else if (selectOption.equals(getString(R.string.option_take_video))) {
                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, AppConstants.VIDEO_LENGTH);
                    takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, AppConstants.VIDEO_QUALITY);
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        mCurrentMarketReportVideoPath = photoFile.getAbsolutePath();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        //  ...
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri videoURI = FileProvider.getUriForFile(getBaseContext(),
                                BuildConfig.APPLICATION_ID,
                                photoFile);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                    }

                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takeVideoIntent, AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_VIDEO);
                    }
                } else if (selectOption.equals(getString(R.string.option_select_from_library))) {
                    chooseSingleVideoFromGallery(AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_VIDEO, AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_VIDEO_PERMISSION);
                    //addMultiplePhotoFromGallery(AppConstants.PICKUP_PHOTO_POST_ACTIVITY_CODE, AppConstants.PICKUP_PHOTO_POST_PERMISSION_CODE);
                }
            }
        });
        fragment.show(getSupportFragmentManager(), "photo");
    }

    public void popupUploadAttachmentDialog(final int actionType, int resOptionId) {
        String[] options = getResources().getStringArray(resOptionId);
        final PickupPhotoDialogFragment fragment = PickupPhotoDialogFragment.newInstance(null, getResources().getStringArray(resOptionId), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String selectOption = options[i];
                if (selectOption.equals(getString(R.string.option_take_photo))) {
                    dispatchTakePictureIntent(actionType);
                } else if (selectOption.equals(getString(R.string.option_select_Image_from_library))) {
                    chooseSinglePhotoFromGallery(AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_PHOTO, AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_PHOTO_PERMISSION);
                } else if (selectOption.equals(getString(R.string.option_file_action))) {
                    choosePdfFromGallery(AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_PDF, AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_FILE_PERMISSION);
                }
            }
        });
        fragment.show(getSupportFragmentManager(), "photo");
    }

    public void popupMediaDialog(final int actionType) {
        final PickupPhotoDialogFragment fragment = PickupPhotoDialogFragment.newInstance(null, getResources().getStringArray(R.array.pickup_media_options), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        // Take Photo
                        dispatchTakePictureIntent(actionType);
                        break;
                    case 1:
                        // Take Video
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, AppConstants.VIDEO_LENGTH);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, AppConstants.VIDEO_QUALITY);
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            mCurrentVideoPath = photoFile.getAbsolutePath();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            //  ...
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri videoURI = FileProvider.getUriForFile(getBaseContext(),
                                    BuildConfig.APPLICATION_ID,
                                    photoFile);
                            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                        }

                        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takeVideoIntent, actionType + 1);
                        }
                        break;
                    case 2:
                        // choose from library
                        addMultiplePhotoFromGallery(actionType + 2, actionType + 3);
                        break;
                }
            }
        });
        fragment.show(getSupportFragmentManager(), "photoh");
    }

    public void popupMediaDialogV2(final int actionCode) {

        // actionType : take photo
        // actionType + 1: take video
        // actionType + 2: choose photo
        // actionType + 3: permission code
        int codeTakePhoto = actionCode;
        int codeTakeVideo = actionCode + 1;
        int codeChooseMedias = actionCode + 2;
        int codeChooseMediasPermission = actionCode + 3;


        final PickupPhotoDialogFragment fragment = PickupPhotoDialogFragment.newInstance(null, getResources().getStringArray(R.array.pickup_media_options), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        dispatchTakePictureIntent(codeTakePhoto);
                        break;
                    case 1:
                        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, AppConstants.VIDEO_LENGTH);
                        takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, AppConstants.VIDEO_QUALITY);
                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                            mCurrentVideoPath = photoFile.getAbsolutePath();
                        } catch (IOException ex) {
                            // Error occurred while creating the File
                            //  ...
                        }
                        // Continue only if the File was successfully created
                        if (photoFile != null) {
                            Uri videoURI = FileProvider.getUriForFile(getBaseContext(),
                                    BuildConfig.APPLICATION_ID,
                                    photoFile);
                            takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                        }

                        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takeVideoIntent, codeTakeVideo);
                        }
                        break;
                    case 2:
                        //need permission
                        addMultiplePhotoFromGallery(codeChooseMedias, codeChooseMediasPermission);
                        break;
                }
            }
        });
        fragment.show(getSupportFragmentManager(), "photoh");
    }

    public void popupPhotoDialogWithDeleteOption(String title, final int actionType) {
        PickupPhotoDialogFragment fragment = PickupPhotoDialogFragment.newInstance(title, getResources().getStringArray(R.array.pickup_photo_options_with_delete_option), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        dispatchTakePictureIntent(actionType);
                        break;
                    case 1:
                        chooseSinglePhotoFromGallery(actionType + 1, actionType + 2);
                        break;
                    case 2:
                        ActivityUtils.deleteFile(mViewModel.getmLiveDataPostPhoto().getValue());
                        mViewModel.upDatePostPhoto(null);
                        break;
                }
            }
        });
        fragment.show(getSupportFragmentManager(), "photoh");
    }

    public void popupPhotoDialog(String title, final int actionType) {

        PickupPhotoDialogFragment fragment = PickupPhotoDialogFragment.newInstance(title, getResources().getStringArray(R.array.pickup_photo_options), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        dispatchTakePictureIntent(actionType);
                        break;
                    case 1:
                        chooseSinglePhotoFromGallery(actionType + 1, actionType + 2);
                        break;
                }
            }
        });
        fragment.show(getSupportFragmentManager(), "photoh");
    }

    public void dispatchTakePictureIntent(int nType) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                mCurrentPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //  ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        BuildConfig.APPLICATION_ID,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, nType);
            }
        }
    }

    public void chooseSinglePhotoFromGallery(int activityCode, int permissionCode) {
        PermissionStatus status = mAppUtil.getStoragePermissionTag();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (status == PermissionStatus.DONOTASK) {
                ReadStoragePermissionDialogFragment.newInstance(getString(R.string.permission_request_storage), getString(R.string.txt_permission_warning_storage)).
                        show(getSupportFragmentManager(), AppConstants.FRAGMENT_DIALOG);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        permissionCode);
            }
        } else {
            chooseSinglePhotoFromGallary(activityCode);
            if (status != PermissionStatus.GRANT) {
                mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
            }
        }
    }

    public void choosePdfFromGallery(int activityCode, int permissionCode) {
        PermissionStatus status = mAppUtil.getStoragePermissionTag();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (status == PermissionStatus.DONOTASK) {
                ReadStoragePermissionDialogFragment.newInstance(getString(R.string.permission_request_storage), getString(R.string.txt_permission_warning_storage)).
                        show(getSupportFragmentManager(), AppConstants.FRAGMENT_DIALOG);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        permissionCode);
            }
        } else {
            ChooseFileFromGallery(activityCode);
            if (status != PermissionStatus.GRANT) {
                mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
            }
        }
    }

    public void chooseSingleVideoFromGallery(int activityCode, int permissionCode) {
        PermissionStatus status = mAppUtil.getStoragePermissionTag();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (status == PermissionStatus.DONOTASK) {
                ReadStoragePermissionDialogFragment.newInstance(getString(R.string.permission_request_storage), getString(R.string.txt_permission_warning_storage)).
                        show(getSupportFragmentManager(), AppConstants.FRAGMENT_DIALOG);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        permissionCode);
            }
        } else {
            chooseSingleVideoFromGallary(activityCode);
            if (status != PermissionStatus.GRANT) {
                mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
            }
        }
    }

    public void addMultiplePhotoFromGallery(int activityCode, int permissionCode) {
        PermissionStatus status = mAppUtil.getStoragePermissionTag();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (status == PermissionStatus.DONOTASK) {
                ReadStoragePermissionDialogFragment.newInstance(getString(R.string.permission_request_storage), getString(R.string.txt_permission_warning_storage)).
                        show(getSupportFragmentManager(), AppConstants.FRAGMENT_DIALOG);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        permissionCode);
            }
        } else {
            chooseMultiplePhotoFromGallary(activityCode);
            if (status != PermissionStatus.GRANT) {
                mAppUtil.setStoragePermissionTag(PermissionStatus.GRANT);
            }
        }
    }

    public void chooseSinglePhotoFromGallary(int nType) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, nType);
    }

    public void chooseSingleVideoFromGallary(int nType) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("video/*");
        startActivityForResult(galleryIntent, nType);
    }

    public void chooseMultiplePhotoFromGallary(int nType) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/* video/*");
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, nType);
    }

    public void ChooseFileFromGallery(int nType) {

        Intent gallery = new Intent();
        gallery.setType("application/pdf");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery, nType);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void clearFragmentStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void switchFragment(Fragment f) {

        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        manager.beginTransaction()
                .replace(R.id.frameLayout_container, f)
                .addToBackStack(null)
                .commit();

    }

    public void setMapButtonImage(Drawable a) {
        mBinding.includeToolbar.imageRight2.setImageDrawable(a);
    }

    public void stackFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_container, f)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStackImmediate();
            Fragment fragment = fm.findFragmentById(R.id.frameLayout_container);
        } else {
            finish();
        }
    }

    public void onBackPressedTwice() {
        onBackPressed();
        onBackPressed();
    }

    public void showProgressBar(Boolean bShow) {
        mBinding.frameLayoutContainer.setVisibility(bShow ? View.INVISIBLE : View.VISIBLE);
        mBinding.progressBar.setVisibility(!bShow ? View.INVISIBLE : View.VISIBLE);
    }

    public void showProgressBar(Boolean bShow, Boolean bError) {
        mBinding.frameLayoutContainer.setVisibility(bShow || bError ? View.INVISIBLE : View.VISIBLE);
        mBinding.progressBar.setVisibility(!bShow ? View.INVISIBLE : View.VISIBLE);
    }

    public void startLoginActivity(int mode) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginFragment.ARG_UI_MODE, mode);
        startActivityForResult(intent, AppConstants.ACTIVITY_REQUEST_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AppConstants.ACTIVITY_REQUEST_LOGIN) { // || requestCode == AppConstants.ACTIVITY_REQUEST_LOGIN) {
            Intent newIntent = new Intent(this, FlashActivity.class);
            startActivity(newIntent);
            finish();
        } else if (requestCode == AppConstants.ACTIVITY_POST_LIVESTOCK && resultCode == RESULT_OK) {

        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_LIVESTOCK) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);

                mViewModel.setFilterPlace(place);
                if (mViewModel.getmLDSearchFilterLivestock().getValue() != null && place.getLatLng() != null) {
                    //SearchFilterPublicLivestock filter = mViewModel.getmLDSearchFilterLivestock().getValue();
                    //filter.setLatLng(place.getLatLng());
                    //filter.setmPlace(place);
                    //mViewModel.setLivestockSearchFilterNew(filter);

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    com.google.android.gms.common.api.Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i(TAG, status.getStatusMessage());
                    Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_SALEYARD) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                mViewModel.setFilterPlace(place);
                if (mViewModel.getmLDSearchFilterSaleyard().getValue() != null && place.getLatLng() != null) {
                    //SearchFilterPublicSaleyard filter = mViewModel.getmLDSearchFilterSaleyard().getValue();
                    //filter.setLatLng(place.getLatLng());
                    //filter.setmPlace(place);
                    //mViewModel.setSaleyardSearchFilterNew(filter);
                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    com.google.android.gms.common.api.Status status = Autocomplete.getStatusFromIntent(data);
                    Log.i(TAG, status.getStatusMessage());
                    Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                }
            }
        } else if (requestCode == AUTOCOMPLETE_REQUEST_CODE_CREATE_LIVESTOCK) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                mViewModel.setPostCurrentLocation(place);
            }
        } else if (requestCode == AppConstants.ACTIVITY_CODE_AVARTAR_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            mViewModel.updateAvatar(mCurrentPhotoPath);
        } else if (requestCode == AppConstants.ACTIVITY_CODE_AVARTAR_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {
            String realPath = ImageFilePath.getPath(this, data.getData());
            mViewModel.updateAvatar(realPath);
        } else if (requestCode == AppConstants.ACTIVITY_SALEYEARD_POST_PDF_PHOTO_TAKE && resultCode == Activity.RESULT_OK) {
            mViewModel.upDatePostSaleyardPdfPhoto(mCurrentPhotoPath);
        } else if (requestCode == AppConstants.ACTIVITY_SALEYEARD_POST_PDF_PHOTO_PICK && resultCode == Activity.RESULT_OK) {
            String realPath = ImageFilePath.getPath(this, data.getData());
            mViewModel.upDatePostSaleyardPdfPhoto(realPath);
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            mViewModel.upDatePostPhoto(mCurrentPhotoPath);
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK) {
            String realPath = ImageFilePath.getPath(this, data.getData());
            mViewModel.upDatePostPhoto(realPath);
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_LIVESTOCK_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            // pickup pictures when creating or editing livestock
            if (data.getClipData() != null) {
                List<String> imageUrlList = new ArrayList<>();
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String realPath = ImageFilePath.getPath(this, imageUri);
                    /*Log.d(TAG, "onActivityResult: filesize1: " + (new File(realPath)).length());
                    String newPath = AppUtil.getCompressImageName(this,realPath);
                    Log.d(TAG, "onActivityResult: filesize2: " + (new File(newPath)).length());
*/
                    if (AppUtil.isVideoFile(realPath)) {
                        mViewModel.addVideo(realPath);
                    } else if (AppUtil.isImageFile(realPath)) {

                        mViewModel.addPhoto(realPath);
                        //imageUrlList.add(realPath);
                    }
                }
                // if (imageUrlList.size() > 0) {
                //     mViewModel.addPhoto(imageUrlList);
                // }
            } else if (data.getData() != null) {
                String realPath = ImageFilePath.getPath(this, data.getData());
                if (AppUtil.isVideoFile(realPath)) {
                    mViewModel.addVideo(realPath);
                } else if (AppUtil.isImageFile(realPath)) {
                    mViewModel.addPhoto(realPath);
                    //imageUrlList.add(realPath);
                }

                // mViewModel.addPhoto(realPath);
            }
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_SALEYARD_CHOOSE_MEDIA && resultCode == Activity.RESULT_OK) {
            if (data.getClipData() != null) {
                List<String> imageUrlList = new ArrayList<>();
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    String realPath = ImageFilePath.getPath(this, imageUri);
                    if (AppUtil.isVideoFile(realPath)) {
                        mViewModel.addPostSaleyardVideo(realPath);
                    } else if (AppUtil.isImageFile(realPath)) {

                        mViewModel.addPostSaleyardPhoto(realPath);
                        //imageUrlList.add(realPath);
                    }
                }
                // if (imageUrlList.size() > 0) {
                //     mViewModel.addPhoto(imageUrlList);
                // }
            } else if (data.getData() != null) {
                String realPath = ImageFilePath.getPath(this, data.getData());
                if (AppUtil.isVideoFile(realPath)) {
                    mViewModel.addPostSaleyardVideo(realPath);
                } else if (AppUtil.isImageFile(realPath)) {
                    mViewModel.addPostSaleyardPhoto(realPath);
                    //imageUrlList.add(realPath);
                }

                // mViewModel.addPhoto(realPath);
            }
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_LIVESTOCK_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            mViewModel.addPhoto(mCurrentPhotoPath);
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_SALEYARD_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            mViewModel.addPostSaleyardPhoto(mCurrentPhotoPath);
        } else if
        (requestCode == AppConstants.ACTIVITY_CODE_POST_LIVESTOCK_TAKE_VIDEO && resultCode == RESULT_OK) {
            Uri videoUri = data.getData();


            Log.d(TAG, "onActivityResult: process video" + videoUri.getPath());
            Log.d(TAG, "onActivityResult: process video" + mCurrentVideoPath);


/*            String msgId = UUID.randomUUID().toString();
            Date createdTime = UIUtil.GetUTCdatetimeAsDate();
            Long channelId = mChannelId;
            Long userId = mUserId;
            String msgType = ApiConstants.MESSAGE_TYPE_VIDEO;
            String msg = mCurrentVideoPath;
            String thumbnail = null;
            Integer status = ApiConstants.MSG_STATE_LOADING;


            Message dbChatMessage = new Message(msgId, createdTime, channelId, userId, msgType, msg, thumbnail, status);*/
            mViewModel.addVideo(mCurrentVideoPath);
        } else if (requestCode == AppConstants.ACTIVITY_CODE_POST_SALEYARD_TAKE_VIDEO && resultCode == RESULT_OK) {
            mViewModel.addPostSaleyardVideo(mCurrentVideoPath);

        } else if (requestCode == AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_VIDEO && resultCode == RESULT_OK) {
//            Uri videoUri = data.getData();
            mViewModel.addVideoToNewMarketReport(mCurrentMarketReportVideoPath);

        } else if (requestCode == AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_VIDEO && resultCode == RESULT_OK) {
            String realPath = ImageFilePath.getPath(this, data.getData());
            mViewModel.addVideoToNewMarketReport(realPath);

        } else if (requestCode == AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO && resultCode == RESULT_OK) {

            Log.e("data", "Attachment Take Picture=>" + mCurrentPhotoPath);
            mViewModel.addAttachmentToNewMarketReport(mCurrentPhotoPath);

        } else if (requestCode == AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            String realPath = ImageFilePath.getPath(this, data.getData());
            Log.e("data", "Attachment Choose Picture 1=>" + realPath);

          /*  Uri selectedImage = data.getData();
            String[] filePath = { MediaStore.Images.Media.DATA };
            Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePath[0]);
            String picturePath = c.getString(columnIndex);
            c.close();
            Log.e("data", "Attachment Choose Picture 2=>" + picturePath);*/

            mViewModel.addAttachmentToNewMarketReport(realPath);
        } else if (requestCode == AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_PDF && resultCode == RESULT_OK) {
            String realPath = ImageFilePath.getPath(this, data.getData());
            Log.e("data", "Attachment Choose File =>" + realPath);
            mViewModel.addAttachmentToNewMarketReport(realPath);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public Boolean requestRecordAudioPermission() {

        PermissionStatus status = mAppUtil.getRecordAudioPermissionTag();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (status == PermissionStatus.DONOTASK) {
                ReadStoragePermissionDialogFragment.newInstance(getString(R.string.permission_request_audio), getString(R.string.txt_permission_warning_audio)).
                        show(getSupportFragmentManager(), AppConstants.FRAGMENT_DIALOG);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.RECORD_AUDIO},
                        AppConstants.ACTIVITY_PERMISSION_AUDIO);
            }
            return false;
        } else {
            //Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            // startRecord();
            if (status != PermissionStatus.GRANT) {
                mAppUtil.setAudioRecordPermissionTag(PermissionStatus.GRANT);
            }
            return true;
        }
    }


    // Interface for location
    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            double longval = location.getLongitude();
            double latval = location.getLatitude();
            LatLng currentLocation = new LatLng(latval, longval);
            mAppUtil.setmCurrentLocation(currentLocation);
            mViewModel.updateCurrentLocation(currentLocation);
            mViewModel.updateLocatingStatus(false);
            // Toast.makeText(this, String.format("onLocationChanged: %f, %f", longval, latval), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    // Interface for request login fragment
    @Override
    public void OnRequestLoginLogin() {
        startLoginActivity(LoginFragment.ARG_UI_MODE_LOGIN);
    }

    @Override
    public void OnRequestLoginRegister() {
        startLoginActivity(LoginFragment.ARG_UI_MODE_REGISTER);
    }

    // Interface for Network checker
    @Override
    public void onLoading(boolean bLoading) {
        showProgressBar(bLoading);
    }

    @Override
    public void onLoading(boolean bLoading, boolean bError) {
        showProgressBar(bLoading, bError);
    }

    @Override
    public void onFailed(Integer code, String msg) {
        if (code.equals(SERVER_ERROR_401)) {
            loginRequired();
        } else {
            ActivityUtils.showServerErrorDialog(this, code, msg);
        }
    }

    public void loginRequired() {
        ActivityUtils.showWarningDialog(this, getString(R.string.app_name), getString(R.string.txt_account_required),
                getString(R.string.txt_cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                },
                getString(R.string.txt_login), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //mBinding.navigation.setSelectedItemId(R.id.navigation_me);
                        OnRequestLoginLogin();


                    }
                },
                getString(R.string.txt_register), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OnRequestLoginRegister();
                    }
                }
        );
    }

    // Interface for Network Error Fragment
    @Override
    public void OnNetworkErrorRetry() {
        onBackPressed();
    }

    @Override
    public void onClickLivestock(EntityLivestock livestock) {
        BidDetailFragment fragment = BidDetailFragment.newInstance(mBidDetailFragmentProvider, livestock);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.frameLayout_container,
                android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if (livestock != null && livestock.getId() != null) {
            logFirebaseEventLivestock(livestock);
        }

    }

    @Override
    public void onClickShareLivestock(EntityLivestock livestock) {
        String string_Url = AppConstants.SERVER_URL_DEV + "livestock/" + livestock.getId();

        String string_Subject = "Share poddock: " + livestock.getName();
        ActivityUtils.share(this, string_Subject, string_Url);
    }

    @Override
    public void onClickSavedLivestock(EntityLivestock livestock, ImageView view) {
        if (mAppUtil.getUserId() == null) {
            loginRequired();
            return;
        }
        Boolean isLiked = (Boolean) view.getTag();
        UIUtils.updateSavedIcon(this, view, !isLiked, livestock);
        mViewModel.setLivestockLike(livestock.getId(), !isLiked).observe(this, new DataObserver<Void>() {
            @Override
            public void onSuccess(Void data) {

            }

            @Override
            public void onError(Integer code, String msg) {
                Boolean isLiked = (Boolean) view.getTag();
                UIUtils.updateSavedIcon(MainActivity.this, view, !isLiked, livestock);
                onFailed(code, msg);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onDirty() {

            }
        });
    }

    @Override
    public void onClickLivestockList(List<EntityLivestock> livestock) {
        if (livestock.size() > 1) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    LivestockListFragment.newInstance(mLivestockListFragmentProvider.get(), livestock), R.id.frameLayout_container);
        } else {
            onClickLivestock(livestock.get(0));
        }
    }

    @Override
    public void onUpdateLivestock(EntityLivestock livestock) {
        Fragment f = PostLivestockFragment.newInstance(mPostLivestockFragmentFragmentProvider, livestock);
        mViewModel.updatePostLivestocks(livestock);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), f, R.id.frameLayout_container);
    }

    @Override
    public void onConfirmLivestock(EntityLivestock livestock, List<Media> mediaListToDelete, Boolean hasChanged) {
        Fragment f = BidDetailFragment.newInstance(mBidDetailFragmentProvider, livestock, BidDetailFragment.ARG_DATA_UI_MODE_POST_CONFIRM, mediaListToDelete, hasChanged);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), f, R.id.frameLayout_container);
    }

    // Interface for search top category page
    @Override
    public void OnClickSaleyardAuctions() {

        SearchFilterPublicSaleyard newSearchFilter = new SearchFilterPublicSaleyard();
        String mSaleyardTypeNormal = SaleyardType.Weaner.name() + "," + SaleyardType.Feature.name() + "," + SaleyardType.Store.name();
        newSearchFilter.setType(mSaleyardTypeNormal);
        SearchResultSaleyardFragment fragment = mSearchResultSaleyardFragmentProvider.get();
        fragment.setmSaleyardType(mSaleyardTypeNormal);
        mViewModel.setSaleyardSearchFilterNew(newSearchFilter);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.frameLayout_container);
    }

    @Override
    public void OnClickPaddockSales() {
        SearchFilterPublicLivestock newSearchFilter = new SearchFilterPublicLivestock();
        mViewModel.setLivestockSearchFilterNew(newSearchFilter);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                SearchLivestockResultFragment.newInstance(mSearchLivestockResultFragmentProvider.get(), false),
                R.id.frameLayout_container);
    }

    @Override
    public void OnClickAuctionPlus() {
        SearchFilterPublicLivestock newSearchFilter = new SearchFilterPublicLivestock();
        mViewModel.setLivestockSearchFilterNew(newSearchFilter);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                SearchLivestockResultFragment.newInstance(mSearchLivestockResultFragmentProvider.get(), true),
                R.id.frameLayout_container);


    }

    @Override
    public void OnClickPrimeSaleyards() {
        SearchFilterPublicSaleyard newSearchFilter = new SearchFilterPublicSaleyard();
        String type = SaleyardType.Prime.name();
        newSearchFilter.setType(type);
        SearchResultSaleyardFragment fragment = mSearchResultSaleyardFragmentProvider.get();
        fragment.setmSaleyardType(type);
        mViewModel.setSaleyardSearchFilterNew(newSearchFilter);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mSearchResultSaleyardFragmentProvider.get(), R.id.frameLayout_container);


    }

    @Override
    public void OnClickMarketReports() {
        ActivityUtils.addTagFragmentToActivity(getSupportFragmentManager(), mMarketReportFragmentProvider.get(), R.id.frameLayout_container, MarketReportFragment.class.getName());
    }

    @Override
    public void OnclickHerdLive() {
        if (mAppUtil.getUserId() == null) {
            loginRequired();
            return;
        }
        // start herdLive code....

        ActivityUtils.addTagFragmentToActivity(getSupportFragmentManager(), mHerdLiveFragmentProvider.get(), R.id.frameLayout_container, HerdLiveFragment.class.getName());
        //ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mSearchResultSaleyardFragmentProvider.get(), R.id.frameLayout_container);
    }

    public void OpenSaleyardItem(EntitySaleyard entitySaleyard, EntitySaleyard oldEntitySaleyard) {
      /*  Integer id = oldEntitySaleyard != null ? oldEntitySaleyard.getId() : null;
        SaleyardDetailFragment saleyardDetailFragment = SaleyardDetailFragment.newInstance(mSaleyardDetailFragmentProvider, id,
                entitySaleyard,oldEntitySaleyard);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), saleyardDetailFragment, R.id.frameLayout_container);*/

        SaleyardConfrimFragment f = SaleyardConfrimFragment.newInstance(mLazyFactory, mMapMarkerFragmentLazy,
                entitySaleyard, oldEntitySaleyard);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), f, R.id.frameLayout_container);


    }

    public void OpenSaleyardItem(EntitySaleyard entitySaleyard) {
    /*    SaleyardDetailFragment saleyardDetailFragment = SaleyardDetailFragment.newInstance(mSaleyardDetailFragmentProvider, entitySaleyard.getId());
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), saleyardDetailFragment, R.id.frameLayout_container);
   */
        // OpenSaleyardItem(null,entitySaleyard);


        SaleyardDetailFragmentV2 f = SaleyardDetailFragmentV2.newInstance(mLazyFactory, mMapMarkerFragmentLazy,
                null, entitySaleyard);
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), f, R.id.frameLayout_container);

        logFirebaseEventSaleyard(entitySaleyard);


    }


    // interface for EntitySaleyard auction
    @Override
    public void OnClickSaleyard(EntitySaleyard entitySaleyard) {
        OpenSaleyardItem(entitySaleyard);
    }

    @Override
    public void OnClickSaleyardList(List<EntitySaleyard> entitySaleyard) {

        if (entitySaleyard.size() > 1) {
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), SaleyardListFragment.newInstance(mSaleyardListFragmentProvider.get(), entitySaleyard), R.id.frameLayout_container);
        } else {
            OnClickSaleyard(entitySaleyard.get(0));
        }
    }


    @Override
    public void OnClickSaleyardFragmentMediaButton(List<Media> mediaList) {


        if (mediaList == null || mediaList.size() == 0) {
            Toast.makeText(this, getString(R.string.txt_no_media_for_this), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, GalleryMainActivity.class);
            ArrayList<Media> arrayList = new ArrayList<>();
            arrayList.addAll(mediaList);
            intent.putParcelableArrayListExtra(GalleryMainActivity.AGR_MEDIA_ARRAY, arrayList);
            startActivity(intent);
        }

    }

    @Override
    public void onClickSaleyardFragmentContactAgentButton(User user) {

    }
    // end


    // Interface stream
    @Override
    public void OnClickStreamItem(StreamItem item) {

    }

    // map interface


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Marker mPerth;
        Marker mSydney;
        Marker mBrisbane;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        LatLng brisbane = new LatLng(-27.444, 153);
        mSydney = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mSydney.setTag("This is sydney");


        mBrisbane = mMap.addMarker(new MarkerOptions().position(brisbane).title("Marker in brisbane"));
        mBrisbane.setTag("This is brisbane");

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, (String) marker.getTag(), Toast.LENGTH_LONG).show();
        return false;
    }

    // Map Marker Listener

    @Override
    public void OnClickLivestockMarkerOnMap(List<EntityLivestock> advertisement) {

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), LivestockListFragment.newInstance(mLivestockListFragmentProvider.get(), advertisement), R.id.frameLayout_container);
    }

    @Override
    public void OnClickSaleyardMarkerOnMap(List<EntitySaleyard> entitySaleyard) {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), SaleyardListFragment.newInstance(mSaleyardListFragmentProvider.get(), entitySaleyard), R.id.frameLayout_container);
    }

    @Override
    public void OnSelectSaleyard(EntitySaleyard entitySaleyard) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout_container);
        if (f instanceof SelectSyleyardFragment) {
            //CreateMarketReportFragment createMarketReportFragment = mCreateMarketReportFragmentProvider.get();
            //createMarketReportFragment.setEntitySaleyard(entitySaleyard);
            mViewModel.refreshSelectedSaleyardMarketReport(entitySaleyard);
            onBackPressed();        // back to previous page
        }
    }

    @Override
    public void onClickSavedSaleyard(EntitySaleyard entitySaleyard, ImageView view) {
        if (mAppUtil.getUserId() == null) {
            loginRequired();
            return;
        }


        final Boolean isLiked = entitySaleyard.getHas_liked();
        UIUtils.updateSavedIcon(this, view, !isLiked, entitySaleyard);
        mViewModel.setSaleyardLike(entitySaleyard.getId(), !isLiked).observe(this, new DataObserver<Void>() {
            @Override
            public void onSuccess(Void data) {
            }

            @Override
            public void onError(Integer code, String msg) {
                UIUtils.updateSavedIcon(MainActivity.this, view, isLiked, entitySaleyard);
                onFailed(code, msg);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onDirty() {

            }
        });
    }

    @Override
    public void onClickShareSaleyard(EntitySaleyard entitySaleyard) {
        String string_Url = AppConstants.SERVER_URL_DEV + "entitySaleyard/" + entitySaleyard.getId();
        String string_Subject = "Share EntitySaleyard: " + entitySaleyard.getName();
        ActivityUtils.share(this, string_Subject, string_Url);
    }

    @Override
    public void OnClickLivestockMarkerOnMap(EntityLivestock advertisement) {
        Intent intent = new Intent(this, MakeBidActivity.class);
        intent.putExtra(MakeBidActivity.TAG_DATA, advertisement);
        startActivity(intent);
    }

    @Override
    public void OnClickSaleyardMarkerOnMap(EntitySaleyard entitySaleyard) {
        OpenSaleyardItem(entitySaleyard);
    }

    // Interface press back button
    @Override
    public void OnClickGoBackButton() {
        onBackPressed();
    }

    // Interface for Bid Item

    @Override
    public void OnClickStreamItem(Bid item) {

    }

    // Interface for market report item
    @Override
    public void OnClickMarketReports(MarketReport marketReport) {
        // ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), MarketReportDetailFragment.newInstance(mMarketReportDetailFragmentProvider,marketReport), R.id.frameLayout_container);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout_container);
        if (f instanceof MarketReportFragment) {
            ((MarketReportFragment) f).setRefreshDataMode(false);
        }
        // CreateMarketReportFragment createMarketReportFragment = CreateMarketReportFragment.newInstance(mCreateMarketReportFragmentProvider, marketReport);
        MarketReportDetailFragment createMarketReportFragment = MarketReportDetailFragment.newInstance(mMarketReportDetailFragmentProvider, marketReport);

        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), createMarketReportFragment, R.id.frameLayout_container);

        if (marketReport != null && marketReport.getId() != null) {
            logFirebaseEventPlayMarketReport(marketReport);
        }

    }

    // Interface for post a new market report


    public String getmAudioFileName() {
        return mAudioFileName;
    }

    public Boolean checkoutPermission() {
        return requestRecordAudioPermission();
    }


    @Override
    public Boolean AudioRecordPermission() {
        return requestRecordAudioPermission();
    }



    // Interface for contact card
    @Override
    public void OnClickPhoneCall(User user) {
/*        if (mAppUtil.getAccessToken() == null) {
            onFailed(SERVER_ERROR_401, "");
            return;
        }*/

        if (!StringUtils.isEmpty(user.getPhone())) {
            ActivityUtils.startCallingApp(this, user.getPhone());
        } else {
            ActivityUtils.showWarningDialog(this, getString(R.string.app_name), getString(R.string.txt_no_phone_number));
        }
    }

    @Override
    public void OnClickSendEmail(User user) {
/*        if (mAppUtil.getAccessToken() == null) {
            onFailed(SERVER_ERROR_401, "");
            return;
        }*/


        if (!StringUtils.isEmpty(user.getEmail())) {
            ActivityUtils.startSendMailApp(this, user.getEmail());
        } else {
            ActivityUtils.showWarningDialog(this, getString(R.string.app_name), getString(R.string.txt_no_email_address));

        }
    }

    @Override
    public void OnClickSendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            ActivityUtils.openSmsMsgAppFnc(this, user.getPhone(), "");
        } else {
            ActivityUtils.showWarningDialog(this, getString(R.string.app_name), getString(R.string.txt_no_phone_number));
        }
    }

    @Override
    public void OnClickUserAvatar(User user) {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), UserProfileFragment.newInstance(mUserProfileFragmentProvider, user.getId(), false), R.id.frameLayout_container);

    }

    @Override
    public void OnClickBidOffer(EntityLivestock livestock) {
        if (mAppUtil.getUserId() == null) {
            loginRequired();
            return;
        }
        if (mAppUtil.isAgentorProducer(livestock)) {
            ActivityUtils.showWarningDialog(this, getString(R.string.app_name), getString(R.string.txt_bid_error));
            return;
        } else {
            Fragment fragment = MakeBidFragment.newInstance(mMakeBidFragmentProvider, livestock);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.frameLayout_container);
        }
    }

    @Override
    public void OnClickMessage(MyChat chat, EntityLivestock livestock) {


    }

    @Override
    public Boolean isHasLocationPermission() {
        return isHasLocationPermission(true);
    }


    public Boolean isHasLocationPermission(Boolean popup) {
        Boolean ret = false;
        PermissionStatus status = mAppUtil.getLocationPermissionTag();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (status == PermissionStatus.DONOTASK && popup) {
                ReadStoragePermissionDialogFragment.newInstance(getString(R.string.permission_request_location), getString(R.string.location_is_denied)).
                        show(getSupportFragmentManager(), AppConstants.FRAGMENT_DIALOG);
            } else {
            }
        } else {
            if (status != PermissionStatus.GRANT) {
                mAppUtil.setLocationPermissionTag(this, PermissionStatus.GRANT);
            }
            ret = true;
        }
        return ret;
    }


    @Override
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                AppConstants.ACTIVITY_PERMISSION_LOCATION);
    }

    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                AppConstants.ACTIVITY_PERMISSION_STORAGE);
    }

/*    public void UpdatePhoto(int actionCode, int permissionCode) {
        PermissionStatus status = UIUtil.getStoragePermissionTag(this);
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (status == PermissionStatus.DONOTASK) {
                ReadStoragePermissionDialogFragment.newInstance(getString(R.string.permission_request), getString(R.string.storage_is_denied)).
                        show(getSupportFragmentManager(), ApiConstants.FRAGMENT_DIALOG);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        permissionCode);
            }
        } else {
            popupPhotoDialog(actionCode);
            if (status != PermissionStatus.GRANT) {
                UIUtil.setStoragePermissionTag(this, PermissionStatus.GRANT);
            }
        }
    }*/


    @Override
    public void requestLocation(LocationListener locationListener) {
        mLocationListener = locationListener;
        try {
            mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            try {
                if ((!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) && (!mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
                    ReadStoragePermissionDialogFragment.newInstance(getString(R.string.warning), getString(R.string.location_disable)).show(getSupportFragmentManager(), "");
                    return;
                } else {
                    //mBinding.textViewCityName.setText(R.string.obtaining_location);
                }
            } catch (Exception ex) {
                return;
            }
            Location m = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 500, locationListener);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 500, locationListener);
            mViewModel.updateLocatingStatus(true);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.i(TAG, "requestLocation: " + e.getMessage());
            ActivityUtils.showWarningDialog(this, getString(R.string.app_name), e.getMessage());
        }
    }

    // Interface for Post

    @Override
    public void onClickPost(Post post) {

    }


    @Override
    public void OnClickOpenLivestockMapView(List<EntityLivestock> list) {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                MapLivestockFragment.newInstance(mMapLivestockFragmentProvider, list), R.id.frameLayout_container);
    }

    @Override
    public void OnClickOpenSaleyardMapView(List<EntitySaleyard> list) {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                MapSaleyardFragment.newInstance(mMapSaleyardFragmentProvider, list), R.id.frameLayout_container);
    }

    public void setToolBar(CustomerToolbar toolBar) {
        mViewModel.updateToolBar(toolBar);
    }


    public void showNavigationBar(Boolean bShow) {
        mViewModel.showNavigationBar(bShow);
    }


    // Location update


    @Override
    public void updateSaleyardSearchLocation() {
        ActivityUtils.startAddressAutoCompleteActivity(this, this, AppConstants.AUTOCOMPLETE_REQUEST_CODE_SALEYARD);
    }

    @Override
    public void updateLivestockSearchLocation() {
        ActivityUtils.startAddressAutoCompleteActivity(this, this, AppConstants.AUTOCOMPLETE_REQUEST_CODE_LIVESTOCK);
    }

    @Override
    public void updateCreateADLocation() {
        ActivityUtils.startAddressAutoCompleteActivity(this, this, AppConstants.AUTOCOMPLETE_REQUEST_CODE_CREATE_LIVESTOCK);

    }

    @Override
    public void updateCreateLivestockLocation() {
        ActivityUtils.startAddressAutoCompleteActivity(this, this, AppConstants.AUTOCOMPLETE_REQUEST_CODE_CREATE_LIVESTOCK);

    }

    // Interface for logout
    @Override
    public void OnLogout() {
        mViewModel.clearData();     // clear cached data in memory
        mAppUtil.logoutCleanUp();       // clear cached data in disk
        Intent newIntent = new Intent(this, FlashActivity.class);
        finish();
        startActivity(newIntent);

    }


    @Override
    public void OnClickADTabPaddockSales() {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mMyAdFragmentProvider.get(), R.id.frameLayout_container);
    }

    @Override
    public void OnClickADTabPrimeSaleyards() {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mPrimeSaleyardsFragmentProvider.get(), R.id.frameLayout_container);

    }


/*    @Override
    public void onConfirmSaleyard(EntitySaleyard saleyard, List<Media> mediaListToDelete, Boolean hasChanged) {
        OpenSaleyardItem(saleyard);
    }*/

    @Override
    public void onUpdateSaleyard(EntitySaleyard entitySaleyard) {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), PostPrimSaleyardFragment.newInstance(mPostPrimSaleyardFragmentProvider, entitySaleyard), R.id.frameLayout_container);
    }

    @Override
    public void onConfirmSaleyardV2(EntitySaleyard newEntitySaleyard, EntitySaleyard oldEntitySaleyard) {
        OpenSaleyardItem(newEntitySaleyard, oldEntitySaleyard);
    }

    @Override
    public void onClickOpenMediaList(List<Media> mediaList) {
        if (mediaList == null || mediaList.size() == 0) {
            Toast.makeText(this, getString(R.string.txt_no_media_for_this), Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, GalleryMainActivity.class);
            ArrayList<Media> arrayList = new ArrayList<>();
            arrayList.addAll(mediaList);
            intent.putParcelableArrayListExtra(GalleryMainActivity.AGR_MEDIA_ARRAY, arrayList);
            startActivity(intent);
        }
    }

    @Override
    public void onClickNotification(HerdNotification notification) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(StringUtils.defaultString(notification.getUrl()))));
    }

    @Override
    public void onClickHerdLiveItem(Integer id) {


        mViewModel.getSaleyardStreamById(String.valueOf(id)).observe(this, new DataObserver<ResSaleyardStreamDetails>() {

            @Override
            public void onSuccess(ResSaleyardStreamDetails data) {
                // mNetworkListener.onLoading(false);
                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), HerdLiveDetailFragment.newInstance(mHerdLiveDetailFragmentProvider, data), R.id.frameLayout_container);
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
        });

    }
}


