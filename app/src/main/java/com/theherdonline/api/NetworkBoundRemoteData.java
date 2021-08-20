package com.theherdonline.api;

/**
 * Created by jackttc on 21/5/18.
 */

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by freddie on 16/05/18.
 */

public abstract class NetworkBoundRemoteData<ResultType,RequestType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkBoundRemoteData(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        this.appExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                Call<RequestType> responseCall = createRequest();
                setValue(Resource.loading(null));
                responseCall.enqueue(new Callback<RequestType>() {
                    @Override
                    public void onResponse(Call<RequestType> call, Response<RequestType> response) {
                        if (call != null) {
                            if (response.isSuccessful())
                            {
                                processResponse(response.body());
                                ResultType reportData = convertResponse2Report(response.body());
                                processReport(reportData);
                                setValue(Resource.success((reportData)));
                            }
                            else
                            {
                                String message = NetworkUtil.parseErrorResponse(response.errorBody());
                                result.setValue(Resource.error(response.code(),message, null));
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<RequestType> call, Throwable t) {
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
    protected abstract ResultType convertResponse2Report(RequestType response);
    protected abstract void processReport(@NonNull ResultType item);
    protected abstract void processResponse(@NonNull RequestType item);
    protected abstract Call<RequestType>  createRequest();
}
