package com.theherdonline.ui.general;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemAdapterBinding;
import com.theherdonline.db.entity.PregnancyEntity;

import java.util.List;

public class PregnancyListAdapter extends ArrayAdapter<PregnancyEntity> {

    private List<PregnancyEntity> mList;

    public PregnancyListAdapter(Context context, List<PregnancyEntity> objects) {
        super(context, 0, objects);
        mList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemViewType(position,convertView,parent);
    }

    private View getItemViewType(final int position, View convertView, ViewGroup parent) {
        LayoutInflater a = LayoutInflater.from(parent.getContext());

        ItemAdapterBinding mBinding = DataBindingUtil.inflate(a, R.layout.item_adapter, parent, false);
        mBinding.tvTitle.setText(mList.get(position).getName());
        return mBinding.getRoot();
    }
}
