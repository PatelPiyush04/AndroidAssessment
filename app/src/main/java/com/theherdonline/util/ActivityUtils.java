package com.theherdonline.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.AnimalCategory;
import com.theherdonline.db.entity.GenderEntity;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.StreamType;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.ui.general.ADType;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId,
                                              int var1, int var2) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(var1, var2);
        transaction.replace(frameId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    public static void addTagFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId, String tag) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void showToast(Context context, String text, boolean isLong) {
        Toast.makeText(context, text, isLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }



        public static void startAddressAutoCompleteActivity(Activity activity, Context context,Integer activityCode) {

        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN, fields).setCountry("AU")
                .build(context);
        activity.startActivityForResult(intent, activityCode);
    }



    public static Spanned htmlText2Spanned(Context context, String string)
    {
        return   Html.fromHtml(ActivityUtils.trimText(context,string));

    }

    public static String formatPlace(Place place)
    {
        if (place == null)
        {
            return null;
        }
        else
        {
            return place.getAddress();
        }
    }

    public static void playVideo(Context context , Media media)
    {
        String path = "https://archive.org/download/ElephantsDream/ed_hd.mp4";
        path = "https://theherdonline.s3.amazonaws.com/livestock/media/260/video.mp4";
       // path = ActivityUtils.getImageAbsoluteUrl(media.getUrl());
        String videoUrl = "http://videosite/myvideo.mp4";
       path = media.getUrl();
        Intent playVideo = new Intent(Intent.ACTION_VIEW);
        playVideo.setDataAndType(Uri.parse(path), "video/*");
        context.startActivity(playVideo);
    }
    public static void playVideo(Context context , String path)
    {
        Intent playVideo = new Intent(Intent.ACTION_VIEW);
        playVideo.setDataAndType(Uri.parse(path), "video/*");
        context.startActivity(playVideo);
    }

    public static Boolean EqualPlace(Place a, Place b)
    {
        if ((a == null && b != null) || (a != null && b == null))
            return false;
        if (a == null && b == null)
            return true;
        return a.getId() == b.getId();
    }



    public static void showToast(Context context, String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static String cent2string(Integer mPrice)
    {
        DecimalFormat df = new DecimalFormat("0.00");
        String s = df.format(mPrice / 100.0f);
        return s;
    }

    public static Integer dollarString2cent(String price)
    {
        return Math.round(Float.valueOf(price) * 100);
    }


    public static Integer centString2cent(String price)
    {
        return Math.round(Float.valueOf(price));
    }

    public static String centString2DollarString(String price)
    {
        String string = cent2string(centString2cent(price));
        return  string;
    }


    public static DividerItemDecoration provideDividerItemDecoration(Context context, int drawableID)
    {
        DividerItemDecoration mItemDividerDecorator = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        mItemDividerDecorator.setDrawable(ContextCompat.getDrawable(context, drawableID));
        return mItemDividerDecorator;
    }


    public static void shareLink(@NonNull Context context, @SuppressWarnings("SameParameterValue") String subject, String text) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (!TextUtils.isEmpty(subject)){
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_title)));
    }


    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId,
                                              @NonNull String tag) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(frameId, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void showWarningDialog(@NonNull Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        dialog.cancel();
                    }
                }).create();
        if (title != null)
            dialog.setTitle(title);
        if (message != null)
            dialog.setMessage(message);
        dialog.show();
    }

    public static void showWarningDialog(@NonNull Context context, String title, String message, DialogInterface.OnClickListener okListener,
                                         DialogInterface.OnClickListener cancleListener ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        if (okListener != null)
        {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(android.R.string.yes), okListener);
        }
        if (cancleListener != null)
        {
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(android.R.string.no), cancleListener);
        }
        if (title != null)
            dialog.setTitle(title);
        if (message != null)
            dialog.setMessage(message);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                if (okListener != null)
                {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorDarkBlue));
                }
                if (cancleListener != null)
                {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context,android.R.color.holo_red_dark));
                }
            }
        });
        dialog.show();
    }




    public static void showWarningDialog(@NonNull Context context, String title, String message,
                                         String neutral, DialogInterface.OnClickListener neutralListener, Integer neutralColor,
                                         String positiveText, DialogInterface.OnClickListener positiveListener, Integer positiveColor,
                                         String negativeText, DialogInterface.OnClickListener negativeListener, Integer negativeColor)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        if (positiveListener != null)
        {
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, ActivityUtils.capFirstLetter(positiveText.toLowerCase()), positiveListener);
        }
        if (neutralListener != null)
        {
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, ActivityUtils.capFirstLetter(neutral.toLowerCase()), neutralListener);
        }
        if (negativeListener != null)
        {
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, ActivityUtils.capFirstLetter(negativeText.toLowerCase()), negativeListener);
        }
        if (title != null)
            dialog.setTitle(title);
        if (message != null)
            dialog.setMessage(message);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                if (neutralListener != null)
                {

                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(neutralColor != null ? neutralColor : ContextCompat.getColor(context, android.R.color.holo_red_dark));
                }
                if (positiveListener != null)
                {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(positiveColor != null ? positiveColor : ContextCompat.getColor(context,R.color.colorDarkBlue));
                }
                if (negativeListener != null)
                {
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(negativeColor != null ? negativeColor : ContextCompat.getColor(context, R.color.colorDarkBlue));
                }
            }
        });
        dialog.show();
    }


    public static void showWarningDialog(@NonNull Context context, String title, String message,
                                         String neutral, DialogInterface.OnClickListener neutralListener,
                                         String positiveText, DialogInterface.OnClickListener positiveListener,
                                         String negativeText, DialogInterface.OnClickListener negativeListener)
    {
        showWarningDialog(context, title, message, neutral, neutralListener, null, positiveText, positiveListener, null, negativeText, negativeListener, null);
    }


    public static void showWarningDialog(@NonNull Context context, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener, Integer positiveColor,
                                         String negativeText, DialogInterface.OnClickListener negativeListener, Integer negativeColor){
        showWarningDialog(context, title, message, null, null, null, positiveText, positiveListener, positiveColor, negativeText, negativeListener, negativeColor);

    }


    public static void showWarningDialog(@NonNull Context context, String title, String message, String positiveText, DialogInterface.OnClickListener positiveListener,
                                         String negativeText, DialogInterface.OnClickListener negativeListener){
        showWarningDialog(context, title, message, null, null, null, positiveText, positiveListener, null, negativeText, negativeListener, null);

    }

    public static void showWarningDialog(@NonNull Context context, Integer title, Integer message, Integer positiveText, DialogInterface.OnClickListener positiveListener,
                                         Integer negativeText, DialogInterface.OnClickListener negativeListener )
    {
        String strTitle = title != null ? context.getString(title) : null;
        String strMessage = message != null ? context.getString(message) : null;
        String strPositiveText = positiveText != null ? context.getString(positiveText) : null;
        String strNegativeText = negativeText != null ? context.getString(negativeText) : null;
        showWarningDialog(context,strTitle,strMessage,strPositiveText,positiveListener,strNegativeText,negativeListener);
    }





    public static void showWarningDialog(@NonNull Context context, String title, String message,
                                         String postitiveButtonText,
                                         String negativeButtonText,
                                         DialogInterface.OnClickListener okListener,
                                         DialogInterface.OnClickListener cancleListener ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setPositiveButton(postitiveButtonText, okListener)
                .setNegativeButton(negativeButtonText,cancleListener)
                .create();
        if (title != null)
            dialog.setTitle(title);
        if (message != null)
            dialog.setMessage(message);
        dialog.show();
    }


    public static void showWarningDialog(@NonNull Context context, String title, String message, DialogInterface.OnClickListener okListener
                                        ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setPositiveButton(android.R.string.yes, okListener)
                .create();
        if (title != null)
            dialog.setTitle(title);
        if (message != null)
            dialog.setMessage(message);
        dialog.show();
    }


    public static void showWarningDialogWithDismiss(@NonNull Context context, String title, String message, DialogInterface.OnDismissListener okListener
    ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        okListener.onDismiss(dialog);
                    }
                })
                .setOnDismissListener(okListener).create();
        if (title != null)
            dialog.setTitle(title);
        if (message != null)
            dialog.setMessage(message);
        dialog.show();
    }



    public static void showInputErrorPopup(Context context, String msg)
    {
        showWarningDialog(context,context.getString(R.string.app_name),msg);
    }

    public static boolean checkInput(Context context, String input, String fieldName)
    {
        boolean ret = false;
        if (TextUtils.isEmpty(input))
        {
            showWarningDialog(context,context.getString(R.string.app_name),
                    context.getString(R.string.txt_invalid_input,  fieldName,fieldName));
        }
        else
        {
            ret = true;
        }
        return ret;
    }

    public static boolean checkCreditCardName(Context context, String input, String fieldName)
    {
        return checkInput(context,input,fieldName);
    }



    public static boolean checkCreditCardNumber(Context context, String input, String fieldName)
    {
        return checkInput(context,input,fieldName);
    }


    public static boolean checkCreditCardExpireDate(Context context, String input, String fieldName)
    {
        return checkInput(context,input,fieldName);
    }


    public static boolean checkCreditCardCvv(Context context, String input, String fieldName)
    {
        return checkInput(context,input,fieldName);
    }

    public static boolean checkEmailAddress(Context context, String emailAddress, String fieldName)
    {
        boolean ret = false;
        if (TextUtils.isEmpty(emailAddress) || ! validateEmailAddress(emailAddress))
        {
            showWarningDialog(context,context.getString(R.string.app_name),
                    context.getString(R.string.txt_invalid_input,  fieldName, fieldName));
        }
        else
        {
            ret = true;
        }
        return ret;
    }

    public static boolean checkPassword(Context context, String password1, String password2)
    {
        boolean ret = false;
        int minPasswordLength = 6;
        if (TextUtils.isEmpty(password1) || TextUtils.isEmpty(password2))
        {
            showWarningDialog(context,context.getString(R.string.app_name),
                            context.getString(R.string.txt_invalid_input,
                            context.getString(R.string.txt_password),
                            context.getString(R.string.txt_password)));
        } else if (!password1.equals(password2))
        {
            showWarningDialog(context,context.getString(R.string.app_name),
                    context.getString(R.string.txt_password_invalid_same));
        }
        else if (password1.length() < minPasswordLength)
        {
            showWarningDialog(context,context.getString(R.string.app_name),
                    context.getString(R.string.txt_password_invalid_length, minPasswordLength));
        }
        else
        {
            ret = true;
        }
        return ret;
    }

    public static void loadImage(Context context, ImageView imageView, int resId, Integer placeholerId)
    {
        GlideApp.with(context).load(resId)
                .placeholder(placeholerId)
                .into(imageView);
    }

    public static void loadImage1(Context context, ImageView imageView, String url, Integer placeholerId)
    {
        Bitmap thumbnail = (BitmapFactory.decodeFile(url));
        //Log.w("path of image from gallery......******************.........", url+"");
        imageView.setImageBitmap(thumbnail);
    }
    public static void loadImage(Context context, ImageView imageView, String url, Integer placeholerId)
    {
//       url=  url.replaceAll("%20"," ");
           // url="/storage/emulated/0/DCIM/Screenshots/Screenshot_20200427-194420_OneUIHome.jpg";

        File newImage =  new File(url);
        if (newImage.isFile())
        {
            if (placeholerId != null)
            {
                GlideApp.with(context).load(newImage)
                        .placeholder(placeholerId)
                        .into(imageView);
            }
            else
            {
                GlideApp.with(context).load(newImage)
                        .into(imageView);
            }
        }
        else
        {
            if (placeholerId != null)
            {
                GlideApp.with(context).load(url)
                        .placeholder(placeholerId)
                        .into(imageView);
            }
            else
            {
                GlideApp.with(context).load(url)
                        .into(imageView);
            }
        }


    }

    public static void loadCircleImage(Context context, ImageView imageView, String url, Integer placeholerId)
    {

        File newImage =  new File(url);
        if (newImage.isFile())
        {
            Uri photoUri = Uri.fromFile( new File( url));
            GlideApp.with(context).load(newImage)
                    .placeholder(placeholerId)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
        else
        {

            GlideApp.with(context).load(url)
                    .placeholder(placeholerId)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView);
        }
        }




    public static void playAudio(Context context, String path) throws IOException
    {
        String url = ActivityUtils.getImageAbsoluteUrl(path);
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepare(); // might take long! (for buffering, etc)
        mediaPlayer.start();

    }

    public static void openSmsMsgAppFnc(Activity activity, String mblNumVar, String smsMsgVar)
    {
        Intent smsMsgAppVar = new Intent(Intent.ACTION_VIEW);
        smsMsgAppVar.setData(Uri.parse("sms:" +  mblNumVar));
        smsMsgAppVar.putExtra("sms_body", smsMsgVar);
        activity.startActivity(smsMsgAppVar);
    }




    public static UserType parseUserType(String userType)
    {
        if (AppConstants.USER_TYPE_AGENT.equals(userType))
        {
            return UserType.AGENT;
        }
        else
        {
            return UserType.PRODUCER;
        }

    }

    public static String getImageAbsoluteUrl(String url){

        if (url == null)
        {
            return null;
        }

        if (url.startsWith("https:") || url.startsWith("http:"))
        {
            return url;
        }
        else {
            return AppConstants.IMAGE_URL + "/" + url;
        }
    }


    public static Boolean isStartWithHttp(String url){
        return url.startsWith("https:") || url.startsWith("http:");
    }

    public static String getImageAbsoluteUrlFromWeb(String url){
        if (url.startsWith("https:") || url.startsWith("http:"))
        {
            return url;
        }
        else {
            if (url.startsWith("/")) {
                return AppConstants.SERVER_URL_DEV + url.substring(1, url.length());
            }
            else
            {
                return AppConstants.SERVER_URL_DEV + url;
            }
        }
    }

    public static String getImageAbsoluteUrlFromServer(String url){
        if (url.startsWith("https:") || url.startsWith("http:"))
        {
            return url;
        }
        else {
            return AppConstants.IMAGE_URL + "/" + url;
        }
    }

    public static String getAudioAbsoluteUrl(String url){
        if ((!url.startsWith("market-reports")) || url.startsWith("https:") || url.startsWith("http:"))
        {
            return url;
        }
        else
        {
            return AppConstants.IMAGE_URL + "/" + url;
        }
    }




    public static boolean deleteFile(String path)
    {
        if (path == null)
        {
            return false;
        }

        File file = new File(path);
        if (file.exists() && file.isFile())
        {
            file.delete();
           // Log.d(TAG, "deleteFile: " + file.getAbsolutePath());
            return true;
        }
        else
        {
            return false;
        }
    }


    public static Boolean isAudioFile(Context context, String url){

        return new File(url).isFile();
    }


    public static String netWorkErrorInfo(@NonNull Context context, String msg)
    {
        return TextUtils.isEmpty(msg) ? context.getString(R.string.warning_dialog_network_error) : msg;
    }

    public static boolean validateEmailAddress(String emailStr) {
        String REGEX_EMAIL = "[A-Za-z0-9._]+@[A-Za-z0-9.-]+.[A-Za-z]{1,}";
        return emailStr.matches(REGEX_EMAIL);
    }





    public static void showServerErrorDialog(Context context, Integer code, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        dialog.cancel();
                    }
                }).create();
        if (code != null) {
            dialog.setTitle(context.getString(R.string.warning_dialog_server_error_num, code));
        }
        dialog.setMessage( message != null ?  trimTextEmpty(message) : context.getString(R.string.txt_unknown_error_try_again));
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,android.R.color.holo_red_dark));
            }
        });
        dialog.show();




        //ActivityUtils.showWarningDialog(context, context.getString(R.string.warning_dialog_server_error_num, code),
        //        message != null ?  trimTextEmpty(message) : context.getString(R.string.txt_unknown_error_try_again));
    }


    public static String postDateString(DateTime dateTime)
    {
        if (dateTime != null) {
            DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
            return format.print(dateTime);
        }else
        {
            return null;
        }
    }

    public static String postDateTimeString(DateTime dateTime, String timeFormat)
    {
        if (dateTime != null) {
            DateTimeFormatter format = DateTimeFormat.forPattern(timeFormat);
            return format.print(dateTime);
        }else
        {
            return null;
        }
    }

    public static DateTime strToDateTime(String dateTime)
    {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        return format.parseDateTime(dateTime);
    }

    public static DateTime strToDate(String dateTime)
    {
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyy-MM-dd");
        return format.parseDateTime(dateTime);
    }



    public static GenderEntity convertString(String sex)
    {
        GenderEntity s = new GenderEntity();
        s.setId(1);
        s.setName("female");
        return s;
    }

    public static String trimText(Context context,  String string)
    {
        return !TextUtils.isEmpty(string) ? string : context.getString(R.string.title_n_a);
    }


    public static String trimFullName(String firstName, String lastName)
    {
        return firstName != null ? firstName : "" + lastName != null ? lastName : "";
    }

    public static String trimTextEmpty(String string)
    {
        return string != null ? string : "";
    }

    public static String trimTextEmpty(String string, String def)
    {
        return string != null ? string : def;
    }



    public static String trimTextEmpty(Integer integer)
    {
        return integer != null ? String.valueOf(integer) : "";
    }


    public static String trimTextEmpty(Integer integer, String def)
    {
        return integer != null ? String.valueOf(integer) : def;
    }

    public static String trimTextEmpty(Float integer)
    {
        return integer != null ? String.valueOf(integer) : "";
    }


    public static String trimTextEmpty(Float integer, String  def)
    {
        return integer != null ? String.valueOf(integer) : def;
    }


    public static String trimIntegerEmpty(Integer integer)
    {
        return integer != null ? String.valueOf(integer) : "";
    }

    public static ADType convertAdType(Integer id)
    {
        return ADType.values()[id - 1];
    }

    public static AnimalCategory convertAnimal(Integer id)
    {
        AnimalCategory animalCategory = new AnimalCategory();
        animalCategory.setId(1);
        animalCategory.setName("adasf");
        return animalCategory;
    }

    public static void openWebPage(final Context context, final String url)
    {
        if (StringUtils.isEmpty(url))
        {
            Toast.makeText(context,context.getString(R.string.txt_this_link_is_invalide),Toast.LENGTH_SHORT).show();
        }
        else {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        }
    }


    public static String getStreamType(StreamType streamType)
    {
        String type = AppConstants.TAG_include_my_ad;
        switch (streamType)
        {
            case POST:
                 type =  AppConstants.TAG_streamType_post;
                 break;
            case SALEYARD:
                type =  AppConstants.TAG_streamType_saleyard;
                break;
            case LIVESTOCK:
                type = AppConstants.TAG_streamType_livestock;
                break;
            case ALL:
                type = null;
                break;
            default:
                type = AppConstants.TAG_streamType_post;
        }
        return type;
    }

    public static StreamType convertStr2StreamType(String type)
    {
        if (AppConstants.TAG_streamType_livestock.equals(type))
        {
            return StreamType.LIVESTOCK;
        }
        else if(AppConstants.TAG_streamType_saleyard.equals(type))
        {
            return StreamType.SALEYARD;
        }
        else if(AppConstants.TAG_streamType_post.equals(type))
        {
            return StreamType.POST;
        }
        else
        {
            return StreamType.ALL;
        }
    }


    public static void startCallingApp(Activity activity, String number)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        activity.startActivity(intent);
    }

    public static void startSendMailApp(Activity activity, String emailAddress) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + emailAddress));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Email from " + activity.getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, "Place your email message here ...");
        activity.startActivity(Intent.createChooser(intent, "Send Email"));
    }

    public static void share(@NonNull Context context, @SuppressWarnings("SameParameterValue") String subject, String text) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        if (!TextUtils.isEmpty(subject)){
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_title)));
    }

    public static String capFirstLetter(String s)
    {
        if (s.length() == 1)
        {
            return s.toUpperCase();
        }
        else
        {
            return s.substring(0,1).toUpperCase() + s.substring(1);
        }
    }

    public static Map<LatLng, List<EntityLivestock>> groupAdvertismentList(List<EntityLivestock> list)
    {
        // add one into a group if the digits before the 3 digits after the decimal point are same.
       // EntityLivestock advertisement = list.get(0);

        Map<LatLng, List<EntityLivestock>> groupItem = new HashMap<LatLng, List<EntityLivestock>>();
        for (EntityLivestock ad: list) {
            if (ad.getLng() != null && ad.getLng() != 0.0 && ad.getLat() != null && ad.getLat() != 0.0) {
                LatLng key = new LatLng(Math.floor(ad.getLat() * 100) / 100f, Math.floor(ad.getLng() * 100) / 100f);
                if (groupItem.get(key) == null) {
                    groupItem.put(key, new ArrayList<EntityLivestock>());
                }
                groupItem.get(key).add(ad);
            } }
        return groupItem;
    }

    public static Map<LatLng, List<EntitySaleyard>> groupSaleyardList(List<EntitySaleyard> list)
    {
        // add one into a group if the digits before the 3 digits after the decimal point are same.

        Map<LatLng, List<EntitySaleyard>> groupItem = new HashMap<LatLng, List<EntitySaleyard>>();
        if (list != null || list.size() != 0) {
            for (EntitySaleyard ad : list) {
                               if (ad.getLng() != null && ad.getLng() != 0.0 && ad.getLat() != null && ad.getLat() != 0.0) {
                    LatLng key = new LatLng(Math.floor(ad.getLat() * 100) / 100f, Math.floor(ad.getLng() * 100) / 100f);
                    if (groupItem.get(key) == null) {
                        groupItem.put(key, new ArrayList<EntitySaleyard>());
                    }
                    groupItem.get(key).add(ad);
                }
            }
        }
        return groupItem;
    }


    public static void showWarningDialog(Context applicationContext, String string, String update_app, String update, DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener1, Object o) {
    }
}
