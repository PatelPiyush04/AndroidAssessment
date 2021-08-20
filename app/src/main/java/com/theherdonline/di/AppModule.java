package com.theherdonline.di;

import android.app.Application;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;


import com.theherdonline.BuildConfig;
import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.api.IncludeFilter;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    @Provides
    @Singleton
    static AppExecutors provideAppExecutors()
    {
        return new AppExecutors();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit()
    {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BASIC);

        return  new Retrofit.Builder()
                .baseUrl(AppConstants.SERVER_URL)
                .client(new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .readTimeout(AppConstants.VAL_READ_TIMEOUT, TimeUnit.SECONDS)
                        .connectTimeout(AppConstants.VAL_CONN_TIMEOUT, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())

                .build();
    }




    @Provides
    @Singleton
    static AppDatabase provideDBClient(Application application,AppExecutors executors)
    {
        return Room.databaseBuilder(application, AppDatabase.class, AppConstants.APP_NAME)
                .allowMainThreadQueries()
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        executors.diskIO().execute(() -> {
                           // AppDatabase database = AppDatabase.getInstance(appContext, executors);
                           // database.setDatabaseCreated();
                        });
                    }
                })
                .fallbackToDestructiveMigration()
                .build();
                //.allowMainThreadQueries().build();
    }

    @Provides
    @Singleton
    @Named(AppConstants.TAG_saleyard_api_include)
    static IncludeFilter<String> provideSaleyardsIncludeParameters()
    {
        return new IncludeFilter<>(AppConstants.TAG_user_dot_organisation,
                AppConstants.TAG_saleyard_dash_category,
                AppConstants.TAG_saleyard_dash_status,
                AppConstants.TAG_saleyard_dash_areas_dot_saleyard_dash_assets_dot_media);
    }


    @Provides
    @Singleton
    static PlacesClient providePlacesClient(Context context)
    {
        Places.initialize(context, AppConstants.GOOGLE_CLOUD_KEY);
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(context);
        return placesClient;
    }




    @Provides
    @Singleton
    @Named(AppConstants.TAG_livestocks_api_include)
    static IncludeFilter<String> provideLivestocksIncludeParameters()
    {
        return new IncludeFilter<>(AppConstants.TAG_producer,
                AppConstants.TAG_agent,
                AppConstants.TAG_media,
                AppConstants.TAG_category,
                AppConstants.TAG_advertisement_dash_status
                );
    }

    @Provides
    @Singleton
    @Named(AppConstants.TAG_saleyard_by_id_api_include)
    static IncludeFilter<String> provideSaleyardByIdIncludeParameters()
    {
        return new IncludeFilter<>(AppConstants.TAG_user,
                AppConstants.TAG_users,
                AppConstants.TAG_saleyard_dash_category,
                AppConstants.TAG_saleyard_dash_status,
                AppConstants.TAG_saleyard_dash_areas,
                AppConstants.TAG_saleyard_dash_areas_dot_saleyard_dash_assets,
                AppConstants.TAG_saleyard_dash_areas_dot_saleyard_dash_assets_dot_media
        );
    }






}
