package com.theherdonline.ui.home;

import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

/**
 * Created by freddie on 12/06/18.
 */

public class PickupPhotoDialogFragment extends DialogFragment {

    public static String ARG_TYPE = "arg_type";
    public static String ARG_LIST_TEXT = "arg_list_text";
    public static String ARG_Title = "arg_title";

    private DialogInterface.OnClickListener onClickListener;
    private int mType;

    private String[] mItems;
    private String mTitle;


    public static PickupPhotoDialogFragment newInstance(String mTitle, String[] mItems, DialogInterface.OnClickListener onDateSetListener) {
        PickupPhotoDialogFragment pickerFragment = new PickupPhotoDialogFragment();
        Bundle bundle = new Bundle();
        //bundle.putInt(ARG_TYPE,type);
        bundle.putStringArray(ARG_LIST_TEXT,mItems);
        bundle.putString(ARG_Title,mTitle);
        pickerFragment.setOnDateSetListener(onDateSetListener);
        pickerFragment.setOptions(mItems);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(getArguments() != null)
        {
          //  mType = getArguments().getInt(ARG_TYPE, ApiConstants.ACTIVITY_PICK_IMAGE_CONTENT);
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

    public void setOptions(String[] options )
    {
        mItems = options;
    }
}
