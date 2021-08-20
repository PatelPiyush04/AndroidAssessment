package com.theherdonline.ui.general;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemLivestockBinding;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.ui.view.ImagePagerAdapter;
import com.theherdonline.util.ActivityUtils;

public class LivestockItemFragment extends Fragment {

    public static String ARG_DATA = "data-item";

    private EntityLivestock mLivestockItem;


    private ItemLivestockBinding mBinding;


    public static LivestockItemFragment newInstance(EntityLivestock livestockItem) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DATA, livestockItem);
        LivestockItemFragment fragment = new LivestockItemFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mLivestockItem = getArguments().getParcelable(ARG_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.item_livestock, container, false);
        if (mLivestockItem != null)
        {
            Context context = getContext();
           // mBinding.linearLayoutPoster.setVisibility(View.GONE);
            mBinding.tvTitle.setText(ActivityUtils.trimText(context,mLivestockItem.getName()));
            mBinding.tvLocation.setText(ActivityUtils.trimText(context, mLivestockItem.getAddress()));
            mBinding.tvLocation.setText(ActivityUtils.trimText(context,mLivestockItem.getQuantity().toString()));
            if (mBinding.listImage.getAdapter() == null)
            {
                ImagePagerAdapter imagePagerAdapter;
                imagePagerAdapter = new ImagePagerAdapter(context,mLivestockItem.getMediaArray());
                mBinding.listImage.setAdapter(imagePagerAdapter);
            }
        }
        return mBinding.getRoot();
    }
}
