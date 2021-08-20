package com.theherdonline.api;

import com.theherdonline.app.AppConstants;
import com.theherdonline.db.DataConverter;
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
import com.theherdonline.ui.general.ADType;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.general.SearchFilterPublicLivestock;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.herdLive.ResAWSMedialiveChannel;
import com.theherdonline.ui.herdLive.ResHerdLive;
import com.theherdonline.ui.herdLive.ResSaleyardStreamDetails;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.FilterUtil;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UserType;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.theherdonline.app.AppConstants.TEXT_PLAIN;
import static com.theherdonline.app.AppConstants.TEXT_URLENCODED;
import static com.theherdonline.util.TimeUtils.Local2UTC;

@Singleton
public class RetrofitClient {

    private final Retrofit retrofit;
    private final AppUtil mAppUtil;
    private final IncludeFilterProvider mIncludeParam;


    @Inject
    public RetrofitClient(Retrofit retrofit, AppUtil appUtil, IncludeFilterProvider includeFilterProvider) {
        this.retrofit = retrofit;
        mAppUtil = appUtil;
        mIncludeParam = includeFilterProvider;
    }

    private static MultipartBody.Part getMultipartFromImage(final String url, final String tag) {
        MultipartBody.Part output = null;
        if (StringUtils.isNotBlank(url)) {
            File f = new File(url);
            RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_IMAGE), f);
            output = MultipartBody.Part.createFormData(tag, f.getName(), body);
        }
        return output;
    }

    private static MultipartBody.Part getMultipartFromAudio(final String url, final String tag) {
        MultipartBody.Part output = null;
        if (StringUtils.isNotBlank(url)) {
            File f = new File(url);
            RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_AUDIO), f);
            output = MultipartBody.Part.createFormData(tag, f.getName(), body);
        }
        return output;
    }

    private static MultipartBody.Part getMultipartFromVideo(final String url, final String tag) {
        MultipartBody.Part output = null;
        if (StringUtils.isNotBlank(url)) {
            File f = new File(url);
            RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_video), f);
            output = MultipartBody.Part.createFormData(tag, f.getName(), body);
        }
        return output;
    }

    private static MultipartBody.Part getMultipartFromAttachment(final String url, final String tag) {
        MultipartBody.Part output = null;
        if (StringUtils.isNotBlank(url)) {
            File f = new File(url);
            RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_video), f);
            output = MultipartBody.Part.createFormData(tag, f.getName(), body);
        }
        return output;
    }

    public void GetMarketData(IRequestCallback<MarketData> callback, final Integer nGymId) {
        Call<MarketData> response = retrofit.create(NetworkService.class)
                .getMarketingData(nGymId);
        response.enqueue(new RequestServiceCallback<MarketData>(callback));
    }

    public Call<EntityLivestock> getLivestockById(Integer id) {
        RequestCommaValue include = new RequestCommaValue(AppConstants.TAG_media, AppConstants.TAG_images, AppConstants.TAG_producer,
                AppConstants.TAG_agent, AppConstants.TAG_organisation, AppConstants.TAG_advertisement_dash_status, AppConstants.TAG_chats);
        RequestCommaValue append = new RequestCommaValue(AppConstants.TAG_bid_count, AppConstants.TAG_has_liked);
        return retrofit.create(NetworkService.class).getLivestockById(mAppUtil.getAccessToken(), id, include.filterValue(), append.filterValue());
    }

    public Call<Void> deleteLivestockMedia(Integer livestockId, Integer mediaId) {
        return retrofit.create(NetworkService.class).deleteLiveStockMedia(mAppUtil.getAccessToken(), livestockId, mediaId);
    }


    public Call<Void> deleteSaleyardMedia(Integer saleyardId, Integer mediaId) {
        return retrofit.create(NetworkService.class).deleteSaleyardMedia(mAppUtil.getAccessToken(), saleyardId, mediaId);
    }

    public Call<Void> deleteLivestock(Integer livestockId) {
        return retrofit.create(NetworkService.class).deleteLivestock(mAppUtil.getAccessToken(), livestockId);
    }

    public Call<MyChat> createChat(Integer livestockId) {
        return retrofit.create(NetworkService.class).postCreateChat(mAppUtil.getAccessToken(), livestockId);
    }


    public Call<List<Media>> postLivestockMediaVideo(Integer livestockId, List<Media> videoMediaList) {

        List<MultipartBody.Part> output = new ArrayList<>();
        if (videoMediaList != null && videoMediaList.size() > 0) {
            for (int i = 0; i < videoMediaList.size(); i++) {
                String url = videoMediaList.get(i).getUrl();
                Boolean isPrimary = videoMediaList.get(i).getPrimary();
                if (url != null && !ActivityUtils.isStartWithHttp(url)) {
                    File imgFile = new File(url);
                    if (imgFile.exists() && imgFile.isFile()) {
                        RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_IMAGE), imgFile);
                        output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_videos, i, AppConstants.TAG_path), imgFile.getName(), body));
                        output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_videos, i, AppConstants.TAG_is_primary), isPrimary ? "1" : "0"));

                    }
                }
            }
        }

        Call<List<Media>> response = retrofit.create(NetworkService.class).postUploadImage(
                mAppUtil.getAccessToken(),
                livestockId,
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.TAG_videos),
                output);
        return response;
    }


    public Call<List<Media>> postLivestockMediaImage(Integer livestockId, List<Media> imageMediaList/*,ProgressRequestBodyObservable.UploadCallbacks listener*/) {
        List<MultipartBody.Part> output = new ArrayList<>();
        if (imageMediaList != null && imageMediaList.size() > 0) {
            for (int i = 0; i < imageMediaList.size(); i++) {
                String url = imageMediaList.get(i).getUrl();
                Boolean isPrimary = imageMediaList.get(i).getPrimary();
                if (url != null && !ActivityUtils.isStartWithHttp(url)) {
                    File imgFile = new File(url);
                    // Log.d("postLivestockMediaImage", "postLivestockMediaImage: 1 " + imgFile.length());
                    //AppUtil.compressImage(url);
                    // Log.d("postLivestockMediaImage", "postLivestockMediaImage: 2 " + imgFile.length());

                    imgFile = new File(url);
                    if (imgFile.exists() && imgFile.isFile()) {
                        RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_IMAGE), imgFile);
                        output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_images, i, AppConstants.TAG_path), imgFile.getName(), body));
                        output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_images, i, AppConstants.TAG_is_primary), isPrimary ? "1" : "0"));
                    }
                }
            }
        }


        Call<List<Media>> response = retrofit.create(NetworkService.class).postUploadImage(
                mAppUtil.getAccessToken(),
                livestockId,
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.TAG_images),
                output);
        return response;
    }


    public Call<List<Media>> postSaleyardkMediaVideo(Integer livestockId, List<Media> videoMediaList) {

        List<MultipartBody.Part> output = new ArrayList<>();
        if (videoMediaList != null && videoMediaList.size() > 0) {
            for (int i = 0; i < videoMediaList.size(); i++) {
                String url = videoMediaList.get(i).getUrl();
                Boolean isPrimary = videoMediaList.get(i).getPrimary();
                if (url != null && !ActivityUtils.isStartWithHttp(url)) {
                    File imgFile = new File(url);
                    if (imgFile.exists() && imgFile.isFile()) {
                        RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_IMAGE), imgFile);
                        output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_videos, i, AppConstants.TAG_path), imgFile.getName(), body));
                        output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_videos, i, AppConstants.TAG_is_primary), isPrimary ? "1" : "0"));

                    }
                }
            }
        }

        Call<List<Media>> response = retrofit.create(NetworkService.class).postSaleyardMedias(
                mAppUtil.getAccessToken(),
                livestockId,
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.TAG_videos),
                output);
        return response;
    }


    public Call<List<Media>> postSaleyardkMediaPdf(Integer livestockId, List<Media> videoMediaList) {

        List<MultipartBody.Part> output = new ArrayList<>();
        if (videoMediaList != null && videoMediaList.size() > 0) {
            for (int i = 0; i < videoMediaList.size(); i++) {
                String url = videoMediaList.get(i).getUrl();
                Boolean isPrimary = videoMediaList.get(i).getPrimary();
                if (url != null && !ActivityUtils.isStartWithHttp(url)) {
                    File imgFile = new File(url);
                    if (imgFile.exists() && imgFile.isFile()) {
                        RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_IMAGE), imgFile);
                        output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_pdf, i, AppConstants.TAG_path), imgFile.getName(), body));
                        //output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_pdfs, i, AppConstants.TAG_is_primary),"0"));
                    }
                }
            }
        }

        Call<List<Media>> response = retrofit.create(NetworkService.class).postSaleyardMedias(
                mAppUtil.getAccessToken(),
                livestockId,
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.TAG_pdf),
                output);
        return response;
    }


    public Call<List<Media>> postSaleyardMediaImage(Integer livestockId, List<Media> imageMediaList/*,ProgressRequestBodyObservable.UploadCallbacks listener*/) {
        List<MultipartBody.Part> output = new ArrayList<>();
        if (imageMediaList != null && imageMediaList.size() > 0) {
            for (int i = 0; i < imageMediaList.size(); i++) {
                String url = imageMediaList.get(i).getUrl();
                Boolean isPrimary = imageMediaList.get(i).getPrimary();
                if (url != null && !ActivityUtils.isStartWithHttp(url)) {
                    File imgFile = new File(url);
                    // Log.d("postLivestockMediaImage", "postLivestockMediaImage: 1 " + imgFile.length());
                    //AppUtil.compressImage(url);
                    // Log.d("postLivestockMediaImage", "postLivestockMediaImage: 2 " + imgFile.length());

                    imgFile = new File(url);
                    if (imgFile.exists() && imgFile.isFile()) {
                        RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_IMAGE), imgFile);
                        output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_images, i, AppConstants.TAG_path), imgFile.getName(), body));
                        output.add(MultipartBody.Part.createFormData(String.format("%s[%d][%s]", AppConstants.TAG_images, i, AppConstants.TAG_is_primary), isPrimary ? "1" : "0"));
                    }
                }
            }
        }


        Call<List<Media>> response = retrofit.create(NetworkService.class).postSaleyardMedias(
                mAppUtil.getAccessToken(),
                livestockId,
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.TAG_images),
                output);
        return response;
    }


    public Call<Media> postUserAvatar(Integer userId, String path) {
        File imgFile = new File(path);
        if (imgFile.exists() && imgFile.isFile()) {
            RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_IMAGE), imgFile);
            Call<Media> response = retrofit.create(NetworkService.class).postUploadAvatar(
                    mAppUtil.getAccessToken(),
                    userId,
                    MultipartBody.Part.createFormData(AppConstants.TAG_avatar_location, imgFile.getName(), body)
            );
            return response;
        }
        return

                null;
    }


    public Call<MarketReport> postMarketReport(final MarketReport marketReport) {
        Map<String, RequestBody> params = new HashMap<>();
        if (marketReport.getTitle() != null) {
            params.put("title", RequestBody.create(MediaType.parse(TEXT_PLAIN), marketReport.getTitle()));

        }

        if (marketReport.getDescription() != null) {
            params.put("description", RequestBody.create(MediaType.parse(TEXT_PLAIN), marketReport.getDescription()));

        }

        if (marketReport.getSaleyard_id() != null) {
            params.put("saleyard_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(marketReport.getSaleyard_id())));

        }

        if (marketReport.getUser_id() != null) {
            params.put("user_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(marketReport.getUser_id())));

        }

        if (marketReport.getSale_at() != null) {
            params.put("sale_at", RequestBody.create(MediaType.parse(TEXT_PLAIN), Local2UTC(marketReport.getSale_at())));

        }

        if (marketReport.getReport_description() != null) {
            params.put("report_description", RequestBody.create(MediaType.parse(TEXT_PLAIN), marketReport.getReport_description()));

        }


        String videoUrl;
        if (ListUtils.emptyIfNull(marketReport.getMediaList()).size() != 0) {
            videoUrl = marketReport.getMediaList().get(0).getUrl();
        } else {
            videoUrl = null;
        }

        String attachmentsUrl;
        if (ListUtils.emptyIfNull(marketReport.getMediaList()).size() != 0) {
            attachmentsUrl = marketReport.getMediaList().get(0).getUrl_attchment();
        } else {
            attachmentsUrl = null;
        }
        return retrofit.create(NetworkService.class).postMarketReport(mAppUtil.getAccessToken(),
                params, getMultipartFromAudio(marketReport.getFull_path(), AppConstants.TAG_path),
                getMultipartFromVideo(videoUrl, AppConstants.TAG_video),
                getMultipartFromAttachment(attachmentsUrl, AppConstants.TAG_attachments));

    }

    public void setMapBody(Map<String, RequestBody> body, String tag, String value) {
        if (value != null) {
            body.put(tag, RequestBody.create(MediaType.parse(TEXT_PLAIN), value));
        }
    }

    public Map<String, RequestBody> userRequestBody(User user, String password) {
        Map<String, okhttp3.RequestBody> params = new HashMap<>();
        setMapBody(params, "first_name", user.getFirstName());
        setMapBody(params, "last_name", user.getLastName());
        setMapBody(params, "email", user.getEmail());
        setMapBody(params, "confirmation_email", user.getEmail());
        setMapBody(params, "address_line_one", user.getAddressLineOne());
        setMapBody(params, "address_line_two", user.getAddressLineTwo());
        setMapBody(params, "address_suburb", user.getAddressSuburb());
        setMapBody(params, "address_city", user.getAddressCity());
        setMapBody(params, "address_postcode", user.getAddressPostcode());
        setMapBody(params, "organistion_id", user.getOrganisationId() != null ? String.valueOf(user.getOrganisationId()) : null);
        setMapBody(params, "lat", user.getLat() != null ? String.valueOf(user.getLat()) : null);
        setMapBody(params, "lng", user.getLng() != null ? String.valueOf(user.getLng()) : null);
        setMapBody(params, "job_title", user.getJobTitle());
        setMapBody(params, "facebook_url", user.getFacebookUrl());
        setMapBody(params, "twitter_url", user.getTwitterUrl());
        setMapBody(params, "phone", user.getPhone());
        setMapBody(params, "youtube_url", user.getYoutubeUrl());
        setMapBody(params, "instagram_url", user.getInstagramUrl());
        setMapBody(params, "password", password);
        setMapBody(params, "password_confirmation", password);
        return params;
    }


    public void setMapBodyUrlCoded(Map<String, RequestBody> body, String tag, String value) {
        if (value != null) {
            body.put(tag, RequestBody.create(MediaType.parse(TEXT_URLENCODED), value));
        }
    }


    public Map<String, RequestBody> userRequestBodyUrlCoded(User user, String password) {
        Map<String, okhttp3.RequestBody> params = new HashMap<>();
        setMapBodyUrlCoded(params, "first_name", user.getFirstName());
        setMapBodyUrlCoded(params, "last_name", user.getLastName());
        setMapBodyUrlCoded(params, "email", user.getEmail());
        setMapBodyUrlCoded(params, "confirmation_email", user.getEmail());
        setMapBodyUrlCoded(params, "address_line_one", user.getAddressLineOne());
        setMapBodyUrlCoded(params, "address_line_two", user.getAddressLineTwo());
        setMapBodyUrlCoded(params, "address_suburb", user.getAddressSuburb());
        setMapBodyUrlCoded(params, "address_city", user.getAddressCity());
        setMapBodyUrlCoded(params, "address_postcode", user.getAddressPostcode());
        setMapBodyUrlCoded(params, "organistion_id", user.getOrganisationId() != null ? String.valueOf(user.getOrganisationId()) : null);
        setMapBodyUrlCoded(params, "lat", user.getLat() != null ? String.valueOf(user.getLat()) : null);
        setMapBodyUrlCoded(params, "lng", user.getLng() != null ? String.valueOf(user.getLng()) : null);
        setMapBodyUrlCoded(params, "job_title", user.getJobTitle());
        setMapBodyUrlCoded(params, "facebook_url", user.getFacebookUrl());
        setMapBodyUrlCoded(params, "twitter_url", user.getTwitterUrl());
        setMapBodyUrlCoded(params, "phone", user.getPhone());
        setMapBodyUrlCoded(params, "youtube_url", user.getYoutubeUrl());
        setMapBodyUrlCoded(params, "instagram_url", user.getInstagramUrl());
        setMapBodyUrlCoded(params, "password", password);
        setMapBodyUrlCoded(params, "password_confirmation", password);
        return params;
    }


    public Call<ChatkitToken> getChatKitToken() {
        return retrofit.create(NetworkService.class).postChatkitToken(mAppUtil.getAccessToken());
    }


    public Call<User> createUser(User user, String password) {
        Map<String, RequestBody> params = userRequestBody(user, password);
        return retrofit.create(NetworkService.class).postRegisterUser(params);
    }

    public Call<User> updateUser(User user) {
        // Map<String,RequestBody> params = userRequestBodyUrlCoded(user, null);
        // return retrofit.create(NetworkService.class).putUpdateUser(mAppUtil.getAccessToken(),user.getId(),params);
        return retrofit.create(NetworkService.class).putUpdateUserNew(mAppUtil.getAccessToken(), user.getId(),
                user.getFirstName(), user.getLastName(), user.getPhone());

    }


    public Call<EntitySaleyard> storeSaleyard(final EntitySaleyard postAdData, final Integer id) {

        Map<String, RequestBody> params = new HashMap<>();
        if (postAdData.getName() != null) {
            params.put("name", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getName()));
        }
        if (postAdData.getType() != null) {
            params.put("type", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getType()));
        }
        if (postAdData.getDescription() != null) {
            params.put("description", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getDescription()));
        }
        if (postAdData.getSaleyardStatusId() != null) {
            params.put("saleyard_status_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getSaleyardStatusId().toString()));
        }


        if (postAdData.getSale_numbers() != null) {
            params.put("sale_numbers", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getSale_numbers()));
        }
        if (postAdData.getStartsAt() != null) {
            params.put("starts_at", RequestBody.create(MediaType.parse(TEXT_PLAIN), TimeUtils.Local2UTCDate_start_at(postAdData.getStartsAt())));
        }
        if (postAdData.getSaleyardStatusId() != null) {
            params.put("sale_numbers", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getSaleyardStatusId().toString()));
        }
        if (postAdData.getSaleyardCategoryId() != null) {
            params.put("saleyard_category_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getSaleyardCategoryId().toString()));
        }
        if (postAdData.getAddressLineOne() != null) {
            params.put("address_line_one", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAddressLineOne()));
        }
        if (postAdData.getAddressLineTwo() != null) {
            params.put("address_line_two", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAddressLineTwo()));
        }
        if (postAdData.getAddressSuburb() != null) {
            params.put("address_suburb", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAddressSuburb()));
        }
        if (postAdData.getAddressPostcode() != null) {
            params.put("address_postcode", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAddressPostcode()));
        }

        if (postAdData.getAddress() != null) {
            params.put("address", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAddress()));
        }


        if (postAdData.getLat() != null) {
            params.put("lat", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getLat())));
        }
        if (postAdData.getLng() != null) {
            params.put("lng", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getLng()))); // postAdData.getWeighed().toString()));
        }

        if (postAdData.getHeadcount() != null) {
            params.put("headcount", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getHeadcount()))); // postAdData.getWeighed().toString()));
        }


        // update
        Map<String, String> paramsUpdate = new HashMap<>();
        if (postAdData.getName() != null) {
            paramsUpdate.put("name", postAdData.getName());
        }
        if (postAdData.getType() != null) {
            paramsUpdate.put("type", postAdData.getType());
        }
        if (postAdData.getDescription() != null) {
            paramsUpdate.put("description", postAdData.getDescription());
        }
        //if (postAdData.getSaleyardStatusId() != null)
        //{
        //    paramsUpdate.put("saleyard_status_id", postAdData.getSaleyardStatusId().toString());
        //}
        if (postAdData.getSale_numbers() != null) {
            paramsUpdate.put("sale_numbers", postAdData.getSale_numbers());
        }
        if (postAdData.getStartsAt() != null) {
            paramsUpdate.put("starts_at", TimeUtils.Local2UTCDate_start_at(postAdData.getStartsAt()));
        }
        if (postAdData.getSaleyardStatusId() != null) {
            paramsUpdate.put("sale_numbers", postAdData.getSaleyardStatusId().toString());
        }
        if (postAdData.getSaleyardCategoryId() != null) {
            paramsUpdate.put("saleyard_category_id", postAdData.getSaleyardCategoryId().toString());
        }
        if (postAdData.getAddressLineOne() != null) {
            paramsUpdate.put("address_line_one", postAdData.getAddressLineOne());
        }
        if (postAdData.getAddressLineTwo() != null) {
            paramsUpdate.put("address_line_two", postAdData.getAddressLineTwo());
        }
        if (postAdData.getAddressSuburb() != null) {
            paramsUpdate.put("address_suburb", postAdData.getAddressSuburb());
        }
        if (postAdData.getAddressPostcode() != null) {
            paramsUpdate.put("address_postcode", postAdData.getAddressPostcode());
        }

        if (postAdData.getAddress() != null) {
            paramsUpdate.put("address", postAdData.getAddress());
        }


        if (postAdData.getLat() != null) {
            paramsUpdate.put("lat", String.valueOf(postAdData.getLat()));
        }
        if (postAdData.getLng() != null) {
            paramsUpdate.put("lng", String.valueOf(postAdData.getLng())); // postAdData.getWeighed().toString()));
        }

        if (postAdData.getHeadcount() != null) {
            paramsUpdate.put("headcount", String.valueOf(postAdData.getHeadcount())); // postAdData.getWeighed().toString()));
        }


        if (id != null) {
            return retrofit.create(NetworkService.class).postUpdateSaleyard(mAppUtil.getAccessToken(), id, paramsUpdate);
        } else {
            return retrofit.create(NetworkService.class).postStoreSaleyard(mAppUtil.getAccessToken(), params);
        }
    }


    public Call<EntityLivestock> storeLivestock(final EntityLivestock postAdData, final Integer id) {

        Map<String, RequestBody> params = new HashMap<>();
        if (postAdData.getName() != null) {
            params.put("name", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getName()));
        }

        if (postAdData.getDescription() != null) {
            params.put("description", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getDescription()));
        }
        if (postAdData.getPrice() != null) {
            params.put("price", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getPrice()));
        }
        if (postAdData.getPriceLow() != null) {
            params.put("price_low", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getPriceLow()));
        }
        if (postAdData.getPriceHigh() != null) {
            params.put("price_high", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getPriceHigh()));
        }
        if (postAdData.getPrice_type() != null) {
            params.put("price_type", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getPrice_type()));
        }

        if (postAdData.getOffer_type() != null) {
            params.put("offer_type", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getOffer_type()));
        }

        if (postAdData.getOno() != null) {
            params.put("ono", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getOno().toString()));
        }
        if (postAdData.getUnder_offer() != null) {
            params.put("under_offer", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getUnder_offer().toString()));
        }

        if (postAdData.getStart_date() != null) {
            params.put("start_date", RequestBody.create(MediaType.parse(TEXT_PLAIN), Local2UTC(postAdData.getStart_date())));
        }

        if (postAdData.getEnd_date() != null) {
            params.put("end_date", RequestBody.create(MediaType.parse(TEXT_PLAIN), Local2UTC(postAdData.getEnd_date())));
        }

        if (postAdData.getAvailable_at() != null) {
            params.put("available_at", RequestBody.create(MediaType.parse(TEXT_PLAIN), TimeUtils.Local2UTCDate_Available(postAdData.getAvailable_at())));
        }
        if (postAdData.getAsap() != null) {
            params.put("is_pick_up_asap", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAsap().toString()));
        }
        if (postAdData.getEarliest_at() != null) {
            params.put("pick_up_at", RequestBody.create(MediaType.parse(TEXT_PLAIN), TimeUtils.Local2UTCDate_Available(postAdData.getEarliest_at())));
        }
        if (postAdData.getLatest_at() != null) {
            params.put("latest_pick_up_at", RequestBody.create(MediaType.parse(TEXT_PLAIN), TimeUtils.Local2UTCDate_Available(postAdData.getLatest_at())));
        }

        if (postAdData.getQuantity() != null) {
            params.put("quantity", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getQuantity().toString()));
        }
        if (postAdData.getAge() != null) {
            params.put("age", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAge().toString()));
        }
        if (postAdData.getAge_type() != null) {
            params.put("age_type", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAge_type()));
        }
        if (postAdData.getLowestWeight() != null) {
            params.put("lowest_weight", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getLowestWeight().toString()));
        }
        if (postAdData.getHighestWeight() != null) {
            params.put("highest_weight", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getHighestWeight().toString()));
        }
        if (postAdData.getAverageWeight() != null) {
            params.put("average_weight", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAverageWeight().toString()));
        }
        if (postAdData.getGender_id() != null) {
            params.put("gender_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getGender_id().toString()));
        }
        if (postAdData.getPregnancy_status_id() != null) {
            params.put("pregnancy_status_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getPregnancy_status_id().toString()));
        }
        if (postAdData.getWeighed() != null) {
            params.put("weighed", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getWeighed().toString()));
        }
        if (postAdData.getEu() != null) {
            params.put("eu", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getEu().toString()));
        }
        if (postAdData.getMouthed() != null) {
            params.put("mouthed", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getMouthed().toString()));
        }
        if (postAdData.getMsa() != null) {
            params.put("msa", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getMsa().toString()));
        }
        if (postAdData.getLpa() != null) {
            params.put("lpa", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getLpa().toString()));
        }
        if (postAdData.getOrganic() != null) {
            params.put("organic", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getOrganic().toString()));
        }
        if (postAdData.getVendorBred() != null) {
            params.put("vendor_bred", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getVendorBred().toString()));
        }
        if (postAdData.getPcas() != null) {
            params.put("pcas", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getPcas().toString()));
        }
        if (postAdData.getGrassFed() != null) {
            params.put("is_grass_fed", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getGrassFed().toString()));
        }
        if (postAdData.getAntibioticFree() != null) {
            params.put("is_antibiotic_free", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAntibioticFree().toString()));
        }
        if (postAdData.getLifeTimeTraceable() != null) {
            params.put("is_life_time_traceable", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getLifeTimeTraceable().toString()));
        }
        if (postAdData.getOneMark() != null) {
            params.put("is_one_mark", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getOneMark().toString()));
        }
        if (postAdData.getHgpFree() != null) {
            params.put("is_hgp_free", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getHgpFree().toString()));
        }
        if (postAdData.getAssessedBy() != null) {
            params.put("assessed_by", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAssessedBy()));
        }
        if (postAdData.getAddressLineOne() != null) {
            params.put("address_line_one", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAddressLineOne()));
        }
        if (postAdData.getAddressLineTwo() != null) {
            params.put("address_line_two", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAddressLineTwo()));
        }
        if (postAdData.getAddressSuburb() != null) {
            params.put("address_suburb", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAddressSuburb()));
        }
        if (postAdData.getAddressPostcode() != null) {
            params.put("address_postcode", RequestBody.create(MediaType.parse(TEXT_PLAIN), postAdData.getAddressPostcode()));
        }
        if (postAdData.getLat() != null) {
            params.put("lat", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getLat())));
        }
        if (postAdData.getLng() != null) {
            params.put("lng", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getLng()))); // postAdData.getWeighed().toString()));
        }
        if (postAdData.getAdvertisementStatusId() != null) {
            params.put("advertisement_status_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getAdvertisementStatusId())));
        }
        if (postAdData.getLivestockCategoryId() != null) {
            params.put("livestock_category_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getLivestockCategoryId())));
        }
        if (postAdData.getLivestockSubCategoryId() != null) {
            params.put("livestock_sub_category_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getLivestockSubCategoryId())));
        }
        if (postAdData.getProducerId() != null) {
            params.put("producer_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getProducerId())));
        }
        if (postAdData.getAgentId() != null) {
            params.put("agent_id", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getAgentId())));
        }

        if (postAdData.getDuration() != null) {
            params.put("duration", RequestBody.create(MediaType.parse(TEXT_PLAIN), String.valueOf(postAdData.getDuration())));
        }


        // update
        Map<String, String> paramsUpdate = new HashMap<>();
        if (postAdData.getName() != null) {
            paramsUpdate.put("name", postAdData.getName());
        }

        if (postAdData.getDescription() != null) {
            paramsUpdate.put("description", postAdData.getDescription());
        }
        if (postAdData.getPrice() != null) {
            paramsUpdate.put("price", postAdData.getPrice());
        }
        if (postAdData.getPrice_type() != null) {
            paramsUpdate.put("price_type", postAdData.getPrice_type());
        }

        if (postAdData.getOffer_type() != null) {
            paramsUpdate.put("offer_type", postAdData.getOffer_type());
        }
        if (postAdData.getPriceLow() != null) {
            paramsUpdate.put("price_low", postAdData.getPriceLow());
        }
        if (postAdData.getPriceHigh() != null) {
            paramsUpdate.put("price_high", postAdData.getPriceHigh());
        }

        if (postAdData.getOno() != null) {
            paramsUpdate.put("ono", postAdData.getOno().toString());
        }

        // user can change start date and end date when editing the adv,  so ignore them
/*        if (postAdData.getStart_date() != null)
        {
            paramsUpdate.put("start_date", postAdData.getStart_date());
            //ActivityUtils.postDateTimeString(postAdData.getStartDateTime())));
        }

        if (postAdData.getEnd_date() != null)
        {
            paramsUpdate.put("end_date", postAdData.getEnd_date());
            //ActivityUtils.postDateTimeString(postAdData.getEndDateTime())));
        }*/

        if (postAdData.getUnder_offer() != null) {
            paramsUpdate.put("under_offer", postAdData.getUnder_offer().toString());
        }

        if (postAdData.getAvailable_at() != null) {
            paramsUpdate.put("available_at", TimeUtils.Local2UTCDate_Available(postAdData.getAvailable_at()));
        }
        if (postAdData.getAsap() != null) {
            paramsUpdate.put("is_pick_up_asap", postAdData.getAsap().toString());
        }
        if (postAdData.getEarliest_at() != null) {
            paramsUpdate.put("pick_up_at", TimeUtils.Local2UTCDate_Available(postAdData.getEarliest_at()));
        }
        if (postAdData.getLatest_at() != null) {
            paramsUpdate.put("latest_pick_up_at", TimeUtils.Local2UTCDate_Available(postAdData.getLatest_at()));
        }
        if (postAdData.getQuantity() != null) {
            paramsUpdate.put("quantity", postAdData.getQuantity().toString());
        }
        if (postAdData.getAge() != null) {
            paramsUpdate.put("age", postAdData.getAge().toString());
        }
        if (postAdData.getMax_age() != null) {
            paramsUpdate.put("max_age", postAdData.getMax_age().toString());
        }
        if (postAdData.getAverage_age() != null) {
            paramsUpdate.put("average_age", postAdData.getAverage_age().toString());
        }
        if (postAdData.getLowestWeight() != null) {
            paramsUpdate.put("lowest_weight", postAdData.getLowestWeight().toString());
        }
        if (postAdData.getHighestWeight() != null) {
            paramsUpdate.put("highest_weight", postAdData.getHighestWeight().toString());
        }
        if (postAdData.getAverageWeight() != null) {
            paramsUpdate.put("average_weight", postAdData.getAverageWeight().toString());
        }
        if (postAdData.getGender_id() != null) {
            paramsUpdate.put("gender_id", postAdData.getGender_id().toString());
        }
        if (postAdData.getPregnancy_status_id() != null) {
            paramsUpdate.put("pregnancy_status_id", postAdData.getPregnancy_status_id().toString());
        }
        if (postAdData.getWeighed() != null) {
            paramsUpdate.put("weighed", postAdData.getWeighed().toString());
        }
        if (postAdData.getDentition() != null) {
            paramsUpdate.put("dentition", postAdData.getDentition().toString());
        }
        if (postAdData.getEu() != null) {
            paramsUpdate.put("eu", postAdData.getEu().toString());
        }
        if (postAdData.getMouthed() != null) {
            paramsUpdate.put("mouthed", postAdData.getMouthed().toString());
        }
        if (postAdData.getMsa() != null) {
            paramsUpdate.put("msa", postAdData.getMsa().toString());
        }
        if (postAdData.getLpa() != null) {
            paramsUpdate.put("lpa", postAdData.getLpa().toString());
        }
        if (postAdData.getOrganic() != null) {
            paramsUpdate.put("organic", postAdData.getOrganic().toString());
        }
        if (postAdData.getVendorBred() != null) {
            paramsUpdate.put("vendor_bred", postAdData.getVendorBred().toString());
        }
        if (postAdData.getPcas() != null) {
            paramsUpdate.put("pcas", postAdData.getPcas().toString());
        }
        if (postAdData.getGrassFed() != null) {
            paramsUpdate.put("is_grass_fed", postAdData.getGrassFed().toString());
        }
        if (postAdData.getAntibioticFree() != null) {
            paramsUpdate.put("is_antibiotic_free", postAdData.getAntibioticFree().toString());
        }
        if (postAdData.getLifeTimeTraceable() != null) {
            paramsUpdate.put("is_life_time_traceable", postAdData.getLifeTimeTraceable().toString());
        }
        if (postAdData.getOneMark() != null) {
            paramsUpdate.put("is_one_mark", postAdData.getOneMark().toString());
        }
        if (postAdData.getHgpFree() != null) {
            paramsUpdate.put("is_hgp_free", postAdData.getHgpFree().toString());
        }
        if (postAdData.getAssessedBy() != null) {
            paramsUpdate.put("assessed_by", postAdData.getAssessedBy());
        }
        if (postAdData.getAddressLineOne() != null) {
            paramsUpdate.put("address_line_one", postAdData.getAddressLineOne());
        }
        if (postAdData.getAddressLineTwo() != null) {
            paramsUpdate.put("address_line_two", postAdData.getAddressLineTwo());
        }
        if (postAdData.getAddressSuburb() != null) {
            paramsUpdate.put("address_suburb", postAdData.getAddressSuburb());
        }
        if (postAdData.getAddressPostcode() != null) {
            paramsUpdate.put("address_postcode", postAdData.getAddressPostcode());
        }
        if (postAdData.getLat() != null) {
            paramsUpdate.put("lat", String.valueOf(postAdData.getLat()));
        }
        if (postAdData.getLng() != null) {
            paramsUpdate.put("lng", String.valueOf(postAdData.getLng())); // postAdData.getWeighed().toString()));
        }
        if (postAdData.getAdvertisementStatusId() != null) {
            paramsUpdate.put("advertisement_status_id", String.valueOf(postAdData.getAdvertisementStatusId()));
        }
        if (postAdData.getLivestockCategoryId() != null) {
            paramsUpdate.put("livestock_category_id", String.valueOf(postAdData.getLivestockCategoryId()));
        }
        if (postAdData.getLivestockSubCategoryId() != null) {
            paramsUpdate.put("livestock_sub_category_id", String.valueOf(postAdData.getLivestockSubCategoryId()));
        }
        if (postAdData.getProducerId() != null) {
            paramsUpdate.put("producer_id", String.valueOf(postAdData.getProducerId()));
        }
        if (postAdData.getAgentId() != null) {
            paramsUpdate.put("agent_id", String.valueOf(postAdData.getAgentId()));
        }
        if (postAdData.getDuration() != null) {
            paramsUpdate.put("duration", String.valueOf(postAdData.getDuration()));
        }

        if (id != null) {
            return retrofit.create(NetworkService.class).postUpdateLiveStock(mAppUtil.getAccessToken(), id, paramsUpdate);
        } else {
            return retrofit.create(NetworkService.class).postStoreLiveStock(mAppUtil.getAccessToken(), params);

        }
    }


    public Call<EntityLivestock> updateBidId(final Integer livestockId, final Integer bidId) {

        // update
        Map<String, String> paramsUpdate = new HashMap<>();
        paramsUpdate.put("bid_id", String.valueOf(bidId));
        return retrofit.create(NetworkService.class).postUpdateLiveStock(mAppUtil.getAccessToken(), livestockId, paramsUpdate);
    }


    public Call<EntityLivestock> getPublicLivestock(final Integer livestockId) {
        return retrofit.create(NetworkService.class).getPublicLivestock(livestockId);
    }

    public Call<Void> postSaleyardRequest() {
        return retrofit.create(NetworkService.class).postSaleyardRequest(mAppUtil.getAccessToken());
    }


    public Call<List<MyChat>> getMyChatList() {
        return retrofit.create(NetworkService.class).getMyChat(mAppUtil.getAccessToken());
    }

    public Call<ResLogin> postLogin(final String username, final String password) {
        return retrofit.create(NetworkService.class).postLogin(
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.LOGIN_GRAND_TYPE),
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.LOGIN_CLIENT_ID.toString()),
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.LOGIN_CLIENT_SECRET),
                RequestBody.create(MediaType.parse(TEXT_PLAIN), username),
                RequestBody.create(MediaType.parse(TEXT_PLAIN), password));
    }


    public Call<ResLogin> postLoginWithFacebook(final String access_token) {
        return retrofit.create(NetworkService.class).postLoginWithFacebook(
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.LOGIN_GRAND_TYPE_social),
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.LOGIN_CLIENT_ID.toString()),
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.LOGIN_CLIENT_SECRET),
                RequestBody.create(MediaType.parse(TEXT_PLAIN), AppConstants.LOGIN_PROVIDER_facebook),
                RequestBody.create(MediaType.parse(TEXT_PLAIN), access_token));
    }

    public Call<Bid> postMakeBid(final Integer userId, final Integer livestockId, final Integer price) {
        return retrofit.create(NetworkService.class).postMakeBid(
                mAppUtil.getAccessToken(), livestockId,
                userId == null ? null : RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId)),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price)));
    }


    public Call<List<PaymentCard>> getPaymentCard() {
        return retrofit.create(NetworkService.class).getPaymentCard(
                mAppUtil.getAccessToken(), mAppUtil.getUserId());
    }

    public Call<PaymentCard> postAddPaymentCard(PaymentCard paymentCard) {
        return retrofit.create(NetworkService.class).postAddPaymentCard(
                mAppUtil.getAccessToken(),
                mAppUtil.getUserId(),
                RequestBody.create(MediaType.parse("text/plain"), paymentCard.getSourceId()),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(paymentCard.getExpMonth())),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(paymentCard.getExpYear())),
                RequestBody.create(MediaType.parse("text/plain"), paymentCard.getClientSecret()),
                RequestBody.create(MediaType.parse("text/plain"), paymentCard.getStatus()),
                RequestBody.create(MediaType.parse("text/plain"), paymentCard.getBrand()),
                RequestBody.create(MediaType.parse("text/plain"), String.valueOf(paymentCard.getLastFour())),
                RequestBody.create(MediaType.parse("text/plain"), paymentCard.getThreeDSecure())
        );
    }


    public Call<ResLivestock> getActionPlus(final Integer pageIndex,
                                            final String include,
                                            final String append,
                                            final String sort) {
        return retrofit.create(NetworkService.class).getPublicActionPlus(mAppUtil.getAccessToken(), AppConstants.NUMBER_PRE_PAGE,
                pageIndex, include, append, AppConstants.TAG_sort_minus_end_date);
    }

    public Call<ResLivestock> getPublicLivestock(final SearchFilter searchFilter, final Integer pageIndex, final Boolean isAuctionPlus) {


        RequestCommaValue include = new RequestCommaValue(
                AppConstants.TAG_media,
                AppConstants.TAG_producer,
                AppConstants.TAG_organisation,
                AppConstants.TAG_agent,
                AppConstants.TAG_agent + "." + AppConstants.TAG_organisation,
                AppConstants.TAG_category,
                AppConstants.TAG_advertisement_dash_status,
                AppConstants.TAG_sub_category,
                AppConstants.TAG_my_dash_bid,
                AppConstants.TAG_chats);

        RequestCommaValue append = new RequestCommaValue(AppConstants.TAG_bid_count, AppConstants.TAG_has_liked, AppConstants.TAG_include_my_chat);

        if (!(searchFilter instanceof SearchFilterPublicLivestock)) {
            throw new RuntimeException("must be SearchFilterPublicLivestock");
        }


        if (isAuctionPlus) {
            return getActionPlus(pageIndex,
                    include.filterValue(),
                    append.filterValue(),
                    AppConstants.TAG_sort_minus_started_at
            );
        }


        SearchFilterPublicLivestock filter = (SearchFilterPublicLivestock) searchFilter;

        if (filter.getmPlace() != null) {
            filter.setLatLng(filter.getmPlace().getLatLng());

        }
        return retrofit.create(NetworkService.class).
                getPublicLivestock(
                        mAppUtil.getAccessToken(),
                        AppConstants.NUMBER_PRE_PAGE,
                        pageIndex,
                        filter.where_like,
                        FilterUtil.getAroundFilter(filter.latLng, filter.radius),
                        FilterUtil.getPriceFilter(filter.minPrice, filter.maxPrice),
                        FilterUtil.getAgeFilter(filter.minAge, filter.maxAge),
                        FilterUtil.getWeightFilter(filter.minWeight, filter.maxWeight),
                        null,
                        filter.genderEntity == null ? null : filter.genderEntity.getId(),
                        filter.quantity,
                        FilterUtil.getBoolTag(filter.weighted),
                        filter.pregnancyEntity == null ? null : filter.pregnancyEntity.getId(),
                        FilterUtil.getBoolTag(filter.dentition),
                        FilterUtil.getBoolTag(filter.mounted),
                        FilterUtil.getBoolTag(filter.eu),
                        FilterUtil.getBoolTag(filter.msa),
                        FilterUtil.getBoolTag(filter.lpa),
                        FilterUtil.getBoolTag(filter.organic),
                        FilterUtil.getBoolTag(filter.pcas),
                        "4,5",
                        filter.livestockCategory != null ? filter.livestockCategory.getId() : null,
                        filter.livestockSubCategory != null ? filter.livestockSubCategory.getId() : null,
                        null,
                        filter.agent_id,
                        filter.producer_id,
                        filter.organisation_id,
                        !isAuctionPlus,
                        filter.ageUnit,
                        include.filterValue(),
                        append.filterValue(),
                        AppConstants.TAG_sort_minus_end_date //AppConstants.TAG_sort_minus_started_at
                );
    }


    public Call<ResPagerSaleyard> getPublicSaleyardForSaleyardReport() {
        return retrofit.create(NetworkService.class).getPublicSaleyard(
                mAppUtil.getAccessToken(),
                1000,
                1,
                null,
                FilterUtil.getWhereBetweenFilterReport(),
                null,
                null,
                null,
                null,
                null,
                null, //2,
                null,
                null,
                null, // AppConstants.TAG_has_liked,
                AppConstants.TAG_sort_minus_starts_at
        );
    }

    // get saleyards
    public Call<ResPagerSaleyard> getPublicSaleyard(final SearchFilter searchFilter, final Integer pageIndex) {
        if (!(searchFilter instanceof SearchFilterPublicSaleyard)) {
            // To Do
            throw new RuntimeException("must be SearchFilterPublicSaleyard");
        }

        SearchFilterPublicSaleyard filter = (SearchFilterPublicSaleyard) searchFilter;


        return retrofit.create(NetworkService.class).getPublicSaleyard(
                mAppUtil.getAccessToken(),
                AppConstants.NUMBER_PRE_PAGE,
                pageIndex,
                filter.where_like,
                FilterUtil.getWhereBetweenFilter(filter.startDateTime, filter.endDateTime),
                FilterUtil.getAroundFilter(filter.latLng, filter.radius),
                filter.name,
                filter.type,
                filter.description,
                FilterUtil.getStartAt(filter.starts_at),
                2,
                filter.saleyard_category != null ? filter.saleyard_category.getId() : null,
                filter.user_id,
                AppConstants.TAG_has_liked,
                AppConstants.TAG_sort_starts_at

        );
    }

    public Call<List<LivestockCategory>> getLivestockCategory() {
        return retrofit.create(NetworkService.class).getLivestockCategory();
    }

    public Call<List<LivestockSubCategory>> getLivestockSubCategory() {
        return retrofit.create(NetworkService.class).getLivestockSubCategory();
    }


    public Call<List<SaleyardCategory>> getSaleyardCategory() {
        return retrofit.create(NetworkService.class).getSaleyardCategory();
    }


    // get market report
    public Call<ResMarketReport> getMarketReport(final Integer pageIndex,
                                                 final SearchFilter saleyardSearchFilter,
                                                 final RequestCommaValue include) {
        return retrofit.create(NetworkService.class).getMarketReport(
                AppConstants.NUMBER_PRE_PAGE,
                pageIndex,
                saleyardSearchFilter.getUser_id(),
                include.toString(),
                AppConstants.TAG_sort_minus_created_at);

    }


    // get market report
    public Call<ResLivestock> getLivestock(final Integer pageIndex,
                                           final SearchFilter searchFilter,
                                           final RequestCommaValue include) {

        if (!(searchFilter instanceof SearchFilterPublicLivestock)) {
            // To Do
            throw new RuntimeException("must be SearchFilterPublicLivestock");

        }

        SearchFilterPublicLivestock filter = (SearchFilterPublicLivestock) searchFilter;

        String adType = "";
        if (filter.adType != null) {
            for (int i = 0; i < filter.getAdType().size(); i++) {
                String t1 = String.valueOf(DataConverter.ADType2Int(filter.getAdType().get(i)));
                String t2 = "";
                if (i != filter.getAdType().size() - 1) {
                    t2 = ",";
                }
                adType += t1 + t2;
            }
        }

        return retrofit.create(NetworkService.class).getLivestock(
                mAppUtil.getAccessToken(),
                AppConstants.NUMBER_PRE_PAGE,
                pageIndex,
                filter.agent_id,
                filter.user_id,
                adType,
                include.toString(),
                AppConstants.TAG_bid_count,
                AppConstants.TAG_sort_minus_created_at
        );
    }


    // get saleyards
    public Call<ResStream> getStream(final Integer pageIndex,
                                     final SearchFilter saleyardSearchFilter,
                                     final RequestCommaValue include) {

        if (saleyardSearchFilter.getOrganisation_id() != null) {
            return retrofit.create(NetworkService.class).getStream(
                    mAppUtil.getAccessToken(),
                    /* AppConstants.NUMBER_PRE_PAGE,*/
                    pageIndex,
                    null,
                    null,
                    ActivityUtils.getStreamType(saleyardSearchFilter.getStreamType()),
                    saleyardSearchFilter.getOrganisation_id(),
                    new RequestCommaValue(AppConstants.TAG_user, AppConstants.TAG_streamable_media).filterValue(),
                    null,
                    AppConstants.TAG_sort_minus_created_at
            );
        } else {
            return retrofit.create(NetworkService.class).getStream(
                    mAppUtil.getAccessToken(),
                    /* AppConstants.NUMBER_PRE_PAGE,*/
                    pageIndex,
                    null,//saleyardSearchFilter.getUser_id(),
                    saleyardSearchFilter.getUser_id(),//getOf_user(),
                    ActivityUtils.getStreamType(saleyardSearchFilter.getStreamType()),
                    saleyardSearchFilter.getOrganisation_id(),
                    include.filterValue(),
                    AppConstants.TAG_has_liked,
                    AppConstants.TAG_sort_minus_created_at
            );
        }


    }


    // get saleyards
    public Call<ResPost> getPost(
            final Integer pageIndex,
            final SearchFilter saleyardSearchFilter,
            final RequestCommaValue include) {
        return retrofit.create(NetworkService.class).getPosts(
                mAppUtil.getAccessToken(),
                AppConstants.NUMBER_PRE_PAGE,
                pageIndex,
                include.filterValue(),
                AppConstants.TAG_sort_minus_created_at,
                saleyardSearchFilter.getUser_id());

    }


    public Call<ResNotification> getNotifications(
            final Integer pageIndex,
            final RequestCommaValue include) {
        return retrofit.create(NetworkService.class).getNotifications(
                mAppUtil.getAccessToken(),
                AppConstants.NUMBER_PRE_PAGE,
                pageIndex);
    }


    public Call<List<HerdNotification>> getNotificationV1() {
        return retrofit.create(NetworkService.class).getNotificationsV1(
                mAppUtil.getAccessToken());
    }

    public Call<ResponseBody> getNotificationV2() {
        return retrofit.create(NetworkService.class).getNotificationsV2(
                mAppUtil.getAccessToken());
    }

    public Call<User> patchPmcToken(Integer userId, String pmcToken) {
        return retrofit.create(NetworkService.class).postPMCToken(
                mAppUtil.getAccessToken(),
                userId,
                pmcToken);
    }

    public Call<ResLivestock> getLikedLivestock(
            final Integer pageIndex,
            final SearchFilter saleyardSearchFilter,
            final RequestCommaValue include) {
        RequestCommaValue r = new RequestCommaValue(AppConstants.TAG_media,
                AppConstants.TAG_producer,
                AppConstants.TAG_images,
                AppConstants.TAG_videos,
                AppConstants.TAG_organisation,
                AppConstants.TAG_agent,
                AppConstants.TAG_agent + "." + AppConstants.TAG_organisation,
                AppConstants.TAG_category,
                AppConstants.TAG_advertisement_dash_status,
                AppConstants.TAG_category,
                AppConstants.TAG_sub_category);
        return retrofit.create(NetworkService.class).getLikedLivestock(
                mAppUtil.getAccessToken(),
                AppConstants.NUMBER_PRE_PAGE,
                pageIndex,
                r.filterValue());

    }


    public Call<ResPagerSaleyard> getLikedSaleyard(
            final Integer pageIndex,
            final SearchFilter filter,
            final RequestCommaValue include) {

        String type = null;
        if (filter != null) {
            SearchFilterPublicSaleyard filterPublicSaleyard = (SearchFilterPublicSaleyard) filter;
            type = filterPublicSaleyard.getType();
        }


        return retrofit.create(NetworkService.class).getLikedSaleyard(
                mAppUtil.getAccessToken(),
                type,
                AppConstants.NUMBER_PRE_PAGE,
                pageIndex);

    }


    public Call<Void> setLikeLivestock(
            final Integer id, final Boolean bLike) {
        if (bLike) {
            return retrofit.create(NetworkService.class).setLikeLivestock(mAppUtil.getAccessToken(),
                    id);
        } else {
            return retrofit.create(NetworkService.class).setUnLikeLivestock(mAppUtil.getAccessToken(),
                    id);
        }
    }


    public Call<Void> setLikeSaleyard(
            final Integer id, final Boolean bLike) {
        if (bLike) {
            return retrofit.create(NetworkService.class).setLikeSaleyard(mAppUtil.getAccessToken(),
                    id);
        } else {
            return retrofit.create(NetworkService.class).setUnLikeSaleyard(mAppUtil.getAccessToken(),
                    id);
        }
    }

    // get saleyards
    public Call<ResBids> getBidList(final Integer pageIndex,
                                    final Integer userId) {
        RequestCommaValue requestInclude = new RequestCommaValue();
        requestInclude.add(AppConstants.TAG_biddable);
        return retrofit.create(NetworkService.class).getBidList(
                mAppUtil.getAccessToken(),
                AppConstants.NUMBER_PRE_PAGE,
                pageIndex,
                userId,
                requestInclude.filterValue()
        );

    }

    // follow a user
    public Call<Void> postFollowUser(final Integer id, Boolean isFollow) {
        if (isFollow) {
            return retrofit.create(NetworkService.class).postFollow(
                    mAppUtil.getAccessToken(),
                    id);
        } else {
            return retrofit.create(NetworkService.class).postUnFollow(
                    mAppUtil.getAccessToken(),
                    id);
        }

    }


    // get saleyard by id
    public Call<EntitySaleyard> getSaleyardById(final Integer id) {

        IncludeFilter includeFilter = new IncludeFilter<>(AppConstants.TAG_user,
                AppConstants.TAG_users,
                AppConstants.TAG_saleyard_dash_category,
                AppConstants.TAG_saleyard_dash_status,
                AppConstants.TAG_saleyard_dash_areas,
                AppConstants.TAG_saleyard_dash_areas_dot_saleyard_dash_assets,
                AppConstants.TAG_saleyard_dash_areas_dot_saleyard_dash_assets_dot_media,
                AppConstants.TAG_users,
                AppConstants.TAG_user_dot_organisation,
                AppConstants.TAG_users_dot_organisation,
                AppConstants.TAG_media
        );

        return retrofit.create(NetworkService.class).getSaleyardById(
                mAppUtil.getAccessToken(),
                id,
                includeFilter.filterValue(),
                AppConstants.TAG_has_liked);
    }

    // get user detail by id
    public Call<User> getUserById(final Integer userId) {
        RequestCommaValue includeValue = new RequestCommaValue(AppConstants.TAG_roles, AppConstants.TAG_organisation, AppConstants.TAG_producers);
        RequestCommaValue appendValue = new RequestCommaValue(AppConstants.TAG_follower_count,
                AppConstants.TAG_is_authed_user_following);
        if (userId != null) {
            return retrofit.create(NetworkService.class).getUserById(mAppUtil.getAccessToken(),
                    userId,
                    includeValue.toString(),
                    appendValue.toString());
        } else {
            includeValue.add(AppConstants.TAG_permissions);
            return retrofit.create(NetworkService.class).getUserByToken(mAppUtil.getAccessToken(),
                    includeValue.toString(),
                    appendValue.toString());
        }
    }


    public Call<Post> createPost(final String text, final String imageUrl) {
        List<String> videoMediaList = new ArrayList<>();
        if (imageUrl != null) {
            videoMediaList.add(imageUrl);
        }
        List<MultipartBody.Part> output = new ArrayList<>();
        if (videoMediaList != null && videoMediaList.size() > 0) {
            for (int i = 0; i < videoMediaList.size(); i++) {
                String url = videoMediaList.get(i);
                if (url != null && !ActivityUtils.isStartWithHttp(url)) {
                    File imgFile = new File(url);
                    if (imgFile.exists() && imgFile.isFile()) {
                        RequestBody body = RequestBody.create(MediaType.parse(AppConstants.HEADER_MULTIPART_IMAGE), imgFile);
                        output.add(MultipartBody.Part.createFormData("photo", imgFile.getName(), body));
                    }
                }
            }
        }
        return retrofit.create(NetworkService.class).postNewPost(mAppUtil.getAccessToken(),
                RequestBody.create(MediaType.parse("text/plain"), text), output);
    }

    public Call<Void> deleteCreditCart(final Integer userId, final Integer sourceId) {
        return retrofit.create(NetworkService.class).deletePaymentCard(mAppUtil.getAccessToken(),
                userId, sourceId);
    }


    public Call<Void> deleteMarketReport(final Integer reportId) {
        return retrofit.create(NetworkService.class).deleteMarketReport(mAppUtil.getAccessToken(),
                reportId);
    }


    public Call<Void> deleteSaleyard(final Integer saleyardId) {
        return retrofit.create(NetworkService.class).deleteSaleyard(mAppUtil.getAccessToken(),
                saleyardId);
    }


    public Call<Void> deletePost(final Integer postId) {
        return retrofit.create(NetworkService.class).deletePost(mAppUtil.getAccessToken(),
                postId);
    }


    public Call<ResVersion> getVersion() {
        return retrofit.create(NetworkService.class).getVersion();
    }


    public Call<ResAdvertisement> getMyAdvertisement(final Integer pageNum, final ADType adType) {
        if (mAppUtil.getUserType() == UserType.AGENT) {
            return retrofit.create(NetworkService.class).getMyAdvertismentAgent(mAppUtil.getAccessToken(),
                    mAppUtil.getUserId(),
                    //"image",
                    adType.ordinal() + 1,
                    AppConstants.NUMBER_PRE_PAGE,
                    pageNum,
                    AppConstants.TAG_include_my_ad);
        } else {
            return retrofit.create(NetworkService.class).getMyAdvertismentProducer(mAppUtil.getAccessToken(),
                    mAppUtil.getUserId(),
                    //"image",
                    adType.ordinal() + 1,
                    AppConstants.NUMBER_PRE_PAGE,
                    pageNum,
                    AppConstants.TAG_include_my_ad);
        }
    }


    public Call<List<EntitySaleyard>> getSaleyards(final Integer userId, final Integer statusId, final String type) {
        return retrofit.create(NetworkService.class).getSaleyards(mAppUtil.getAccessToken(), mIncludeParam.getmSaleyardInclude().filterValue(),
                userId, statusId, type);
    }

    public Call<ResPagerSaleyard> getPagerSaleyards(final Integer userId, final Integer statusId, final String type) {
        return retrofit.create(NetworkService.class).getPagerSaleyards(mAppUtil.getAccessToken(), mIncludeParam.getmSaleyardInclude().filterValue(),
                userId, statusId, type, AppConstants.TAG_sort_minus_starts_at);
    }

    public Call<Organisation> getOrganisation(final Integer id) {
        IncludeFilter includeFilter = new IncludeFilter<>(AppConstants.TAG_users);
        return retrofit.create(NetworkService.class).getOrganisationById(id, includeFilter.toString());
    }

    public Call<List<Agent>> getAgent() {
        return retrofit.create(NetworkService.class).getAgent();
    }

    public Call<List<PregnancyEntity>> getPregnancyStatus() {
        return retrofit.create(NetworkService.class).getPregnancyStatus();
    }


    public Call<List<GenderEntity>> getGenders() {
        return retrofit.create(NetworkService.class).getGender();
    }

    public Call<List<ResHerdLive>> getLiveSaleyardStreams(final Integer islive) {

        return retrofit.create(NetworkService.class).getLiveSaleyardStreams(mAppUtil.getAccessToken(), islive);
    }

    public Call<List<ResHerdLive>> getLiveSaleyardStreamsUpcoming(final String afterDate) {

        return retrofit.create(NetworkService.class).getLiveSaleyardStreamsUpcoming(mAppUtil.getAccessToken(), afterDate);
    }

    public Call<List<ResHerdLive>> getLiveSaleyardStreamsPast(final String beforeDate) {

        return retrofit.create(NetworkService.class).getLiveSaleyardStreamsPast(mAppUtil.getAccessToken(), beforeDate);
    }

    public Call<List<ResHerdLive>> getLiveSaleyardStreamsMy(final String userID) {

        return retrofit.create(NetworkService.class).getLiveSaleyardStreamsMy(mAppUtil.getAccessToken(), userID);
    }

    public Call<ResSaleyardStreamDetails> getSingleSaleyardStream(final String itemId) {

        return retrofit.create(NetworkService.class).getSaleyardStreamById(mAppUtil.getAccessToken(), itemId,AppConstants.TAG_MediaLiveChannel_Vods);
    }
    public Call<ResAWSMedialiveChannel> getAwsMedialiveChannel(final String awsMediaId) {

        return retrofit.create(NetworkService.class).getAwsMedialiveChannel(mAppUtil.getAccessToken(), awsMediaId);
    }
    public Call<ResAWSMedialiveChannel> startSaleyardStream(final String awsMediaId) {

        return retrofit.create(NetworkService.class).startSaleyardStream(mAppUtil.getAccessToken(), awsMediaId);
    }
    public Call<ResAWSMedialiveChannel> getMediaLiveInput(final String mediaInputId) {

        return retrofit.create(NetworkService.class).getMediaLiveInput(mAppUtil.getAccessToken(), mediaInputId);
    }
    public Call<ResAWSMedialiveChannel> stopSaleyardStream(final String awsMediaId) {

        return retrofit.create(NetworkService.class).stopSaleyardStream(mAppUtil.getAccessToken(), awsMediaId);
    }

    public Call<ResHerdLive> createSaleyardStream(
            final String name, final String streamDate) {
        return retrofit.create(NetworkService.class).createSaleyardStream(mAppUtil.getAccessToken(),
                name, streamDate);

    }

    public interface IRequestCallback<T> {
        void onResponse(T model);

        void onFailure(int responseCode, String msg, Throwable t);

    }

    public static class RequestServiceCallback<T> implements Callback {
        final private IRequestCallback<T> callback;

        RequestServiceCallback(IRequestCallback<T> callback) {
            this.callback = callback;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public void onResponse(Call call, Response response) {
            if (callback != null) {
                if (response.code() >= 200 && response.code() < 300) {
                    //noinspection unchecked
                    callback.onResponse((T) response.body());
                } else {

                    String msg = response.message();
                    try {
                        msg = response.errorBody().string();
                        try {
                            JSONObject jsonObject = new JSONObject(msg);
                            msg = jsonObject.getString("message");
                        } catch (JSONException e) {
                        }
                    } catch (IOException e) {
                    }
                    callback.onFailure(response.code(), msg, null);
                }
            }
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public void onFailure(Call call, Throwable t) {
            if (callback != null) {
                callback.onFailure(-1, "Something is wrong", t);
            }
        }
    }

}
