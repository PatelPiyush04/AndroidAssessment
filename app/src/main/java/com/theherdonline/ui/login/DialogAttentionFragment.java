package com.theherdonline.ui.login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.theherdonline.R;

import org.apache.commons.lang3.StringUtils;

public class DialogAttentionFragment extends DialogFragment {

    final public static String ARG_PRODUCER_ID = "arg-producer";
    final public static String ARG_LIVESTOCK_ID = "arg-livestock";
    final public static String ARG_TITLE = "arg-title";
    final public static String ARG_DESCRIPTION = "arg-description";



    private Integer mBuyerId;
    private Integer mLivestockId;
    private Float mPrice;
    private AcceptOfferInterface mListener;

    private String mTtitle;
    private String mDescription;

    public void setmListener(AcceptOfferInterface mListener) {
        this.mListener = mListener;
    }

    public static DialogAttentionFragment newInstance(Integer producerId, Integer livestockId, AcceptOfferInterface offerInterface,
                                                      String title, String des) {
        DialogAttentionFragment pickerFragment = new DialogAttentionFragment();
        Bundle bundle = new Bundle();
        if (producerId != null)
        {
            bundle.putInt(ARG_PRODUCER_ID,producerId);
        }
        bundle.putInt(ARG_LIVESTOCK_ID,livestockId);
        bundle.putString(ARG_TITLE,title);
        bundle.putString(ARG_DESCRIPTION,des);
        pickerFragment.setmListener(offerInterface);
        pickerFragment.setArguments(bundle);
        return pickerFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments() != null)
        {
            mBuyerId = getArguments().getInt(ARG_PRODUCER_ID);
            mLivestockId = getArguments().getInt(ARG_LIVESTOCK_ID);
            mTtitle = getArguments().getString(ARG_TITLE);
            mDescription = getArguments().getString(ARG_DESCRIPTION);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View dialogView = inflater.inflate(R.layout.dialog_attention_agent, null);
        ((TextView)dialogView.findViewById(R.id.tv_title)).setText(StringUtils.defaultString(mTtitle));
        if (StringUtils.isEmpty(mDescription))
        {
            ((TextView)dialogView.findViewById(R.id.tv_des)).setVisibility(View.GONE);

        }
        else
        {
            ((TextView)dialogView.findViewById(R.id.tv_des)).setVisibility(View.VISIBLE);
            ((TextView)dialogView.findViewById(R.id.tv_des)).setText(StringUtils.defaultString(mDescription));
        }

        String okButtunText = StringUtils.isEmpty(mDescription) ? getString(R.string.txt_confirm) : getString(R.string.txt_accept_offer);
        String editHint = StringUtils.isEmpty(mDescription) ? getString(R.string.txt_enter_sale_price) : getString(R.string.txt_enter_agreed_sale_price);
        EditText editText = dialogView.findViewById(R.id.et_price);
        editText.setHint(editHint);
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(okButtunText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        EditText editText = dialogView.findViewById(R.id.et_price);
                        if (StringUtils.isEmpty(editText.getText().toString()))
                        {
                            Toast.makeText(getContext(),R.string.txt_price_is_required,Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Float price = Float.valueOf(editText.getText().toString());
                        mListener.OnClickAcceptOffer(mBuyerId,mLivestockId,price);
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       // LoginDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    public interface AcceptOfferInterface
    {
        void OnClickAcceptOffer(Integer userId, Integer livestockId, Float price);
    }

}
