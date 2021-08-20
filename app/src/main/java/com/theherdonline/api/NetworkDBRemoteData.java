package com.theherdonline.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkDBRemoteData<ResultType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkDBRemoteData(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        this.appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                ResultType dbObject = getDataFromDB();
                if (dbDataValid(dbObject))
                {
                    setValue(Resource.success((dbObject)));
                }
                Call<ResultType> responseCall = createRequest();
                setValue(Resource.loading(null));
                responseCall.enqueue(new Callback<ResultType>() {
                    @Override
                    public void onResponse(Call<ResultType> call, Response<ResultType> response) {
                        if (call != null) {
                            if (response.isSuccessful())
                            {
                                if (response.body() != null && !response.body().equals(dbObject))
                                {
                                    interDataToDB(response.body());
                                    setValue(Resource.success((response.body())));
                                }
                            }
                            else
                            {
                                String message = NetworkUtil.parseErrorResponse(response.errorBody());
                                result.setValue(Resource.error(response.code(),message, null));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ResultType> call, Throwable t) {
                        String hostname = "";
                        try {
                            URL url = new URL(AppConstants.SERVER_URL_DEV);
                            hostname = url.getHost();
                        }
                        catch (MalformedURLException e1)
                        {
                            hostname = "";
                        }
                        if (t.getMessage().contains(hostname))
                        {
                            result.setValue(Resource.error(AppConstants.ERROR_SERVER_CONNECTION + "\n" + t.getMessage(), null));
                        }
                        else
                        {
                            result.setValue(Resource.error(t.getMessage(), null));
                        }
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
    protected abstract Call<ResultType>  createRequest();
    protected abstract ResultType getDataFromDB();
    protected abstract void interDataToDB(@NonNull ResultType item);
    protected Boolean dbDataValid(ResultType items){return items != null; }

}
