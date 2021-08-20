package com.theherdonline.ui.general;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class PickupDurationDayDialogFragment extends DialogFragment {
    public static String ARG_Title = "arg_title";
    private OnSelectedItemListener onClickListener;

   // OnSelectedItemListener onSelectedItemListener;
    private List<Integer> mList;
    private String mTitle;

    public static PickupDurationDayDialogFragment newInstance(String mTitle, Integer [] arrayInteger, OnSelectedItemListener onDateSetListener) {
        PickupDurationDayDialogFragment pickerFragment = new PickupDurationDayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_Title,mTitle);
        pickerFragment.setOnDateSetListener(onDateSetListener);
        List<Integer> list1 = new ArrayList<>();
        for (Integer i : arrayInteger)
        {
            list1.add(i);
        }
        pickerFragment.setmList(list1);
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
        DurationDayListAdapter  adapter = new DurationDayListAdapter(getContext(),mList);
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

    public void setmList(List<Integer> mList) {
        this.mList = mList;
    }

    public interface OnSelectedItemListener{
        void OnSelectedItem(Integer livestockCategory);
    }
}
