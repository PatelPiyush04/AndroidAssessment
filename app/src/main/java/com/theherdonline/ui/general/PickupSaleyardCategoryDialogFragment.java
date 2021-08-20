package com.theherdonline.ui.general;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.SaleyardCategory;

import java.util.List;

public class PickupSaleyardCategoryDialogFragment extends DialogFragment {
    public static String ARG_Title = "arg_title";
    private OnSelectedItemListener onClickListener;

   // OnSelectedItemListener onSelectedItemListener;
    private List<SaleyardCategory> mList;
    private String mTitle;

    public static PickupSaleyardCategoryDialogFragment newInstance(String mTitle, List<SaleyardCategory> list, OnSelectedItemListener onDateSetListener) {
        PickupSaleyardCategoryDialogFragment pickerFragment = new PickupSaleyardCategoryDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_Title,mTitle);
        pickerFragment.setOnDateSetListener(onDateSetListener);
        pickerFragment.setmList(list);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(getArguments() != null)
        {
            mTitle = getArguments().getString(ARG_Title);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        SaleyardCategoryListAdapter adapter = new SaleyardCategoryListAdapter(getContext(),mList);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickListener.OnSelectedItem(mList.get(which));
                dismiss();
            }
        });
        return builder.create();
    }

    public void setOnDateSetListener(OnSelectedItemListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    public void setmList(List<SaleyardCategory> mList) {
        this.mList = mList;
    }

    public interface OnSelectedItemListener{
        void OnSelectedItem(SaleyardCategory livestockCategory);
    }
}
