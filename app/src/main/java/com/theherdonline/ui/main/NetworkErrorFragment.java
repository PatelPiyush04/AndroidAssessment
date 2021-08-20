package com.theherdonline.ui.main;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentNetworkErrorBinding;
import com.theherdonline.di.ViewModelFactory;
import dagger.Lazy;
import dagger.android.support.DaggerFragment;

public class NetworkErrorFragment extends DaggerFragment {
    private OnFragmentInteractionListener mListener;

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    FragmentNetworkErrorBinding mBinding;

    @Inject
    public NetworkErrorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_network_error, container, false);

        mBinding.buttonRetry.setOnClickListener(l->{
            mListener.OnNetworkErrorRetry();
        });

        mBinding.buttonRegister.setOnClickListener(l->{
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
        void OnNetworkErrorRetry();
    }
}
