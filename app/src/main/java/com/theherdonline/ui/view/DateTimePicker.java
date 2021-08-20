package com.theherdonline.ui.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;

import org.joda.time.DateTime;

import com.theherdonline.R;
import com.theherdonline.ui.general.DatePickerFragment;

public class DateTimePicker extends DialogFragment {

    InterfaceTimeDate mListener;

    DateTime mDateTime;

    public static DateTimePicker newInstance(InterfaceTimeDate interfaceTimeDate, DateTime initTime) {

       // Bundle args = new Bundle();

        DateTimePicker fragment = new DateTimePicker();
       // fragment.setArguments(args);

        fragment.setmListener(interfaceTimeDate);
        fragment.setmDateTime(initTime);


        return fragment;
    }

    public void setmDateTime(DateTime mDateTime) {
        this.mDateTime = mDateTime;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction


        if (mDateTime == null)
        {
            mDateTime  = new DateTime();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        //FragmentDateTimePickerBinding  mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_date_time_picker,  null, true);
       // FragmentDateTimePickerBinding mBinding = FragmentDateTimePickerBinding.inflate(inflater);

        View view =  inflater.inflate(R.layout.fragment_date_time_picker,null);

        NumberPicker numberPickerHour = view.findViewById(R.id.picker_hour);
        numberPickerHour.setMinValue(1);
        numberPickerHour.setMaxValue(12);
        String [] stringHours = new String[12];
        for (int i = 0 ; i < 12; i ++)
        {
            stringHours[i] =  String.format("%02d", i + 1);
        }
        numberPickerHour.setDisplayedValues(stringHours);
        numberPickerHour.setWrapSelectorWheel(true);
        NumberPicker numberPickerMin = view.findViewById(R.id.picker_min);
        numberPickerMin.setMinValue(0);
        numberPickerMin.setMaxValue(59);
        String [] stringMin = new String[60];
        for (int i = 0 ; i < 60; i ++)
        {
            stringMin[i] =  String.format("%02d", i);
        }
        numberPickerMin.setDisplayedValues(stringMin);
        numberPickerMin.setWrapSelectorWheel(true);
        NumberPicker numberPickerDay = view.findViewById(R.id.picker_day);
        numberPickerDay.setMinValue(0);
        numberPickerDay.setMaxValue(1);
        String [] stringDay = new String[2];
        stringDay[0] = "am";
        stringDay[1] = "pm";
        numberPickerDay.setDisplayedValues(stringDay);
        numberPickerDay.setWrapSelectorWheel(true);

        DateTextView dateTextView = view.findViewById(R.id.tv_date);

        if (mDateTime.getHourOfDay() > 12)
        {
            numberPickerHour.setValue(mDateTime.getHourOfDay() - 12);
            numberPickerDay.setValue(1);
        }
        else
        {
            numberPickerHour.setValue(mDateTime.getHourOfDay());
            numberPickerDay.setValue(0);
        }
        numberPickerMin.setValue(mDateTime.getMinuteOfHour());
        dateTextView.setDate(mDateTime);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //int minute = endDate.getMinuteOfHour();
                        //int hourOfDay = endDate.getHourOfDay();
                        mDateTime = new DateTime(year,month + 1,dayOfMonth,0,0);
                        dateTextView.setDate(mDateTime);
                        //mViewModel.getmLiveDataEndDate().setValue(dateTime);
                    }
                },new DateTime());
                newFragment.show(getActivity().getSupportFragmentManager(),"datePicker");
            }
        });

        ImageView imageViewPreDay = view.findViewById(R.id.image_pre_day);

        ImageView imageViewNextDay = view.findViewById(R.id.image_next_day);

        imageViewPreDay.setOnClickListener(l->{

            DateTime currentDate = dateTextView.getDateTime();
            DateTime nextDay = currentDate.plusDays(-1);
            dateTextView.setDate(nextDay);
        });

        imageViewNextDay.setOnClickListener(l->{
            DateTime currentDate = dateTextView.getDateTime();
            DateTime nextDay = currentDate.plusDays(1);
            dateTextView.setDate(nextDay);
        });

        builder.setView(/*inflater.inflate(R.layout.fragment_date_time_picker, null)*/view)
                // Add action buttons
                .setPositiveButton(R.string.txt_set, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        DateTime dateTime = new DateTime();
                        DateTime date = dateTextView.getDateTime();
                        int hour = numberPickerDay.getValue() == 0 ? numberPickerHour.getValue() : numberPickerHour.getValue() + 12;
                        DateTime newDateTime = new DateTime(date.getYear(),date.getMonthOfYear(), date.getDayOfMonth(),hour,
                                numberPickerMin.getValue()
                                );
                        mListener.setTimeDate(newDateTime);
                    }
                })
                .setNegativeButton(R.string.txt_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //LoginDialogFragment.this.getDialog().cancel();
                    }
                });
        builder.setTitle(R.string.txt_pickup_time_date);
        return builder.create();
    }

    public void setmListener(InterfaceTimeDate mListener) {
        this.mListener = mListener;
    }

    public interface InterfaceTimeDate{
        void setTimeDate(DateTime dateTime);
    }

}
