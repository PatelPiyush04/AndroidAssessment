package com.theherdonline.ui.stream;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentCreatePostBinding;
import com.theherdonline.db.entity.Post;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.UIUtils;

import dagger.Lazy;


public class CreatePostFragment extends HerdFragment {

    FragmentCreatePostBinding mBinding;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    MainActivityViewModel mMainViewModel;

    private String mPhotoUrl;

    @Inject
    public CreatePostFragment() {
        // Required empty public constructor
    }


    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(getString(R.string.txt_create_post),mExitListener,null,null,null,null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_post, container, false);
        UIUtils.showToolbar(getContext(),mBinding.toolbar,getString(R.string.txt_create_post),mExitListener,null,null,null,null);


        mBinding.constraintLayoutAddPhoto.setOnClickListener(v->{
            if (mMainViewModel.getmLiveDataPostPhoto().getValue() != null)
            {
                ((MainActivity)getActivity()).popupPhotoDialogWithDeleteOption(getString(R.string.txt_add_a_photo), AppConstants.ACTIVITY_CODE_POST_TAKE_PHOTO);
            }
            else
            {
                ((MainActivity)getActivity()).popupPhotoDialog(getString(R.string.txt_add_a_photo), AppConstants.ACTIVITY_CODE_POST_TAKE_PHOTO);
            }

        });

        mBinding.buttonSubmit.setOnClickListener(l->{
            String string = mBinding.editTextPost.getText().toString();
            if (!ActivityUtils.checkInput(getContext(),string,getString(R.string.txt_post_content)))
            {
                return;
            }
            mMainViewModel.createNewPost(string).observe(this, new DataObserver<Post>() {
                @Override
                public void onSuccess(Post data) {
                    mNetworkListener.onLoading(false);
                    ActivityUtils.showWarningDialog(getContext(), getString(R.string.txt_create_post), getString(R.string.txt_your_post_has_been_created),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mGobackListener.OnClickGoBackButton();
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

        });


        return mBinding.getRoot();
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        mMainViewModel.getmLiveDataPostPhoto().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String url) {
                mBinding.imageViewAddPhoto.setVisibility(url == null ? View.VISIBLE : View.INVISIBLE);
                mBinding.imagePhoto.setVisibility(url != null ? View.VISIBLE : View.INVISIBLE);
                mPhotoUrl = url;
                if (url != null)
                {
                    ActivityUtils.loadImage(getContext(),mBinding.imagePhoto,
                                url,
                                null);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ActivityUtils.deleteFile(mPhotoUrl);
        mMainViewModel.upDatePostPhoto(null);
    }
}
