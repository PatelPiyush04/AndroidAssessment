package com.theherdonline.ui.profile.mybid;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentListPagerDataBinding;
import com.theherdonline.db.entity.SaleyardType;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.profile.mypost.MyPostListFragment;
import com.theherdonline.ui.profile.mysavedlivestock.MySavedLivestockPagerFragment;
import com.theherdonline.ui.profile.mysavedsaleyard.MySavedSaleyardPagerFragment;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.UIUtils;

public class MyBidFragment extends HerdFragment {

    static final public String ARG_MODE = "ARG_MODE";
    static final public Integer ARG_MODE_bid = 1;
    static final public Integer ARG_MODE_post = 2;
    static final public Integer ARG_MODE_save_ads = 3;
    static final public Integer ARG_MODE_save_saleyard = 4;
    static final public Integer ARG_MODE_save_prime_saleyard = 5;


    public static MyBidFragment newInstance(MyBidFragment fragment, Integer mode) {

        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }


    @Inject
    AppUtil mAppUtil;



    @Inject
    public MyBidFragment() {
    }

    private FragmentListPagerDataBinding mBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_pager_data, container, false);
        int nMode = getArguments().getInt(ARG_MODE);
        String toolbarTitle;
        if (nMode == ARG_MODE_bid)
        {
            toolbarTitle = getString(R.string.txt_my_bids);
        }
        else if (nMode == ARG_MODE_post)
        {
            toolbarTitle = getString(R.string.txt_my_posts);

        }
        else if (nMode == ARG_MODE_save_ads)
        {
            toolbarTitle = getString(R.string.txt_saved_paddock_sales);
        }
        else if (nMode == ARG_MODE_save_saleyard)
        {
            toolbarTitle = getString(R.string.txt_saved_saleyard_auctions);
        }
        else
        {
            toolbarTitle = getString(R.string.txt_saved_prime_auctions);
        }

        UIUtils.showToolbar(getContext(), mBinding.includeToolbar, toolbarTitle,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGobackListener.OnClickGoBackButton();
                    }
                },  null, null, null, null);

        if (nMode == ARG_MODE_bid) {
            ActivityUtils.addFragmentToActivity(getChildFragmentManager(),
                    MyBidListFragment.newInstance(), R.id.frameLayout_container);
        }
        else if (nMode == ARG_MODE_post)
        {
            ActivityUtils.addFragmentToActivity(getChildFragmentManager(),
                    MyPostListFragment.newInstance(), R.id.frameLayout_container);
        }
        else if (nMode == ARG_MODE_save_ads)
        {

            ActivityUtils.addFragmentToActivity(getChildFragmentManager(),
                    MySavedLivestockPagerFragment.newInstance(null), R.id.frameLayout_container);
        }
        else if (nMode == ARG_MODE_save_saleyard)
        {
            SearchFilterPublicSaleyard saleyard = new SearchFilterPublicSaleyard();
            saleyard.setType(SaleyardType.Feature.name() + "," + SaleyardType.Weaner.name() + "," + SaleyardType.Store.name());
            ActivityUtils.addFragmentToActivity(getChildFragmentManager(),
                    MySavedSaleyardPagerFragment.newInstance(saleyard), R.id.frameLayout_container);
        }
        else
        {
            SearchFilterPublicSaleyard saleyard = new SearchFilterPublicSaleyard();
            saleyard.setType(SaleyardType.Prime.name());
            ActivityUtils.addFragmentToActivity(getChildFragmentManager(),
                    MySavedSaleyardPagerFragment.newInstance(saleyard), R.id.frameLayout_container);

        }



        return mBinding.getRoot();
    }

}
