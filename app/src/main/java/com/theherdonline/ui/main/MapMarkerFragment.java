package com.theherdonline.ui.main;

import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentMapMarkerBinding;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.MapMarkerBitmap;

public class MapMarkerFragment extends HerdFragment {

    static public String ARG_LAT = "arg-lat";
    static public String ARG_LNG = "arg-lng";


    private FragmentMapMarkerBinding mBinding;
    private Double mLat, mLng;


    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar("",
                mExitListener, null, null, null, null);
    }

    public static MapMarkerFragment newInstance(MapMarkerFragment fragment,  Double lat, Double lng) {

        Bundle args = new Bundle();
        args.putDouble(ARG_LAT,lat);
        args.putDouble(ARG_LNG,lng);
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    public MapMarkerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map_marker, container, false);
        mLat = getArguments().getDouble(ARG_LAT);
        mLng = getArguments().getDouble(ARG_LNG);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLngBounds.Builder LatLngBoundsBuilder = new LatLngBounds.Builder();
                LatLng latLng = new LatLng(mLat, mLng);
                Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng)); //.title("Marker in Sydney"));
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(new MapMarkerBitmap(getContext(), 0).asBitmap()));
                LatLngBoundsBuilder.include(latLng);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLngBoundsBuilder.build().getCenter(), 10);
                googleMap.moveCamera(cameraUpdate);

            }
        });
        return mBinding.getRoot();
    }



}
