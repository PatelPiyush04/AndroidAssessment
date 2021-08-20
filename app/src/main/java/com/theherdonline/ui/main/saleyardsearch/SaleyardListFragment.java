package com.theherdonline.ui.main.saleyardsearch;

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
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.main.SaleyardRecyclerViewAdapter;

public class SaleyardListFragment extends HerdFragment {


    private FragmentListBinding mBinding;
    public static String ARG_LIST_DATA = "list-data";
    private SaleyardInterface mSaleyardListener;
    List<EntitySaleyard> mEntitySaleyardList;

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(getString(R.string.txt_saleyard_auctions),
                mExitListener, null, null, null, null);
    }

    @Inject
    public SaleyardListFragment() {
    }
    public static SaleyardListFragment newInstance(SaleyardListFragment fragment, List<EntitySaleyard> list) {
        Bundle args = new Bundle();
        ArrayList<EntitySaleyard> arrayList = new ArrayList<>(list.size());
        arrayList.addAll(list);
        args.putParcelableArrayList(ARG_LIST_DATA,arrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        if (getArguments() != null)
        {
            mEntitySaleyardList = getArguments().getParcelableArrayList(ARG_LIST_DATA);
            SaleyardRecyclerViewAdapter adapter = new SaleyardRecyclerViewAdapter(getContext(), mEntitySaleyardList,mSaleyardListener);
            mBinding.list.setAdapter(adapter);
        }
        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SaleyardInterface) {
            mSaleyardListener = (SaleyardInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapMarkListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSaleyardListener = null;
    }

}
