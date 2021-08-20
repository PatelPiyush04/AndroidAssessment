package com.theherdonline.ui.general;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.db.entity.EntitySaleyard;

public class SaleyardPickupFragment extends DialogFragment
        /*implements TimePickerDialog.OnTimeSetListener*/ {
    public static String TAG_DATE = "date";

    public List<EntitySaleyard> mEntitySaleyardList;
    private InterfaceSelectSaleyard mListener;

    public static SaleyardPickupFragment newInstance(List<EntitySaleyard> entitySaleyardList,
                                                     SaleyardPickupFragment.InterfaceSelectSaleyard listener) {

        //Bundle args = new Bundle();
        //args.putLong(TAG_DATE,initDateTime == null ? DateTime.now().getMillis() : initDateTime.getMillis());
        SaleyardPickupFragment fragment = new SaleyardPickupFragment();
        fragment.setmListener(listener);
        fragment.setmEntitySaleyardList(entitySaleyardList);
        //fragment.setArguments(args);
        //fragment.setListener(listener);
        return fragment;
    }

    public void setmEntitySaleyardList(List<EntitySaleyard> mEntitySaleyardList) {
        this.mEntitySaleyardList = mEntitySaleyardList;
    }

    public void setmListener(InterfaceSelectSaleyard mListener) {
        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        SaleyardListAdapter adapter = new SaleyardListAdapter(getContext(), mEntitySaleyardList);
        builder.setTitle(R.string.txt_select_saleyard)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            mListener.OnClickSaleyard(mEntitySaleyardList.get(which));
                            dismiss();
                    }
                });
        return builder.create();
    }

    public interface InterfaceSelectSaleyard{
        void OnClickSaleyard(EntitySaleyard entitySaleyard);
    }
}

