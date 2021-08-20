package com.theherdonline.ui.main;

import android.content.Context;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentRequestLoginBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.login.AttentionActivity;

import dagger.Lazy;

public class RequestLoginFragment extends HerdFragment {

    private OnFragmentInteractionListener mListener;

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    FragmentRequestLoginBinding mBinding;

    @Inject
    public RequestLoginFragment() {
    }


    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_login, container, false);
        mBinding.buttonLogin.setOnClickListener(l->{
            mListener.OnRequestLoginLogin();
        });
        mBinding.buttonRegister.setOnClickListener(l->{
            mListener.OnRequestLoginRegister();
        });

        mBinding.tvAgent.setOnClickListener(l->{
            Intent intent = new Intent(this.getActivity(), AttentionActivity.class);
            this.getActivity().startActivity(intent);
        });

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener{
        // TODO: Update argument type and name
        void OnRequestLoginLogin();
        void OnRequestLoginRegister();
    }

}
