package com.theherdonline.ui.Flash;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.ActionBar;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.theherdonline.BuildConfig;
import com.theherdonline.R;
import com.theherdonline.databinding.ActivityFlashBinding;
import com.theherdonline.db.DataConverter;
import com.theherdonline.db.entity.ResVersion;
import com.theherdonline.db.entity.Role;
import com.theherdonline.db.entity.User;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;

import org.apache.commons.lang3.BooleanUtils;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class FlashActivity extends DaggerAppCompatActivity {


    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    @Inject
    AppUtil mAppUtil;

    private FlashActivityViewModel mViewModel;

    private ProgressBar mProgressBar;

    private CardView mCardViewUpdate;

    boolean startFromNotification = false;

    private DataObserver<User> mInitialResponse = new DataObserver<User>() {
        @Override
        public void onSuccess(User data) {

            Log.e("Data", "mInitialResponse 1" + data);
            if (data != null) {
                Log.e("Data", "mInitialResponse 2" + data);
                mAppUtil.updateUserId(data.getId());
                Role role = DataConverter.ListRole2Role(data.getRoles());
                mAppUtil.updateUserType(DataConverter.Role2UserType(role));
                mAppUtil.updateDontshowAgain(false);
            }
            Log.e("Data", "mInitialResponse 3" + data);
            mProgressBar.setVisibility(View.GONE);
            Intent intent = new Intent(FlashActivity.this, MainActivity.class);
            // startFromNotification = true;
            intent.putExtra(MainActivity.ARG_START_FROM_NOTIFICATION, startFromNotification);
            finish();
            startActivity(intent);

        }

        @Override
        public void onError(Integer code, String msg) {
            Log.e("Data", "mInitialResponse 11" + msg);
            mProgressBar.setVisibility(View.INVISIBLE);
            networkError(code, msg);
        }

        @Override
        public void onLoading() {
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onDirty() {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_flash);
        ActivityFlashBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_flash);
        //FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        Log.e("Data", "On create ");
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().keySet().contains("data")) {
            // String messageId = getIntent().getStringExtra("source");
            // Toast.makeText(this,"Start from notification " /*+ messageId*/, Toast.LENGTH_LONG).show();
            startFromNotification = true;
        } else {
            startFromNotification = false;
        }


        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            binding.textViewVersion.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mProgressBar = findViewById(R.id.progressBar);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(FlashActivityViewModel.class);

        mCardViewUpdate = findViewById(R.id.cardView_new_version);
        findViewById(R.id.button_go_to_app_store).setOnClickListener(l -> {
            String appPackageName = getPackageName();
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            finish();
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.e("Data", "On Start ");
        mViewModel.getmLiveDataIntiApplication().observe(this, mInitialResponse);
        mViewModel.getmLvResVersion().observe(this, new DataObserver<ResVersion>() {
            @Override
            public void onSuccess(ResVersion data) {


                    Log.e("Data", "On Start success " + data);
                    Integer versionNumber = data.getVersionNumber();
                    Integer localVersionNumber = BuildConfig.VERSION_CODE;
                    Log.e("Data", "On Start success " + versionNumber + "\nLocval num=>" + localVersionNumber);

                    if (localVersionNumber < versionNumber && BooleanUtils.isTrue(data.getAndroid_force_update())) {
                        findViewById(R.id.cardView_new_version).setVisibility(View.VISIBLE);
                        Log.e("Data", "On Start Inside If statement");
                      /*  String appPackageName = getPackageName();
                        ActivityUtils.showWarningDialog(FlashActivity.this, getString(R.string.app_name), getString(R.string.txt_forced_update_text), getString(R.string.txt_update), getString(R.string.txt_cancel),
                                (dialog, which) -> {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    finish();
                                }, (dialog, which) -> finish());*/
                    } else {
                        // This build is newer version
                        Log.e("Data", "On Start Inside else statement");
                        mViewModel.refreshApplication();
                    }

            }

            @Override
            public void onError(Integer code, String msg) {
                mViewModel.refreshApplication();

                // mProgressBar.setVisibility(View.INVISIBLE);
                // networkError(code,msg);
            }

            @Override
            public void onLoading() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDirty() {

            }
        });

        mViewModel.refreshVersion();
    }

    public void networkError(Integer code, String msg) {
        ActivityUtils.showWarningDialog(FlashActivity.this,
                getString(R.string.warning_dialog_title),
                ActivityUtils.netWorkErrorInfo(FlashActivity.this, msg), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
    }
}
