package com.theherdonline.api;

/**
 * Created by jackttc on 21/5/18.
 */

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.util.Objects;


import com.theherdonline.app.AppExecutors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by freddie on 16/05/18.
 */

public abstract class NetworkBoundData<ResultType,RequestType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundData(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        this.appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                final ResultType dbData = loadFromDb();
                if (isValidDbData(dbData))
                {
                    setValue(Resource.success(dbData));
                }
                else
                {
                    setValue(Resource.loading((dbData)));
                }
                Call<RequestType> responseCall = createRequest();
                responseCall.enqueue(new Callback<RequestType>() {
                    @Override
                    public void onResponse(Call<RequestType> call, Response<RequestType> response) {
                        if (call != null) {
                            if (response.code() >= 200 && response.code() < 300)
                            {
                                processResponse(response.body());
                                ResultType reportData = convertResponse2Report(response.body());
                                processReport(reportData);
                                if (!reportData.equals(dbData))
                                {
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
                                    setValue(Resource.nochange(reportData));
                                }
                            }
                            else
                            {
                                String message = NetworkUtil.parseErrorResponse(response.errorBody());
                                result.setValue(Resource.error(response.code(),message, null));}
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

    protected abstract void processReport(@NonNull ResultType item);

    protected abstract void processResponse(@NonNull RequestType item);

    protected abstract ResultType loadFromDb();

    protected abstract boolean isValidDbData(@NonNull ResultType data);

    protected abstract void updateDb(ResultType resultType);


    protected abstract Call<RequestType>  createRequest();
}
