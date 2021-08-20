package com.theherdonline.ui.view;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import com.theherdonline.R;
import com.theherdonline.util.AppUtil;

public class PhotoViewImageView extends LinearLayout {

    private TextView mLabel;
    private PhotoView mImage;

    public PhotoViewImageView(Context context) {
        super(context);
    }

    public PhotoViewImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoViewImageView(Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PhotoViewImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(int index, int total, int resId){

        inflate(getContext(),R.layout.view_photoview_image,this);
        mLabel  = findViewById(R.id.tv_label);
        mImage  = findViewById(R.id.imageView);
        mLabel.setText(index + "/" + total);
        //mImage.setScaleType(ImageView.ScaleType.FIT_XY);
        mImage.setImageResource(resId);
    }

    public void init(int index, int total, String imageUrl){

        imageUrl = "https://s3-ap-southeast-2.amazonaws.com/theherdonline/users/4/avatars/tIi2hF8a5s0M4dvIfiZROijFTnPUGFZxs7j69GpE.jpeg";
        inflate(getContext(),R.layout.view_photoview_image,this);
        mLabel  = (TextView)findViewById(R.id.tv_label);
        mImage      = findViewById(R.id.imageView);
        mLabel.setText((index + 1) + "/" + total);
        //mImage.setScaleType(ImageView.ScaleType.FIT_XY);
        AppUtil.loadImage(this.getContext(),mImage,imageUrl,R.drawable.image_place_holder);
    }

}
