package com.theherdonline.ui.general;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.theherdonline.R;

public class HerdButton extends ConstraintLayout {

    String mText = "";
    Drawable mIcon;
    Context mContext;
    TextView mTextViem;
    ImageView mImageViewIcon;

    public HerdButton(Context context) {
        super(context);
    }

    public HerdButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mText = "";
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.HerdButton,
                0, 0);

        try {
            mText = a.getString(R.styleable.HerdButton_HerdButton_text);
            mIcon = a.getDrawable(R.styleable.HerdButton_HerdButton_icon); //  ContextCompat.getDrawable(mContext,a.getInteger(R.styleable.HerdButton_HerdButton_icon, 1));
        } finally {
            a.recycle();
        }
        init();

    }

    private void init() {
        View rootView = inflate(mContext, R.layout.item_button, this);
        mTextViem = rootView.findViewById(R.id.tv_title);
        mImageViewIcon = rootView.findViewById(R.id.image_icon);
        mTextViem.setText(mText);
        if (mIcon == null) {
            mImageViewIcon.setVisibility(GONE);
        }
        else
        {
            mImageViewIcon.setImageDrawable(mIcon);
        }
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        if (result < desiredSize){
            Log.e("ChartView", "The view is too small, the content might get cut");
        }
        return result;
    }



   /* @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 500;
        int desiredHeight = 200;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
   //     setMeasuredDimension(width, height);




*//*        Log.d("Chart onMeasure w", MeasureSpec.toString(widthMeasureSpec));
        Log.d("Chart onMeasure h", MeasureSpec.toString(heightMeasureSpec));

        int desiredWidth = getSuggestedMinimumWidth() + getPaddingLeft() + getPaddingRight();
        int desiredHeight = getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom();

        Log.d("desiredHeight", String.valueOf(desiredHeight));*//*



        GradientDrawable gradientDrawable = (GradientDrawable) ContextCompat.getDrawable(getContext(),R.drawable.background_button);
        gradientDrawable.setSize(widthSize,heightSize);
        gradientDrawable.setCornerRadius(heightSize/2);
        this.setBackground(gradientDrawable);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }*/

    public HerdButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
