package com.theherdonline.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.theherdonline.R;
import com.theherdonline.databinding.ActivityRequestFirstLoginBinding;
import com.theherdonline.di.MyApplication;
import com.theherdonline.ui.login.AttentionActivity;

public class RequestAccessFragment extends DialogFragment {
    private RequestLoginFragment.OnFragmentInteractionListener mListener;
    ActivityRequestFirstLoginBinding mBinding;

    public RequestAccessFragment() {
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_request_first_login, container, false);
        mBinding.buttonLogin.setOnClickListener(l->{
            mListener.OnRequestLoginLogin();
        });
        mBinding.buttonRegister.setOnClickListener(l->{
            mListener.OnRequestLoginRegister();
        });

        mBinding.tvDontShow.setOnClickListener(l->{
            ((MyApplication)getActivity().getApplication()).getmAppUtil().updateDontshowAgain(false);
            dismiss();
        });

        mBinding.imageClose.setOnClickListener(l->{
            dismiss();
        });

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RequestLoginFragment.OnFragmentInteractionListener) {
            mListener = (RequestLoginFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RequestLoginFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
}
