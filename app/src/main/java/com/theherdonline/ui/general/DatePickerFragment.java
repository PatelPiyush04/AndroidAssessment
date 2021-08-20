package com.theherdonline.ui.general;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import org.joda.time.DateTime;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        /*implements DatePickerDialog.OnDateSetListener*/ {

    public static String TAG_DATE = "date";
    DatePickerDialog.OnDateSetListener mListener;

    public DatePickerFragment() {
    }

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener,DateTime initDateTime) {
        Bundle args = new Bundle();
        if (initDateTime == null)
        {
            initDateTime = new DateTime();
        }
        args.putLong(TAG_DATE,initDateTime == null ? DateTime.now().getMillis() : initDateTime.getMillis());
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(DatePickerDialog.OnDateSetListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        getArguments().getLong(TAG_DATE);

        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getArguments().getLong(TAG_DATE));
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), mListener, year, month, day);
    }

    /*@Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }*/
}
