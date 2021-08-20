package com.theherdonline.ui.marketReport;

import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentListBinding;
import com.theherdonline.db.entity.ResPagerSaleyard;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.main.SaleyardRecyclerViewAdapter;
import dagger.Lazy;

public class SelectSyleyardFragment extends HerdFragment {

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    CreateMarketReportViewModel mViewModel;

    private SaleyardInterface mSaleyardListener;
    protected FragmentListBinding mBinding;


    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(getString(R.string.txt_select_saleyard_auction),
                mExitListener, null, null, null, null);
    }

    private DataObserver<ResPagerSaleyard> mSaleyardList = new DataObserver<ResPagerSaleyard>() {
        @Override
        public void onSuccess(ResPagerSaleyard data) {
            mNetworkListener.onLoading(false);
            SaleyardRecyclerViewAdapter adapter = new SaleyardRecyclerViewAdapter(getContext(), data.getData(),mSaleyardListener,true);
            mBinding.list.setAdapter(adapter);

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
    };

    @Inject
    public SelectSyleyardFragment() {
    }



    public void setmSaleyardListener(SaleyardInterface mSaleyardListener) {
        this.mSaleyardListener = mSaleyardListener;
    }

    public static SelectSyleyardFragment newInstance(Lazy<SelectSyleyardFragment> lazy, SaleyardInterface saleyardInterface) {
        SelectSyleyardFragment fragment = lazy.get();
        fragment.setmSaleyardListener(saleyardInterface);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(CreateMarketReportViewModel.class);
        mViewModel.getmLiveDataSaleyard().observe(this,mSaleyardList);
    }
}
