package com.theherdonline.ui.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.File;
import java.util.List;

import com.theherdonline.R;
import com.theherdonline.db.entity.Media;
import com.theherdonline.util.ActivityUtils;

public class ImagePagerAdapter extends PagerAdapter {

    private Context mContext;
    private String [] mUrlList;
    private List<Media> mMediaList;
    View.OnClickListener mListener;

    public ImagePagerAdapter(Context context, String [] urlList) {
        this.mContext = context;
        this.mUrlList = urlList;
    }

    public ImagePagerAdapter(Context context, List<Media> mediaList) {
        this.mContext = context;
        int len = mediaList.size();
        mMediaList = mediaList;
        mUrlList = new String[len];
        for (int i = 0; i < len; i++)
        {
            Media media = mediaList.get(i);
            if (! new File(media.getUrl()).exists())
            {
                mUrlList[i] = ActivityUtils.getImageAbsoluteUrl(media.getUrl());
            }
            else
            {
                mUrlList[i] = media.getUrl();

            }
        }

    }

    public void updateList(List<Media> mediaList)
    {
        int len = mediaList.size();
        mMediaList = mediaList;
        mUrlList = new String[len];
        for (int i = 0; i < len; i++)
        {
            Media media = mediaList.get(i);
            if (! new File(media.getUrl()).exists())
            {
                mUrlList[i] = ActivityUtils.getImageAbsoluteUrl(media.getUrl());
            }
            else
            {
                mUrlList[i] = media.getUrl();

            }
        }
        notifyDataSetChanged();
    }


    public void setmListener(View.OnClickListener mListener) {
        this.mListener = mListener;
    }

    private int[] mImages = new int[]{
            R.drawable.animal_defaul_photo
    };

    @Override
    public int getCount() {
        return mUrlList.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        Integer count =  getCount();
        LabelledImageView imageView = new LabelledImageView(mContext); //,position + 1, mImages.length);
        FrameLayout.LayoutParams tp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(tp);
        if (mUrlList != null)
        {
            //imageView.init(position,mUrlList.length,mUrlList[position]);
            imageView.init(position,mMediaList.size(),mMediaList.get(position));
            imageView.setOnClickListener(
                    mListener
            );
        }
        container.addView(imageView);
       // try {

            //if (position <= mUrlList.length)
            //{
       // if (position == container.getChildCount())
        //        container.addView(imageView, position);

            //}
       // }
       // catch (Exception e)
        //{

        //}
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }


}
