package com.theherdonline.ui.login;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.theherdonline.BuildConfig;
import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentLoginBinding;
import com.theherdonline.db.DataConverter;
import com.theherdonline.db.entity.ResLogin;
import com.theherdonline.db.entity.User;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.api.Resource;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import dagger.Lazy;
import dagger.android.support.DaggerFragment;


import javax.inject.Inject;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import org.apache.commons.collections4.ListUtils;

import java.util.Arrays;

import static android.app.Activity.RESULT_OK;


public class LoginFragment extends DaggerFragment {

    public static final String ARG_UI_MODE = "login-ui-mode";
    public static final Integer ARG_UI_MODE_LOGIN = 1;
    public static final Integer ARG_UI_MODE_REGISTER = 2;


    private static final String TAG = LoginFragment.class.getName();
    private NetworkInterface mNetworkListener;

    CallbackManager mCallbackManager;// = CallbackManager.Factory.create();


    public static LoginFragment newInstance(LoginFragment loginFragment, int uiMode) {
        Bundle args = new Bundle();
        args.putInt(ARG_UI_MODE,uiMode);
        loginFragment.setArguments(args);
        return loginFragment;
    }

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    @Inject
    AppUtil mAppUtil;

    LoginActivityViewModel mViewModel;

    FragmentLoginBinding mBinding;

    private Integer mUiMode;


    private User mRegisterUser;


    private String mRegisterPassword;

    private static final String EMAIL = "email";



    private DataObserver<ResLogin> mObserver = new DataObserver<ResLogin>() {

        @Override
        public void onSuccess(ResLogin data) {
           // mNetworkListener.OnLoginSuccess();
           mAppUtil.updateAccessToken(data.getAccess_token());
           mAppUtil.updateRefreshToken(data.getRefresh_token());
           mViewModel.getUserInf();

           if (mBinding.checkbotRememberMe.isChecked())
           {
               mAppUtil.setLoginEmailAddress(mViewModel.mUsername);
               mAppUtil.setLoginPassword(mViewModel.mPassword);
                mAppUtil.setEnableRemember(true);
           }
           else
            {
                mAppUtil.setLoginEmailAddress("");
                mAppUtil.setLoginPassword("");
                mAppUtil.setEnableRemember(false);
            }



        }

        @Override
        public void onError(Integer code, String msg) {
            mNetworkListener.onLoading(false);
            mNetworkListener.onFailed(code,msg);
        }

        @Override
        public void onLoading() {
            mNetworkListener.onLoading(true);
        }

        @Override
        public void onDirty() {
            mNetworkListener.onLoading(false);
            mBinding.editEmail.setText("");
            mBinding.editPassword.setText("");
        }

        @Override
        public void onChanged(@Nullable Resource<ResLogin> resLoginResource) {
            super.onChanged(resLoginResource);
        }

    };

    private DataObserver<User> mObserverUserInfo = new DataObserver<User>() {
        @Override
        public void onSuccess(User data) {
            mAppUtil.updateUserId(data.getId());
            mAppUtil.updateUserType(DataConverter.Roles2UserType(data.getRoles()));
            mAppUtil.updateUserName(data.getEmail());
            mAppUtil.updateAgentId(data.getAgentId());
            mAppUtil.updatePermissionType(ListUtils.emptyIfNull(data.getPermissions()));
            getActivity().setResult(RESULT_OK);
            getActivity().finish();
        }

        @Override
        public void onError(Integer code, String msg) {
            mNetworkListener.onLoading(false);
            mNetworkListener.onFailed(code,msg);
        }

        @Override
        public void onLoading() {
            mNetworkListener.onLoading(true);
        }

        @Override
        public void onDirty() {
            mNetworkListener.onLoading(false);
            mBinding.editEmail.setText("");
            mBinding.editPassword.setText("");
        }

        @Override
        public void onChanged(@Nullable Resource<User> resLoginResource) {
            super.onChanged(resLoginResource);
        }

    };


    @Inject
    public LoginFragment() {
        // Required empty public constructor
    }







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

        mBinding.buttonFacebaookLogin.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.background_button_facebook));
        mBinding.buttonFacebaookLogin.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
        if (mUiMode == null)
        {
            mUiMode = getArguments().getInt(ARG_UI_MODE);
        }
        if (mUiMode == null || mUiMode == ARG_UI_MODE_LOGIN)
        {
            mBinding.linearRegister.setVisibility(View.GONE);
            mBinding.linearLogin.setVisibility(View.VISIBLE);
        }
        else
        {
            mBinding.linearRegister.setVisibility(View.VISIBLE);
            mBinding.linearLogin.setVisibility(View.GONE);
        }


        if (mAppUtil.getEnableRemember())
        {
            String name = mAppUtil.getLoginEmailAddress();
            String password = mAppUtil.getLoginPassword();
            mBinding.editEmail.setText(name);
            mBinding.editPassword.setText(password);
            mBinding.checkbotRememberMe.setChecked(true);
        }
        else
        {
            mBinding.checkbotRememberMe.setChecked(false);
            mBinding.editEmail.setText("");
            mBinding.editPassword.setText("");
        }



        mRegisterUser = new User();

        // Login
        mBinding.buttonLogin.setOnClickListener(l->{
            String username = mBinding.editEmail.getText().toString().trim();
            if (TextUtils.isEmpty(username))
            {
                // username is wrong
                ActivityUtils.showWarningDialog(getContext(),
                        getString(R.string.app_name),
                        getString(R.string.txt_username_invalid));
                return;
            }
            String password = mBinding.editPassword.getText().toString().trim();
            if (TextUtils.isEmpty(password))
            {
                // password is invalid
                ActivityUtils.showWarningDialog(getContext(),
                        getString(R.string.app_name),
                        getString(R.string.txt_username_invalid));
                return;
            }
            mViewModel.login(username,password);
            View view = getView();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        // Register
        mBinding.buttonRegister.setOnClickListener(l->{
            if (inputChecking())
            {
                String password = mBinding.editRegPassword.getText().toString();
                mViewModel.createUer(mRegisterUser, password);
            }
        });

        if (BuildConfig.DEBUG)
        {
            mBinding.buttonChangeUser.setOnClickListener(l->{
                mBinding.editPassword.setText(BuildConfig.TEST_ACCOUNT_PASSWORD);
                if (mBinding.buttonChangeUser.isChecked())
                {
                    mBinding.editEmail.setText(BuildConfig.TEST_ACCOUNT_AGENT);
                }
                else
                {
                    mBinding.editEmail.setText(BuildConfig.TEST_ACCOUNT_PRODUCER);
                }
            });
            mBinding.buttonChangeUser.setVisibility(View.VISIBLE);
        }
        else
        {
         //   mBinding.editEmail.setText("");
         //   mBinding.editPassword.setText("");
            mBinding.buttonChangeUser.setVisibility(View.GONE);
        }


        mBinding.tvForgetPassword.setOnClickListener(l->{

            ActivityUtils.showWarningDialog(getContext(), getString(R.string.txt_forgot_password),
                    getString(R.string.txt_open_web_password),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String url = AppConstants.SERVER_URL_DEV + AppConstants.SERVER_URL_FORGOT_PASSWORD;
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url));
                            getActivity().startActivity(i);
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

        });

        mBinding.imageClose.setOnClickListener(l->{
            getActivity().finish();
        });

        // Register a new user
        mBinding.tvRegister.setOnClickListener(l->{
            mBinding.linearLogin.setVisibility(View.GONE);
            mBinding.linearRegister.setVisibility(View.VISIBLE);
        });


        mBinding.tvLogin.setOnClickListener(l->{
            mBinding.linearLogin.setVisibility(View.VISIBLE);
            mBinding.linearRegister.setVisibility(View.GONE);
        });




        // Login from facebook

        //loginButton = (LoginButton) findViewById(R.id.login_button);
        mBinding.buttonFacebaookLogin.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration


        mBinding.buttonFacebaookLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken().getToken());
               // Toast.makeText(getContext(),"onSuccess:" + loginResult.getAccessToken().getToken() , Toast.LENGTH_LONG).show();
                mViewModel.loginWithFackbook(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {
                // App code
                Log.d(TAG, "onCancel: ");
                Toast.makeText(getContext(),"onCancel", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d(TAG, "onError: ");
                Toast.makeText(getContext(),exception.getMessage(), Toast.LENGTH_LONG).show();

            }
        });

        mBinding.editEmail.setFocusable(true);
        mBinding.editEmail.setFocusableInTouchMode(true);
        mBinding.editPassword.setFocusable(true);
        mBinding.editPassword.setFocusableInTouchMode(true);
        mBinding.editEmail.requestFocus();
        return mBinding.getRoot();
    }


    boolean inputChecking() {

        String userFirstName = mBinding.editFirstName.getText().toString();
        String userLastName = mBinding.editLastName.getText().toString();
        String userEmail = mBinding.editRegEmail.getText().toString();
        String userPassword = mBinding.editRegPassword.getText().toString();
        String userPasswordConfirm = mBinding.editRegPasswordConfirm.getText().toString();
        String userPhoneNumber = mBinding.editPhoneNumber.getText().toString();

        if (!ActivityUtils.checkInput(getContext(),getString(R.string.txt_firstname), userFirstName))
        {
            return false;
        }
        if (!ActivityUtils.checkInput(getContext(),getString(R.string.txt_lastname), userLastName))
        {
            return false;
        }

        if (!ActivityUtils.checkEmailAddress(getContext(),userEmail,getString(R.string.txt_email)))
        {
            return false;
        }
        if (!ActivityUtils.checkPassword(getContext(),userPassword,userPasswordConfirm))
        {
            return false;
        }
        if (!ActivityUtils.checkInput(getContext(),getString(R.string.txt_phone_number), userPhoneNumber))
        {
            return false;
        }
        mRegisterPassword = userPassword;
        mRegisterUser.setFirstName(userFirstName);
        mRegisterUser.setLastName(userLastName);
        mRegisterUser.setEmail(userEmail);
        mRegisterUser.setPhone(userPhoneNumber);
        return true;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(LoginActivityViewModel.class);
        mViewModel.getmLiveDataLogin().observe(this, mObserver);
        mViewModel.getmLiveDataUserInfo().observe(this,mObserverUserInfo);

        // for Register
        mViewModel.getmLDResNewUser().observe(this, new DataObserver<User>() {
            @Override
            public void onSuccess(User data) {
                mNetworkListener.onLoading(false);
                // Register done, start login
                String password = mBinding.editRegPassword.getText().toString().trim();
                String username = mBinding.editRegEmail.getText().toString().trim();
                mViewModel.login(username,password);
            }

            @Override
            public void onError(Integer code, String msg) {
                mNetworkListener.onLoading(false);
                mNetworkListener.onFailed(code,msg);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NetworkInterface) {
            mNetworkListener = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mCallbackManager = ((LoginActivity)getActivity()).callbackManager;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNetworkListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnLoginSuccess();
        void OnLoginLoading(Boolean bShow);
        void OnClickRegister();
        void OnClose();

    }

}
