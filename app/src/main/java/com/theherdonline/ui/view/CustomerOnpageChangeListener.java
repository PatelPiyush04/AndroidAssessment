package com.theherdonline.ui.view;

import com.google.android.material.tabs.TabLayout;
import android.widget.RadioGroup;

public class CustomerOnpageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {

    RadioGroup mRadioGroup;

    Integer [] mArrayId;

    public CustomerOnpageChangeListener(TabLayout tabLayout, RadioGroup radioGroup, Integer [] arrayId) {
        super(tabLayout);
        mRadioGroup = radioGroup;
        mArrayId = arrayId;
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        if (mRadioGroup != null)
        {
            Integer id   = mArrayId[position];

            if (id != mRadioGroup.getCheckedRadioButtonId())
            {
                mRadioGroup.check(id);
            }
        }
    }
}
