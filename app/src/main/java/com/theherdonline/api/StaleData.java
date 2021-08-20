package com.theherdonline.api;

/**
 * Created by jackttc on 21/5/18.
 */

import androidx.lifecycle.LiveData;

/**
 * Created by freddie on 16/05/18.
 */

public class StaleData<T> extends LiveData<Resource<T>> {
    public StaleData() {
        setValue(Resource.stale(null));
    }
}
