package com.theherdonline.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;
import com.theherdonline.R;
import com.theherdonline.databinding.ItemAgentCardBinding;
import com.theherdonline.databinding.ItemContactCardBinding;
import com.theherdonline.databinding.ItemLivestockBinding;
import com.theherdonline.databinding.ItemPostBinding;
import com.theherdonline.databinding.ItemSaleyardBinding;
import com.theherdonline.databinding.ItemToolbarBinding;
import com.theherdonline.db.DataConverter;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.Post;
import com.theherdonline.db.entity.User;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.di.MyApplication;
import com.theherdonline.ui.general.ADType;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.herdinterface.PostInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.herdinterface.ShowValueInterface;
import com.theherdonline.ui.view.ImagePagerAdapter;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class UIUtils {


    public static float distance(LatLng start, LatLng end)
    {
        float [] results =  {1.1f,1.2f};
        Location.distanceBetween(start.latitude,start.longitude,end.latitude,end.longitude,results);
        return results[0];
    }

    public static void presentLivestock(Context context, ItemLivestockBinding binding, EntityLivestock livestockItem, LivestockInterface listener) {
        binding.tvTitle.setText(ActivityUtils.trimText(context, livestockItem.getName()));
        binding.tvBidNumber.setText(context.getString(R.string.txt_number_bid, livestockItem.getBid_count() == null ? 0 : livestockItem.getBid_count()));

        if (livestockItem.getQuantity() != null)
        {
            binding.linearQuantity.setVisibility(View.VISIBLE);
            binding.tvQuantity.setText(context.getString(R.string.txt_number_head, livestockItem.getQuantity()));
        }
        else
        {
            binding.linearQuantity.setVisibility(View.GONE);
        }


        if (livestockItem.getAuctions_plus_id() == null || livestockItem.getCategory() != null) {
            binding.linearBreed.setVisibility(View.VISIBLE);
            ActivityUtils.loadImage(context, binding.imageViewAnimal, ActivityUtils.getImageAbsoluteUrlFromWeb(livestockItem.getCategory().getPath()), R.drawable.animal_defaul_photo);
            binding.tvBreed.setText(ActivityUtils.trimTextEmpty(livestockItem.getSub_category().getName()));
        }
        else
        {
            binding.linearBreed.setVisibility(View.GONE);
        }


        if (livestockItem.getAuctions_plus_id() == null)
        {
            binding.tvSaleType.setText(context.getString(R.string.txt_paddock_sale));

        }
        else
        {
            binding.tvSaleType.setText(context.getString(R.string.txt_auctions_plus));
        }


        MyApplication application = (MyApplication) context.getApplicationContext();
        LatLng currentLocation = application.getmAppUtil().getmCurrentLocation();


        if (!StringUtils.isEmpty(livestockItem.getAddress()))
        {
            binding.tvLocation.setText(livestockItem.getAddress());
        }
        else
        {
            binding.tvLocation.setText(context.getString(R.string.txt_n_a));
        }

        if (currentLocation != null && livestockItem.getLat() != null && livestockItem.getLng() != null)
        {
            float [] result = {1.0f,1.0f,1.0f,1.0f,1.0f};
            Location.distanceBetween(currentLocation.latitude,currentLocation.longitude, livestockItem.getLat(), livestockItem.getLng(),result);
            binding.tvDistance.setVisibility(View.VISIBLE);
            binding.tvDistance.setText(String.format ("%.2fKM", result[0] / 1000));
        }
        else
        {
            binding.tvDistance.setVisibility(View.GONE);
        }

        if (livestockItem.getEnd_date() != null) {
            binding.linearExpires.setVisibility(View.VISIBLE);
            String string = TimeUtils.BackendUTC2Local(
                    ActivityUtils.trimTextEmpty(livestockItem.getEnd_date()));
            binding.tvExpires.setText(context.getString(R.string.txt_expires_on, StringUtils.defaultString(string)));
        }
        else
        {
            binding.linearExpires.setVisibility(View.GONE);
        }



        if (livestockItem.getMedia() == null || livestockItem.getMedia().size() == 0) {
            binding.imageNoAvailablePicture.setVisibility(View.VISIBLE);
            binding.listImage.setVisibility(View.INVISIBLE);
        } else {
            binding.imageNoAvailablePicture.setVisibility(View.INVISIBLE);
            binding.listImage.setVisibility(View.VISIBLE);
            ImagePagerAdapter imagePagerAdapter;
            // Remove video
            List<Media> mediaList = new ArrayList<>();
            if (livestockItem.getMedia() != null)
            {
                for (Media media : livestockItem.getMedia())
                {
                  //  if (!AppUtil.isVideo(media))
                   // {
                        mediaList.add(media);
                    //}
                }
            }

            imagePagerAdapter = new ImagePagerAdapter(context, mediaList);
            imagePagerAdapter.setmListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClickLivestock(livestockItem);
                }
            });
            binding.listImage.setAdapter(imagePagerAdapter);
            //}
        }
        if (livestockItem.getAuctions_plus_id() == null)
        {
            binding.imageSaved.setVisibility(View.VISIBLE);
            updateSavedIcon(context, binding.imageSaved, livestockItem.getHas_liked(), livestockItem);
            binding.linearBreed.setVisibility(View.VISIBLE);

        }
        else
        {
            binding.imageSaved.setVisibility(View.GONE);
            binding.linearBreed.setVisibility(View.GONE);
        }
    }

    public static void showValueOnView(ShowValueInterface showValueInterface){}


    public static HashMap<String, List<Media>> classifyMedias(List<Media> list)
    {

        HashMap<String, List<Media>> hashMap = new HashMap<>();
        List<Media> mediaListPdf = new ArrayList<>();
        List<Media> mediaListOthers = new ArrayList<>();
        for (Media media: ListUtils.emptyIfNull(list))
        {
            if (Media.CoolectionName.pdfs.toString().equals(media.getCollection_name()))
            {
                mediaListPdf.add(media);
            }
            else
            {
                mediaListOthers.add(media);
            }
        }

        if (mediaListOthers.size() != 0)
        {
            hashMap.put(Media.CoolectionName.others.name(), mediaListOthers);
        }

        if (mediaListPdf.size() != 0)
        {
            hashMap.put(Media.CoolectionName.pdfs.name(), mediaListPdf);
        }
        return hashMap;
    }


    public static HashMap<String, List<Media>> classifyMediasFullTypes(List<Media> list)
    {

        HashMap<String, List<Media>> hashMap = new HashMap<>();
        List<Media> mediaListPdf = new ArrayList<>();
        List<Media> mediaListVideo = new ArrayList<>();
        List<Media> mediaListImage = new ArrayList<>();
        List<Media> mediaListOthers = new ArrayList<>();


        for (Media media: ListUtils.emptyIfNull(list))
        {
            if (Media.CoolectionName.pdfs.toString().equals(media.getCollection_name()))
            {
                mediaListPdf.add(media);
            }
            else if (Media.CoolectionName.images.toString().equals(media.getCollection_name()))
            {
                mediaListImage.add(media);
            }
            else if (Media.CoolectionName.videos.toString().equals(media.getCollection_name()))
            {
                mediaListVideo.add(media);
            }
            else
            {
                mediaListOthers.add(media);
            }
        }

        if (mediaListOthers.size() != 0)
        {
            hashMap.put(Media.CoolectionName.others.name(), mediaListOthers);
        }

        if (mediaListPdf.size() != 0)
        {
            hashMap.put(Media.CoolectionName.pdfs.name(), mediaListPdf);
        }
        if (mediaListImage.size() != 0)
        {
            hashMap.put(Media.CoolectionName.images.name(), mediaListImage);
        }
        if (mediaListVideo.size() != 0)
        {
            hashMap.put(Media.CoolectionName.videos.name(), mediaListVideo);
        }
        return hashMap;
    }

    public static String ADType2String(Context context, ADType adType) {

        switch (adType) {
            case DRAFT:
                return context.getString(R.string.txt_draft);
            case REJECTED:
                return context.getString(R.string.txt_rejected);
            case COMPLETED:
                return context.getString(R.string.txt_sold).toUpperCase();
            case SCHEDULED:
                return context.getString(R.string.txt_scheduled);
            case REQUEST:
                return context.getString(R.string.txt_request);
            case PUBLISHED:
                return context.getString(R.string.txt_publish);
            default:
                return "";

        }
    }

    public static String str2CurrencyStr(String str)
    {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        try {
            Double d = Double.valueOf(str);
            return format.format(d);
        }
        catch (NumberFormatException e)
        {
            return null;
        }
    }

    public static void updateSavedIcon(Context context, ImageView view, Boolean saved, EntityLivestock item) {
        Drawable savedDrawable = ContextCompat.getDrawable(context, saved ? R.drawable.ic_button_save : R.drawable.ic_button_unsave);
        view.setImageDrawable(savedDrawable);
        view.setTag(saved);
        item.setHas_liked(saved);
    }


    public static void updateSavedIcon(Context context, ImageView view, Boolean saved, EntitySaleyard item) {
        Drawable savedDrawable = ContextCompat.getDrawable(context, saved ? R.drawable.ic_button_save : R.drawable.ic_button_unsave);
        view.setImageDrawable(savedDrawable);
        view.setTag(saved);
        item.setHas_liked(saved);
    }

    public static void showLivestock(Context context, ItemLivestockBinding binding, EntityLivestock livestockItem, LivestockInterface listener, Boolean showLabel) {
        presentLivestock(context, binding, livestockItem, listener);
        binding.linearLayoutContent.setOnClickListener(l -> {
            if (listener != null) {
                listener.onClickLivestock(livestockItem);
            }
        });

        binding.imageNoAvailablePicture.setOnClickListener(l -> {
            if (listener != null) {
                listener.onClickLivestock(livestockItem);
            }
        });

        binding.imageSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickSavedLivestock(livestockItem, binding.imageSaved);
            }
        });
       // binding.imageSaved.setVisibility(livestockItem.getAuctions_plus_id() == null ? View.VISIBLE : View.GONE);

        if (showLabel) {
            ADType adType = DataConverter.Int2ADType(livestockItem.getAdvertisementStatusId());
            binding.tvTypeLabel.setBackground(ContextCompat.getDrawable(context,R.drawable.background_sold_label));
            binding.tvTypeLabel.setText(ADType2String(context, adType));
            binding.tvTypeLabel.setVisibility(View.VISIBLE);
            binding.tvTypeLabel.setVisibility(adType == ADType.COMPLETED ? View.VISIBLE : View.GONE);
        }
        if (livestockItem.getBid_id() != null)
        {
            binding.tvTypeLabel.setBackground(ContextCompat.getDrawable(context,R.drawable.market_gradieent_red));
            binding.tvTypeLabel.setText(context.getString(R.string.txt_sold).toUpperCase());
            binding.tvTypeLabel.setVisibility(View.VISIBLE);
        }
         else if ( BooleanUtils.isTrue(livestockItem.isUnderOffer()))
        {
            binding.tvTypeLabel.setBackground(ContextCompat.getDrawable(context,R.drawable.market_gradient_blue));
            binding.tvTypeLabel.setText(context.getString(R.string.txt_under_offer).toUpperCase());
            binding.tvTypeLabel.setVisibility(View.VISIBLE);
        } else {
            binding.tvTypeLabel.setVisibility(View.GONE);
        }
        binding.executePendingBindings();
    }

    public static void showLivestock(Context context, ItemLivestockBinding binding, List<EntityLivestock> livestockList, LivestockInterface listener) {

        if (livestockList != null && livestockList.size() != 0) {
            EntityLivestock livestockItem = livestockList.get(0);
            presentLivestock(context, binding, livestockItem, listener);
            binding.frameLayoutPhotos.setVisibility(View.GONE);
            binding.tvBidNumber.setVisibility(View.GONE);
            int number = livestockList.size();
            if (number > 1) {
                binding.tvTitle.setText(context.getString(R.string.txt_number_paddock_in_this_area, number));
                binding.linearBreed.setVisibility(View.GONE);
                binding.linearQuantity.setVisibility(View.GONE);
                binding.linearExpires.setVisibility(View.GONE);
                binding.frameLayoutPoddockSale.setVisibility(View.GONE);
            }
            binding.getRoot().setOnClickListener(l -> {
                if (listener != null) {
                    listener.onClickLivestockList(livestockList);
                }
            });

            binding.executePendingBindings();
        }
    }


    public static void showPost(Context context, ItemPostBinding binding, Post postItem, PostInterface listener) {
        //binding.tvTitle.setText(ActivityUtils.trimText(context,postItem.getFullName()));
        binding.tvPostDescription.setText(ActivityUtils.htmlText2Spanned(context, postItem.getText()));
        if (StringUtils.isEmpty(postItem.getPath()))
        {
            binding.imagePhoto.setVisibility(View.GONE);
        }
        else
        {
            binding.imagePhoto.setVisibility(View.VISIBLE);
            ActivityUtils.loadImage(context,binding.imagePhoto,ActivityUtils.getImageAbsoluteUrlFromServer(postItem.getPath()), null);
        }
        binding.tvPosterName.setText(ActivityUtils.trimText(context, postItem.getFullName()));

        binding.tvBidTime.setText(ActivityUtils.trimText(context, TimeUtils.BackendUTC2Local(postItem.getCreatedAt())));
        ActivityUtils.loadCircleImage(context,
                binding.imagePosterAvatar,
                ActivityUtils.getImageAbsoluteUrl(postItem.getAvatarLocationFullUrl()),
                R.drawable.ic_default_person);


      /*  User user = postItem.getUser();
        if (user != null) {
            binding.tvPosterName.setText(ActivityUtils.trimText(context, user.getFullName()));
            binding.tvBidTime.setText(ActivityUtils.trimText(context, TimeUtils.BackendUTC2Local(postItem.getCreatedAt())));
            ActivityUtils.loadCircleImage(context,
                    binding.imagePosterAvatar,
                    ActivityUtils.getImageAbsoluteUrl(user.getAvatarLocation()),
                    R.drawable.ic_default_person);
        } else {
            binding.linearLayoutPoster.setVisibility(View.GONE);
        }*/

        binding.getRoot().setOnClickListener(l -> {
            listener.onClickPost(postItem);
        });
        binding.executePendingBindings();
    }


    public static void showToolbar(ItemToolbarBinding binding, String title, BackPressInterface backPressInterface
    ) {
        binding.tvToolbarTitle.setText(ActivityUtils.trimTextEmpty(title));
        binding.imageGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPressInterface.OnClickGoBackButton();
            }
        });
        binding.imageRight1.setVisibility(View.GONE);
        binding.imageRight2.setVisibility(View.GONE);

    }

    public static void showToolbar(ItemToolbarBinding binding, String title
    ) {
        binding.imageGoback.setVisibility(View.GONE);
        binding.imageRight1.setVisibility(View.GONE);
        binding.imageRight2.setVisibility(View.GONE);
        binding.tvToolbarTitle.setText(ActivityUtils.trimTextEmpty(title));
    }


    public static void showToolbar(Context context, ItemToolbarBinding binding, String title, BackPressInterface backPressInterface,
                                   int imageId1, View.OnClickListener listener1
    ) {

        showToolbar(binding, title, backPressInterface);
        binding.imageRight1.setVisibility(View.VISIBLE);
        binding.imageRight1.setImageDrawable(ContextCompat.getDrawable(context, imageId1));
        binding.imageRight1.setOnClickListener(listener1);
    }


    public static void showToolbar(Context context, ItemToolbarBinding binding, String title, View.OnClickListener listenerLeft,
                                   String command0, View.OnClickListener commandlistener0,
                                   Integer imageId0, View.OnClickListener listener0,
                                   Integer imageId1, View.OnClickListener listener1,
                                   Integer imageId2, View.OnClickListener listener2
    ) {
        binding.tvText1.setVisibility(command0 == null ? View.GONE : View.VISIBLE);
        binding.tvText1.setText((ActivityUtils.trimTextEmpty(command0)));
        if (commandlistener0 != null)
        {
            binding.tvText1.setOnClickListener(commandlistener0);
        }
        if (imageId0 != null)
        {
            binding.imageRight0.setImageDrawable(ContextCompat.getDrawable(context, imageId0));
        }
        binding.imageRight0.setVisibility(imageId0 == null ? View.GONE : View.VISIBLE);
        if (listener0 != null) {
            binding.imageRight0.setOnClickListener(listener0);
        }
        showToolbar(context, binding, title, listenerLeft, imageId1, listener1, imageId2, listener2);
    }

    public static void showToolbar(Context context, ItemToolbarBinding binding, String title, View.OnClickListener listenerLeft,
                                   String command0, View.OnClickListener commandlistener0,
                                   Integer imageId_a, View.OnClickListener listener_a,
                                   Integer imageId0, View.OnClickListener listener0,
                                   Integer imageId1, View.OnClickListener listener1,
                                   Integer imageId2, View.OnClickListener listener2
    ) {


        binding.imageLeft.setVisibility(imageId_a == null ? View.GONE : View.VISIBLE);
        if (listener_a != null) {
            binding.imageLeft.setImageDrawable(ContextCompat.getDrawable(context, imageId_a));
            binding.imageLeft.setOnClickListener(listener_a);
        }
        showToolbar(context,binding,title,listenerLeft, command0, commandlistener0, imageId0, listener0,imageId1, listener1, imageId2, listener2);
    }


    public static void showToolbar(Context context, ItemToolbarBinding binding, String title, View.OnClickListener listenerLeft,
                                   Integer imageId1, View.OnClickListener listener1,
                                   Integer imageId2, View.OnClickListener listener2
    ) {
        binding.imageGoback.setVisibility(listenerLeft == null ? View.INVISIBLE : View.VISIBLE);
        binding.imageRight1.setVisibility(imageId1 == null ? View.GONE : View.VISIBLE);
        binding.imageRight2.setVisibility(imageId2 == null ? View.GONE : View.VISIBLE);
        binding.tvToolbarTitle.setText(ActivityUtils.trimTextEmpty(title));


        if (listenerLeft != null) {
            binding.imageGoback.setOnClickListener(listenerLeft);
        }

        if (imageId1 != null) {
            binding.imageRight1.setImageDrawable(ContextCompat.getDrawable(context, imageId1));
            binding.imageRight1.setOnClickListener(listener1);
        }
        if (imageId2 != null) {
            binding.imageRight2.setImageDrawable(ContextCompat.getDrawable(context, imageId2));
            binding.imageRight2.setOnClickListener(listener2);
        }

    }


    public static void showSaleyard(Context context, ItemSaleyardBinding binding, EntitySaleyard salyardItem, SaleyardInterface listener) {
        binding.tvTitle.setText(ActivityUtils.trimText(context, salyardItem.getName()));
        binding.tvTime.setText(ActivityUtils.trimText(context, TimeUtils.BackendUTC2LocalUTC(salyardItem.getStartsAt())));

        if (StringUtils.isEmpty(salyardItem.getAddress()))
        {
            binding.tvLocation.setVisibility(View.GONE);
        }
        else
        {
            binding.tvLocation.setVisibility(View.VISIBLE);

            binding.tvLocation.setText(ActivityUtils.trimText(context, salyardItem.getAddress()));

        }

        MyApplication application = (MyApplication) context.getApplicationContext();
        LatLng currentLocation = application.getmAppUtil().getmCurrentLocation();
        Double zero = new Double(0.0);
        if (currentLocation != null && salyardItem.getLat() != null && (!zero.equals(salyardItem.getLat()))
                && salyardItem.getLng() != null && (! zero.equals(salyardItem.getLng())))
        {
            float [] result = {1.0f,1.0f,1.0f,1.0f,1.0f};
            Location.distanceBetween(currentLocation.latitude,currentLocation.longitude, salyardItem.getLat(), salyardItem.getLng(),result);
            binding.tvDistance.setVisibility(View.VISIBLE);
            binding.tvDistance.setText(String.format ("%.2fKM", result[0] / 1000));
        }
        else
        {
            binding.tvDistance.setVisibility(View.GONE);
        }

        if ((salyardItem.getLat() == null || zero.equals(salyardItem.getLat())) &&
                (salyardItem.getLng() == null || zero.equals(salyardItem.getLng()))  &&
                StringUtils.isEmpty(salyardItem.getAddress()))
        {
            binding.linearLocation.setVisibility(View.GONE);
        }
        else
        {
            binding.linearLocation.setVisibility(View.VISIBLE);
        }



        updateSavedIcon(context, binding.imageSaved, salyardItem.getHas_liked(), salyardItem);
        binding.tvSaleyardType.setText(context.getString(R.string.txt_str_sale, ActivityUtils.trimTextEmpty(salyardItem.getType())));

        if (salyardItem.getHeadcount() == null) {
            binding.linearQuantity.setVisibility(View.GONE );
        }
        else
        {
            binding.linearQuantity.setVisibility(View.VISIBLE);
            binding.tvHeadcount.setText(ActivityUtils.trimTextEmpty(context.getString(R.string.txt_num_head, salyardItem.getHeadcount())));
        }

        if (salyardItem.getSaleyardCategory() != null) {
            binding.linearAnimalType.setVisibility(View.VISIBLE);
            ActivityUtils.loadImage(context, binding.imageViewAnimal, ActivityUtils.getImageAbsoluteUrlFromWeb(salyardItem.getSaleyardCategory().getPath()), R.drawable.animal_defaul_photo);
            binding.tvAnimal.setText(ActivityUtils.trimTextEmpty(salyardItem.getSaleyardCategory().getName()));
        }
        else
        {
            binding.linearAnimalType.setVisibility(View.GONE);
        }
        binding.getRoot().setOnClickListener(l -> {
            if (listener != null) {
                listener.OnClickSaleyard(salyardItem);
            }
        });
        binding.imageSaved.setOnClickListener(l -> {
            if (listener != null) {
                listener.onClickSavedSaleyard(salyardItem, binding.imageSaved);
            }
        });

    }

    public static void showSaleyard(Context context, ItemSaleyardBinding binding, List<EntitySaleyard> entitySaleyardList, SaleyardInterface listener) {
        if (entitySaleyardList != null && entitySaleyardList.size() != 0) {
            EntitySaleyard salyardItem = entitySaleyardList.get(0);
            showSaleyard(context, binding, salyardItem, listener);
            binding.tvTitle.setText(context.getString(R.string.txt_number_saleyard_in_this_area, entitySaleyardList.size()));
            binding.linearTime.setVisibility(View.GONE);
            binding.frameLayoutType.setVisibility(View.GONE);
            binding.linearQuantity.setVisibility(View.GONE);
            binding.linearAnimalType.setVisibility(View.GONE);
            binding.getRoot().setOnClickListener(l -> {
                if (listener != null) {
                    listener.OnClickSaleyardList(entitySaleyardList);
                }
            });

        }
    }


    public static void showContactCard(Context context, ItemContactCardBinding binding, User user, InterfaceContactCard cardListener) {
        ActivityUtils.loadCircleImage(context, binding.imageViewAvatar, ActivityUtils.getImageAbsoluteUrl(user.getAvatarLocation()), R.drawable.animal_defaul_photo);
        binding.tvName.setText(ActivityUtils.trimText(context, user.getFullName()));
        binding.tvLocation.setText(ActivityUtils.trimText(context, user.getAddressLineOne()));
        binding.buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListener.OnClickPhoneCall(user);
            }
        });
        binding.buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListener.OnClickSendEmail(user);
            }
        });
        binding.imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListener.OnClickUserAvatar(user);
            }
        });
    }

    public static void showContactCard(Context context, ItemAgentCardBinding binding, User user, InterfaceContactCard cardListener) {
        ActivityUtils.loadImage(context, binding.imageOrganisation, ActivityUtils.getImageAbsoluteUrl(user.getAvatar_location_full_url()) /*.getOrganisation().getAvatarLocation())*/, R.drawable.logo);
        binding.tvAgentName.setText(ActivityUtils.trimText(context, user.getFullName()));
        binding.buttonCallOrganisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListener.OnClickPhoneCall(user);
            }
        });
        binding.imageOrganisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardListener.OnClickUserAvatar(user);
            }
        });
    }

}
