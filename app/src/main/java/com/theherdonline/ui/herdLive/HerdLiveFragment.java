package com.theherdonline.ui.herdLive;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentHerdLiveBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class HerdLiveFragment extends HerdFragment {


    private final List<ResHerdLive> mUpComingList = new ArrayList<>();


    @Inject
    Lazy<CreateSaleyardStreamFragment> mCreateSaleyardStreamFragmentProvider;
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    MainActivityViewModel mMainActivityViewModel;
    FragmentHerdLiveBinding mBinding;
    String mUserId;
    private NetworkInterface mNetworkListener;
    private HerdLiveListAdapter mHerdLiveListAdapter;

    private OnHerdLiveInterface mOnHerdLiveInterface;

    @Inject
    public HerdLiveFragment() {
        // Required empty public constructor
    }


    /*public static HerdLiveFragment newInstance(String param1, String param2) {
        HerdLiveFragment fragment = new HerdLiveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/


    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public CustomerToolbar getmCustomerToolbar() {

        if (mAppUtil.isCreateLiveStream()) {
            return new CustomerToolbar(true, getString(R.string.txt_herd_live),
                    R.drawable.ic_addstream, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if (activity != null) {

                        ActivityUtils.addTagFragmentToActivity(requireActivity().getSupportFragmentManager(), mCreateSaleyardStreamFragmentProvider.get(), R.id.frameLayout_container, HerdLiveFragment.class.getName());
                        //setRefreshDataMode(true);
                        // ActivityUtils.addFragmentToActivity(((FragmentActivity) activity).getSupportFragmentManager(), CreateMarketReportFragment.newInstance(mCreateMarketReportFragmentLazy,null) , R.id.frameLayout_container);
                    }
                }
            },
                    null, null);
        } else {
            return new CustomerToolbar(true, getString(R.string.txt_herd_live), null, null, null, null);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivityViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        displayAllUpcomming();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_herd_live, container, false);

        mBinding.divide2.setVisibility(mAppUtil.isCreateLiveStream() ? View.VISIBLE : View.GONE);
        mBinding.radioOption3.setVisibility(mAppUtil.isCreateLiveStream() ? View.VISIBLE : View.GONE);


        mBinding.rvHerdLive.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.rvHerdLive.addItemDecoration(ActivityUtils.provideDividerItemDecoration(requireActivity(), R.drawable.list_divider));


        mHerdLiveListAdapter = new HerdLiveListAdapter(requireActivity(), mUpComingList);
        mBinding.rvHerdLive.setAdapter(mHerdLiveListAdapter);

        mUserId = String.valueOf(mAppUtil.getUserId());


        mBinding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mBinding.radioOption1.isChecked()) {
                    displayAllUpcomming();
                } else if (mBinding.radioOption2.isChecked()) {
                    displayPastHerdLive();
                }
                if (mBinding.radioOption3.isChecked()) {
                    displatMyHerdLive();
                }
            }
        });
        mBinding.linearRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            switch (checkedId) {
                case R.id.radio_option_1: {
                    displayAllUpcomming();
                    break;
                }
                case R.id.radio_option_2: {
                    displayPastHerdLive();
                    break;
                }
                case R.id.radio_option_3: {
                    displatMyHerdLive();
                    break;
                }
                default: {
                    displayAllUpcomming();
                    break;
                }

            }
        });
        return mBinding.getRoot();
    }

    private void displatMyHerdLive() {

        mMainActivityViewModel.getHerdMy(mUserId).observe(requireActivity(), new DataObserver<List<ResHerdLive>>() {

            @Override
            public void onSuccess(List<ResHerdLive> data) {
                mNetworkListener.onLoading(false);
                mBinding.swiperefresh.setRefreshing(false);
                mUpComingList.clear();

                if (data.size() == 0) {
                    mBinding.tvNoRecordFound.setVisibility(View.VISIBLE);
                    mBinding.swiperefresh.setVisibility(View.GONE);
                    mBinding.tvNoRecordFound.setText("No Herd Live Recrod Found...");

                } else {
                    mBinding.tvNoRecordFound.setVisibility(View.GONE);
                    mBinding.swiperefresh.setVisibility(View.VISIBLE);
                    mUpComingList.addAll(data);
                    mHerdLiveListAdapter.notifyDataSetChanged();
                    sortHerdLiveDSC();
                }

            }

            @Override
            public void onError(Integer code, String msg) {
                ActivityUtils.showWarningDialog(requireActivity(), null, "\n" + msg);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onDirty() {

            }
        });


    }

    private void displayPastHerdLive() {

        mMainActivityViewModel.getHerdPast(TimeUtils.getCurrentDate()).observe(requireActivity(), new DataObserver<List<ResHerdLive>>() {

            @Override
            public void onSuccess(List<ResHerdLive> data) {
                mNetworkListener.onLoading(false);
                mBinding.swiperefresh.setRefreshing(false);
                mUpComingList.clear();

                if (data.size() == 0) {
                    mBinding.tvNoRecordFound.setVisibility(View.VISIBLE);
                    mBinding.swiperefresh.setVisibility(View.GONE);
                    mBinding.tvNoRecordFound.setText("No Herd Live Recrod Found...");

                } else {
                    mBinding.tvNoRecordFound.setVisibility(View.GONE);
                    mBinding.swiperefresh.setVisibility(View.VISIBLE);
                    mUpComingList.addAll(data);
                    mHerdLiveListAdapter.notifyDataSetChanged();
                    sortHerdLiveDSC();
                }

            }

            @Override
            public void onError(Integer code, String msg) {
                ActivityUtils.showWarningDialog(requireActivity(), null, "\n" + msg);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onDirty() {

            }
        });

    }

    private void displayAllUpcomming() {

        //Log.e("Data", "Current date=>" + TimeUtils.getCurrentDate());
        mMainActivityViewModel.getHerdUpcoming(TimeUtils.getCurrentDate()).observe(requireActivity(), new DataObserver<List<ResHerdLive>>() {

            @Override
            public void onSuccess(List<ResHerdLive> data) {
                mNetworkListener.onLoading(false);
                mBinding.swiperefresh.setRefreshing(false);
                mUpComingList.clear();

                if (data.size() == 0) {
                    mBinding.tvNoRecordFound.setVisibility(View.VISIBLE);
                    mBinding.swiperefresh.setVisibility(View.GONE);
                    mBinding.tvNoRecordFound.setText("No Herd Live Recrod Found...");

                } else {
                    mBinding.tvNoRecordFound.setVisibility(View.GONE);
                    mBinding.swiperefresh.setVisibility(View.VISIBLE);
                    mUpComingList.addAll(data);
                    mHerdLiveListAdapter.notifyDataSetChanged();
                    sortHerdLiveASC();
                }

            }

            @Override
            public void onError(Integer code, String msg) {
                ActivityUtils.showWarningDialog(requireActivity(), null, "\n" + msg);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onDirty() {

            }
        });
    }

    private void sortHerdLiveASC() {
        Collections.sort(mUpComingList, new Comparator<ResHerdLive>() {
            public int compare(ResHerdLive o1, ResHerdLive o2) {
                return o1.getStreamDate().compareTo(o2.getStreamDate());
            }
        });
        mHerdLiveListAdapter.notifyDataSetChanged();
    }

    private void sortHerdLiveDSC() {
        Collections.sort(mUpComingList, new Comparator<ResHerdLive>() {
            public int compare(ResHerdLive o1, ResHerdLive o2) {
                return o2.getStreamDate().compareTo(o1.getStreamDate());
            }
        });
        mHerdLiveListAdapter.notifyDataSetChanged();
    }

    public Lazy<ViewModelFactory> getmLazyFactory() {
        return mLazyFactory;
    }

    public void setmLazyFactory(Lazy<ViewModelFactory> mLazyFactory) {
        this.mLazyFactory = mLazyFactory;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnHerdLiveInterface) {
            mOnHerdLiveInterface = (OnHerdLiveInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onHerdLiveInterface");
        }

        if (context instanceof NetworkInterface) {
            mNetworkListener = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NetworkInterface");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNetworkListener = null;
    }

    public interface OnHerdLiveInterface {
        void onClickHerdLiveItem(Integer id);
    }
}