package com.theherdonline.ui.postad;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.ItemPhotoBinding;
import com.theherdonline.db.entity.Media;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.util.AppUtil;


public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<PhotoRecyclerViewAdapter.ViewHolder> {

    private final List<Media> mValues;
    private Context mContext;
    private MainActivity mMainActivity;

    private LayoutInflater layoutInflater;
    private OnClickPhotoInterface mPhotoInterface;





    public PhotoRecyclerViewAdapter(MainActivity mainActivity, List<Media> items, OnClickPhotoInterface photoListener) {
        mContext = mainActivity;
        mMainActivity = mainActivity;
        mValues = items;
        mPhotoInterface = photoListener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemPhotoBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.item_photo, parent, false);
        return new ViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if  (position == 0)
        {
            holder.binding.imageViewDelete.setVisibility(View.INVISIBLE);
            holder.binding.imageViewVideo.setVisibility(View.INVISIBLE);
            holder.binding.image.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.drawable_camera));
            holder.binding.image.setOnClickListener(l->{
                mMainActivity.popupMediaDialog(AppConstants.ACTIVITY_CODE_POST_LIVESTOCK_TAKE_PHOTO);
            });
        }
        else
        {


        holder.mItem = mValues.get(position-1);
        if (AppUtil.isVideo(holder.mItem))
        {
            if (holder.mItem.getUrl().startsWith("http"))
            {
                //  remote resource
                AppUtil.loadImage(mContext,
                        holder.binding.image,
                        holder.mItem.getUrl(),
                        R.drawable.image_place_holder);
            }
            else {
                // local resource
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(holder.mItem.getUrl(),
                        MediaStore.Images.Thumbnails.MINI_KIND);
                if (thumb != null) {
                    Drawable drawable = new BitmapDrawable(mContext.getResources(), thumb);
                    holder.binding.image.setImageDrawable(drawable);
                }
            }
        }
        else
        {
            AppUtil.loadImage(mContext,
                    holder.binding.image,
                    holder.mItem.getUrl(),
                    R.drawable.image_place_holder);
        }
        holder.binding.imageViewDelete.setVisibility(View.GONE);


        holder.binding.imageViewDelete.setVisibility(position == 1  && !AppUtil.isVideo(holder.mItem) ? View.VISIBLE : View.INVISIBLE);
//        holder.binding.imageViewVideo.setVisibility(AppUtil.isVideo(holder.mItem) ? View.VISIBLE : View.INVISIBLE);
        holder.binding.imageViewVideo.setVisibility(View.INVISIBLE);

        holder.binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.navigation_primary:
                                mPhotoInterface.setPrimaryImage(holder.mItem);
                                return true;
                            case R.id.navigation_delete:
                                  mPhotoInterface.deletePhoto(holder.mItem);
                               // ActivityUtils.playVideo(mContext,holder.mItem);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.inflate(AppUtil.isVideo(holder.mItem) ?  R.menu.menu_image_option_on_video : R.menu.menu_image_option);
                popupMenu.show();

            }
        });
        }





        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mValues.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Media mItem;
        private final ItemPhotoBinding binding;
        public ViewHolder(final ItemPhotoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    public interface OnClickPhotoInterface
    {
        void deletePhoto(Media media);
        void setPrimaryImage(Media media);
    }
}
