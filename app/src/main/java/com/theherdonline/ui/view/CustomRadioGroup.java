package com.theherdonline.ui.view;

import android.content.Context;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.widget.CompoundButton;

import javax.annotation.Nullable;

public class CustomRadioGroup extends AppCompatRadioButton {

    TabLayout.ViewPagerOnTabSelectedListener mListener;

    public CustomRadioGroup(Context context) {
        super(context);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setmListener(TabLayout.ViewPagerOnTabSelectedListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void setOnCheckedChangeListener(@Nullable CompoundButton.OnCheckedChangeListener listener) {
        super.setOnCheckedChangeListener(listener);
    }
}
