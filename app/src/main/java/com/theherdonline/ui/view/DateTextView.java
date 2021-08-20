package com.theherdonline.ui.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.theherdonline.R;


public class DateTextView extends AppCompatTextView {

    public DateTime mDateTime = new DateTime();
    private Context mContext;
    public DateTextView(Context context) {
        super(context);
        mContext = context;
    }

    public DateTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

    }

    public DateTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

    }

    public void setDate(DateTime dateTime)
    {
        mDateTime = dateTime;
        DateTime now = new DateTime();
        DateTime tomorrow = new DateTime().plusDays(1);
        DateTime yesterday = new DateTime().plusDays(-1);
        if (now.getDayOfYear() == dateTime.getDayOfYear())
        {
            setText(mContext.getString(R.string.txt_today));
        }
        else if(tomorrow.getDayOfYear() == dateTime.getDayOfYear()
                )
        {
            setText(mContext.getString(R.string.txt_tomorrow));

        }
        else if (yesterday.getDayOfYear() == dateTime.getDayOfYear()
                )
        {
            setText(mContext.getString(R.string.txt_yesterday));
        }
        else
        {
            DateTimeFormatter format = DateTimeFormat.forPattern("MMM,dd,yyyy");
            String stringDate = format.print(dateTime);
            setText(stringDate);
        }
    }

    public DateTime getDateTime() {
        return mDateTime;
    }
}
