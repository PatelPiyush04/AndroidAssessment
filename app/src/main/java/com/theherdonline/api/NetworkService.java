package com.theherdonline.api;

import com.theherdonline.app.AppConstants;
import com.theherdonline.db.entity.Agent;
import com.theherdonline.db.entity.Bid;
import com.theherdonline.db.entity.ChatkitToken;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.db.entity.GenderEntity;
import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.LivestockSubCategory;
import com.theherdonline.db.entity.MarketData;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.MyChat;
import com.theherdonline.db.entity.Organisation;
import com.theherdonline.db.entity.PaymentCard;
import com.theherdonline.db.entity.Post;
import com.theherdonline.db.entity.PregnancyEntity;
import com.theherdonline.db.entity.ResAdvertisement;
import com.theherdonline.db.entity.ResBids;
import com.theherdonline.db.entity.ResLivestock;
import com.theherdonline.db.entity.ResLogin;
import com.theherdonline.db.entity.ResMarketReport;
import com.theherdonline.db.entity.ResNotification;
import com.theherdonline.db.entity.ResPagerSaleyard;
import com.theherdonline.db.entity.ResPost;
import com.theherdonline.db.entity.ResStream;
import com.theherdonline.db.entity.ResVersion;
import com.theherdonline.db.entity.SaleyardCategory;
import com.theherdonline.db.entity.User;
import com.theherdonline.ui.herdLive.ResAWSMedialiveChannel;
import com.theherdonline.ui.herdLive.ResHerdLive;
import com.theherdonline.ui.herdLive.ResSaleyardStreamDetails;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NetworkService {

    @Multipart
    @POST("oauth/token/")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResLogin> postLogin(
            @Part("grant_type") RequestBody grantType,
            @Part("client_id") RequestBody clientId,
            @Part("client_secret") RequestBody clientSecret,
            @Part("username") RequestBody name,
            @Part("password") RequestBody password
    );

    @Multipart
    @POST("oauth/token/")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResLogin> postLoginWithFacebook(
            @Part("grant_type") RequestBody grantType,
            @Part("client_id") RequestBody clientId,
            @Part("client_secret") RequestBody clientSecret,
            @Part("provider") RequestBody provider,
            @Part("access_token") RequestBody access_token
    );

    // checkout user by token
    @GET("api/auth")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<User> getUserByToken(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                              @Query(AppConstants.TAG_include) String include,
                              @Query(AppConstants.TAG_append) String append);

    // checkout user by Id
    @GET("api/public/user/{id}")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<User> getUserById(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                           @Path("id") Integer userId,
                           @Query(AppConstants.TAG_include) String include,
                           @Query(AppConstants.TAG_append) String append);

    // checkout user by Id
    @GET("api/public/organisations/{id}")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Organisation> getOrganisationById(
            @Path("id") Integer orgId,
            @Query(AppConstants.TAG_include) String include
    );


    @POST("api/livestocks")
    @Multipart
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<EntityLivestock> postStoreLiveStock(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                             @PartMap Map<String, RequestBody> params);


    @POST("api/saleyards")
    @Multipart
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<EntitySaleyard> postStoreSaleyard(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                           @PartMap Map<String, RequestBody> params);

    @PUT("api/saleyards/{id}")
    @FormUrlEncoded
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<EntitySaleyard> postUpdateSaleyard(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization, @Path("id") Integer livestockId,
                                            @FieldMap Map<String, String> params);


    @POST("api/chat/authenticate")
    @Multipart
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ChatkitToken> postChatkitToken(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization
    );

    @POST("api/chat/livestock/{id}")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<MyChat> postCreateChat(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                @Path("id") Integer id
    );


    @PUT("api/livestocks/{id}")
    @FormUrlEncoded
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<EntityLivestock> postUpdateLiveStock(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization, @Path("id") Integer livestockId,
                                              @FieldMap Map<String, String> params);

    @GET("api/livestocks/{id}")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<EntityLivestock> getLivestockById(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization, @Path("id") Integer livestockId,
                                           @Query(AppConstants.TAG_include) String include,
                                           @Query(AppConstants.TAG_append) String append);

    @POST("api/register/")
    @Multipart
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<User> postRegisterUser(
            @PartMap Map<String, RequestBody> params);


    @PUT("api/user/{userID}")
    @Multipart
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<User> putUpdateUser(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization, @Path("userID") Integer livestockId,
                             @PartMap Map<String, RequestBody> params);


    @PUT("api/user/{userID}")
    //@Multipart
    @FormUrlEncoded
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<User> putUpdateUserNew(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization, @Path("userID") Integer livestockId,
                                @Field("first_name") String first_name,
                                @Field("last_name") String last_name,
                                @Field("phone") String phone);


    // @FormUrlEncoded


    @POST("api/public/market-reports")
    @Multipart
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<MarketReport> postMarketReport(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                        @PartMap Map<String, RequestBody> params,
                                        @Part MultipartBody.Part audio,
                                        @Part MultipartBody.Part video,
                                        @Part MultipartBody.Part attachment);


    @POST("api/user/{userId}/follow")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Void> postFollow(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization, @Path("userId") Integer userId);

    @POST("api/user/{userId}/unfollow")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Void> postUnFollow(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization, @Path("userId") Integer userId);

    @GET("api/livestocks")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResAdvertisement> getMyAdvertismentAgent(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                  @Query(AppConstants.TAG_filter_agent_id) Integer id,
            /*@Query(AppConstants.TAG_filter_media_collection_name) String media,*/
                                                  @Query(AppConstants.TAG_filter_ad_status_id) Integer ad_id,
                                                  @Query(AppConstants.TAG_per_page) Integer pageNum,
                                                  @Query(AppConstants.TAG_page) Integer pageIndex,
                                                  @Query(AppConstants.TAG_include) String type);


    @GET("api/livestocks")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResLivestock> getLivestock(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                    @Query(AppConstants.TAG_per_page) Integer pageNum,
                                    @Query(AppConstants.TAG_page) Integer pageIndex,
                                    @Query(AppConstants.TAG_filter_agent_id) Integer agent_id,
                                    @Query(AppConstants.TAG_filter_producer_id) Integer producer_id,
                                    @Query(AppConstants.TAG_filter_ad_status_id) String adType,
                                    @Query(AppConstants.TAG_include) String include,
                                    @Query(AppConstants.TAG_append) String append,
                                    @Query(AppConstants.TAG_sort) String sort
    );


    @GET("api/livestocks")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResAdvertisement> getMyAdvertismentProducer(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                     @Query(AppConstants.TAG_filter_agent_id) Integer id,
            /* @Query(AppConstants.TAG_filter_media_collection_name) String media,*/
                                                     @Query(AppConstants.TAG_filter_ad_status_id) Integer ad_id,
                                                     @Query(AppConstants.TAG_per_page) Integer pageNum,
                                                     @Query(AppConstants.TAG_page) Integer pageIndex,
                                                     @Query(AppConstants.TAG_include) String type);

    @GET("api/saleyards")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<EntitySaleyard>> getSaleyards(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                            @Query(AppConstants.TAG_include) String include,
                                            @Query(AppConstants.TAG_saleyard_filter_user_id) Integer user_id,
                                            @Query(AppConstants.TAG_filter_saleyard_status_id) Integer status_id,
                                            @Query(AppConstants.TAG_filter_type) String type);


    @GET("api/saleyards")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResPagerSaleyard> getPagerSaleyards(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                             @Query(AppConstants.TAG_include) String include,
                                             @Query(AppConstants.TAG_saleyard_filter_user_id) Integer user_id,
                                             @Query(AppConstants.TAG_filter_saleyard_status_id) Integer status_id,
                                             @Query(AppConstants.TAG_filter_type) String type,
                                             @Query(AppConstants.TAG_sort) String sort);


    @GET("api/public/saleyard/{saleyard_id}")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<EntitySaleyard> getSaleyardById(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                         @Path("saleyard_id") Integer saleyard_id,
                                         @Query(AppConstants.TAG_include) String include,
                                         @Query(AppConstants.TAG_append) String append);

    @GET("api/public/stream")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResStream> getStream(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            // @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageIndex,
            @Query(AppConstants.TAG_filter_user_id) Integer userId,
            @Query(AppConstants.TAG_filter_of_user) Integer OfUserId,
            @Query(AppConstants.TAG_filter_type) String type,
            @Query(AppConstants.TAG_filter_of_organisation) Integer of_organisation,
            @Query(AppConstants.TAG_include) String include,
            @Query(AppConstants.TAG_append) String append,
            @Query(AppConstants.TAG_sort) String sort);


    @GET("api/posts")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResPost> getPosts(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageIndex,
            @Query(AppConstants.TAG_include) String include,
            @Query(AppConstants.TAG_sort) String sortType,
            @Query(AppConstants.TAG_filter_user_id) Integer userId);


    @GET("api/livestock/likes")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResLivestock> getLikedLivestock(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageInde,
            @Query(AppConstants.TAG_include) String include
    );

    @GET("api/user/push-notifications")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResNotification> getNotifications(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageInde
    );


    @GET("api/user/push-notifications")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<HerdNotification>> getNotificationsV1(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization
    );


    @GET("api/user/push-notifications")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResponseBody> getNotificationsV2(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization
    );


    @GET("api/public/livestock/{livestockId}")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<EntityLivestock> getPublicLivestock(@Path("livestockId") Integer livestockId);


    @GET("api/saleyard/likes")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResPagerSaleyard> getLikedSaleyard(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Query(AppConstants.TAG_filter_type) String type,
            @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageIndex
    );


    @GET("api/chat")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<MyChat>> getMyChat(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization
    );


    @POST("api/livestock/{id}/like")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Void> setLikeLivestock(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Path("id") Integer id
    );

    @POST("api/livestock/{id}/unlike")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Void> setUnLikeLivestock(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Path("id") Integer id
    );


    @POST("api/saleyard/{id}/like")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Void> setLikeSaleyard(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Path("id") Integer id
    );

    @POST("api/saleyard/{id}/unlike")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Void> setUnLikeSaleyard(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Path("id") Integer id
    );


    @GET("api/public/market-reports")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResMarketReport> getMarketReport(
            @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageIndex,
            @Query(AppConstants.TAG_filter_user_id) Integer userId,
            @Query(AppConstants.TAG_include) String include,
            @Query(AppConstants.TAG_sort) String sort);


    @GET("api/bid")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResBids> getBidList(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                             @Query(AppConstants.TAG_per_page) Integer numPrePage,
                             @Query(AppConstants.TAG_page) Integer pageIndex,
                             @Query(AppConstants.TAG_filter_user_id) Integer userId,
                             @Query(AppConstants.TAG_include) String include);

    @GET("api/public/auctionsplus")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResLivestock> getPublicActionPlus(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageIndex,
            @Query(AppConstants.TAG_include) String type,
            @Query(AppConstants.TAG_append) String append,
            @Query(AppConstants.TAG_sort) String sort);

    @GET("api/public/livestock")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResLivestock> getPublicLivestock(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageIndex,
            @Query(AppConstants.TAG_filter_where_like) String where_like,
            @Query(AppConstants.TAG_filter_filter_around) String around,
            @Query(AppConstants.TAG_filter_between_price) String price,
            @Query(AppConstants.TAG_filter_between_age) String between_age,
            @Query(AppConstants.TAG_filter_between_wight) String between_wight,
            @Query(AppConstants.TAG_filter_age) Integer age,
            @Query(AppConstants.TAG_filter_gender_id) Integer gender_id,
            @Query(AppConstants.TAG_filter_quantity) Integer quantity,
            @Query(AppConstants.TAG_filter_weighted) Integer weighted,
            @Query(AppConstants.TAG_filter_pregnancy_status_id) Integer pregnancy_status_id,
            @Query(AppConstants.TAG_filter_dentition) Integer dentition,
            @Query(AppConstants.TAG_filter_mounted) Integer mounted,
            @Query(AppConstants.TAG_filter_eu) Integer eu,
            @Query(AppConstants.TAG_filter_msa) Integer msa,
            @Query(AppConstants.TAG_filter_lpa) Integer lpa,
            @Query(AppConstants.TAG_filter_orgnic) Integer orgnic,
            @Query(AppConstants.TAG_filter_pcas) Integer pcas,
            @Query(AppConstants.TAG_filter_ad_status_id) String ad_status_id,
            @Query(AppConstants.TAG_filter_ls_cat_id) Integer ls_cat_id,
            @Query(AppConstants.TAG_filter_ls_sub_cat_id) Integer ls_sub_cat_id,
            @Query(AppConstants.TAG_filter_ls_classification_id) Integer ls_classification_id,
            @Query(AppConstants.TAG_filter_producer_id) Integer producer_id,
            @Query(AppConstants.TAG_filter_agent_id) Integer agent_id,
            @Query(AppConstants.TAG_filter_orgisation_id) Integer orgisation_id,
            @Query(AppConstants.TAG_filter_auctions_plus) Boolean auctions_plus,
            @Query(AppConstants.TAG_filter_age_type) String age_type,

            @Query(AppConstants.TAG_include) String type,
            @Query(AppConstants.TAG_append) String append,
            @Query(AppConstants.TAG_sort) String sort

    );


    @GET("api/public/saleyard")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResPagerSaleyard> getPublicSaleyard(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageIndex,
            @Query(AppConstants.TAG_filter_where_like) String where_like,
            @Query(AppConstants.TAG_filter_where_between) String where_between,
            @Query(AppConstants.TAG_filter_around_lat_lng) String around,
            @Query(AppConstants.TAG_filter_name) String name,
            @Query(AppConstants.TAG_filter_type) String type,
            @Query(AppConstants.TAG_filter_description) String description,
            @Query(AppConstants.TAG_filter_starts_at) String starts_at,
            @Query(AppConstants.TAG_filter_saleyard_status_id) Integer status_id,
            @Query(AppConstants.TAG_filter_saleyard_category_id) Integer category_id,
            @Query(AppConstants.TAG_filter_user_id) Integer weighted,
            @Query(AppConstants.TAG_append) String append,
            @Query(AppConstants.TAG_sort) String sort
    );


    @GET("api/public/livestock-category")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<LivestockCategory>> getLivestockCategory(
    );


    @GET("api/public/livestock-sub-category")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<LivestockSubCategory>> getLivestockSubCategory(
    );


    @GET("api/public/saleyard-category")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<SaleyardCategory>> getSaleyardCategory(
    );


    @GET("api/public/livestock")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResAdvertisement> searchLivestock(
            @Query(AppConstants.TAG_per_page) Integer numPrePage,
            @Query(AppConstants.TAG_page) Integer pageIndex,
            @Query(AppConstants.TAG_filter_where_like) String where_like,
            @Query(AppConstants.TAG_filter_filter_around) String around,
            @Query(AppConstants.TAG_filter_between_price) String price,
            @Query(AppConstants.TAG_filter_between_wight) String between_wight,
            @Query(AppConstants.TAG_filter_sex) String sex,
            @Query(AppConstants.TAG_filter_quantity) Integer quantity,
            @Query(AppConstants.TAG_filter_age) Integer age,
            @Query(AppConstants.TAG_filter_weighted) Integer weighted,
            @Query(AppConstants.TAG_filter_pregnancy_status) String pregnancy_status,
            @Query(AppConstants.TAG_filter_dentition) Integer dentition,
            @Query(AppConstants.TAG_filter_mounted) Integer mounted,
            @Query(AppConstants.TAG_filter_eu) Integer eu,
            @Query(AppConstants.TAG_filter_msa) Integer msa,
            @Query(AppConstants.TAG_filter_lpa) Integer lpa,
            @Query(AppConstants.TAG_filter_orgnic) Integer orgnic,
            @Query(AppConstants.TAG_filter_pcas) Integer pcas,
            @Query(AppConstants.TAG_filter_ad_status_id) Integer ad_status_id,
            @Query(AppConstants.TAG_filter_ls_cat_id) Integer ls_cat_id,
            @Query(AppConstants.TAG_filter_ls_sub_cat_id) Integer ls_sub_cat_id,
            @Query(AppConstants.TAG_filter_ls_classification_id) Integer ls_classification_id,
            @Query(AppConstants.TAG_filter_producer_id) Integer producer_id,
            @Query(AppConstants.TAG_filter_agent_id) Integer agent_id,
            @Query(AppConstants.TAG_filter_orgisation_id) Integer orgisation_id,
            @Query(AppConstants.TAG_include) String type
    );


    @Multipart
    @POST("api/livestocks/{livestockId}/bid")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Bid> postMakeBid(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Path("livestockId") Integer livestockId,
            @Part("user_id") RequestBody clientId,
            @Part("amount") RequestBody grantType

    );

    @PATCH("api/user/{userId}/fcm-token")
    @Headers(AppConstants.HEADER_ACCEPT)
    @FormUrlEncoded
    Call<User> postPMCToken(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Path("userId") Integer userId,
            @Field("fcm_token") String fcmToken

    );


    @Multipart
    @POST("api/livestocks/{id}/media")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<Media>> postUploadImage(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                      @Path("id") Integer id,
                                      @Part("type") RequestBody type,
                                      @Part List<MultipartBody.Part> files
    );

    @Multipart
    @POST("api/saleyards/{id}/media")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<Media>> postSaleyardMedias(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                         @Path("id") Integer id,
                                         @Part("type") RequestBody type,
                                         @Part List<MultipartBody.Part> files
    );


    @POST("api/saleyard/request")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Void> postSaleyardRequest(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization);


    @Multipart
    @POST("api/posts")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Post> postNewPost(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                           @Part("text") RequestBody text,
                           @Part List<MultipartBody.Part> files
    );


    @Multipart
    @POST("api/user/{id}/avatar")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<Media> postUploadAvatar(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                 @Path("id") Integer id,
                                 @Part MultipartBody.Part avatar
    );

    @Multipart
    @POST("api/user/{userId}/sources/")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<PaymentCard> postAddPaymentCard(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Path("userId") Integer userId,
            @Part(AppConstants.TAG_source_id) RequestBody source_id,
            @Part(AppConstants.TAG_exp_month) RequestBody exp_month,
            @Part(AppConstants.TAG_exp_year) RequestBody exp_year,
            @Part(AppConstants.TAG_client_secret) RequestBody client_secret,
            @Part(AppConstants.TAG_status) RequestBody status,
            @Part(AppConstants.TAG_brand) RequestBody brand,
            @Part(AppConstants.TAG_last_four) RequestBody last_four,
            @Part(AppConstants.TAG_three_d_secure) RequestBody three_d_secure
    );


    @GET("/mockapi/api/agent")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<Agent>> getAgent();


    @GET("/api/tvs/get-marketings/{gymId}")
    Call<MarketData> getMarketingData(@Path("gymId") Integer nGymId);


    @GET("api/user/{userId}/sources")
    Call<List<PaymentCard>> getPaymentCard(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization, @Path("userId") Integer userId);

    @DELETE("api/user/{userId}/sources/{sourceId}")
    Call<Void> deletePaymentCard(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                 @Path("userId") Integer userId,
                                 @Path("sourceId") Integer sourceId);


    @DELETE("api/public/market-reports/{reportId}")
    Call<Void> deleteMarketReport(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                  @Path("reportId") Integer reportId);


    @DELETE("api/saleyards/{saleyard}")
    Call<Void> deleteSaleyard(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                              @Path("saleyard") Integer userId);


    @DELETE("api/livestocks/{id}")
    Call<Void> deleteLivestock(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                               @Path("id") Integer id);


    @DELETE("api/posts/{postId}")
    Call<Void> deletePost(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                          @Path("postId") Integer userId);

    @DELETE("api/livestocks/{livestockId}/media/{mediaId}")
    Call<Void> deleteLiveStockMedia(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                    @Path("livestockId") Integer livestockId, @Path("mediaId") Integer mediaId);


    @DELETE("api/saleyards/{saleyardId}/media/{mediaId}")
    Call<Void> deleteSaleyardMedia(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                   @Path("saleyardId") Integer livestockId, @Path("mediaId") Integer mediaId);


    @GET("api/public/pregnancy-statuses")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<PregnancyEntity>> getPregnancyStatus();

    @GET("api/public/genders")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<GenderEntity>> getGender();


    @GET("api/public/mobile-versions")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResVersion> getVersion();


    @GET("/api/public/saleyard-stream")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<ResHerdLive>> getLiveSaleyardStreams(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                   @Query("filter[is_live]") Integer islive);

    @GET("/api/public/saleyard-stream")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<ResHerdLive>> getLiveSaleyardStreamsUpcoming(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                           @Query("filter[after_stream_date]") String todayDate);

    @GET("/api/public/saleyard-stream")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<ResHerdLive>> getLiveSaleyardStreamsPast(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                       @Query("filter[before_stream_date]") String todayDate);

    @GET("/api/public/saleyard-stream")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<List<ResHerdLive>> getLiveSaleyardStreamsMy(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                     @Query("filter[user_id]") String userId);

    @GET("/api/public/saleyard-stream/{id}")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResSaleyardStreamDetails> getSaleyardStreamById(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                         @Path("id") String id,
                                                         @Query(AppConstants.TAG_include) String type);

    @GET("/api/medialive/channels/{aws_medialive_id}")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResAWSMedialiveChannel> getAwsMedialiveChannel(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                        @Path("aws_medialive_id") String awsMediaId);

    @POST("/api/medialive/channels/{aws_medialive_id}/start")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResAWSMedialiveChannel> startSaleyardStream(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                     @Path("aws_medialive_id") String awsMediaId);

    @GET("/api/medialive/inputs/{InputId}")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResAWSMedialiveChannel> getMediaLiveInput(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                   @Path("InputId") String inputId);

    @POST("/api/medialive/channels/{aws_medialive_id}/stop")
    @Headers(AppConstants.HEADER_ACCEPT)
    Call<ResAWSMedialiveChannel> stopSaleyardStream(@Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
                                                     @Path("aws_medialive_id") String awsMediaId);

    @POST("/api/saleyard-stream")
    @Headers(AppConstants.HEADER_ACCEPT)
    @FormUrlEncoded
    Call<ResHerdLive> createSaleyardStream(
            @Header(AppConstants.HEADER_AUTHORIZATION) String authorization,
            @Field("name") String name,
            @Field("stream_date") String streamDate
    );

}
