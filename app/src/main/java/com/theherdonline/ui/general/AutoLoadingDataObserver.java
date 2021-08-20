package com.theherdonline.ui.general;

import androidx.annotation.Nullable;

import com.theherdonline.api.Resource;

public abstract class AutoLoadingDataObserver<T> extends DataObserver<T> {
    public abstract void onLoadingMore();

    @Override
    public void onChanged(@Nullable Resource<T> tResource) {
        super.onChanged(tResource);
        switch (tResource.status)
        {
            case LOADINGMORE:
                onLoadingMore();
                break;
        }
    }
}
