package com.theherdonline.api;

/**
 * Created by jackttc on 21/5/18.
 */

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.MainThread;

import java.util.Objects;

import com.theherdonline.app.AppExecutors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by freddie on 16/05/18.
 */

public abstract class NetworkBoundAppendData<ResultType,RequestType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundAppendData(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        this.appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                setValue(Resource.loading((null)));
                Call<RequestType> responseCall = createRequest();
                responseCall.enqueue(new Callback<RequestType>() {
                    @Override
                    public void onResponse(Call<RequestType> call, Response<RequestType> response) {
                        if (call != null) {
                            if (response.code() == 200)
                            {
                                ResultType reportData = convertResponse2Report(response.body());
                                appExecutors.diskIO().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            updateDb(reportData);
                                        }
                                });
                                setValue(Resource.success((reportData)));
                            }
                            else
                            {
                                String message = NetworkUtil.parseErrorResponse(response.errorBody());
                                result.setValue(Resource.error(response.code(),
                                        String.format("Server Error(%d) \n%s",response.code(), message), null));}
                        }
                    }
                    @Override
                    public void onFailure(Call<RequestType> call, Throwable t) {
                        result.setValue(Resource.error(t.getMessage(),null));
                    }
                });

            }
        });
    }

    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals(result.getValue(), newValue)) {
            result.postValue(newValue);
        }
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }
    protected abstract ResultType convertResponse2Report(RequestType response);
    protected abstract void updateDb(ResultType resultType);
    protected abstract Call<RequestType>  createRequest();
}
