package com.theherdonline.ui.makebid;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentMakeBidBinding;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.Bid;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import dagger.Lazy;


public class MakeBidFragment extends HerdFragment {


    public static String ARG_DATA_LIVESTOCK = "arg-data-livestock";
    private OnFragmentInteractionListener mListener;
    private NetworkInterface mNetworkListener;
    private BackPressInterface mGoBackListener;

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    MainActivityViewModel mViewModel;

    FragmentMakeBidBinding mBinding;

    private Integer mCurrentBidPrice;

    @Inject
    AppUtil mAppUtil;

    EntityLivestock mBidLivestock;

    public static MakeBidFragment newInstance(Lazy<MakeBidFragment> lazy, EntityLivestock livestock) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA_LIVESTOCK,livestock);
        MakeBidFragment fragment = lazy.get();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(true, getString(R.string.txt_submit_bid), null,null,null,null);
    }

    @Inject
    public MakeBidFragment() {
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_make_bid, container, false);





        mBinding.tvBidPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String price = s.toString();
                mBinding.tvBidDec.setText(getString(R.string.txt_make_bid_amount,price));
                //mViewModel.setPrice(Math.round(Float.valueOf(price) * 100));
            }
        });



        mBinding.buttonSubmit.setOnClickListener(l->{
            String sPrice = mBinding.tvBidPrice.getText().toString();
            if(mAppUtil.isAgentorProducer(mBidLivestock))
            {
                ActivityUtils.showWarningDialog(getContext(),getString(R.string.app_name),getString(R.string.txt_bid_error));
                return;
            }
            else if (TextUtils.isEmpty(sPrice))
            {
                String fieldName = getString(R.string.txt_amount);
                ActivityUtils.showWarningDialog(getContext(),getString(R.string.app_name),
                        getString(R.string.txt_invalid_input,  fieldName,fieldName));
                return;
            }
            else
            {
                Integer newPrice = ActivityUtils.dollarString2cent (mBinding.tvBidPrice.getText().toString());
                if (mCurrentBidPrice != null  &&  newPrice <= mCurrentBidPrice)
                {
                    ActivityUtils.showWarningDialog(getContext(),getString(R.string.app_name),getString(R.string.txt_price_cannot_lower_than,ActivityUtils.cent2string(mCurrentBidPrice)));
                    return;
                }
                else
                {
                    mViewModel.makeBid(mBidLivestock.getId(),newPrice);
                    subscription();
                }
            }
        });
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null)
        {
            mBidLivestock = getArguments().getParcelable(ARG_DATA_LIVESTOCK);
            mCurrentBidPrice = ActivityUtils.centString2cent(mBidLivestock.getPrice());
            mBinding.tvBidDec.setText(getString(R.string.txt_make_bid_amount,ActivityUtils.cent2string(mCurrentBidPrice)));
            mBinding.tvBidPrice.setText(ActivityUtils.centString2DollarString(mBidLivestock.getPrice()));
        }

    }



    public void subscription()
    {
        if (mViewModel.getmLvResMakeBid() != null)
        {
            mViewModel.getmLvResMakeBid().observe(this, new DataObserver<Bid>() {
                @Override
                public void onSuccess(Bid data) {
                    mNetworkListener.onLoading(false);
                    ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_make_a_successful_bid),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mGoBackListener.OnClickGoBackButton();
                                }
                            });
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BackPressInterface) {
            mGoBackListener = (BackPressInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BackPressInterface");
        }
        if (context instanceof NetworkInterface) {
            mNetworkListener = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NetworkInterface");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.makeBid(null,null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoBackListener = null;
        mNetworkListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnSubmitBid(Integer price);
    }

}
