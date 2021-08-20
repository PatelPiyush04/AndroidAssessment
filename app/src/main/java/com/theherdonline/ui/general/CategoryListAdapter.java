package com.theherdonline.ui.general;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemAdapterBinding;
import com.theherdonline.db.entity.LivestockCategory;

public class CategoryListAdapter extends ArrayAdapter<LivestockCategory> {

    private List<LivestockCategory> mSaleyardList;

    public CategoryListAdapter(Context context, List<LivestockCategory> objects) {
        super(context, 0, objects);
        mSaleyardList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemViewType(position,convertView,parent);
    }

    private View getItemViewType(final int position, View convertView, ViewGroup parent) {
        LayoutInflater a = LayoutInflater.from(parent.getContext());

        ItemAdapterBinding mBinding = DataBindingUtil.inflate(a, R.layout.item_adapter, parent, false);
        mBinding.tvTitle.setText(mSaleyardList.get(position).getName());
       // UIUtils.showSaleyard(getContext(),mBinding, mEntitySaleyardList.get(position),null);
        return mBinding.getRoot();
    }
}
