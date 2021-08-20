package com.theherdonline.api;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.google.gson.Gson;
import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.User;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class NetworkDbData<ResultType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    public NetworkDbData(AppExecutors appExecutors, int id) {
        this.appExecutors = appExecutors;
        Gson mGson = new Gson();
        this.appExecutors.networkIO().execute(() -> {
            String dbString = getDataFromDB(id);
            ResultType dBResult = toResult(dbString);
            if (isValid(dBResult))
            {
                setValue(Resource.success(dBResult));
            }
            Call<ResponseBody> responseCall = createRequest();
            setValue(Resource.loading(null));
            responseCall.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (call != null) {
                        if (response.isSuccessful())
                        {
                            try{
                                String json = response.body().string();
                                if (!json.equals(dbString))
                                {
                                    setValue(Resource.success(toResult(json)));
                                }
                            }
                            catch (Exception e)
                            {

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
                public void onFailure(Call<ResponseBody> call, Throwable t) {
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

               /* @Override
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
                }*/
                /*@Override
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
                }*/
            });

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
    protected abstract Call<ResponseBody>  createRequest();
    protected abstract String getDataFromDB(int id);
    protected abstract void updateDb(Integer id, String jsonStr);
    protected abstract String toJsonString(ResultType item);
    protected abstract ResultType toResult(String jsonString);
    protected Boolean isValid(ResultType items){return items != null; }

}
