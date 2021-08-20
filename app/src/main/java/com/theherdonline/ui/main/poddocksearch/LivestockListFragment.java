package com.theherdonline.ui.main.poddocksearch;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentListBinding;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.main.AdverRecyclerViewAdapter;
import com.theherdonline.ui.herdinterface.LivestockInterface;

public class LivestockListFragment extends HerdFragment {

    public static String ARG_LIST_DATA = "list-data";

    public LivestockInterface mLivestockListener;
    protected FragmentListBinding mBinding;

    List<EntityLivestock> mLivestockList;

    public static LivestockListFragment newInstance(LivestockListFragment fragment, List<EntityLivestock> list) {
        Bundle args = new Bundle();
        ArrayList<EntityLivestock> arrayList = new ArrayList<>(list.size());
        arrayList.addAll(list);
        args.putParcelableArrayList(ARG_LIST_DATA,arrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    public LivestockListFragment() {

    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(getString(R.string.txt_paddock_sales),
                mExitListener, null, null, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null)
        {
            mLivestockList = getArguments().getParcelableArrayList(ARG_LIST_DATA);
            AdverRecyclerViewAdapter adapter = new AdverRecyclerViewAdapter(getContext(), mLivestockList,mLivestockListener);
            mBinding.list.setAdapter(adapter);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LivestockInterface) {
            mLivestockListener = (LivestockInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapMarkListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mLivestockListener = null;
    }
}
