package com.theherdonline.ui.general;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.theherdonline.R;
import com.theherdonline.databinding.ItemAdapterBinding;

import java.util.List;

public class StringListAdapter extends ArrayAdapter<String> {

    private List<String> mList;

    public StringListAdapter(Context context, List<String> objects) {
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
        mBinding.tvTitle.setText(mList.get(position));
        return mBinding.getRoot();
    }
}
