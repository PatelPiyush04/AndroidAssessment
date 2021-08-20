package com.theherdonline.ui.profile;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentAccountBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdinterface.LogoutInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.payment.PaymentFragment;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import dagger.Lazy;


public class AccountFragment extends HerdFragment {

    FragmentAccountBinding mBinding;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;



    @Inject
    Lazy<PaymentFragment> mPaymentFragmentProvider;


    @Inject
    Lazy<EditProfileFragment> mEditProfileFragmentProvider;

    @Inject
    AppUtil mAppUtil;


    MainActivityViewModel mMainViewModel;

    private LogoutInterface mLogoutListener;


    @Inject
    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(getString(R.string.txt_account),mExitListener,null,null,null,null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        mBinding.linearAccountDetail.setOnClickListener(l->{
            ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                    mEditProfileFragmentProvider.get()
                    ,R.id.frameLayout_container);
        });
        mBinding.linearPaymentDetail.setOnClickListener(l->{
            ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                    mPaymentFragmentProvider.get()
                    ,R.id.frameLayout_container);
        });
        mBinding.linearLogout.setOnClickListener(l->{
            ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_confirm_logout),
                  getString(R.string.txt_logout),
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mLogoutListener.OnLogout();
                }
            }, ContextCompat.getColor(getContext(),android.R.color.holo_red_dark),

                    getString(R.string.txt_cancel),new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            },ContextCompat.getColor(getContext(),R.color.colorDarkBlue));
        });
        return mBinding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LogoutInterface)
        {
            mLogoutListener = (LogoutInterface) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    + " must implement LogoutInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLogoutListener = null;
    }
}
