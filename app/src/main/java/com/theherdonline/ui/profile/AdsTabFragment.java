package com.theherdonline.ui.profile;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theherdonline.db.entity.StreamItem;
import com.theherdonline.db.entity.StreamType;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.herdinterface.PostInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;

public class AdsTabFragment extends PagerListFragment<StreamItem>{


    public static String ARG_user_id = "user_id";
    public static String ARG_stream_type = "stream_type";
    public static String ARG_organisation_id = "organisation_id";


    private Integer mUserID;
    private Integer mOfUser;
    private Integer mOrganisationID;

    private StreamType mStreamType;


    private StreamPagerViewModel mStreamViewModel;

    private InterfaceStreamItemListener mStreamListener;
    private LivestockInterface mLivestockListener;
    private SaleyardInterface mSaleyardListener;
    private PostInterface mPostInterface;



    public static AdsTabFragment newInstance(Integer userId, Integer ofUserId, StreamType type) {

        Bundle args = new Bundle();
        AdsTabFragment fragment = new AdsTabFragment();
        fragment.setmStreamType(type);
        fragment.setmUserID(userId);
        fragment.setmOfUser(ofUserId);
        fragment.setArguments(args);
        return fragment;
    }



    public static AdsTabFragment newInstance(Integer userId, Integer ofUserId, Integer orId, StreamType type) {

        Bundle args = new Bundle();
        AdsTabFragment fragment = new AdsTabFragment();
        fragment.setmStreamType(type);
        fragment.setmUserID(userId);
        fragment.setmOfUser(ofUserId);
        fragment.setArguments(args);
        fragment.setmOrganisationID(orId);
        return fragment;
    }


    public Integer getmOrganisationID() {
        return mOrganisationID;
    }

    public void setmOrganisationID(Integer mOrganisationID) {
        this.mOrganisationID = mOrganisationID;
    }

    public Integer getmOfUser() {
        return mOfUser;
    }

    public void setmOfUser(Integer mOfUser) {
        this.mOfUser = mOfUser;
    }

    public void setmStreamType(StreamType mStreamType) {
        this.mStreamType = mStreamType;
    }

    public void setmUserID(Integer mUserID) {
        this.mUserID = mUserID;
    }

    @Override
    public CustomRecyclerViewAdapter getAdapter() {
        //return new AdverRecyclerViewAdapter(mStreamViewModel.getmLiveDataStreamList().getValue());
        return  new StreamRecyclerViewAdapter(getContext(),mStreamViewModel.getDataList(), mStreamListener, mLivestockListener,
                mSaleyardListener,mPostInterface);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        StreamPagerViewModel.Factory factoryProfile = new StreamPagerViewModel.Factory(getActivity().getApplication(),
                mUserID,
                mOfUser,
                mOrganisationID,
                mStreamType);
        mStreamViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(StreamPagerViewModel.class);
        mStreamViewModel.getmLiveDataStreamList().observe(this, mListDataObserver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceStreamItemListener) {
            mStreamListener = (InterfaceStreamItemListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if (context instanceof LivestockInterface) {
            mLivestockListener = (LivestockInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LivestockInterface");
        }


        if (context instanceof SaleyardInterface) {
            mSaleyardListener = (SaleyardInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LivestockInterface");
        }


        if (context instanceof PostInterface) {
            mPostInterface = (PostInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PostInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mStreamListener = null;
        mLivestockListener = null;
        mSaleyardListener = null;
        mPostInterface = null;
    }

    @Override
    public PagerViewModel getViewModel() {
        return mStreamViewModel;
    }
}
