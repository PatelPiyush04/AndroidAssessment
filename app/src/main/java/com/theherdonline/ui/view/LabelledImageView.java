package com.theherdonline.ui.view;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.theherdonline.R;
import com.theherdonline.db.entity.Media;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;

public class LabelledImageView extends FrameLayout {

    private TextView mLabel;
    private ImageView mImage;
    private ImageView mImagePlayButton;

    public LabelledImageView(Context context) {
        super(context);
    }

    public LabelledImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LabelledImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LabelledImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init(int index, int total, int resId) {

        inflate(getContext(), R.layout.view_labelled_image, this);
        mLabel = (TextView) findViewById(R.id.tv_label);
        mImage = findViewById(R.id.imageView);
        mImagePlayButton = findViewById(R.id.imageView_play_button);
        mLabel.setText(index + "/" + total);
        mImage.setScaleType(ImageView.ScaleType.FIT_XY);
        mImage.setImageResource(resId);
    }

    public void init(int index, int total, Media media) {

        String imageUrl = AppUtil.isFile(media.getUrl()) ? media.getUrl() : ActivityUtils.getImageAbsoluteUrl(media.getUrl());
        inflate(getContext(), R.layout.view_labelled_image, this);
        mLabel = findViewById(R.id.tv_label);

        mImage = findViewById(R.id.imageView);
        mImagePlayButton = findViewById(R.id.imageView_play_button);

        if (total <= 1) {
            mLabel.setVisibility(INVISIBLE);
        } else {
            mLabel.setVisibility(INVISIBLE);
            //mLabel.setText((index + 1) + "/" + total);
        }
        if (AppUtil.isVideo(media)) {
            //mImage.setBackgroundColor(ContextCompat.getColor(this.getContext(), android.R.color.black));
            mImage.setBackgroundColor(ContextCompat.getColor(this.getContext(), android.R.color.white));
            AppUtil.loadImage(this.getContext(), mImage, imageUrl, R.drawable.image_place_holder);
            mImagePlayButton.setVisibility(VISIBLE);
            mImagePlayButton.setOnClickListener(l->{
               ActivityUtils.playVideo(l.getContext(),media);

            });
        } else {
            mImagePlayButton.setVisibility(INVISIBLE);
            mImage.setBackgroundColor(ContextCompat.getColor(this.getContext(), android.R.color.white));
            AppUtil.loadImage(this.getContext(), mImage, imageUrl, R.drawable.image_place_holder);


        }
    }

    public void init(int index, int total, String imageUrl) {

        inflate(getContext(), R.layout.view_labelled_image, this);
        mLabel = (TextView) findViewById(R.id.tv_label);
        mImage = findViewById(R.id.imageView);
        if (total <= 1) {
            mLabel.setVisibility(INVISIBLE);
        } else {
            mLabel.setVisibility(INVISIBLE);
            //mLabel.setText((index + 1) + "/" + total);
        }
        AppUtil.loadImage(this.getContext(), mImage, imageUrl, R.drawable.image_place_holder);
    }

}
