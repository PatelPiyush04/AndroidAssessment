package com.theherdonline.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.theherdonline.R;
import com.theherdonline.api.CreateSaleyardNew;
import com.theherdonline.api.NetworkDBRemoteData;
import com.theherdonline.api.NetworkDbData;
import com.theherdonline.api.UpdateSaleyard;
import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.Agent;
import com.theherdonline.db.entity.AnimalCategory;
import com.theherdonline.db.entity.Bid;
import com.theherdonline.db.entity.BreedCategory;
import com.theherdonline.db.entity.GenderEntity;
import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.db.entity.JsonEntity;
import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.LivestockSubCategory;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.MyChat;
import com.theherdonline.db.entity.Organisation;
import com.theherdonline.db.entity.PaymentCard;
import com.theherdonline.db.entity.Post;
import com.theherdonline.db.entity.PregnancyEntity;
import com.theherdonline.db.entity.ResAdvertisement;
import com.theherdonline.db.entity.ResLogin;
import com.theherdonline.db.entity.ResUploadImage;
import com.theherdonline.db.entity.ResVersion;
import com.theherdonline.db.entity.SaleyardCategory;
import com.theherdonline.db.entity.User;
import com.theherdonline.db.entity.ResPagerSaleyard;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.api.CreateLivestockNew;
import com.theherdonline.api.CreateUser;
import com.theherdonline.api.NetworkBoundAppendData;
import com.theherdonline.api.NetworkBoundData;
import com.theherdonline.api.NetworkBoundRemoteData;
import com.theherdonline.api.NetworkUtil;
import com.theherdonline.api.Resource;
import com.theherdonline.api.RetrofitClient;
import com.theherdonline.api.UpdateLivestock;
import com.theherdonline.api.UpdateUserProfile;
import com.theherdonline.api.UploadImageList;
import com.theherdonline.ui.general.ADType;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.herdLive.ResAWSMedialiveChannel;
import com.theherdonline.ui.herdLive.ResHerdLive;
import com.theherdonline.ui.herdLive.ResSaleyardStreamDetails;
import com.theherdonline.util.AppUtil;

import org.apache.commons.collections4.ListUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class DataRepository {

    private AppExecutors mAppExecutors;
    private Context mContext;
    private AppDatabase mAppDatabase;
    private RetrofitClient mRetrofitClient;
    private AppUtil mAppUtil;


    @Inject
    public DataRepository(Context context, AppExecutors appExecutors, AppDatabase appDatabase, RetrofitClient retrofitClient, AppUtil appUtil

                          ) {
        mAppExecutors = appExecutors;
        mContext = context;
        mAppDatabase = appDatabase;
        mRetrofitClient = retrofitClient;
        mAppUtil = appUtil;

    }





    public LiveData<List<BreedCategory>> getLiveDataBreed() {
        return mAppDatabase.breedDao().loadLiveDataAll();
    }

    public LiveData<List<AnimalCategory>> getLiveDataAnimalCategory() {

        return mAppDatabase.animalDao().loadLiveDataAll();
    }


    public LiveData<Resource<MyChat>> raiseChat(final Integer livestockId) {
        return new NetworkBoundRemoteData<MyChat, MyChat>(mAppExecutors) {
            @Override
            protected MyChat convertResponse2Report(MyChat response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull MyChat item) {

            }

            @Override
            protected void processResponse(@NonNull MyChat item) {

            }

            @Override
            protected Call<MyChat> createRequest() {
                return mRetrofitClient.createChat(livestockId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> deleteCreditCard(final Integer userId, final Integer sourceId) {
        return new NetworkBoundRemoteData<Void, Void>(mAppExecutors) {
            @Override
            protected Void convertResponse2Report(Void response) {
                return null;
            }

            @Override
            protected void processReport(@NonNull Void item) {

            }

            @Override
            protected void processResponse(@NonNull Void item) {

            }

            @Override
            protected Call<Void> createRequest() {
                return mRetrofitClient.deleteCreditCart(userId, sourceId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Post>> createPost(final String txt, final String imageUrl) {
        return new NetworkBoundRemoteData<Post, Post>(mAppExecutors) {
            @Override
            protected Post convertResponse2Report(Post response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Post item) {

            }

            @Override
            protected void processResponse(@NonNull Post item) {

            }

            @Override
            protected Call<Post> createRequest() {
                return mRetrofitClient.createPost(txt, imageUrl);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> deletePost(final Integer id) {
        return new NetworkBoundRemoteData<Void, Void>(mAppExecutors) {
            @Override
            protected Void convertResponse2Report(Void response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Void item) {

            }

            @Override
            protected void processResponse(@NonNull Void item) {

            }

            @Override
            protected Call<Void> createRequest() {
                return mRetrofitClient.deletePost(id);
            }
        }.asLiveData();
    }


    public LiveData<Resource<ResVersion>> getVersion()
    {
        return new NetworkBoundRemoteData<ResVersion,ResVersion>(mAppExecutors)
        {
            @Override
            protected ResVersion convertResponse2Report(ResVersion response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull ResVersion item) {

            }

            @Override
            protected void processResponse(@NonNull ResVersion item) {

            }

            @Override
            protected Call<ResVersion> createRequest() {
                return mRetrofitClient.getVersion();
            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> initApplication() {
        final MediatorLiveData<Resource<User>> live = new MediatorLiveData<>();
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                try {

                    live.postValue(Resource.loading(null));

                    // load category

                    Call<List<LivestockCategory>> callLivestockCategory = mRetrofitClient.getLivestockCategory();
                    Response<List<LivestockCategory>> responseCategory = callLivestockCategory.execute();
                    if (responseCategory.isSuccessful()) {
                        LivestockCategory allCat = new LivestockCategory();
                        allCat.setId(0);
                        allCat.setName(mContext.getString(R.string.txt_all));
                        responseCategory.body().add(0, allCat);
                        mAppDatabase.livestockCategoryDao().insertAll(responseCategory.body());

                    } else {
                        String message = NetworkUtil.parseErrorResponse(responseCategory.errorBody());
                        live.postValue(Resource.error(responseCategory.code(), message, null));
                        return;
                    }
                    // load sub category
                    Call<List<LivestockSubCategory>> call = mRetrofitClient.getLivestockSubCategory();
                    Response<List<LivestockSubCategory>> response = call.execute();
                    if (response.isSuccessful()) {
                        LivestockSubCategory allCat = new LivestockSubCategory();
                        allCat.setId(0);
                        allCat.setName(mContext.getString(R.string.txt_all));
                        response.body().add(0, allCat);
                        mAppDatabase.livestockSubCategoryDao().insertAll(response.body());
                    } else {
                        String message = NetworkUtil.parseErrorResponse(response.errorBody());
                        live.postValue(Resource.error(response.code(), message, null));
                        return;
                    }
                    // load sub category
                    Call<List<SaleyardCategory>> callSaleyardCategory = mRetrofitClient.getSaleyardCategory();
                    Response<List<SaleyardCategory>> responseSaleyardCategory = callSaleyardCategory.execute();
                    if (responseSaleyardCategory.isSuccessful()) {
                        SaleyardCategory allCat = new SaleyardCategory();
                        allCat.setId(0);
                        allCat.setName(mContext.getString(R.string.txt_all));
                        responseSaleyardCategory.body().add(0, allCat);
                        mAppDatabase.saleyardCategoryDao().insertAll(responseSaleyardCategory.body());
                    } else {
                        String message = NetworkUtil.parseErrorResponse(responseSaleyardCategory.errorBody());
                        live.postValue(Resource.error(responseSaleyardCategory.code(), message, null));
                        return;
                    }

                    // load pregnancy status
                    Call<List<PregnancyEntity>> callPregnancy = mRetrofitClient.getPregnancyStatus();
                    Response<List<PregnancyEntity>> responsePregnancy = callPregnancy.execute();
                    if (responseSaleyardCategory.isSuccessful()) {
                        PregnancyEntity entity = new PregnancyEntity();
                        entity.setId(0);
                        entity.setName(mContext.getString(R.string.txt_all));
                        responsePregnancy.body().add(0, entity);
                        mAppDatabase.pregnancyEntityDao().insertAll(responsePregnancy.body());
                    } else {
                        String message = NetworkUtil.parseErrorResponse(responsePregnancy.errorBody());
                        live.postValue(Resource.error(responsePregnancy.code(), message, null));
                        return;
                    }

                    // load gender
                    Call<List<GenderEntity>> callGender = mRetrofitClient.getGenders();
                    Response<List<GenderEntity>> responseGender = callGender.execute();
                    if (responseGender.isSuccessful()) {
                        GenderEntity entity = new GenderEntity();
                        entity.setId(0);
                        entity.setName(mContext.getString(R.string.txt_all));
                        responseGender.body().add(0, entity);
                        mAppDatabase.genderEntityDao().insertAll(responseGender.body());
                    } else {
                        String message = NetworkUtil.parseErrorResponse(responseGender.errorBody());
                        live.postValue(Resource.error(responseGender.code(), message, null));
                        return;
                    }


                    // load user Infor
                    User currentUser = null;
                    if (mAppUtil.getAccessToken() != null) {
                        Call<User> callUser = mRetrofitClient.getUserById(null);
                        Response<User> responseUser = callUser.execute();
                        if (responseUser.isSuccessful()) {

                            currentUser = responseUser.body();
                            mAppDatabase.userDao().insertUser(currentUser);
                            if (responseUser.body().getProducer() != null) {
                                if (responseUser.body().getProducer().size() != 0) {
                                    mAppDatabase.userDao().insertAll(responseUser.body().getProducer());
                                }
                            }
                        }
                    }
                    live.postValue(Resource.success(currentUser));



                } catch (IOException e) {

                    String hostname = "";
                    try {
                        URL url = new URL(AppConstants.SERVER_URL_DEV);
                        hostname = url.getHost();
                    }
                    catch (MalformedURLException e1)
                    {
                        hostname = "";
                    }
                    if (e.getMessage().contains(hostname))
                    {
                        live.postValue(Resource.error(1001, AppConstants.ERROR_SERVER_CONNECTION + "\n" + e.getMessage(),null));
                    }
                    else
                    {
                        live.postValue(Resource.error(1001, e.getMessage(), null));
                    }

                }
            }
        });


        return live;
    }





    // Get user by id
    public LiveData<Resource<User>> getUserById(final Integer userId) {
        return new NetworkBoundData<User, User>(mAppExecutors) {
            @Override
            protected User convertResponse2Report(User response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull User item) {
                if (item.getProducer() != null) {
                    mAppDatabase.userDao().insertAll(item.getProducer());
                }
            }

            @Override
            protected void processResponse(@NonNull User item) {

            }

            @Override
            protected User loadFromDb() {
                return mAppDatabase.userDao().getUserById(userId);
            }

            @Override
            protected boolean isValidDbData(@NonNull User data) {
                return data != null && data.getId() == userId;
            }

            @Override
            protected void updateDb(User user) {
                mAppDatabase.userDao().insertUser(user);
            }

            @Override
            protected Call<User> createRequest() {
                return mRetrofitClient.getUserById(userId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> setFollowing(final Integer userId, Boolean bFollow) {
        return new NetworkBoundRemoteData<Void, Void>(mAppExecutors) {
            @Override
            protected Void convertResponse2Report(Void response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Void item) {

            }

            @Override
            protected void processResponse(@NonNull Void item) {

            }

            @Override
            protected Call<Void> createRequest() {
                return mRetrofitClient.postFollowUser(userId, bFollow);
            }
        }.asLiveData();
    }




    public LiveData<Resource<Bid>> makeBid(final Integer userId, final Integer livestockId, final Integer price) {
        return new NetworkBoundRemoteData<Bid, Bid>(mAppExecutors) {
            @Override
            protected Bid convertResponse2Report(Bid response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Bid item) {

            }

            @Override
            protected void processResponse(@NonNull Bid item) {

            }

            @Override
            protected Call<Bid> createRequest() {

                return mRetrofitClient.postMakeBid(userId, livestockId, price);
            }
        }.asLiveData();
    }

    public  LiveData<Resource<Void>> deleteLiveStock(Integer id)
    {
        return new NetworkBoundRemoteData<Void,Void>(mAppExecutors){
            @Override
            protected Void convertResponse2Report(Void response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Void item) {

            }

            @Override
            protected void processResponse(@NonNull Void item) {

            }

            @Override
            protected Call<Void> createRequest() {
                return mRetrofitClient.deleteLivestock(id);
            }
        }.asLiveData();
    }






    public LiveData<Resource<PaymentCard>> postAddPaymentCart(final PaymentCard paymentCard) {
        return new NetworkBoundData<PaymentCard, PaymentCard>(mAppExecutors) {
            @Override
            protected PaymentCard convertResponse2Report(PaymentCard response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull PaymentCard item) {

            }

            @Override
            protected void processResponse(@NonNull PaymentCard item) {

            }

            @Override
            protected PaymentCard loadFromDb() {
                return null;
            }

            @Override
            protected boolean isValidDbData(@NonNull PaymentCard data) {
                return false;
            }

            @Override
            protected void updateDb(PaymentCard paymentCard) {
                mAppDatabase.paymentCardDao().insert(paymentCard);
            }

            @Override
            protected Call<PaymentCard> createRequest() {
                return mRetrofitClient.postAddPaymentCard(paymentCard);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<PaymentCard>>> getPaymentCard() {
        return new NetworkBoundData<List<PaymentCard>, List<PaymentCard>>(mAppExecutors) {
            @Override
            protected List<PaymentCard> convertResponse2Report(List<PaymentCard> response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull List<PaymentCard> item) {

            }

            @Override
            protected void processResponse(@NonNull List<PaymentCard> item) {
            }

            @Override
            protected List<PaymentCard> loadFromDb() {
                return mAppDatabase.paymentCardDao().loadAll();
            }

            @Override
            protected boolean isValidDbData(@NonNull List<PaymentCard> data) {
                return true;
            }

            @Override
            protected void updateDb(List<PaymentCard> paymentCards) {
                mAppDatabase.paymentCardDao().deleteAll();  // delete all
                mAppDatabase.paymentCardDao().insertAll(paymentCards);
            }

            @Override
            protected Call<List<PaymentCard>> createRequest() {
                return mRetrofitClient.getPaymentCard();
            }
        }.asLiveData();
    }


    public LiveData<Resource<EntitySaleyard>> getSaleyardById(final Integer saleyard_id) {
        return new NetworkBoundRemoteData<EntitySaleyard, EntitySaleyard>(mAppExecutors) {
            @Override
            protected EntitySaleyard convertResponse2Report(EntitySaleyard response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull EntitySaleyard item) {

            }

            @Override
            protected void processResponse(@NonNull EntitySaleyard item) {

            }

            @Override
            protected Call<EntitySaleyard> createRequest() {
                return mRetrofitClient.getSaleyardById(saleyard_id);
            }
        }.asLiveData();
    }


    public LiveData<Resource<Void>> getLikeLivestockResponse(final Integer id, final Boolean like) {
        return new NetworkBoundRemoteData<Void, Void>(mAppExecutors) {
            @Override
            protected Void convertResponse2Report(Void response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Void item) {

            }

            @Override
            protected void processResponse(@NonNull Void item) {

            }

            @Override
            protected Call<Void> createRequest() {
                return mRetrofitClient.setLikeLivestock(id, like);
            }
        }.asLiveData();
    }


    public LiveData<Resource<Void>> getLikeSaleyardResponse(final Integer id, final Boolean like) {
        return new NetworkBoundRemoteData<Void, Void>(mAppExecutors) {
            @Override
            protected Void convertResponse2Report(Void response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Void item) {

            }

            @Override
            protected void processResponse(@NonNull Void item) {

            }

            @Override
            protected Call<Void> createRequest() {
                return mRetrofitClient.setLikeSaleyard(id, like);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<EntitySaleyard>>> getFirstPageSaleyard(SearchFilterPublicSaleyard filter) {
        return new NetworkBoundRemoteData<List<EntitySaleyard>, ResPagerSaleyard>(mAppExecutors) {
            @Override
            protected List<EntitySaleyard> convertResponse2Report(ResPagerSaleyard response) {
                return response.getData();
            }

            @Override
            protected void processReport(@NonNull List<EntitySaleyard> item) {

            }

            @Override
            protected void processResponse(@NonNull ResPagerSaleyard item) {

            }

            @Override
            protected Call<ResPagerSaleyard> createRequest() {
                return mRetrofitClient.getPublicSaleyard(filter, 1);
            }
        }.asLiveData();
    }


    public LiveData<Resource<ResUploadImage>> uploadImageForLivestock(final Integer id, final List<String> image) {
        return new UploadImageList(mAppExecutors, mRetrofitClient, id, image).asLiveData();
    }


    public LiveData<Resource<Media>> updateAccountAvatar(final Integer id, final String image) {
        return new NetworkBoundRemoteData<Media, Media>(mAppExecutors) {
            @Override
            protected Media convertResponse2Report(Media response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Media item) {

            }

            @Override
            protected void processResponse(@NonNull Media item) {

            }

            @Override
            protected Call<Media> createRequest() {
                return mRetrofitClient.postUserAvatar(id, image);
            }
        }.asLiveData();

    }


    public LiveData<Resource<User>> updateAccountProfile(final User user) {
        return new NetworkBoundData<User, User>(mAppExecutors) {
            @Override
            protected User convertResponse2Report(User response) {
                return user;
            }

            @Override
            protected void processReport(@NonNull User item) {

            }

            @Override
            protected void processResponse(@NonNull User item) {

            }

            @Override
            protected User loadFromDb() {
                return null;
            }

            @Override
            protected boolean isValidDbData(@NonNull User data) {
                return false;
            }

            @Override
            protected void updateDb(User user) {
                mAppDatabase.userDao().insertUser(user);
            }

            @Override
            protected Call<User> createRequest() {
                return mRetrofitClient.updateUser(user);
            }
        }.asLiveData();
    }


    public LiveData<Resource<EntityLivestock>> createLivestock(final EntityLivestock postAdData) {
        return new CreateLivestockNew(mAppExecutors, mRetrofitClient, postAdData, mAppUtil.getExternalFilesDir()).asLiveData();
    }


    public LiveData<Resource<EntitySaleyard>> createSaleyard(final EntitySaleyard postAdData) {
        return new CreateSaleyardNew(mAppExecutors, mRetrofitClient, postAdData).asLiveData();
    }


    public LiveData<Resource<EntityLivestock>> updateLivestock(Integer id, EntityLivestock livestock, List<Media> deleteList, Boolean bNeedUpdate) {
        return new UpdateLivestock(mAppExecutors, mRetrofitClient, id, livestock, deleteList, bNeedUpdate).asLiveData();
    }

    public LiveData<Resource<EntitySaleyard>> updateSaleyard(Integer id, EntitySaleyard livestock, List<Media> deleteList, List<Media> uploadList) {
        return new UpdateSaleyard(mAppExecutors, mRetrofitClient, id, livestock, deleteList, uploadList).asLiveData();
    }

    public LiveData<Resource<User>> createUser(final User user, String passwrod, String avatarPath) {
        return new CreateUser(mAppExecutors, mRetrofitClient, user, passwrod, avatarPath, mAppUtil).asLiveData();
    }


    public LiveData<Resource<User>> updateUser(final Integer userId, final User user, String avatarPath) {
        return new UpdateUserProfile(mAppExecutors, mRetrofitClient, userId, user, avatarPath).asLiveData();
    }


    public LiveData<Resource<EntityLivestock>> updateBidId(final Integer livestockId, final Integer bidId) {
        return new NetworkBoundRemoteData<EntityLivestock, EntityLivestock>(mAppExecutors) {
            @Override
            protected EntityLivestock convertResponse2Report(EntityLivestock response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull EntityLivestock item) {

            }

            @Override
            protected void processResponse(@NonNull EntityLivestock item) {

            }

            @Override
            protected Call<EntityLivestock> createRequest() {
                return mRetrofitClient.updateBidId(livestockId, bidId);
            }
        }.asLiveData();
    }


    public LiveData<Resource<EntityLivestock>> getPublicLivestock(final Integer livestockId) {
        return new NetworkBoundRemoteData<EntityLivestock, EntityLivestock>(mAppExecutors) {
            @Override
            protected EntityLivestock convertResponse2Report(EntityLivestock response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull EntityLivestock item) {

            }

            @Override
            protected void processResponse(@NonNull EntityLivestock item) {

            }

            @Override
            protected Call<EntityLivestock> createRequest() {
                return mRetrofitClient.getPublicLivestock(livestockId);
            }
        }.asLiveData();
    }


    /*public LiveData<Resource<ResRegister>> register(final RegisterInfo registerInfo) {
        return new NetworkBoundData<ResRegister, ResRegister>(mAppExecutors) {
            @Override
            protected ResRegister convertResponse2Report(ResRegister response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull ResRegister item) {
                mAppUtil.updateAccessToken(item.getApi_access_token());
            }

            @Override
            protected void processResponse(@NonNull ResRegister item) {

            }

            @Override
            protected ResRegister loadFromDb() {
                return null;
            }

            @Override
            protected boolean isValidDbData(@NonNull ResRegister data) {
                return false;
            }

            @Override
            protected void updateDb(ResRegister resRegister) {

            }

            @Override
            protected Call<ResRegister> createRequest() {
                return mRetrofitClient.postRegister(registerInfo);
            }
        }.asLiveData();
    }
*/

    public LiveData<Resource<EntityLivestock>> getLivestock(Integer livestockId)
    {
        return new NetworkBoundRemoteData<EntityLivestock, EntityLivestock>(mAppExecutors)
        {
            @Override
            protected EntityLivestock convertResponse2Report(EntityLivestock response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull EntityLivestock item) {

            }

            @Override
            protected void processResponse(@NonNull EntityLivestock item) {

            }

            @Override
            protected Call<EntityLivestock> createRequest() {
                return mRetrofitClient.getLivestockById(livestockId);
            }
        }.asLiveData();
    }

    public LiveData<Resource<ResLogin>> login(final String username, final String password) {
        return new NetworkBoundRemoteData<ResLogin, ResLogin>(mAppExecutors) {
            @Override
            protected ResLogin convertResponse2Report(ResLogin response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull ResLogin item) {
                mAppUtil.updateAccessToken(item.getAccess_token());
                mAppUtil.updateRefreshToken(item.getRefresh_token());
            }

            @Override
            protected void processResponse(@NonNull ResLogin item) {

            }

            @Override
            protected Call<ResLogin> createRequest() {
                if (username == null)
                {
                    return mRetrofitClient.postLoginWithFacebook(password);
                }
                else
                {
                    return mRetrofitClient.postLogin(username, password);
                }
            }
        }.asLiveData();
    }

    public LiveData<Resource<ResLogin>> dirtLoginRes() {
        final MutableLiveData<Resource<ResLogin>> data = new MutableLiveData<>();
        data.setValue(Resource.stale(null));
        return data;
    }


    // get Agent
    public LiveData<Resource<List<Agent>>> getAgent() {
        return new NetworkBoundData<List<Agent>, List<Agent>>(mAppExecutors) {
            @Override
            protected List<Agent> convertResponse2Report(List<Agent> response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull List<Agent> item) {

            }

            @Override
            protected void processResponse(@NonNull List<Agent> item) {

            }

            @Override
            protected List<Agent> loadFromDb() {
                return mAppDatabase.agentDao().loadAll();
            }

            @Override
            protected boolean isValidDbData(@NonNull List<Agent> data) {
                return data != null && data.size() != 0;
            }

            @Override
            protected void updateDb(List<Agent> agentList) {
                if (agentList != null && agentList.size() != 0) {
                    mAppDatabase.agentDao().insertAll(agentList);
                }
            }

            @Override
            protected Call<List<Agent>> createRequest() {
                return mRetrofitClient.getAgent();
            }
        }.asLiveData();
    }


    public LiveData<Resource<List<EntitySaleyard>>> getSaleyard(final Integer userId, final Integer statusId, final String type) {
        return new NetworkBoundRemoteData<List<EntitySaleyard>, List<EntitySaleyard>>(mAppExecutors) {
            @Override
            protected List<EntitySaleyard> convertResponse2Report(List<EntitySaleyard> response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull List<EntitySaleyard> item) {

            }

            @Override
            protected void processResponse(@NonNull List<EntitySaleyard> item) {

            }

            @Override
            protected Call<List<EntitySaleyard>> createRequest() {
                return mRetrofitClient.getSaleyards(userId, statusId, type);
            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> postFcmToken(Integer userId, String token)
    {
        return new NetworkBoundRemoteData<User,User>(mAppExecutors){
            @Override
            protected User convertResponse2Report(User response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull User item) {

            }

            @Override
            protected void processResponse(@NonNull User item) {

            }

            @Override
            protected Call<User> createRequest() {
                return mRetrofitClient.patchPmcToken(userId,token);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<HerdNotification>>> getNotificationV2()
    {
        Gson mGson = new Gson();
        return new NetworkDbData<List<HerdNotification>>(mAppExecutors,AppDatabase.JSON_NOTIFICATION){
            @Override
            protected Call<ResponseBody> createRequest() {
                return mRetrofitClient.getNotificationV2();
            }

            @Override
            protected String getDataFromDB(int id) {
                JsonEntity jsonEntity = mAppDatabase.jsonDao().load(id);
                return jsonEntity != null ? jsonEntity.toString() : "";
            }

            @Override
            protected void updateDb(Integer id, String jsonStr) {
                mAppDatabase.jsonDao().insertUser(new JsonEntity(id, jsonStr));
            }

            @Override
            protected String toJsonString(List<HerdNotification> item) {
                return  mGson.toJson(item);
            }

            @Override
            protected List<HerdNotification> toResult(String item) {
                try {
                    return Arrays.asList(mGson.fromJson(item, HerdNotification[].class));
                } catch (Exception e) {
                    return ListUtils.emptyIfNull(null);
                }
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<HerdNotification>>> getNotification()
    {
        return new NetworkDBRemoteData<List<HerdNotification>>(mAppExecutors)
        {
            @Override
            protected Call<List<HerdNotification>> createRequest() {
                return mRetrofitClient.getNotificationV1();
            }

            @Override
            protected List<HerdNotification> getDataFromDB() {
                return mAppDatabase.notificationDao().loadAll();
            }

            @Override
            protected void interDataToDB(@NonNull List<HerdNotification> item) {
                mAppDatabase.notificationDao().insertAll(item);
            }

            @Override
            protected Boolean dbDataValid(List<HerdNotification> items) {
                return items != null && (!items.isEmpty());
            }
        }.asLiveData();
    }

    public LiveData<Resource<Organisation>> getOrganisation(final Integer id)
    {
        return new NetworkDBRemoteData<Organisation>(mAppExecutors)
        {
            @Override
            protected Call<Organisation> createRequest() {
                return mRetrofitClient.getOrganisation(id);
            }

            @Override
            protected Organisation getDataFromDB() {
                return mAppDatabase.orgDao().getOrganisationById(id);
            }

            @Override
            protected void interDataToDB(@NonNull Organisation item) {
                mAppDatabase.orgDao().insertOrg(item);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Void>> postSaleyardRequest()
    {
        return new NetworkBoundRemoteData<Void,Void>(mAppExecutors)
        {
            @Override
            protected Void convertResponse2Report(Void response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Void item) {

            }

            @Override
            protected void processResponse(@NonNull Void item) {

            }

            @Override
            protected Call<Void> createRequest() {
                return mRetrofitClient.postSaleyardRequest();
            }
        }.asLiveData();
    }

    public LiveData<Resource<ResPagerSaleyard>> getSaleyardForReport() {
        return new NetworkBoundRemoteData<ResPagerSaleyard, ResPagerSaleyard>(mAppExecutors) {
            @Override
            protected ResPagerSaleyard convertResponse2Report(ResPagerSaleyard response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull ResPagerSaleyard item) {

            }

            @Override
            protected void processResponse(@NonNull ResPagerSaleyard item) {

            }

            @Override
            protected Call<ResPagerSaleyard> createRequest() {
               // return mRetrofitClient.getSaleyards(userId);
                return mRetrofitClient.getPublicSaleyardForSaleyardReport();
            }
        }.asLiveData();
    }

   /* public LiveData<Resource<ResBids>> getBidList(final Integer userId)
    {
        return new NetworkBoundRemoteData<ResBids, ResBids>(mAppExecutors){
            @Override
            protected ResBids convertResponse2Report(ResBids response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull ResBids item) {

            }

            @Override
            protected void processResponse(@NonNull ResBids item) {

            }

            @Override
            protected Call<ResBids> createRequest() {
                return mRetrofitClient.getBidList(userId);
            }
        }.asLiveData();
    }*/

    // Get My EntityLivestock
    public LiveData<Resource<ResAdvertisement>> getMyAdvertisement(final Integer pageNumber) {
        return new NetworkBoundRemoteData<ResAdvertisement, ResAdvertisement>(mAppExecutors) {
            @Override
            protected ResAdvertisement convertResponse2Report(ResAdvertisement response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull ResAdvertisement item) {
                for (EntityLivestock ad : item.getData()) {
                    ad.initMediaList();
                }

            }

            @Override
            protected void processResponse(@NonNull ResAdvertisement item) {

            }

            @Override
            protected Call<ResAdvertisement> createRequest() {
                return mRetrofitClient.getMyAdvertisement(pageNumber, ADType.SCHEDULED);
            }
        }.asLiveData();
    }

    // Create a new market report
    public LiveData<Resource<MarketReport>> postMarketReport(final MarketReport marketReport) {
        return new NetworkBoundRemoteData<MarketReport, MarketReport>(mAppExecutors) {
            @Override
            protected MarketReport convertResponse2Report(MarketReport response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull MarketReport item) {

            }

            @Override
            protected void processResponse(@NonNull MarketReport item) {

            }

            @Override
            protected Call<MarketReport> createRequest() {
                return mRetrofitClient.postMarketReport(marketReport);
            }
        }.asLiveData();
    }

    // Get My EntityLivestock Append model
    public LiveData<Resource<ResAdvertisement>> getMyAdvertisementAppend(final Integer pageNumber) {
        return new NetworkBoundAppendData<ResAdvertisement, ResAdvertisement>(mAppExecutors) {
            @Override
            protected ResAdvertisement convertResponse2Report(ResAdvertisement response) {
                return response;
            }

            @Override
            protected void updateDb(ResAdvertisement advertisements) {
                for (EntityLivestock ad : advertisements.getData()) {
                    ad.initMediaList();
                }
                mAppDatabase.advertisementDao().insertAll(advertisements.getData());
            }

            @Override
            protected Call<ResAdvertisement> createRequest() {
                return mRetrofitClient.getMyAdvertisement(pageNumber, ADType.SCHEDULED);
            }
        }.asLiveData();
    }

    // getLive Now

    public LiveData<Resource<List<ResHerdLive>>> getLiveNow(Integer isLive)
    {
        return  new NetworkBoundAppendData<List<ResHerdLive>, List<ResHerdLive>>(mAppExecutors){

            @Override
            protected List<ResHerdLive> convertResponse2Report(List<ResHerdLive> response) {
                return response;
            }

            @Override
            protected void updateDb(List<ResHerdLive> resHerdLives) {

            }

            @Override
            protected Call<List<ResHerdLive>> createRequest() {
                return mRetrofitClient.getLiveSaleyardStreams(isLive);
            }
        }.asLiveData();
    }

    public LiveData<Resource<List<ResHerdLive>>> getLiveNow1(Integer isLive)
    {
        return new NetworkDBRemoteData<List<ResHerdLive>>(mAppExecutors){

            @Override
            protected Call<List<ResHerdLive>> createRequest() {
                return mRetrofitClient.getLiveSaleyardStreams(isLive);
            }

            @Override
            protected List<ResHerdLive> getDataFromDB() {
                return null;
            }

            @Override
            protected void interDataToDB(@NonNull List<ResHerdLive> item) {

            }
        }.asLiveData();
    }

    public LiveData<Resource<List<ResHerdLive>>> getHerLiveUpcoming(String afterDate)
    {
        return new NetworkDBRemoteData<List<ResHerdLive>>(mAppExecutors){

            @Override
            protected Call<List<ResHerdLive>> createRequest() {
                return mRetrofitClient.getLiveSaleyardStreamsUpcoming(afterDate);
            }

            @Override
            protected List<ResHerdLive> getDataFromDB() {
                return null;
            }

            @Override
            protected void interDataToDB(@NonNull List<ResHerdLive> item) {

            }
        }.asLiveData();
    }
    public LiveData<Resource<List<ResHerdLive>>> getHerLivePast(String beforeDate)
    {
        return new NetworkDBRemoteData<List<ResHerdLive>>(mAppExecutors){

            @Override
            protected Call<List<ResHerdLive>> createRequest() {
                return mRetrofitClient.getLiveSaleyardStreamsPast(beforeDate);
            }

            @Override
            protected List<ResHerdLive> getDataFromDB() {
                return null;
            }

            @Override
            protected void interDataToDB(@NonNull List<ResHerdLive> item) {

            }
        }.asLiveData();
    }
    public LiveData<Resource<List<ResHerdLive>>> getHerdLiveMy(String userID)
    {
        return new NetworkDBRemoteData<List<ResHerdLive>>(mAppExecutors){

            @Override
            protected Call<List<ResHerdLive>> createRequest() {
                return mRetrofitClient.getLiveSaleyardStreamsMy(userID);
            }

            @Override
            protected List<ResHerdLive> getDataFromDB() {
                return null;
            }

            @Override
            protected void interDataToDB(@NonNull List<ResHerdLive> item) {

            }
        }.asLiveData();
    }

    public LiveData<Resource<ResHerdLive>> postSaleyardStream(final String name, final String streamDate) {
        return new NetworkBoundRemoteData<ResHerdLive, ResHerdLive>(mAppExecutors) {
            @Override
            protected ResHerdLive convertResponse2Report(ResHerdLive response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull ResHerdLive item) {

            }

            @Override
            protected void processResponse(@NonNull ResHerdLive item) {

            }

            @Override
            protected Call<ResHerdLive> createRequest() {
                return mRetrofitClient.createSaleyardStream(name, streamDate);
            }
        }.asLiveData();
    }

    public LiveData<Resource<ResSaleyardStreamDetails>> getSaleyardStream(String itemId)
    {
        return new NetworkDBRemoteData<ResSaleyardStreamDetails>(mAppExecutors){

            @Override
            protected Call<ResSaleyardStreamDetails> createRequest() {
                return mRetrofitClient.getSingleSaleyardStream(itemId);
            }

            @Override
            protected ResSaleyardStreamDetails getDataFromDB() {
                return null;
            }

            @Override
            protected void interDataToDB(@NonNull ResSaleyardStreamDetails item) {

            }
        }.asLiveData();
    }

    public LiveData<Resource<ResAWSMedialiveChannel>> getMedialiveChannel(String awsMediaId)
    {
        return new NetworkDBRemoteData<ResAWSMedialiveChannel>(mAppExecutors){

            @Override
            protected Call<ResAWSMedialiveChannel> createRequest() {
                return mRetrofitClient.getAwsMedialiveChannel(awsMediaId);
            }

            @Override
            protected ResAWSMedialiveChannel getDataFromDB() {
                return null;
            }

            @Override
            protected void interDataToDB(@NonNull ResAWSMedialiveChannel item) {

            }
        }.asLiveData();
    }

    public LiveData<Resource<ResAWSMedialiveChannel>> startSaleyardStream(String awsMediaId)
    {
        return new NetworkDBRemoteData<ResAWSMedialiveChannel>(mAppExecutors){

            @Override
            protected Call<ResAWSMedialiveChannel> createRequest() {
                return mRetrofitClient.startSaleyardStream(awsMediaId);
            }

            @Override
            protected ResAWSMedialiveChannel getDataFromDB() {
                return null;
            }

            @Override
            protected void interDataToDB(@NonNull ResAWSMedialiveChannel item) {

            }
        }.asLiveData();
    }

    public LiveData<Resource<ResAWSMedialiveChannel>> getMediaLiveInput(String mediaInputId)
    {
        return new NetworkDBRemoteData<ResAWSMedialiveChannel>(mAppExecutors){

            @Override
            protected Call<ResAWSMedialiveChannel> createRequest() {
                return mRetrofitClient.getMediaLiveInput(mediaInputId);
            }

            @Override
            protected ResAWSMedialiveChannel getDataFromDB() {
                return null;
            }

            @Override
            protected void interDataToDB(@NonNull ResAWSMedialiveChannel item) {

            }
        }.asLiveData();
    }
    public LiveData<Resource<ResAWSMedialiveChannel>> stopSaleyardStream(String mediaInputId)
    {
        return new NetworkDBRemoteData<ResAWSMedialiveChannel>(mAppExecutors){

            @Override
            protected Call<ResAWSMedialiveChannel> createRequest() {
                return mRetrofitClient.stopSaleyardStream(mediaInputId);
            }

            @Override
            protected ResAWSMedialiveChannel getDataFromDB() {
                return null;
            }

            @Override
            protected void interDataToDB(@NonNull ResAWSMedialiveChannel item) {

            }
        }.asLiveData();
    }
}
