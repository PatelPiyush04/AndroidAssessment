package com.theherdonline.ui.main;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.theherdonline.api.Resource;
import com.theherdonline.api.StaleData;
import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.entity.Bid;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.MyChat;
import com.theherdonline.db.entity.Organisation;
import com.theherdonline.db.entity.PaymentCard;
import com.theherdonline.db.entity.Post;
import com.theherdonline.db.entity.ResLogin;
import com.theherdonline.db.entity.User;
import com.theherdonline.repository.DataRepository;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.SearchFilterPublicLivestock;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.herdLive.ResAWSMedialiveChannel;
import com.theherdonline.ui.herdLive.ResHerdLive;
import com.theherdonline.ui.herdLive.ResMedialive_channel;
import com.theherdonline.ui.herdLive.ResSaleyardStreamDetails;
import com.theherdonline.util.AppUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;


public class MainActivityViewModel extends AndroidViewModel {

    final static private String mDirtyTag = "dirt";
    final static private String mTagId = "id";
    final static private String mTagSaleyard = "saleyard";

    private static final String TAG = MainActivityViewModel.class.getName();

    final private DataRepository mDataRepository;
    final private AppUtil mAppUtil;
    // Login
    final private LiveData<Resource<ResLogin>> mLiveDataLogin;
    final private MutableLiveData<String> mLiveDataLoginTrigger = new MutableLiveData<>();
    // My saleyard
    final private LiveData<Resource<List<EntitySaleyard>>> mLiveDataUserSaleyard;
    final private MutableLiveData<EntityLivestock> mLvMakeBidTrigger = new MutableLiveData<>();
    //EntitySaleyard
    final private LiveData<Resource<EntitySaleyard>> mLDResponseSpecificSaleyard;
    private final AppDatabase mAppDatabase;
    //This user's bid
    //  final private LiveData<Resource<ResBids>> mLDResponseBid;
    // Below for search feature
    private final MutableLiveData<SearchFilterPublicSaleyard> mLDSearchFilterSaleyard = new MutableLiveData<>();
    private final MutableLiveData<SearchFilterPublicLivestock> mLDSearchFilterLivestock = new MutableLiveData<>();
    private final MutableLiveData<Integer> mAppUserIdForSaleyard = new MutableLiveData<>();
    private final MutableLiveData<EntitySaleyard> mMarketReportSelectedSaleyard = new MutableLiveData<>();
    private final MutableLiveData<HashMap<String, Object>> mLdSaleyardRefreshTrigger = new MutableLiveData<>();
    private final MutableLiveData<Integer> mLDSaleyardId = new MutableLiveData<>();
    private final MutableLiveData<Integer> mLDAppUserId = new MutableLiveData<>();
    private final MutableLiveData<String> mAvatarPath = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mShowNavigationBar = new MutableLiveData<>();
    private final MutableLiveData<Place> mLDPostLivestockLocation = new MutableLiveData<>();
    private final MutableLiveData<CustomerToolbar> mToolbarMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Place> mFilterPlaceLivedata = new MutableLiveData<>();
    private final MutableLiveData<List<Media>> mLiveDataMediaList = new MutableLiveData<>();
    private final List<Media> mMediaListToDelete = new ArrayList<>();
    private final MutableLiveData<LatLng> mLiveDataCurrentLocation = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mLiveDataIsLocating = new MutableLiveData<>();
    // Post photo
    private final MutableLiveData<String> mLiveDataPostPhoto = new MutableLiveData<>();
    private final MutableLiveData<String> mLiveDataToken = new MutableLiveData<>();
    private final AppExecutors mAppExecutors;
    public MutableLiveData<EntityLivestock> mLiveDataPostLivestock = new MutableLiveData<>();
    public MutableLiveData<EntitySaleyard> mLiveDataPostSaleyard = new MutableLiveData<>();
    public MutableLiveData<Media> mLiveDataMarketReportVideo = new MutableLiveData<>();
    public MutableLiveData<Media> mLiveDataMarketReportAttachment = new MutableLiveData<>();
    public MutableLiveData<String> mLiveDataMarketReportAudio = new MutableLiveData<>();
    // Make a  bid
    private LiveData<Resource<Bid>> mLvResMakeBid;
    private String mUsername = null;
    private String mPassword = null;
    // Payment card
    private LiveData<Resource<List<PaymentCard>>> mLDPaymentCardList;
    // Add a Payment card
    private LiveData<Resource<PaymentCard>> mLDAddPaymentCard;
    private MutableLiveData<List<Media>> mLiveDataSaleyardMediaList = new MutableLiveData<>();
    private List<Media> mMediaListSaleyardToDelete = new ArrayList<>();
    // post saleyard pdf photo
    private MutableLiveData<String> mLiveDataPostSaleyardPhoto = new MutableLiveData<>();

    @Inject
    public MainActivityViewModel(Application application, DataRepository dataRepository,
                                 AppUtil appUtil,
                                 AppDatabase appDatabase,
                                 AppExecutors appExecutors
    ) {
        super(application);
        mDataRepository = dataRepository;
        mAppUtil = appUtil;
        mAppDatabase = appDatabase;
        mAppExecutors = appExecutors;









        /*mLDResponseSpecificSaleyard = Transformations.switchMap(mLDSaleyardId, (Integer saleyardId) -> {
            if (saleyardId == null) {
                MutableLiveData<Resource<EntitySaleyard>>  liveData = new MutableLiveData<>();
                liveData.setValue(Resource.success(mLiveDataPostSaleyard.getValue()));
                return liveData;
            } else {
                return mDataRepository.getSaleyardById(saleyardId);
            }
        });
*/

        mLDResponseSpecificSaleyard = Transformations.switchMap(mLdSaleyardRefreshTrigger, trigger -> {
            Integer id = (Integer) trigger.get(mTagId);
            if (id != null) {
                return mDataRepository.getSaleyardById(id);
            } else {
                MutableLiveData<Resource<EntitySaleyard>> liveData = new MutableLiveData<>();
                liveData.setValue(Resource.success((EntitySaleyard) trigger.get(mTagSaleyard)));
                return liveData;
            }
        });


        mLiveDataLogin = Transformations.switchMap(mLiveDataLoginTrigger, (String trigger) -> {
            if (mDirtyTag.equals(trigger)) {
                return new StaleData<ResLogin>();
            }
            return mDataRepository.login(mUsername, mPassword);
        });

        // For saleyard list
        mLiveDataUserSaleyard = Transformations.switchMap(mAppUserIdForSaleyard, (Integer userId) -> {
            if (userId != null) {
                return mDataRepository.getSaleyard(userId, null, null);
            } else {
                return new StaleData<List<EntitySaleyard>>();
            }
        });
    }

    public List<Media> getmMediaListSaleyardToDelete() {
        return mMediaListSaleyardToDelete;
    }

    public void setmMediaListSaleyardToDelete(List<Media> mMediaListSaleyardToDelete) {
        this.mMediaListSaleyardToDelete = mMediaListSaleyardToDelete;
    }

    public MutableLiveData<List<Media>> getmLiveDataSaleyardMediaList() {
        return mLiveDataSaleyardMediaList;
    }

    public void setmLiveDataSaleyardMediaList(MutableLiveData<List<Media>> mLiveDataSaleyardMediaList) {
        this.mLiveDataSaleyardMediaList = mLiveDataSaleyardMediaList;
    }

    public MutableLiveData<Place> getmFilterPlaceLivedata() {
        return mFilterPlaceLivedata;
    }

    public MutableLiveData<EntitySaleyard> getmLiveDataPostSaleyard() {
        return mLiveDataPostSaleyard;
    }

    public void setmLiveDataPostSaleyard(MutableLiveData<EntitySaleyard> mLiveDataPostSaleyard) {
        this.mLiveDataPostSaleyard = mLiveDataPostSaleyard;
    }

    public void updatePostSaleyard(EntitySaleyard entitySaleyard) {
        this.mLiveDataPostSaleyard.setValue(entitySaleyard);
    }


    //public insertRoom(Room room)

    public void setFilterPlace(Place place) {
        this.mFilterPlaceLivedata.setValue(place);
    }

    public void initChatkit(String token) {
        mLiveDataToken.setValue(token);
    }

    public MutableLiveData<String> getmLiveDataPostPhoto() {
        return mLiveDataPostPhoto;
    }

    public void upDatePostPhoto(String photoUrl) {
        this.mLiveDataPostPhoto.setValue(photoUrl);
    }


    public MutableLiveData<String> getmLiveDataPostSaleyardPhoto() {
        return mLiveDataPostSaleyardPhoto;
    }

    public void setmLiveDataPostSaleyardPhoto(MutableLiveData<String> mLiveDataPostSaleyardPhoto) {
        this.mLiveDataPostSaleyardPhoto = mLiveDataPostSaleyardPhoto;
    }

    public void upDatePostSaleyardPdfPhoto(String url) {
        this.mLiveDataPostSaleyardPhoto.setValue(url);
    }

    public LiveData<Resource<Organisation>> getOrganisation(final Integer id) {
        return mDataRepository.getOrganisation(id);
    }

    public MutableLiveData<EntityLivestock> getmLiveDataPostLivestock() {
        return mLiveDataPostLivestock;
    }

    public void updatePostLivestocks(EntityLivestock livestock) {
        mLiveDataPostLivestock.setValue(livestock);
    }

    public void addDeleteMediaList(Media media) {
        mMediaListToDelete.add(media);
    }

    public void clearDeleteMediaList() {
        mMediaListToDelete.clear();
        mMediaListSaleyardToDelete.clear();
    }

    public void clearDeleteMediaListSaleyard() {
        mMediaListSaleyardToDelete.clear();
    }

    public void updateLocatingStatus(Boolean isBusy) {
        mLiveDataIsLocating.setValue(isBusy);
    }

    public MutableLiveData<Boolean> getmLiveDataIsLocating() {
        return mLiveDataIsLocating;
    }

    public void updateCurrentLocation(LatLng latLng) {
        mLiveDataCurrentLocation.setValue(latLng);
    }

    public MutableLiveData<LatLng> getmLiveDataCurrentLocation() {
        return mLiveDataCurrentLocation;
    }

    public LiveData<Resource<Void>> postSaleyardRequest() {
        return mDataRepository.postSaleyardRequest();
    }

    public LiveData<Resource<User>> postFcmToken(Integer userId, String token) {
        return mDataRepository.postFcmToken(userId, token);
    }

    public LiveData<Resource<EntityLivestock>> getLivestock(Integer id) {
        return mDataRepository.getLivestock(id);
    }

    public MutableLiveData<Boolean> getmShowNavigationBar() {
        return mShowNavigationBar;
    }

    public MutableLiveData<CustomerToolbar> getmToolbarMutableLiveData() {
        return mToolbarMutableLiveData;
    }

    public void updateToolBar(CustomerToolbar toolbar) {
        this.mToolbarMutableLiveData.setValue(toolbar);
    }

    public void showNavigationBar(Boolean bShow) {
        this.mShowNavigationBar.setValue(bShow);
    }

    public void setMediaList(List<Media> list) {
        mLiveDataMediaList.setValue(list);
    }

    public MutableLiveData<List<Media>> getmLiveDataMediaList() {
        return mLiveDataMediaList;
    }

    public void setMediaListSaleyard(List<Media> list) {
        mLiveDataSaleyardMediaList.setValue(list);
    }


    public LiveData<Resource<Void>> deleteLivestock(Integer id) {
        return mDataRepository.deleteLiveStock(id);
    }


    public void addPhoto(String photo_url) {
        Media media = new Media();
        media.setUrl(photo_url);
        media.setName(AppConstants.TAG_images);
        media.setCollection_name(Media.CoolectionName.images.name());
        List<Media> list = mLiveDataMediaList.getValue();
        list.add(media);

        mLiveDataMediaList.setValue(list);
        if (list.get(0) != null) {
            list.get(0).setPrimary(true);
        }

    }


    public void addPostSaleyardPhoto(String photo_url) {
        Media media = new Media();
        media.setUrl(photo_url);
        media.setCollection_name(Media.CoolectionName.images.name());
        List<Media> list = mLiveDataSaleyardMediaList.getValue();
        list.add(media);
        mLiveDataSaleyardMediaList.setValue(list);
        if (list.get(0) != null) {
            list.get(0).setPrimary(true);
        }

    }

    public void addPhoto(List<String> photoList) {

        List<Media> list = mLiveDataMediaList.getValue();
        for (String photo_url : photoList) {
            Media media = new Media();
            media.setUrl(photo_url);
            media.setName(AppConstants.TAG_images);
            media.setCollection_name(Media.CoolectionName.images.name());
            if (list.size() == 0) {
                media.setPrimary(true);
            }
            list.add(media);
        }
        mLiveDataMediaList.setValue(list);
    }

    public void addMediaList(List<Media> medialist) {

        List<Media> list = mLiveDataMediaList.getValue();
        list.addAll(medialist);
        mLiveDataMediaList.setValue(list);
    }


    public void addVideo(String photo_url) {
        Media media = new Media();
        media.setUrl(photo_url);
        media.setName(AppConstants.TAG_videos);
        media.setCollection_name(Media.CoolectionName.videos.name());
        List<Media> list = mLiveDataMediaList.getValue();
        list.add(media);
        mLiveDataMediaList.setValue(list);
        if (list.get(0) != null) {
            list.get(0).setPrimary(true);
        }
    }


    public void addPostSaleyardVideo(String photo_video) {
        Media media = new Media();
        media.setUrl(photo_video);
        media.setCollection_name(Media.CoolectionName.videos.name());
        List<Media> list = mLiveDataSaleyardMediaList.getValue();
        list.add(media);
        mLiveDataSaleyardMediaList.setValue(list);
        if (list.get(0) != null) {
            list.get(0).setPrimary(true);
        }
    }


    public void addAudioToNewMarketReport(String audio_path) {
        mLiveDataMarketReportAudio.setValue(audio_path);
    }

    public MutableLiveData<String> getmLiveDataMarketReportAudio() {
        return mLiveDataMarketReportAudio;
    }

    public void addVideoToNewMarketReport(String photo_url) {
        if (photo_url == null) {
            mLiveDataMarketReportVideo.setValue(null);
        } else {
            Media media = new Media();
            media.setUrl(photo_url);
            media.setName(AppConstants.TAG_videos);
            media.setCollection_name(AppConstants.TAG_videos);
            mLiveDataMarketReportVideo.setValue(media);
        }
    }

    public void addAttachmentToNewMarketReport(String photo_url) {
        if (photo_url == null) {
            mLiveDataMarketReportAttachment.setValue(null);
        } else {
            Media media = new Media();
            media.setUrl_attchment(photo_url);
            media.setName(AppConstants.TAG_market_report_attachment);
            media.setCollection_name(AppConstants.TAG_market_report_attachment);
            mLiveDataMarketReportAttachment.setValue(media);
        }
    }

    public MutableLiveData<Media> getmLiveDataMarketReportVideo() {
        return mLiveDataMarketReportVideo;
    }

    public MutableLiveData<Media> getmLiveDataMarketReportAttachment() {
        return mLiveDataMarketReportAttachment;
    }

    public void deleteMarketReportAudio() {
        if (mLiveDataMarketReportAudio.getValue() != null) {
            String url = mLiveDataMarketReportAudio.getValue();
            if (url != null) {
                AppUtil.deleteFile(url);
            }
        }
        mLiveDataMarketReportAudio.setValue(null);

    }


    public void deleteMarketReportVideo() {
        if (mLiveDataMarketReportVideo.getValue() != null) {
            String url = mLiveDataMarketReportVideo.getValue().getUrl();
            if (url != null) {
                AppUtil.deleteFile(url);
            }
        }
        mLiveDataMarketReportVideo.setValue(null);
    }

    public void deleteMarketReportAttachment() {
        if (mLiveDataMarketReportAttachment.getValue() != null) {
            String url = mLiveDataMarketReportAttachment.getValue().getUrl_attchment();
            if (url != null) {
                AppUtil.deleteFile(url);
            }
        }
        mLiveDataMarketReportAttachment.setValue(null);
    }


    public void removePhoto(Media media) {
        mMediaListToDelete.add(media);
        List<Media> list = mLiveDataMediaList.getValue();
        list.remove(media);
        mLiveDataMediaList.setValue(list);
        // Delete photo if it is existed
        AppUtil.deleteFile(media.getUrl());
    }


    public void removeSaleyardListPhoto(Media media) {
        mMediaListSaleyardToDelete.add(media);
        List<Media> list = mLiveDataSaleyardMediaList.getValue();
        list.remove(media);
        mLiveDataSaleyardMediaList.setValue(list);
        // Delete photo if it is existed
        AppUtil.deleteFile(media.getUrl());
    }

    public List<Media> getmMediaListToDelete() {
        return mMediaListToDelete;
    }


    public void setPrimaryPhoto(MutableLiveData<List<Media>> mLiveDataMediaList, Media media) {
        List<Media> list = mLiveDataMediaList.getValue();
        Integer index = null;
        for (int i = 0; i < list.size(); i++) {
            if (media.getUrl().equals(list.get(i).getUrl())) {
                index = i;
                list.get(i).setPrimary(true);
            } else {
                list.get(i).setPrimary(false);
            }
        }
        if (index != null) {
            if (index != 0) {
                Media primary = list.get(index);
                list.remove(primary);
                list.add(0, primary);
            }
        }
        mLiveDataMediaList.setValue(list);
    }


    public void setPrimarySaleyardPhoto(Media media) {

        setPrimaryPhoto(mLiveDataSaleyardMediaList, media);


    }

    public void setPrimaryPhoto(Media media) {
        setPrimaryPhoto(mLiveDataMediaList, media);
    }


    public LiveData<Resource<Post>> createNewPost(final String txt) {
        return mDataRepository.createPost(txt, mLiveDataPostPhoto.getValue());
    }

    public LiveData<Resource<EntityLivestock>> postNewLivestock(EntityLivestock newLivestock) {
        return mDataRepository.createLivestock(newLivestock);
    }


    public LiveData<Resource<EntitySaleyard>> postCreateSaleyard(EntitySaleyard entitySaleyard) {
        return mDataRepository.createSaleyard(entitySaleyard);
    }

    public LiveData<Resource<EntitySaleyard>> updateSaleyard(Integer id, EntitySaleyard entitySaleyard, List<Media> deleteList, List<Media> uploadList) {
        return mDataRepository.updateSaleyard(id, entitySaleyard, deleteList, uploadList);
    }


    public MutableLiveData<Place> getmLDPostLivestockLocation() {
        return mLDPostLivestockLocation;
    }

    public void setPostCurrentLocation(Place postCurrentLocation) {
        mLDPostLivestockLocation.setValue(postCurrentLocation);
    }


    public LiveData<Resource<EntityLivestock>> updateLivestock(Integer id, EntityLivestock advertisement, List<Media> deleteList, Boolean bNeedUpdate) {
        return mDataRepository.updateLivestock(id, advertisement, deleteList, bNeedUpdate);
    }


    public MutableLiveData<String> getmAvatarPath() {
        return mAvatarPath;
    }


    public void updateAvatar(String avatar) {
        mAvatarPath.setValue(avatar);
    }


    public LiveData<Resource<Void>> setLivestockLike(Integer id, Boolean isLike) {
        return mDataRepository.getLikeLivestockResponse(id, isLike);
    }


    public LiveData<Resource<Void>> setSaleyardLike(Integer id, Boolean isLike) {
        return mDataRepository.getLikeSaleyardResponse(id, isLike);
    }

    public LiveData<Resource<Bid>> makeBid(Integer id, Integer price) {
        mLvResMakeBid = mDataRepository.makeBid(null, id, price);
        return mLvResMakeBid;
    }


    public LiveData<Resource<List<EntitySaleyard>>> getFirstPageSaleyard(SearchFilterPublicSaleyard filterPublicSaleyard) {
        return mDataRepository.getFirstPageSaleyard(filterPublicSaleyard);
    }


    public void setSaleyardSearchFilterNew(SearchFilterPublicSaleyard searchFilter) {
        mLDSearchFilterSaleyard.setValue(searchFilter);
    }

    public MutableLiveData<SearchFilterPublicSaleyard> getmLDSearchFilterSaleyard() {
        return mLDSearchFilterSaleyard;
    }

    public void setLivestockSearchFilterNew(SearchFilterPublicLivestock searchFilter) {
        mLDSearchFilterLivestock.setValue(searchFilter);
    }

    public MutableLiveData<SearchFilterPublicLivestock> getmLDSearchFilterLivestock() {
        return mLDSearchFilterLivestock;
    }


    // Add a payment card
    public LiveData<Resource<PaymentCard>> getmLDAddPaymentCard(PaymentCard paymentCard) {
        mLDAddPaymentCard = mDataRepository.postAddPaymentCart(paymentCard);
        return mLDAddPaymentCard;
    }

    // Get payment card List
    public LiveData<Resource<List<PaymentCard>>> getmLDPaymentCardList() {
        mLDPaymentCardList = mDataRepository.getPaymentCard();
        return mLDPaymentCardList;
    }


    public LiveData<Resource<MyChat>> createChat(Integer livestockId) {
        return mDataRepository.raiseChat(livestockId);
    }

    public LiveData<Resource<Bid>> getmLvResMakeBid() {
        return mLvResMakeBid;
    }

    public void clearData() {
        refreshSaleyard(null);
    }

    public void refreshSelectedSaleyardMarketReport(EntitySaleyard entitySaleyard) {
        mMarketReportSelectedSaleyard.setValue(entitySaleyard);
    }

    public LiveData<Resource<Void>> deletePaymentCreditCard(Integer userId, Integer sourceId) {
        return mDataRepository.deleteCreditCard(userId, sourceId);
    }

    public MutableLiveData<EntitySaleyard> getmMarketReportSelectedSaleyard() {
        return mMarketReportSelectedSaleyard;
    }

    public void refreshSaleyard(Integer userId) {
        mAppUserIdForSaleyard.setValue(userId);
    }

    public LiveData<Resource<List<EntitySaleyard>>> getmLiveDataUserSaleyard() {
        return mLiveDataUserSaleyard;
    }

    // Refresh bid list for app user.
    public void refreshBidList(Integer appUserId) {
        mLDAppUserId.setValue(appUserId);
    }

    //public LiveData<Resource<ResBids>> getmLDResponseBid() {
    //    return mLDResponseBid;
    // }

    // for saleyard page
    public void refreshSaleyardById(Integer saleyardId) {
        mLDSaleyardId.setValue(saleyardId);
    }


    public void refreshSaleyardDetail(Integer id, EntitySaleyard entitySaleyard) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        if (id != null) {
            map.put(mTagId, id);
        }
        if (entitySaleyard != null) {
            map.put(mTagSaleyard, entitySaleyard);
        }

        mLdSaleyardRefreshTrigger.setValue(map);
    }


    public void resetSpecificSaleyard() {
        refreshSaleyardById(null);
    }

    public LiveData<Resource<EntitySaleyard>> getmLDResponseSpecificSaleyard() {
        return mLDResponseSpecificSaleyard;
    }


    // Login in
    public void login(final String username, final String passwrod) {
        mUsername = username;
        mPassword = passwrod;
        mLiveDataLoginTrigger.setValue("login");
    }

    public void dirtLogin() {
        mLiveDataLoginTrigger.setValue(mDirtyTag);
    }

    public LiveData<Resource<ResLogin>> getmLiveDataLogin() {
        return mLiveDataLogin;
    }


    //HeardLive

    public LiveData<Resource<List<ResHerdLive>>> getHerdLiveNow1(Integer isLive) {
        return mDataRepository.getLiveNow1(isLive);
    }

    public LiveData<Resource<List<ResHerdLive>>> getHerdUpcoming(String afterDate) {
        return mDataRepository.getHerLiveUpcoming(afterDate);
    }
    public LiveData<Resource<List<ResHerdLive>>> getHerdPast(String beforeDate) {
        return mDataRepository.getHerLivePast(beforeDate);
    }
    public LiveData<Resource<List<ResHerdLive>>> getHerdMy(String userID) {
        return mDataRepository.getHerdLiveMy(userID);
    }

    public LiveData<Resource<ResHerdLive>> postSaleyardStream(String name, String streamDate) {
        return mDataRepository.postSaleyardStream(name, streamDate);
    }
    public LiveData<Resource<ResSaleyardStreamDetails>> getSaleyardStreamById(String itemId) {
        return mDataRepository.getSaleyardStream(itemId);
    }

    public LiveData<Resource<ResAWSMedialiveChannel>> getAwsMedialiveChannel(String awsMedialiveId) {
        return mDataRepository.getMedialiveChannel(awsMedialiveId);
    }

    public LiveData<Resource<ResAWSMedialiveChannel>> startSaleyardStream(String awsMedialiveId) {
        return mDataRepository.startSaleyardStream(awsMedialiveId);
    }
    public LiveData<Resource<ResAWSMedialiveChannel>> getMediaLiveInputId(String medialiveInputId) {
        return mDataRepository.getMediaLiveInput(medialiveInputId);
    }
    public LiveData<Resource<ResAWSMedialiveChannel>> stopSaleyardStream(String awsMedialiveId) {
        return mDataRepository.stopSaleyardStream(awsMedialiveId);
    }
}