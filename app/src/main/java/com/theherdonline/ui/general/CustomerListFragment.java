package com.theherdonline.ui.general;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.theherdonline.databinding.FragmentGenericListBinding;

import com.theherdonline.R;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.util.UIUtils;

public abstract class CustomerListFragment extends HerdFragment {

    protected FragmentGenericListBinding mBinding;

    private BackPressInterface mGoBackListener;

    public abstract String getToolBarText(Context context);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_generic_list, container, false);
        UIUtils.showToolbar(getContext(),mBinding.frameLayoutToolbar,getToolBarText(getContext()),mExitListener,
                null,null,
                null,null);
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BackPressInterface) {
            mGoBackListener = (BackPressInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapMarkListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mGoBackListener = null;
    }
}
