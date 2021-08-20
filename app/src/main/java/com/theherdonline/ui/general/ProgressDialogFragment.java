package com.theherdonline.ui.general;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.theherdonline.R;

public class ProgressDialogFragment  extends DialogFragment {
    public static ProgressDialogFragment newInstance() {
        ProgressDialogFragment output = new ProgressDialogFragment();
        return output;
    }

    public static ProgressDialogFragment newInstance(DialogInterface.OnDismissListener onDismissListener, String info) {
        ProgressDialogFragment output = new ProgressDialogFragment(onDismissListener, info);
        return output;
    }

    private DialogInterface.OnDismissListener mDismissListener;
    private String mInfo;

    public ProgressDialogFragment(){
        this(null,  null);
    }

    @SuppressLint("ValidFragment")
    public ProgressDialogFragment(DialogInterface.OnDismissListener mDismissListener, String info){
        super();
        this.mDismissListener = mDismissListener;
        this.mInfo = info;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Activity activity = getActivity();
        View view = View.inflate(activity, R.layout.dialog_progress, null);
        TextView textView = view.findViewById(R.id.tv_info);
        ProgressBar pBar = view.findViewById(R.id.progressbar);
        textView.setText(mInfo == null ? getString(R.string.txt_loading) : mInfo);
        //int textColor = ColorUtils.getTextColor(activity);
        //pBar.setIndeterminateTintList(ColorStateList.valueOf(textColor));
        // pBar.setIndeterminateDrawable(Utils.getDrawable(pBar.getIndeterminateDrawable(), textColor));

        return new AlertDialog.Builder(activity)
                .setView(view)
                .setOnDismissListener(mDismissListener)
                .setOnKeyListener(new DialogInterface.OnKeyListener(){
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent){
                        if (keyCode == KeyEvent.KEYCODE_BACK){
                            if (isCancelable()){
                                dialogInterface.dismiss();
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .create();
    }
}
