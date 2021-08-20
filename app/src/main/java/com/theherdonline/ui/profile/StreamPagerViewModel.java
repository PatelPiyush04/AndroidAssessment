package com.theherdonline.ui.profile;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import java.util.List;

import com.theherdonline.app.AppConstants;
import com.theherdonline.db.entity.ResStream;
import com.theherdonline.db.entity.StreamItem;
import com.theherdonline.db.entity.StreamType;
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.ui.general.SearchFilter;
import retrofit2.Call;

public class StreamPagerViewModel extends PagerViewModel<StreamItem, ResStream> {

    final Integer mUserId;
    final Integer mOfUserId;
    final Integer mOrganisationId;

    final StreamType mStreamType;

    public StreamPagerViewModel(@NonNull Application application, Integer mUserId, Integer mOfUser, Integer organisationId, StreamType streamType) {
        super(application);
        this.mUserId = mUserId;
        this.mOfUserId = mOfUser;
        this.mStreamType = streamType;
        this.mOrganisationId = organisationId;
        SearchFilter filter = new SearchFilter();
        filter.setUser_id(mUserId);
        filter.setStreamType(mStreamType);
        filter.setOf_user(mOfUser);
        filter.setOrganisation_id(mOrganisationId);


        mRepository.setFilter(filter);
        RequestCommaValue requestInclude = new RequestCommaValue(AppConstants.TAG_streamable,
                AppConstants.TAG_user,
                AppConstants.TAG_streamable_media,
                AppConstants.TAG_streamable_images);
        mRepository.setRequestInclude(requestInclude);
    }


    @Override
    public List<StreamItem> convertResponse2ItemList(ResStream resStream) {
        return resStream.getData();
    }

    @Override
    public Call<ResStream> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
        return mRetrofitClient.getStream(currentPage,filter,requestInclude);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Integer mUserId;
        private final Integer mOfUserId;        // for following
        private final Integer mOrganisationId;        // for following


        private final StreamType mStreamType;

        public Factory(@NonNull Application application, Integer userId, Integer ofUserId, Integer orId, StreamType streamType) {
            mApplication = application;
            mUserId = userId;
            mStreamType = streamType;
            mOfUserId = ofUserId;
            mOrganisationId = orId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new StreamPagerViewModel(mApplication, mUserId, mOfUserId, mOrganisationId,  mStreamType);
        }
    }

}
