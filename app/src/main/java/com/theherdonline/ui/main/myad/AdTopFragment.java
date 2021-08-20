package com.theherdonline.ui.main.myad;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentAdTabBinding;
import com.theherdonline.databinding.FragmentSearchTopCategoryBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.AppUtil;

import javax.inject.Inject;

import dagger.Lazy;

public class AdTopFragment extends HerdFragment {

    private AdTabListener mListener;

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;



    @Inject
    AppUtil mAppUtil;


    MainActivityViewModel mViewModel;

    FragmentAdTabBinding mBinding;

    @Inject
    public AdTopFragment() {
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(false,getString(R.string.title_home),null,null,null,null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ad_tab, container, false);

        mBinding.viewPrimeSaleyard.setOnClickListener(l->{
            mListener.OnClickADTabPrimeSaleyards();
        });



        mBinding.viewPaddock.setOnClickListener(l->{
            mListener.OnClickADTabPaddockSales();
        });

        mBinding.cardPrimeSaleyard.setVisibility(mAppUtil.isPrimeUser() ? View.VISIBLE : View.GONE);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AdTabListener) {
            mListener = (AdTabListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AdTabListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);

    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface AdTabListener{
        // TODO: Update argument type and name
        void OnClickADTabPaddockSales();
        void OnClickADTabPrimeSaleyards();

    }

}
