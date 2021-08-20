package com.theherdonline.ui.general;

import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;

import com.theherdonline.api.Resource;


public abstract class DataObserver<T> implements Observer<Resource<T>> {

    public abstract void onSuccess(T data);
    public abstract void onError(Integer code, String msg);
    public abstract void onLoading();
    public abstract void onDirty();

    @Override
    public void onChanged(@Nullable Resource<T> tResource) {
        switch (tResource.status)
        {
            case SUCCESS:
                onSuccess(tResource.data);
                break;
            case LOADING:
                onLoading();
                break;
            case ERROR:
                onError(tResource.errorCode, tResource.message);
                break;
            case STALE:
                onDirty();
                break;
        }
    }
}
