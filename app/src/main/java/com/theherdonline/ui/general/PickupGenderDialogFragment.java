package com.theherdonline.ui.general;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import com.theherdonline.db.entity.GenderEntity;

public class PickupGenderDialogFragment extends DialogFragment {
    public static String ARG_Title = "arg_title";
    public static String ARG_List_data = "arg_list_data";


    final public static Integer INCLUDE_ALL = 0;
    final public static Integer STADANDAR = 1;
    private OnSelectedItemListener onClickListener;

   // OnSelectedItemListener onSelectedItemListener;
    private List<GenderEntity> mList;
    private String mTitle;

    public static PickupGenderDialogFragment newInstance(String title, List<GenderEntity> list, OnSelectedItemListener onDateSetListener) {
        PickupGenderDialogFragment pickerFragment = new PickupGenderDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_Title,title);
        bundle.putParcelableArrayList(ARG_List_data, new ArrayList<>(list));
        pickerFragment.setOnDateSetListener(onDateSetListener);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(getArguments() != null)
        {
            mTitle = getArguments().getString(ARG_Title);
            mList = getArguments().getParcelableArrayList(ARG_List_data);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        GenderListAdapter adapter = new GenderListAdapter(getContext(), mList);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickListener.OnSelectedItem(mList.get(which));
                dismiss();
            }
        });

        Dialog dialog = builder.create();
        if (mTitle != null)
        {
            builder.create().setTitle(mTitle);
        }
        return dialog;
    }

    public void setOnDateSetListener(OnSelectedItemListener onClickListener)
    {
        this.onClickListener = onClickListener;
    }

    public interface OnSelectedItemListener{
        void OnSelectedItem(GenderEntity genderEntity);
    }
}
