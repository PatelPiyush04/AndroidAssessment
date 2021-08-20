package com.theherdonline.ui.general;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class PickupStringDialogFragment extends DialogFragment {
    public static String ARG_Title = "arg_title";
    public static String ARG_res_id = "arg_res_id";

    private OnSelectedUnitListener onClickListener;
    private String mTitle;
    private int mResId;


    public static PickupStringDialogFragment newInstance(String title, OnSelectedUnitListener onDateSetListener, int resId ) {
        PickupStringDialogFragment pickerFragment = new PickupStringDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_Title,title);
        bundle.putInt(ARG_res_id,resId);
        pickerFragment.setOnDateSetListener(onDateSetListener);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(getArguments() != null)
        {
            mTitle = getArguments().getString(ARG_Title);
            mResId = getArguments().getInt(ARG_res_id);
        }
//R.array.array_age_unit
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String [] strings = getContext().getResources().getStringArray(mResId);
        List<String> list = new ArrayList<>();
        for (int i = 0 ; i < strings.length; i++)
        {
            list.add(strings[i]);
        }
        StringListAdapter adapter = new StringListAdapter(getContext(),list);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickListener.OnSelectedItem(list.get(which));
                dismiss();
            }
        });
        return builder.create();
    }

    public void setOnDateSetListener(OnSelectedUnitListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    public interface OnSelectedUnitListener{
        void OnSelectedItem(String livestockCategory);
    }
}
