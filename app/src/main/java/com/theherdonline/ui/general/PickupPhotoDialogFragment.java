package com.theherdonline.ui.general;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class PickupPhotoDialogFragment extends DialogFragment {
    public static String ARG_LIST_TEXT = "arg_list_text";
    public static String ARG_LIST_ARRAY = "arg_list_array";

    public static String ARG_Title = "arg_title";

    private DialogInterface.OnClickListener onClickListener;
    private String [] mItems;
    private ArrayList mList;
    private String mTitle;

    public static PickupPhotoDialogFragment newInstance(String mTitle, String [] mItems, DialogInterface.OnClickListener onDateSetListener) {
        PickupPhotoDialogFragment pickerFragment = new PickupPhotoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArray(ARG_LIST_TEXT,mItems);
        bundle.putString(ARG_Title,mTitle);
        pickerFragment.setOnDateSetListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDateSetListener.onClick(dialog,which);
                pickerFragment.dismiss();
            }
        });
        pickerFragment.setOptions(mItems);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }


    public static PickupPhotoDialogFragment newInstance(String mTitle, ArrayList arrayList, DialogInterface.OnClickListener onDateSetListener) {
        PickupPhotoDialogFragment pickerFragment = new PickupPhotoDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_Title,mTitle);
        pickerFragment.setOnDateSetListener(onDateSetListener);
        pickerFragment.setmList(arrayList);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(getArguments() != null)
        {
            mItems = getArguments().getStringArray(ARG_LIST_TEXT);
            mTitle = getArguments().getString(ARG_Title);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(mItems, onClickListener);
        if (mTitle != null) {
            builder.setTitle(mTitle);
        }

        return builder.create();
    }

    public void setOnDateSetListener(DialogInterface.OnClickListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    public void setOptions(String [] options )
    {
        mItems = options;
    }

    public void setmList(ArrayList mList) {
        this.mList = mList;
    }
}
