package com.theherdonline.ui.general;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import com.theherdonline.db.entity.EntitySaleyard;


public class PickupSaleyardDialogFragment extends DialogFragment {
    public static String ARG_Title = "arg_title";
    private OnSelectedItemListener onClickListener;

   // OnSelectedItemListener onSelectedItemListener;
    private List<EntitySaleyard> mList;
    private String mTitle;

    public static PickupSaleyardDialogFragment newInstance(String mTitle, List<EntitySaleyard> list, OnSelectedItemListener onDateSetListener) {
        PickupSaleyardDialogFragment pickerFragment = new PickupSaleyardDialogFragment();
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
        SaleyardListAdapter  adapter = new SaleyardListAdapter(getContext(),mList);
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

    public void setmList(List<EntitySaleyard> mList) {
        this.mList = mList;
    }

    public interface OnSelectedItemListener{
        void OnSelectedItem(EntitySaleyard livestockCategory);
    }
}