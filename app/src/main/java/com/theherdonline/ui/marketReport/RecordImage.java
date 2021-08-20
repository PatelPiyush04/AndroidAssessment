package com.theherdonline.ui.marketReport;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.theherdonline.R;
import com.theherdonline.ui.general.InterfacePermissionCheck;

public class RecordImage extends AppCompatImageView {
    boolean mStartRecording = true;
    InterfaceAudioRecorder mAudioRecordListener;
    InterfacePermissionCheck mPermissionListener;

    OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPermissionListener.AudioRecordPermission()) {
                mAudioRecordListener.startRecord(mStartRecording);
                if (mStartRecording) {
                    setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_button_stoprecording));
                } else {
                    setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_button_playrecording));
                }
                mStartRecording = !mStartRecording;
            }
        }
    };

    public RecordImage(Context context) {
        super(context);
    }

    public RecordImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecordImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmAudioRecordListener(InterfaceAudioRecorder mAudioRecordListener) {
        this.mAudioRecordListener = mAudioRecordListener;
        setOnClickListener(mClickListener);
    }

    public void setmPermissionListener(InterfacePermissionCheck mPermissionListener) {
        this.mPermissionListener = mPermissionListener;
    }

}
