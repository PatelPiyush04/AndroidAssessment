package com.theherdonline.ui.main.poddocksearch;

import android.location.LocationListener;

public interface LocationPermissionListener {
    Boolean isHasLocationPermission();
    void requestLocationPermission();
    void requestLocation(LocationListener locationListener);

}
