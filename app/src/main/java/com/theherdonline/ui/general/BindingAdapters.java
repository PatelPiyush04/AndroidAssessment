package com.theherdonline.ui.general;

import androidx.databinding.BindingAdapter;
import android.view.View;

public class BindingAdapters {
    @BindingAdapter("my_visible")
    public static void showHide(View view, boolean show) {
        int showid = show ? View.VISIBLE : View.GONE;
        if (view.getVisibility() != showid)
            view.setVisibility(showid);
    }
}
