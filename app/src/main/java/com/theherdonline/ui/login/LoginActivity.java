package com.theherdonline.ui.login;

import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.ActivityLoginBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

import static com.theherdonline.ui.login.LoginFragment.ARG_UI_MODE;

public class LoginActivity extends DaggerAppCompatActivity implements
        NetworkInterface, RememberInterface {

    @Inject
    Lazy<LoginFragment> mLoginFragmentProvider;

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    @Inject
    AppUtil mAppUtil;

    ActivityLoginBinding mBinding;

    CallbackManager callbackManager = CallbackManager.Factory.create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int uiMode =  getIntent().getIntExtra(ARG_UI_MODE, LoginFragment.ARG_UI_MODE_LOGIN);



        LoginManager.getInstance().logOut();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                LoginFragment.newInstance(mLoginFragmentProvider.get(),uiMode)
                ,R.id.frameLayout_container);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void showProgressBar(boolean bShow) {
        mBinding.loginProgress.setVisibility(bShow ? View.VISIBLE : View.INVISIBLE);
        mBinding.frameLayoutContainer.setVisibility(!bShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showProgressBar(Boolean bShow) {
        mBinding.frameLayoutContainer.setVisibility(bShow ? View.INVISIBLE : View.VISIBLE);
        mBinding.loginProgress.setVisibility(!bShow ? View.INVISIBLE : View.VISIBLE);
    }

    public void showProgressBar(Boolean bShow, Boolean bError) {
        mBinding.frameLayoutContainer.setVisibility(bShow || bError ? View.INVISIBLE : View.VISIBLE);
        mBinding.loginProgress.setVisibility(!bShow ? View.INVISIBLE : View.VISIBLE);
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
        ActivityUtils.showServerErrorDialog(this,code,msg);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void updateEmailPassword(String email, String password) {
        mAppUtil.setLoginEmailAddress(email);
        mAppUtil.setLoginPassword(password);
    }
}
