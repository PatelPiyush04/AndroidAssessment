package com.theherdonline.app;

import com.theherdonline.BuildConfig;

public class AppConstants {

    public static final String APP_NAME = "theherd";

    public static final String SERVER_URL_DEV = BuildConfig.CONFIG_API_SERVER;
    public static final String STRIPE_PUBLIC_KEY = BuildConfig.CONFIG_STRIPE_PUBLIC_KEY;
    public static final String CHATKIT_TOKEN = BuildConfig.CONFIG_CHATKIT_KEY_INSTANCE_LOCATOR;
    public static final String GOOGLE_CLOUD_KEY = BuildConfig.CONFIG_GOOGLE_API_KEY;
    public static final Integer LOGIN_CLIENT_ID = BuildConfig.CONFIG_CLIENT_ID;
    public static final String LOGIN_CLIENT_SECRET = BuildConfig.CONFIG_CLIENT_SECRET;


    public static final String LOGIN_GRAND_TYPE = "password";
    public static final String LOGIN_GRAND_TYPE_social = "social";
    public static final String LOGIN_PROVIDER_google = "google";
    public static final String LOGIN_PROVIDER_facebook = "facebook";


    public static final String TAG_auction_plus_url = "auction_plus";


    public static final String HERD_HOME_PAGE = BuildConfig.CONFIG_API_SERVER;//"https://herdonline.com.au/";

    public static final String IMAGE_URL = "https://s3-ap-southeast-2.amazonaws.com/theherdonline";
    public static final String SERVER_URL_FORGOT_PASSWORD = "password/reset";
    public static final String HaveYourHerdURL = HERD_HOME_PAGE + "have-you-herd"; //"https://herdonline.com.au/have-you-herd";
    public static final String WantedLiveStockURL = HERD_HOME_PAGE + "sale-alert";//"https://herdonline.com.au/sale-alert";
    public static final String MARKET_REPORT_SHARE = HERD_HOME_PAGE + "market-reports/";//"https://herdonline.com.au/sale-alert";

    //public static final String CHATKIT_TOKEN = "v1:us1:912d291c-4c34-4556-91c8-9ced0cd8b4f2";
    public static final String STRIPE_PRIVATE_KEY = "sk_test_3S2vXj9UzJVa0RbCVp8TXdeo";
    public static final int VAL_READ_TIMEOUT = 30; // seconds
    public static final int VAL_CONN_TIMEOUT = 30; // seconds

    // STRIPE_PRIVATE_KEY=sk_live_rOgSqvyJiAaMzjLhK923JkC5
    // STRIPE_PUBLIC_KEY=pk_live_ZKfR56zAoq34BdhLXvohi690


    // STRIPE_PRIVATE_KEY=sk_test_3S2vXj9UzJVa0RbCVp8TXdeo
    // STRIPE_PUBLIC_KEY=pk_test_ZSAOCaEujXFIcFsj392XCdKI

/*
    for dev
    public static final String GOOGLE_CLOUD_KEY = "AIzaSyApvKxN51whPX1Pwp_FP-2OrwXJ1l4VBHg"; //"AIzaSyC8gmuMHptCGHIBik-ulohzw0byBpmWxAA";

*/
/*

    public static final String GOOGLE_CLOUD_KEY = "AIzaSyCDR4xdq7FMtRyx-LNucWQie38xTlwtZyU"; //"AIzaSyC8gmuMHptCGHIBik-ulohzw0byBpmWxAA";
*/


    //for production
    public static final Integer ACTIVITY_REQUEST_LOGIN = 10001;
    public static final Integer ACTIVITY_REQUEST_REGISTER = 10002;
    public static final Integer NUMBER_PRE_PAGE = 50;
    public static final String HEADER_ACCEPT = "Accept: application/json";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_MULTIPART_IMAGE = "image/*";
    public static final String HEADER_MULTIPART_AUDIO = "audio/*";
    public static final String HEADER_MULTIPART_video = "video/*";
    public static final String TAG_include = "include";
    public static final String TAG_image = "image";
    public static final String TAG_images = "images";
    public static final String TAG_is_primary = "is_primary";
    public static final String TAG_avatar_location = "avatar_location";
    public static final String TAG_has_liked = "has_liked";
    public static final String TAG_bid_count = "bid_count";
    public static final String TAG_saleyard = "saleyard";
    public static final String TAG_my_dash_bid = "my-bid";
    public static final String TAG_append = "append";
    public static final String TAG_follower_count = "follower_count";
    public static final String TAG_is_authed_user_following = "is_authed_user_following";
    public static final String TAG_source_id = "source_id";
    public static final String TAG_exp_month = "exp_month";
    public static final String TAG_exp_year = "exp_year";
    public static final String TAG_client_secret = "client_secret";
    public static final String TAG_status = "status";
    public static final String TAG_brand = "brand";
    public static final String TAG_last_four = "last_four";
    public static final String TAG_three_d_secure = "three_d_secure";
    public static final String TAG_type = "type";
    public static final String TAG_media = "media";
    public static final String TAG_path = "path";
    public static final String TAG_video = "video";
    public static final String TAG_attachments = "attachments";
    public static final String TAG_per_page = "per_page";
    public static final String TAG_filter_animal_id = "filter[animal_id]";
    public static final String TAG_filter_breed_id = "filter[breed_id]";
    public static final String TAG_filter_distance = "filter[distance]";
    public static final String TAG_filter_where_like = "filter[where_like]";
    public static final String TAG_filter_filter_around = "filter[around_lat_lng]";
    public static final String TAG_filter_between_price = "filter[between_price]";
    public static final String TAG_filter_between_wight = "filter[between_weight]";
    public static final String TAG_filter_between_age = "filter[between_age]";
    public static final String TAG_filter_where_between = "filter[where_between]";
    public static final String TAG_filter_around_lat_lng = "filter[around_lat_lng]";
    public static final String TAG_filter_gender_id = "filter[gender_id]";
    public static final String TAG_filter_pregnancy_status_id = "filter[pregnancy_status_id]";
    public static final String TAG_filter_sex = "filter[sex]";
    public static final String TAG_filter_quantity = "filter[quantity]";
    public static final String TAG_filter_age = "filter[age]";
    public static final String TAG_filter_weighted = "filter[weighed]";
    public static final String TAG_filter_pregnancy_status = "filter[pregnancy_status]";
    public static final String TAG_filter_dentition = "filter[dentition]";
    public static final String TAG_filter_mounted = "filter[mouthed]";
    public static final String TAG_filter_eu = "filter[eu]";
    public static final String TAG_filter_msa = "filter[msa]";
    public static final String TAG_filter_lpa = "filter[lpa]";
    public static final String TAG_filter_orgnic = "filter[organic]";
    public static final String TAG_filter_pcas = "filter[pcas]";
    public static final String TAG_filter_ad_status_id = "filter[advertisement_status_id]";
    public static final String TAG_filter_ls_cat_id = "filter[livestock_category_id]";
    public static final String TAG_filter_ls_sub_cat_id = "filter[livestock_sub_category_id]";
    public static final String TAG_sub_category = "sub-category";
    public static final String TAG_filter_ls_classification_id = "filter[livestock_classification_id]";
    public static final String TAG_filter_orgisation_id = "filter[organisation_id]";
    public static final String TAG_filter_auctions_plus = "filter[auctions_plus]";
    public static final String TAG_filter_age_type = "filter[age_type]";
    public static final String TAG_filter_agent_id = "filter[agent_id]";
    public static final String TAG_filter_producer_id = "filter[producer_id]";
    public static final String TAG_filter_media_collection_name = "filter[media.collection_name]";
    public static final String TAG_include_my_ad = "producer,agent,media,category,advertisement-status";
    public static final String TAG_include_my_chat = "my_chat";
    public static final String TAG_saleyard_filter_user_id = "[filter]user_id";
    public static final String TAG_filter_name = "filter[name]";
    public static final String TAG_filter_type = "filter[type]";
    public static final String TAG_filter_description = "filter[description]";
    public static final String TAG_filter_starts_at = "filter[starts_at]";
    public static final String TAG_filter_saleyard_status_id = "filter[saleyard_status_id]";
    public static final String TAG_filter_saleyard_category_id = "filter[saleyard_category_id]";
    public static final String TAG_filter_user_id = "filter[user_id]";
    public static final String TAG_filter_of_user = "filter[of_user]";
    public static final String TAG_filter_of_organisation = "filter[of_organisation]";
    public static final String TAG_biddable = "biddable";
    public static final String TAG_sort = "sort";
    public static final String TAG_sort_minus_created_at = "-created_at";
    public static final String TAG_sort_minus_started_at = "-start_date";
    public static final String TAG_sort_created_at = "created_at";
    public static final String TAG_sort_minus_end_date = "-end_date";
    public static final String TAG_MediaLiveChannel_Vods = "medialive_channel,vods";
    public static final String TAG_sort_starts_at = "starts_at";
    public static final String TAG_sort_minus_starts_at = "-" + TAG_sort_starts_at;
    public static final String TAG_saleyard_include = "user.organisation,users.organisation,saleyard-category,saleyard-status,saleyard-areas.saleyard-assets.media";
    public static final String TAG_roles = "roles";
    public static final String TAG_organisation = "organisation";
    public static final String TAG_saleyard_api_include = "saleyard_api_include";
    public static final String TAG_livestocks_api_include = "livestocks_api_include";
    public static final String TAG_saleyard_by_id_api_include = "saleyard_by_id_api_include";
    public static final String TAG_streamType_livestock = "Livestock";
    public static final String TAG_streamType_post = "Post";
    public static final String TAG_streamType_saleyard = "Saleyard";//"EntitySaleyard";
    public static final String TAG_streamable = "streamable";
    public static final String TAG_streamable_media = "streamable.media";
    public static final String TAG_streamable_images = "streamable.images";
    public static final String TAG_streamable_video = "streamable.videos";
    public static final String TAG_chats = "chats";
    public static final String TAG_producer = "producer";
    public static final String TAG_producers = "producers";
    public static final String TAG_permissions = "permissions";
    public static final String TAG_agent = "agent";
    public static final String TAG_category = "category";
    public static final String TAG_advertisement_dash_status = "advertisement-status";
    public static final String TAG_user_dot_organisation = "user.organisation";
    public static final String TAG_users_dot_organisation = "users.organisation";
    public static final String TAG_agent_dot_organistion = "agent.organistion";
    public static final String TAG_videos = "videos";
    public static final String TAG_pdfs = "pdfs";
    public static final String TAG_pdf = "pdf";
    public static final String TAG_market_report_attachment = "market_report_attachments";
    public static final String TAG_saleyard_dash_category = "saleyard-category";
    public static final String TAG_saleyard_dash_status = "saleyard-status";
    public static final String TAG_user = "user";
    public static final String TAG_users = "users";
    public static final String TAG_saleyard_dash_areas = "saleyard-areas";
    public static final String TAG_saleyard_dash_areas_dot_saleyard_dash_assets = "saleyard-areas.saleyard-assets";
    public static final String TAG_saleyard_dash_areas_dot_saleyard_dash_assets_dot_media = "saleyard-areas.saleyard-assets.media";
    public static final String TAG_page = "page";
    public static final String FRAGMENT_DIALOG = "dialog";
    public static final Integer GLB_min_distance = 0;
    public static final Integer GLB_max_distance = 1000;
    public static final String TEXT_PLAIN = "text/plain";
    public static final String TEXT_URLENCODED = "application/x-www-form-urlencoded";
    public static final Integer SERVER_ERROR_401 = 401;
    public static final Integer DATA_ERROR_END = 1001;
    public static final Integer DATA_ERROR_UPLOADIMAGE = 1002;
    public static final Integer DATA_ERROR_UNKNOW = 1003;
    public static final String DATA_ERROR_END_INOF = "No more data";
    public static final String USER_TYPE_AGENT = "agent";
    public static final String USER_TYPE_PRODUCER = "producer";
    public static final String TEST_TOKEN = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQ3M2IyNmZlZjUzMTIxYjQ2YjAxOTk4YzEwY2MzYzBkN2ZhNjllYTE5MzU3MjcyNzQ3YTc0YTQxMTA2MjAzMTQ3OTk5YTc2YmZmOTQ1MDM4In0.eyJhdWQiOiIzIiwianRpIjoiZDczYjI2ZmVmNTMxMjFiNDZiMDE5OThjMTBjYzNjMGQ3ZmE2OWVhMTkzNTcyNzI3NDdhNzRhNDExMDYyMDMxNDc5OTlhNzZiZmY5NDUwMzgiLCJpYXQiOjE1NDc2ODE3MTgsIm5iZiI6MTU0NzY4MTcxOCwiZXhwIjo0NzAzMzU1MzE4LCJzdWIiOiI0Iiwic2NvcGVzIjpbXX0.gSLd_qn7JqiD-nKE3r1WaNCBJdzO5WkbsVyKV1bHfg7IouvoOIQF1W5qjZlcHwkmXVB-2ShoHuesK-P5nT7ac1GVVRel_XkZAuyIEs5z_YghviEVzSWp9agEJZbhMNpCwZTwzhaGVrOB5ObkMMFtg6MVIbvR5IbixF3sxbQ0dfhDFrBcLVdcxj_VyaLs3080QwNLQuKfK0gJyOpVLgopQddef5iQvuCdZrpx51RcdQHHknu8tyNsQ83TBZiZSvRK3QpqF_-Vexjp6gByRIDnvbVyiksaLkNwb3DxRQbl8srCx9KlAqApv889OSlYVyfFzL_ElHTeZ5UKh0fvPtqcPz6eb3AJ6sRNniZaFxLQFEGDHpwfexqzr7bgqD4E7actm3jTIxoIAGtwG0iXfz30Z7GFe2P27yYpRvh3_49IZgbjz5jPiZ4L7433Y-h88RuoI6EfMyPaJ745_f1Ixx6XHP6QKdQm2t6-tlOC9yWZnUMyuRkBE-TBFPaQYjU9VIMrCTHq6ZYmE87LcMFnjAfuqvxXACgs4lhPLzXeM_HsTxnb9V97TUhcrS8BfMvlfxZAa2yyGWixb3cQ4ql0BZRonL4TNA9X7BkhquLkmGFOxuvPzM1NcR5704AdYzt3Lpvjs2ibe2K4pxdiicApMNUZIU1gikuuMuhSAR5EFCmTGu4";
    public static final Integer DEFAULT_DURATION_DAY = 14;
    public static final String ERROR_SERVER_CONNECTION = "Connection Error. Please check your internet connection.";
    public static final String TAG_SALEYARD_TYPE_PRIME = "Prime";
    public static final Boolean defaultContactByEmail = true;
    static public String TAG_prime_permission = "edit prime";
    static public String TAG_createLivestream_permission = "create livestream";
    static public String TAG_STATE_RUNNING ="running";
    public static String SERVER_URL = SERVER_URL_DEV;
    public static int AUTOCOMPLETE_REQUEST_CODE_SALEYARD = 2001;
    public static int AUTOCOMPLETE_REQUEST_CODE_LIVESTOCK = 2002;
    public static int AUTOCOMPLETE_REQUEST_CODE_CREATE_LIVESTOCK = 2003;
    public static int VIDEO_LENGTH = 60;
    public static int VIDEO_QUALITY = 0;
    public static int ACTIVITY_TAKE_VIDEO = 108;
    public static int ACTIVITY_PERMISSION_LOCATION = 1001;
    public static int ACTIVITY_PERMISSION_STORAGE_POST_LIVESTOCK = 1004;
    public static int ACTIVITY_PERMISSION_STORAGE = 1002;
    public static int ACTIVITY_PERMISSION_STORAGE_BANNER = 1003;
    public static int ACTIVITY_PERMISSION_STORAGE_AVATAR = 1004;
    public static int ACTIVITY_POST_LIVESTOCK = 1004;
    public static int ACTIVITY_PERMISSION_AUDIO = 1005;
    public static int ACTIVITY_PICK_IMAGE_POST = 140;
    public static int ACTIVITY_TAKE_PHOTO_POST = 141;
    public static int PICKUP_PHOTO_POST_ACTIVITY_CODE = 3001;
    public static int PICKUP_PHOTO_POST_PERMISSION_CODE = 3002;
    public static int PICKUP_PHOTO_AVATAR_ACTIVITY_CODE = 3003;
    public static int PICKUP_PHOTO_AVATAR_PERMISSION_CODE = 3004;
    public static int ACTIVITY_CODE_POST_TAKE_PHOTO = 3010;
    public static int ACTIVITY_CODE_POST_CHOOSE_PHOTO = 3011;
    public static int ACTIVITY_CODE_POST_CHOOSE_PHOTO_PERMISSION = 3012;
    public static int ACTIVITY_CODE_AVARTAR_TAKE_PHOTO = 3020;
    public static int ACTIVITY_CODE_AVARTAR_CHOOSE_PHOTO = 3021;
    public static int ACTIVITY_CODE_AVARTAR_CHOOSE_PHOTO_PERMISSION = 3022;
    public static int ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO = 3030;
    public static int ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_VIDEO = ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO + 1;
    public static int ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_VIDEO = ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO + 2;
    public static int ACTIVITY_CODE_MARKET_REPORT_UPLOAD_VIDEO_PERMISSION = ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO + 3;
    public static int ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_PHOTO = ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO + 4;
    public static int ACTIVITY_CODE_MARKET_REPORT_UPLOAD_CHOOSE_PDF = ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO + 5;
    public static int ACTIVITY_CODE_MARKET_REPORT_UPLOAD_PHOTO_PERMISSION = ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO + 6;
    public static int ACTIVITY_CODE_MARKET_REPORT_UPLOAD_FILE_PERMISSION = ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO + 6;
    public static int ACTIVITY_CODE_POST_LIVESTOCK_TAKE_PHOTO = 3060;
    public static int ACTIVITY_CODE_POST_LIVESTOCK_TAKE_VIDEO = ACTIVITY_CODE_POST_LIVESTOCK_TAKE_PHOTO + 1;
    public static int ACTIVITY_CODE_POST_LIVESTOCK_CHOOSE_MEDIA = ACTIVITY_CODE_POST_LIVESTOCK_TAKE_PHOTO + 2;
    public static int ACTIVITY_CODE_POST_LIVESTOCK_CHOOSE_MEDIA_PERMISSION = ACTIVITY_CODE_POST_LIVESTOCK_TAKE_PHOTO + 3;
    public static int ACTIVITY_CODE_POST_SALEYARD_TAKE_PHOTO = 3050;
    public static int ACTIVITY_CODE_POST_SALEYARD_TAKE_VIDEO = ACTIVITY_CODE_POST_SALEYARD_TAKE_PHOTO + 1;
    public static int ACTIVITY_CODE_POST_SALEYARD_CHOOSE_MEDIA = ACTIVITY_CODE_POST_SALEYARD_TAKE_PHOTO + 2;
    public static int ACTIVITY_CODE_POST_SALEYARD_CHOOSE_MEDIA_PERMISSION = ACTIVITY_CODE_POST_SALEYARD_TAKE_PHOTO + 3;
    public static int ACTIVITY_SALEYEARD_POST_PDF_PHOTO_TAKE = 3040;
    public static int ACTIVITY_SALEYEARD_POST_PDF_PHOTO_PICK = 3041;
    public static int ACTIVITY_SALEYEARD_POST_PDF_PERMISSION = 3042;


}
