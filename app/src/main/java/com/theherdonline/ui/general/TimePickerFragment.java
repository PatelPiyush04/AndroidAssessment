package com.theherdonline.ui.general;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.format.DateFormat;

import org.joda.time.DateTime;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        /*implements TimePickerDialog.OnTimeSetListener*/ {
    public static String TAG_DATE = "date";

    public static TimePickerFragment newInstance(TimePickerDialog.OnTimeSetListener listener, DateTime initDateTime) {

        Bundle args = new Bundle();
        args.putLong(TAG_DATE,initDateTime == null ? DateTime.now().getMillis() : initDateTime.getMillis());
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    TimePickerDialog.OnTimeSetListener mListener;


    public void setListener(TimePickerDialog.OnTimeSetListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(getArguments().getLong(TAG_DATE));
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), mListener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    /*public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
    }*/
}

