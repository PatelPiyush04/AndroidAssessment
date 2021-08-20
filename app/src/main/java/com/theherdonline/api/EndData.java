package com.theherdonline.api;

import androidx.lifecycle.LiveData;



public class EndData<T> extends LiveData<Resource<T>> {
    public EndData() {
        setValue(Resource.error(0,"No more data", null));
    }
}
