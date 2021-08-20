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

public class DurationDayListAdapter extends ArrayAdapter<Integer> {

    private List<Integer> mDayList;

    public DurationDayListAdapter(Context context, List<Integer> objects) {
        super(context, 0, objects);
        mDayList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemViewType(position,convertView,parent);
    }

    private View getItemViewType(final int position, View convertView, ViewGroup parent) {
        LayoutInflater a = LayoutInflater.from(parent.getContext());
        ItemAdapterBinding mBinding = DataBindingUtil.inflate(a, R.layout.item_adapter, parent, false);
        Integer i = mDayList.get(position);
        String txt = getContext().getString(i == 1 ? R.string.txt_number_day : R.string.txt_number_days, i);
        mBinding.tvTitle.setText(txt);
        return mBinding.getRoot();
    }
}
