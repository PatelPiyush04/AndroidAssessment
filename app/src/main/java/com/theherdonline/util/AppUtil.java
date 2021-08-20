package com.theherdonline.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.DataConverter;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.Permission;
import com.theherdonline.ui.home.PermissionStatus;

import static com.google.common.primitives.Ints.max;

@Singleton
public class AppUtil {

    private static final String TAG = AppUtil.class.getName();
    public String IGNYTE_PREFERENCES = AppConstants.APP_NAME;
    public String SHARE_TOKEN = "share-access-token";
    public String SHARE_REFRESH_TOKEN = "share-refresh-token";
    public String SHARE_USER_TYPE = "share-user-type";
    public String SHARE_PERMISSION_TYPE = "share-permission_type";
    public String SHARE_PERMISSION_CREATELIVESTREAM_TYPE = "share-permission_createlivestream";

    public String SHARE_USER_ID = "share-user-id";
    public String SHARE_USER_NAME = "share-user-name";
    public String SHARE_AGENT_ID = "share-agent-id";
    public String SHARE_LAT_VALUE = "share-lat-value";
    public String SHARE_LONG_VALUE = "share-long-value";
    public String SHARE_CLEAN_CACHE_INTERVAL = "share-clean-cache-interval";
    public String SHARE_market_report = "share-market_report";
    public String SHARE_email_address = "share-login-email-address";
    public String SHARE_password = "share-login-password";
    public String SHARE_enable_remember = "share-enable_remember";
    public String SHARE_dont_show_again = "share-dont-show-again";

    public long MB = 1048576;
    public long IMAGE_CACHE_MAX_SIZE = 300 * MB;



    private LatLng mCurrentLocation;



    public String DEFAULT_TOKEN = "null";
    public String DEFAULT_USER_TYPE = "agent";


    public static String SHARE_PERMISSION_STORAGE_TAG = "share-permission-never-storage";
    public static String SHARE_PERMISSION_LOCATION_TAG = "share-permission-never-location";
    public static String SHARE_PERMISSION_RECORD_AUDIO_TAG = "share-permission-never-audio-record";
    public static String SHARE_PERMISSION_CAMERA_TAG = "share-permission-never-camera";



    public static Integer CLEAN_CACHE_INTERVAL_DAY = 1;  // clean cache interval time
    public static Integer CLEAN_CACHE_INTERVAL_MIL_SEC = CLEAN_CACHE_INTERVAL_DAY * 24 * 3600 * 1000;  // clean cache interval time


    private Context mContex;
    private AppDatabase mAppDatabase;

    SharedPreferences mSharedPref;

    DividerItemDecoration mItemDividerDecorator;

    private Boolean mIsPrimeUser = null;
    private Boolean mIsCreateLiveStreamUser = null;


    @Inject
    public AppUtil(Context mContex, AppDatabase database) {
        this.mContex = mContex;
        this.mAppDatabase = database;
        mSharedPref = mContex.getSharedPreferences(IGNYTE_PREFERENCES, Context.MODE_PRIVATE);
    }

    public Context getmContex() {
        return mContex;
    }

    public void logoutCleanUp()
    {
        // clean up sharePreference
        String userAddress = getLoginEmailAddress();
        String password = getLoginPassword();
        Boolean isRemember = getEnableRemember();
        //clear all preference, then recover email, password if remember is enabled.
        mSharedPref.edit().clear().commit();
        if (isRemember)
        {
            setLoginEmailAddress(userAddress);
            setLoginPassword(password);
        }
        setEnableRemember(isRemember);
        // clean up database
        mAppDatabase.userDao().deleteAll();
        mAppDatabase.paymentCardDao().deleteAll();
        mAppDatabase.orgDao().deleteAll();
        mIsPrimeUser = null;
        mIsCreateLiveStreamUser =null;

    }




    public void shutdownCleanUp()
    {
        // clean up
        Long lastCleanTime = lastCleanCacheTime();
        DateTime now = new DateTime();
        //updateLngLocation(null);
        //updateLatLocation(null);
        long size = 0;
        File[] files =  GlideApp.getPhotoCacheDir(mContex).listFiles();
        for (File f:files) {
            size = size+f.length();
        }
        if (size > IMAGE_CACHE_MAX_SIZE)
        {
            GlideApp.get(mContex).clearDiskCache();
        }

        mAppDatabase.clearDBWhenShutdown(getUserId());

        if (lastCleanTime != 0 && Math.abs(now.getMillis() - lastCleanTime) > CLEAN_CACHE_INTERVAL_MIL_SEC)
        {
            // clean up user table
            Integer userId = getUserId();
            mAppDatabase.userDao().deleteOthers(userId);
            markCleanCacheTime();
            clearCachedMarketReport();
            mAppDatabase.paymentCardDao().deleteAll();
        }
    }

    public LatLng getmCurrentLocation() {
        if (mCurrentLocation == null)
        {
            return getCurrentLocation();
        }
        else
        {
            return mCurrentLocation;
        }
    }

    public void setmCurrentLocation(LatLng mCurrentLocation) {
        updateCurrentLocation(mCurrentLocation);
        this.mCurrentLocation = mCurrentLocation;

    }


    public Long lastCleanCacheTime()
    {
        return mSharedPref.getLong(SHARE_CLEAN_CACHE_INTERVAL,0);
    }

    public void markCleanCacheTime()
    {
        DateTime dateTime = new DateTime();
        mSharedPref.edit().putLong (SHARE_CLEAN_CACHE_INTERVAL,dateTime.getMillis()).commit();
    }

    public void updateDontshowAgain(boolean b)
    {
        mSharedPref.edit().putBoolean(SHARE_dont_show_again,b).commit();
    }

    public boolean getShowDontshowAgainDialogFlag()
    {
        return mSharedPref.getBoolean(SHARE_dont_show_again,true);
    }


    public void showToast(String string)
    {
        Toast.makeText(mContex,string,Toast.LENGTH_LONG).show();
    }




    public Float getLngLocation()
    {
        return mSharedPref.getFloat(SHARE_LONG_VALUE,0);
    }

    public Float getLatLocation()
    {
        return mSharedPref.getFloat(SHARE_LAT_VALUE,0);
    }


    public void updateLngLocation(Float lng)
    {
        if (lng != null)
        {
            mSharedPref.edit().putFloat(SHARE_LONG_VALUE,lng).commit();
        }
    }

    public void updateLatLocation(Float lat)
    {
        if (lat != null) {
            mSharedPref.edit().putFloat(SHARE_LAT_VALUE, lat).commit();

        }}


    public void updateCurrentLocation(LatLng latLng)
    {
        if (latLng != null)
        {
            mSharedPref.edit().putFloat(SHARE_LAT_VALUE, (float) latLng.latitude)
                    .putFloat(SHARE_LONG_VALUE, (float) latLng.longitude)
                    .commit();
        }
        else
        {
            mSharedPref.edit().putFloat(SHARE_LAT_VALUE, 0.0f)
                    .putFloat(SHARE_LONG_VALUE, 0.0f)
                    .commit();
        }
    }

    public LatLng getCurrentLocation()
    {
        Float lat = mSharedPref.getFloat(SHARE_LAT_VALUE,0.0f);
        Float lng = mSharedPref.getFloat(SHARE_LONG_VALUE,0.0f);
        if (lat.equals(0.0f) || lng.equals(0.0f))
        {
            return null;
        }
        else
        {
            return  new LatLng(lat,lng);
        }
    }



    public DividerItemDecoration provideDividerItemDecoration()
    {
        if (mItemDividerDecorator == null)
        {
            mItemDividerDecorator = new DividerItemDecoration(mContex, DividerItemDecoration.VERTICAL);
            mItemDividerDecorator.setDrawable(ContextCompat.getDrawable(mContex, R.drawable.list_item_divider));
        }
        return mItemDividerDecorator;
    }


    public String getAccessToken()
    {
        String string = getShareValue(SHARE_TOKEN,DEFAULT_TOKEN);
        if (DEFAULT_TOKEN.equals(string)) {
            return null;
        }
        else
        {
            return string;
        }
    }

    public String getUserName()
    {
        String string = getShareValue(SHARE_USER_NAME,DEFAULT_TOKEN);
        if (DEFAULT_TOKEN.equals(string)) {
            return null;
        }
        else
        {
            return string;
        }
    }


    public Boolean isAgentorProducer(EntityLivestock livestock)
    {
        Integer userId = getUserId();
        Boolean isAgent = false;
        if (livestock.getAgentId() != null)
        {
            isAgent = livestock.getAgentId() == userId;
        }

        Boolean isProducer = false;
        if (livestock.getProducerId() != null)
        {
            isProducer = livestock.getProducerId() == userId;
        }
        return isAgent || isProducer;
    }


    public String getRefreshToken()
    {
        return getShareValue(SHARE_REFRESH_TOKEN,DEFAULT_TOKEN);
    }

    public boolean isLoginStatus()
    {
        return !DEFAULT_TOKEN.equals(getAccessToken());
    }


    // share value user type
    public UserType getUserType()
    {

        String strType = mSharedPref.getString(SHARE_USER_TYPE,DataConverter.USER_TYPE_USER);
        return DataConverter.Str2UserType(strType);
    }

    public void updateUserType(UserType userType)
    {
        String type = DataConverter.UserType2Str(userType);
        mSharedPref.edit().putString(SHARE_USER_TYPE,type).commit();
    }


    public Boolean isPrimeUser()
    {
        if (mIsPrimeUser == null)
        {
            mIsPrimeUser = mSharedPref.getBoolean(SHARE_PERMISSION_TYPE,false);
        }
        return mIsPrimeUser;
    }

    public Boolean isCreateLiveStream()
    {
        if (mIsCreateLiveStreamUser == null)
        {
            mIsCreateLiveStreamUser = mSharedPref.getBoolean(SHARE_PERMISSION_CREATELIVESTREAM_TYPE,false);
        }
        return mIsCreateLiveStreamUser;
    }

    public void updatePermissionType(List<Permission> permissionList)
    {
        Boolean isPrime = false;
        Boolean iscreatelivestream = false;
        for (Permission p:  permissionList)
        {
            if (AppConstants.TAG_prime_permission.equalsIgnoreCase(p.getName()))
            {
                isPrime = true;
               // break;
            }

            if (AppConstants.TAG_createLivestream_permission.equalsIgnoreCase(p.getName()))
            {
                iscreatelivestream = true;
                //break;
            }

        }

        mSharedPref.edit().putBoolean(SHARE_PERMISSION_TYPE,isPrime).commit();
        mSharedPref.edit().putBoolean(SHARE_PERMISSION_CREATELIVESTREAM_TYPE,iscreatelivestream).commit();

        boolean a = mSharedPref.getBoolean(SHARE_PERMISSION_CREATELIVESTREAM_TYPE,false);
    }

    // share value user id
    public Integer getUserId()
    {
        Integer userId =  mSharedPref.getInt(SHARE_USER_ID,0);
        if (userId == 0)
        {
            userId = null;
        }
        return userId;
    }
    public void updateUserId(Integer userId)
    {
        mSharedPref.edit().putInt(SHARE_USER_ID, userId).commit();
    }

    public Integer getAgentId()
    {
        Integer integer = mSharedPref.getInt(SHARE_AGENT_ID,0);
        if (integer == 0)
        {
            return  null;
        }
        return integer;
    }

    public void updateAgentId(Integer agentId)
    {
        mSharedPref.edit().putInt(SHARE_AGENT_ID, agentId == null ? 0 : agentId).commit();
    }


    public void setLoginEmailAddress(String emailAddress)
    {
        mSharedPref.edit().putString(SHARE_email_address,emailAddress).commit();
    }


    public String getLoginEmailAddress()
    {
        return mSharedPref.getString (SHARE_email_address,"");
    }


    public void setLoginPassword(String password)
    {
        mSharedPref.edit().putString(SHARE_password,password).commit();
    }

    public String getLoginPassword()
    {
        return mSharedPref.getString (SHARE_password,"");
    }


    public Boolean getEnableRemember()
    {
        return mSharedPref.getBoolean (SHARE_enable_remember,false);
    }

    public void setEnableRemember(Boolean enable)
    {
        mSharedPref.edit().putBoolean(SHARE_enable_remember,enable).commit();
    }




    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static boolean isVideoFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("video");
    }


    public void updateAccessToken(String accessToken)
    {
        setShareValue(SHARE_TOKEN,"Bearer " + accessToken);
    }

    public void updateUserName(String userName)
    {
        setShareValue(SHARE_USER_NAME,userName);
    }

    public void updateRefreshToken(String accessToken)
    {
        setShareValue(SHARE_REFRESH_TOKEN,accessToken);
    }

    public void clearCachedMarketReport()
    {
        updateCachedMarketReport(null);
    }

    public void updateCachedMarketReport(MarketReport marketReport)
    {
        String string;
        if (marketReport != null)
        {
            Gson gson = new Gson();
            string = gson.toJson(marketReport,MarketReport.class);
        }
        else
        {
            string = null;
        }
        mSharedPref.edit().putString(SHARE_market_report,string);
    }

    public MarketReport getCachedMarketReport()
    {
        String string = mSharedPref.getString(SHARE_market_report,null);
        if (string != null)
        {
            Gson gson = new Gson();
            MarketReport marketReport = gson.fromJson(string,MarketReport.class);
            return marketReport;
        }
        else
        {
            return  null;
        }
    }



    public String getShareValue(String key)
    {
        return mSharedPref.getString(key,"");
    }

    public String getShareValue(String key, String defaultValue)
    {
        return mSharedPref.getString(key,defaultValue);
    }

    public void setShareValue(String key, String value) {
        mSharedPref.edit().putString(key, value).commit();
    }

    public void setShareValue(String key, Boolean value) {
        mSharedPref.edit().putBoolean(key, value).commit();
    }

    public static void loadCircleImage(Context context, ImageView imageView, String url, Integer placeholerId)
    {
    //    GlideApp.with(context).load(url)
    //            .apply(new RequestOptions()
    //                    .placeholder(R.drawable.ic_avatar_rounded))
    //            .into(imageView);

        GlideApp.with(context).load(url)
                //.placeholder(placeholerId)
                .into(imageView);
    }





    public void  setStoragePermissionTag(PermissionStatus value)
    {
        mSharedPref.edit().putLong(SHARE_PERMISSION_STORAGE_TAG,value.getValue()).commit();
    }



    public void  setLocationPermissionTag(PermissionStatus value)
    {
        mSharedPref.edit().putLong(SHARE_PERMISSION_LOCATION_TAG,value.getValue()).commit();

    }

    public PermissionStatus  getLocationPermissionTag()
    {

        Long value =  getShareLongValue(SHARE_PERMISSION_LOCATION_TAG, PermissionStatus.GRANT.getValue());
        if (value == PermissionStatus.GRANT.getValue()) {
            return PermissionStatus.GRANT;
        }
        else if (value == PermissionStatus.DENIED.getValue())
        {
            return PermissionStatus.DENIED;
        }
        else
        {
            return PermissionStatus.DONOTASK;
        }

    }





    public PermissionStatus  getRecordAudioPermissionTag()
    {

        Long value =  getShareLongValue(SHARE_PERMISSION_RECORD_AUDIO_TAG, PermissionStatus.GRANT.getValue());
        if (value == PermissionStatus.GRANT.getValue()) {
            return PermissionStatus.GRANT;
        }
        else if (value == PermissionStatus.DENIED.getValue())
        {
            return PermissionStatus.DENIED;
        }
        else
        {
            return PermissionStatus.DONOTASK;
        }

    }

    public PermissionStatus  getCameraPermissionTag()
    {

        Long value =  getShareLongValue(SHARE_PERMISSION_CAMERA_TAG, PermissionStatus.GRANT.getValue());
        if (value == PermissionStatus.GRANT.getValue()) {
            return PermissionStatus.GRANT;
        }
        else if (value == PermissionStatus.DENIED.getValue())
        {
            return PermissionStatus.DENIED;
        }
        else
        {
            return PermissionStatus.DONOTASK;
        }

    }


    public static Matrix getRotationMatrix(String photoPath)
    {
        ExifInterface exif;
        Matrix matrix = new Matrix();
        try {
            exif = new ExifInterface(photoPath);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            if (rotation != 0f) {
                matrix.preRotate(rotationInDegrees);
            }
        }
        catch (IOException ex)
        {
        }
        return matrix;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
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
            Log.d(TAG, "deleteFile: " + file.getAbsolutePath());
            return true;
        }
        else
        {
            return false;
        }
    }


    public static String compressAvatarImage(String photoPath)
    {
        return compressAndResizeImage(photoPath, 500, 50);
    }

    public static String compressAndResizeImage(String photoPath, int longEdge, int compressRadio)
    {

        String newPhotoPath = photoPath + ".sml";
        Bitmap rawBitmap = BitmapFactory.decodeFile(photoPath);
        //String newFilePath = photoPath + ".compressed";
        int height = rawBitmap.getHeight();
        int width = rawBitmap.getWidth();
        int maxLength =  max(height,width);
        float radio = ((float) longEdge) / ((float) maxLength);
        int newWidth = (int)(radio * width);
        int newHeight = (int)(radio * height);
        Matrix matrix = getRotationMatrix(photoPath);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(rawBitmap, newWidth, newHeight,  true);
        Bitmap newBitmap = Bitmap.createBitmap(scaledBitmap,0,0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
        if  (newBitmap != null)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.JPEG, compressRadio, stream);
            byte[] data = stream.toByteArray();
            FileOutputStream output = null;
            File newBitmapFile = new File(newPhotoPath);
            try {
                output = new FileOutputStream(newBitmapFile);
                output.write(data);
            } catch (IOException e) {
                Log.e("Compressed Image", "Something wrong on compressing image", e);
                return null;
            } finally {
                if (null != output) {
                    try {
                        output.close();
                        //deleteFile(photoPath);
                        return newPhotoPath;
                    } catch (IOException ignored) {
                        Log.e("Compressed Image", "Something wrong on compressing image, ignored", ignored);
                        return null;
                    }
                }
            }
        }

        if (new File(newPhotoPath).isFile())
        {



            return newPhotoPath;
        }
        else
        {
            return null;
        }

    }

    public static void  compressImage(String photoPath)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if (bitmap != null)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] data = stream.toByteArray();

            FileOutputStream output = null;
            File newBitmap = new File(photoPath);
            if (newBitmap.isFile())
                newBitmap.delete();
            try {
                output = new FileOutputStream(newBitmap);
                output.write(data);
            } catch (IOException e) {
                Log.e("UI", "ImageSaver onCaptureCompleted", e);
            } finally {
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException ignored) {}
                }
            }
        }
    }

    public static String  compressImageToNewFile(String photoPath)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        if (bitmap != null)
        {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] data = stream.toByteArray();

            FileOutputStream output = null;
            File newBitmap = new File(photoPath);
            if (newBitmap.isFile())
                newBitmap.delete();
            try {
                output = new FileOutputStream(newBitmap);
                output.write(data);
            } catch (IOException e) {
                Log.e("UI", "ImageSaver onCaptureCompleted", e);
            } finally {
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException ignored) {}
                }
            }
        }
        return null;
    }


    public File getExternalFilesDir()
    {
        return mContex.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }

    public static String getCompressImageName(Context context, final String oldPhotoPath)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {

            Bitmap bitmap = BitmapFactory.decodeFile(oldPhotoPath);
            if (bitmap != null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] data = stream.toByteArray();
                FileOutputStream output = null;
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",   /* suffix */
                        storageDir      /* directory */
                );
                String newFilePath = image.getAbsolutePath();
                File newBitmap = new File(newFilePath);
                try {
                    output = new FileOutputStream(newBitmap);
                    output.write(data);
                    deleteFile(oldPhotoPath);
                    output.close();
                    return newFilePath;
                } catch (IOException e) {
                    Log.e("UI", "ImageSaver onCaptureCompleted", e);
                    deleteFile(newFilePath);
                    return oldPhotoPath;
                } /*finally {
                    if (null != output) {
                        try {
                            output.close();
                            deleteFile(newFilePath);
                            return oldPhotoPath;
                        } catch (IOException ignored) {
                            deleteFile(newFilePath);
                            return oldPhotoPath;
                        }
                    }
                }*/
            }
            else
            {
                return oldPhotoPath;
            }
        }
        catch (IOException e)
        {
            return oldPhotoPath;
        }
    }


    public static String getCompressImageName(final File cacheDir, final String oldPhotoPath)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        try {

            Bitmap bitmap = BitmapFactory.decodeFile(oldPhotoPath);
            if (bitmap != null)
            {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                byte[] data = stream.toByteArray();
                FileOutputStream output = null;
                File image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",   /* suffix */
                        cacheDir      /* directory */
                );
                String newFilePath = image.getAbsolutePath();
                File newBitmap = new File(newFilePath);
                try {
                    output = new FileOutputStream(newBitmap);
                    output.write(data);
                    deleteFile(oldPhotoPath);
                    output.close();
                    return newFilePath;
                } catch (IOException e) {
                    Log.e("UI", "ImageSaver onCaptureCompleted", e);
                    deleteFile(newFilePath);
                    return oldPhotoPath;
                } /*finally {
                    if (null != output) {
                        try {
                            output.close();
                            deleteFile(newFilePath);
                            return oldPhotoPath;
                        } catch (IOException ignored) {
                            deleteFile(newFilePath);
                            return oldPhotoPath;
                        }
                    }
                }*/
            }
            else
            {
                return oldPhotoPath;
            }
        }
        catch (IOException e)
        {
            return oldPhotoPath;
        }
    }






    public Long getShareLongValue(String key, Long value) {
        return mSharedPref.getLong(key,value);
    }

    public PermissionStatus getStoragePermissionTag()
    {

        Long value =  getShareLongValue(SHARE_PERMISSION_STORAGE_TAG, PermissionStatus.GRANT.getValue());
        if (value == PermissionStatus.GRANT.getValue()) {
            return PermissionStatus.GRANT;
        }
        else if (value == PermissionStatus.DENIED.getValue())
        {
            return PermissionStatus.DENIED;
        }
        else
        {
            return PermissionStatus.DONOTASK;
        }

    }

    public void  setLocationPermissionTag(Context context ,PermissionStatus value)
    {
        mSharedPref.edit().putLong(SHARE_PERMISSION_LOCATION_TAG, value.getValue()).commit();
    }

    public void  setAudioRecordPermissionTag(PermissionStatus value)
    {
        mSharedPref.edit().putLong(SHARE_PERMISSION_RECORD_AUDIO_TAG, value.getValue()).commit();
    }



    static public Boolean isVideo(Media media)
    {
        return AppConstants.TAG_videos.equals(media.getCollection_name());
    }



    static public Boolean isPdf(Media media)
    {

        return AppConstants.TAG_pdfs.equals(media.getCollection_name());
    }



    static public Boolean isImage(Media media)
    {
        return AppConstants.TAG_images.equals(media.getCollection_name());
    }

    public static void loadImage(Context context, ImageView imageView, String url, Integer placeholderId)
    {
        GlideApp.with(context).load(url)
                .placeholder(placeholderId)
                .into(imageView);
    }

    static public Boolean isFile(String path)
    {
        File file = new File(path);
        return  file.isFile() && file.exists();

    }















}
