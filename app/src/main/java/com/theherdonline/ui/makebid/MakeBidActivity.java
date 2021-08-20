package com.theherdonline.ui.makebid;

import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.view.View;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.ActivitySimplyBinding;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.ResMakeBid;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.api.Resource;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.util.ActivityUtils;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class MakeBidActivity extends DaggerAppCompatActivity implements
        MakeBidFragment.OnFragmentInteractionListener,
        BidDetailFragment.OnFragmentInteractionListener{

    public static String TAG_DATA = "livestock";


    @Inject
    Lazy<BidDetailFragment> mBidDetailFragmentProvider;
    @Inject
    Lazy<MakeBidFragment> mMakeBidFragmentProvider;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    ActivitySimplyBinding mBinding;

    private EntityLivestock mLivestoce;

    private MakeBidActivityViewModel mViewModel;
    private DataObserver<ResMakeBid> mObserverBid = new DataObserver<ResMakeBid>() {
        @Override
        public void onSuccess(ResMakeBid data) {
            // mListener.OnLoginSuccess();
            ActivityUtils.showWarningDialog(MakeBidActivity.this, getString(R.string.app_name), getString(R.string.txt_make_a_successful_bid),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
        }

        @Override
        public void onError(Integer code, String msg) {
            showProgressBar(false);
            ActivityUtils.showWarningDialog(MakeBidActivity.this,getString(R.string.app_name),
                    ActivityUtils.netWorkErrorInfo(MakeBidActivity.this,msg));
        }

        @Override
        public void onLoading() {
            showProgressBar(true);
        }

        @Override
        public void onDirty() {
            showProgressBar(false);
        }

        @Override
        public void onChanged(@Nullable Resource<ResMakeBid> resLoginResource) {
            super.onChanged(resLoginResource);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null)
        {
            mLivestoce = getIntent().getParcelableExtra(TAG_DATA);
        }
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_simply);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MakeBidActivityViewModel.class);
        if (mLivestoce != null)
        {
            mViewModel.setLivestock(mLivestoce);
        }
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mBidDetailFragmentProvider.get(),R.id.frameLayout_container);

      //  mViewModel.getmLvResMakeBit().observe(this,mObserverBid);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStackImmediate();
        }
        else {
            finish();
        }
    }

    public void showProgressBar(boolean bShow) {
        mBinding.progressBar.setVisibility(bShow ? View.VISIBLE : View.INVISIBLE);
        mBinding.frameLayoutContainer.setVisibility(!bShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnSubmitBid(Integer price) {
        mViewModel.makeBid(price);
    }

    // interface for BidDetailFragment

    @Override
    public void OnClickBidToOffer(int id) {
        ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),mMakeBidFragmentProvider.get(),R.id.frameLayout_container);
    }

    @Override
    public void OnClickContactAgent(int id) {

    }
}
