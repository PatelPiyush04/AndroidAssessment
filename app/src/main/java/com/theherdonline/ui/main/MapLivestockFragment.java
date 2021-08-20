package com.theherdonline.ui.main;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentMapBinding;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.MapMarkerBitmap;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.herdinterface.MapMarkListener;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.UIUtils;
import dagger.Lazy;
import dagger.android.support.DaggerFragment;

public class MapLivestockFragment extends DaggerFragment {


    private MapMarkListener mMapListener;
    private LivestockInterface mLivestockListener;


    public static String ARG_LIST_DATA = "list data";

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    List<EntityLivestock> mSelectList;

    MainActivityViewModel mViewModel;

    FragmentMapBinding mBinding;
    private ArrayList mDataList;
    public static MapLivestockFragment newInstance(Lazy<MapLivestockFragment> mMapFragmentProvider, List<EntityLivestock> advertisementsList ) {
        Bundle args = new Bundle();
        MapLivestockFragment fragment = mMapFragmentProvider.get();
        ArrayList<EntityLivestock> list = new ArrayList<>(advertisementsList.size());
        list.addAll(advertisementsList);
        args.putParcelableArrayList(ARG_LIST_DATA, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    public MapLivestockFragment() {
    }

    public void presentCard(List<EntityLivestock> list)
    {
        mBinding.includeSaleyardItem.getRoot().setVisibility(View.GONE);
        mBinding.includeLivestockItem.getRoot().setVisibility(View.VISIBLE);
        mBinding.frameLayoutAnimalCard.setVisibility(View.VISIBLE);
        UIUtils.showLivestock(getContext(),mBinding.includeLivestockItem, list,mLivestockListener);
    }

    @Override
    public void onResume() {
        super.onResume();
    if (mSelectList != null && mSelectList.size() != 0)
    {
        presentCard(mSelectList);
    }
    }


    public void refreshData(List<EntityLivestock> itemList)
    {
        mBinding.imageViewClose.setOnClickListener(l->{
            //  mBinding.frameLayoutAnimalCard.setVisibility(View.INVISIBLE);
        });

        mBinding.frameLayoutAnimalCard.setVisibility(View.INVISIBLE);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLngBounds.Builder LatLngBoundsBuilder = new LatLngBounds.Builder();
                LatLng startPoint = null;
                if (itemList.size() == 0)
                {
                    return;
                }
                Map<LatLng, List<EntityLivestock>> markerList =  ActivityUtils.groupAdvertismentList((List<EntityLivestock>) itemList);
                Set<LatLng> groupedMarker = markerList.keySet();
                Integer validPoint = 0;

                googleMap.clear();

                for (LatLng location: groupedMarker) {
                    List<EntityLivestock> advertisementList = markerList.get(location);
                    if (advertisementList.size() > 0) {
                        EntityLivestock advertisement = advertisementList.get(0);
                        LatLng latLng = new LatLng(advertisement.getLat(), advertisement.getLng());
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng)); //.title("Marker in Sydney"));
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(new MapMarkerBitmap(getContext(),advertisementList.size()).asBitmap()));
                        marker.setTag(advertisementList);
                        if (startPoint == null) {
                            startPoint = latLng;
                        }
                        LatLngBoundsBuilder.include(latLng);
                        validPoint ++;
                    }
                }
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        final List<EntityLivestock> list = (List<EntityLivestock>) marker.getTag();
                        mSelectList = list;
                        presentCard(list);
                        return false;
                    }
                });

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        mBinding.frameLayoutAnimalCard.setVisibility(View.GONE);
                    }
                });


                Integer padding = 100;
                if (validPoint != 0) {
                    // ensure has some data
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LatLngBoundsBuilder.build(), padding));
                }
                }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataList = getArguments().getParcelableArrayList(ARG_LIST_DATA);
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        refreshData(mDataList);
        return mBinding.getRoot();
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MapMarkListener) {
            mMapListener = (MapMarkListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapMarkListener");
        }

        if (context instanceof LivestockInterface) {
            mLivestockListener = (LivestockInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapMarkListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMapListener = null;
        mLivestockListener = null;
    }
}
