package com.theherdonline.ui.profile.mypost;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theherdonline.R;
import com.theherdonline.db.entity.Post;
import com.theherdonline.ui.herdinterface.PostInterface;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.ui.profile.PagerViewModel;

public class MyPostListFragment extends PagerListFragment<Post>{

    private MyPostPagerViewModel mMyAdFragmentViewModel;
    private PostInterface mLivestockListener;


    public static MyPostListFragment newInstance() {
        Bundle args = new Bundle();
        MyPostListFragment fragment = new MyPostListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemId(Post post) {
        return post.getId();
    }

    @Override
    public String deleteDescription(Post post) {
        return getString(R.string.txt_are_you_sure_delete_post, "");
    }

    @Override
    public CustomRecyclerViewAdapter getAdapter() {
        //return new AdverRecyclerViewAdapter(mMyAdFragmentViewModel.getmLiveDataStreamList().getValue());
        return  new MyPostRecyclerViewAdapter(getContext(),mMyAdFragmentViewModel.getDataList(), mLivestockListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setEnableSwipe(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MyPostPagerViewModel.Factory factoryProfile = new MyPostPagerViewModel.Factory(getActivity().getApplication()
               );
        mMyAdFragmentViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(MyPostPagerViewModel.class);
        mMyAdFragmentViewModel.getmLiveDataStreamList().observe(this, mListDataObserver);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PostInterface) {
            mLivestockListener = (PostInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement PostInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLivestockListener = null;
    }

    @Override
    public PagerViewModel getViewModel() {
        return mMyAdFragmentViewModel;
    }
}
