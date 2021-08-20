package com.theherdonline.ui.organisation;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentOrganisationNewBinding;
import com.theherdonline.db.entity.Organisation;
import com.theherdonline.db.entity.PaymentCard;
import com.theherdonline.db.entity.StreamType;
import com.theherdonline.db.entity.User;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.payment.AddCardFragment;
import com.theherdonline.ui.main.poddocksearch.SearchLivestockResultFragment;
import com.theherdonline.ui.profile.AdsTabFragment;
import com.theherdonline.ui.view.CustomerOnCheckedChangeListener;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;


public class OrganisationFragment extends HerdFragment {

    static public String ARG_ID = "org_id";


    FragmentOrganisationNewBinding mBinding;
    MainActivityViewModel mViewModel;
    InterfaceContactCard mInterfaceContactCard;

    AgentRecyclerViewAdapter mAgentAdapter;
    Integer [] mRadioButtonsId = {R.id.radio_agents,R.id.radio_stream};

    List<PaymentCard> mPaymentCardList = null;

    private Integer mOrgId;


    @Inject
    Lazy<ViewModelFactory> mLazyFactory;


    @Inject
    Lazy<AddCardFragment> mLazyAddCardFragment;


    @Inject
    AppUtil mAppUtil;

    public void setImageLinks(final View view, final String links)
    {
        if (StringUtils.isEmpty(links))
        {
            view.setVisibility(View.GONE);
        }
        else
        {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(l->{
                ActivityUtils.openWebPage(getActivity(), links);
            });
        }
    }

    DataObserver<Organisation> mObserver = new DataObserver<Organisation>() {
        @Override
        public void onSuccess(Organisation data) {
         //   mBinding.swiperefresh.setRefreshing(false);
            ActivityUtils.loadImage(getContext(), mBinding.imageAvatar, ActivityUtils.getImageAbsoluteUrl(data.getAvatar_location_full_url()), null);
            setTextViewText(mBinding.tvOrgName, data.getName());
            setTextViewText(mBinding.tvOrgBio,data.getBiography());
            setTextViewText(mBinding.tvOrgAddress,getAddress(data.getAddressLineOne(),
                    data.getAddressLineTwo(),
                    data.getAddressPostcode()));
            User fakeUser = new User();
            fakeUser.setPhone(data.getPhone());
            fakeUser.setEmail(data.getEmail());
            mBinding.imageEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mInterfaceContactCard.OnClickSendEmail(fakeUser);
                }
            });
            mBinding.imagePhoneCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mInterfaceContactCard.OnClickPhoneCall(fakeUser);
                }
            });


            setImageLinks(mBinding.imageFacebook,data.getFacebookUrl());
            setImageLinks(mBinding.imageIns,data.getInstagramUrl());
            setImageLinks(mBinding.imageYoutube,data.getYoutubeUrl());
            setImageLinks(mBinding.imageTwitter,data.getTwitterUrl());
            setImageLinks(mBinding.imageWeb,data.getWebsite_url());

          /*
            if (StringUtils.isEmpty(data.getFacebookUrl()))
            {
                mBinding.imageFacebook.setVisibility(View.GONE);
            }
            else
            {
                mBinding.imageFacebook.setOnClickListener(l->{
                    ActivityUtils.openWebPage(getActivity(), data.getFacebookUrl());
                });
            }






            mBinding.imageIns.setOnClickListener(l->{
                ActivityUtils.openWebPage(getActivity(), data.getInstagramUrl());
            });


            mBinding.imageYoutube.setOnClickListener(l->{
                ActivityUtils.openWebPage(getActivity(), data.getYoutubeUrl());
            });

            mBinding.imageTwitter.setOnClickListener(l->{
                ActivityUtils.openWebPage(getActivity(), data.getTwitterUrl());
            });


            mBinding.imageWeb.setOnClickListener(l->{
                ActivityUtils.openWebPage(getActivity(), data.getWebsite_url());
            });*/

            if (StringUtils.isEmpty(data.getFacebookUrl()) && StringUtils.isEmpty(data.getInstagramUrl())  &&
            StringUtils.isEmpty(data.getYoutubeUrl()) && StringUtils.isEmpty(data.getTwitterUrl()) &&
                    StringUtils.isEmpty(data.getWebsite_url()))
            {
                mBinding.linearLinks.setVisibility(View.GONE);
            }
            else
            {
                mBinding.linearLinks.setVisibility(View.VISIBLE);
            }

        //    mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mUserId);
         //   mSectionsPagerAdapter.setRefreshMode(true);
       // }
       // else
        //{
  //          mSectionsPagerAdapter.setRefreshMode(mIsNeedRefresh);
//
    //    }
        mBinding.streamContainer.setAdapter(new SectionsPagerAdapter(getChildFragmentManager(), data.getUsers(), data.getId()));
            mBinding.radioAgents.setChecked(true);

            mBinding.linearRadioGroup.setOnCheckedChangeListener(new CustomerOnCheckedChangeListener(mBinding.streamContainer,
                    mRadioButtonsId ));



         //   mBinding.listAgents.setVisibility(View.GONE);
         //   mBinding.frameLayoutContainerStream.setVisibility(View.VISIBLE);

/*            mAgentList = data.getUsers();

            mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mUserId);
        }
        else
        {
            mSectionsPagerAdapter.setRefreshMode(mIsNeedRefresh);

        }
        mBinding.streamContainer. .setAdapter(mSectionsPagerAdapter);*/




/*
        if (mAgentAdapter != null)
        {
            mAgentAdapter.update(data.getUsers());
        }*/











        }

        @Override
        public void onError(Integer code, String msg) {
           // mBinding.swiperefresh.setRefreshing(false);

        }

        @Override
        public void onLoading() {
          //  mBinding.swiperefresh.setRefreshing(false);

        }

        @Override
        public void onDirty() {
           // mBinding.swiperefresh.setRefreshing(false);

        }
    };

    @Inject
    public OrganisationFragment() {
    }


    public static OrganisationFragment newInstance(OrganisationFragment f, Integer id) {
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        f.setArguments(args);
        return f;
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(true,null,null,null,null,null);
    }
    @Inject
    Lazy<SearchLivestockResultFragment> mSearchLivestockResultFragmentProvider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_organisation_new, container, false);
        mOrgId = getArguments().getInt(ARG_ID);





        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mBinding.swiperefreshContainer.setRefreshing(true);
        subscription();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        //mViewModel.getmLDPaymentCardList().observe(this,mRefreshObser);
    }

    public void setTextViewText(TextView view, String string)
    {
        if (StringUtils.isEmpty(string))
        {
            view.setVisibility(View.GONE);
        }
        else
        {
            view.setText(Html.fromHtml(string));
            view.setVisibility(View.VISIBLE);
        }
    }

    public String getAddress(String lineOne, String lineTow, String postCode)
    {
        if (StringUtils.isEmpty(lineOne) && StringUtils.isEmpty(lineTow) && StringUtils.isEmpty(postCode)) {
            return null;
        }
        else
        {
            return String.format("%s %s %s", StringUtils.defaultString(lineOne), StringUtils.defaultString(lineTow), StringUtils.isEmpty(postCode) ? "" : "," + postCode);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void subscription() {
        //     mMarketViewModel.getmLiveDataUser().observe(this, mUserObserver);
        mViewModel.getOrganisation(mOrgId).observe(this,mObserver);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceContactCard) {
            mInterfaceContactCard = (InterfaceContactCard) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InterfaceContactCard");
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mInterfaceContactCard = null;
    }


    public List<User> mAgentList;

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        final public List<User>  mUserList;
        final public Integer mOrganisationId;
        public SectionsPagerAdapter(FragmentManager fm, List<User> userList, Integer id) {
            super(fm);
            mUserList = userList;
            mOrganisationId = id;

        }




        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // MarketReportListFragment f = MarketReportListFragment.newInstance(null);
                // f.setmRfreshOnResume(true);
                // mFragmentList.add(position, f);
/*                List<User> list = new ArrayList<>(mUserList);
                list.addAll(list);
                list.addAll(list);
                list.addAll(list);*/

                return OrganisationAgentListFragment.newInstance(mUserList);
            } else {
                // MarketReportListFragment f = MarketReportListFragment.newInstance(mUserId);
                // f.setmRfreshOnResume(true);
                // mFragmentList.add(position, f);
                return AdsTabFragment.newInstance(null, null, mOrganisationId, StreamType.ALL);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }


}
