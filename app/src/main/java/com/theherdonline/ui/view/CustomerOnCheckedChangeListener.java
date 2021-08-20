package com.theherdonline.ui.view;

import androidx.viewpager.widget.ViewPager;
import android.widget.RadioGroup;


public class CustomerOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

    private ViewPager mViewPager;
    private Integer []mButtonIdArray;


    public CustomerOnCheckedChangeListener(ViewPager viewPager, Integer[] idArray) {
        super();
        this.mViewPager = viewPager;
        this.mButtonIdArray = idArray;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (mButtonIdArray == null || mButtonIdArray.length == 0)
            return;
        Integer index = null;
        for (int i = 0; i < mButtonIdArray.length; i++)
        {
            if (mButtonIdArray[i] == checkedId)
            {
                index = i;
                break;
            }
        }
        if (index == null)
            return;
        if (mViewPager != null)
        {
            if (mViewPager.getCurrentItem() != index)
            {
                mViewPager.setCurrentItem(index);
            }
        }
    }


}
