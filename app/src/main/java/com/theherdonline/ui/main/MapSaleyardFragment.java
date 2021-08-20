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
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.MapMarkerBitmap;
import com.theherdonline.ui.herdinterface.MapMarkListener;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.UIUtils;
import dagger.Lazy;
import dagger.android.support.DaggerFragment;

public class MapSaleyardFragment extends DaggerFragment {

    public static String ARG_LIST_DATA = "list data";

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    private MapMarkListener mMapListener;
    private SaleyardInterface mSaleyardListener;

    private List<EntitySaleyard> mSelectList;
    private MainActivityViewModel mViewModel;
    private FragmentMapBinding mBinding;
    private ArrayList mDataList;

    @Inject
    public MapSaleyardFragment() {
    }

    public static MapSaleyardFragment newInstance(Lazy<MapSaleyardFragment> mMapFragmentProvider, List<EntitySaleyard> advertisementsList) {
        Bundle args = new Bundle();
        MapSaleyardFragment fragment = mMapFragmentProvider.get();
        ArrayList<EntitySaleyard> list = new ArrayList<>(advertisementsList.size());
        list.addAll(advertisementsList);
        args.putParcelableArrayList(ARG_LIST_DATA, list);
        fragment.setArguments(args);
        return fragment;
    }

    public void refreshData(List<EntitySaleyard> itemList)
    {

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
                Map<LatLng, List<EntitySaleyard>> markerList = ActivityUtils.groupSaleyardList((List<EntitySaleyard>) itemList);
                Set<LatLng> groupedMarker = markerList.keySet();
                Integer validPoint = 0;
                googleMap.clear();

                for (LatLng location : groupedMarker) {
                    List<EntitySaleyard> entitySaleyardList = markerList.get(location);
                    if (entitySaleyardList.size() > 0) {
                        EntitySaleyard advertisement = entitySaleyardList.get(0);
                        LatLng latLng = new LatLng(advertisement.getLat(), advertisement.getLng());
                        Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng)); //.title("Marker in Sydney"));
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(new MapMarkerBitmap(getContext(), entitySaleyardList.size()).asBitmap()));
                        marker.setTag(entitySaleyardList);
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
                        final List<EntitySaleyard> list = (List<EntitySaleyard>) marker.getTag();
                        mSelectList = list;
                        presentCard(mSelectList);
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

    public void presentCard(List<EntitySaleyard> list) {
        mBinding.frameLayoutAnimalCard.setVisibility(View.VISIBLE);
        mBinding.includeLivestockItem.getRoot().setVisibility(View.GONE);
        mBinding.includeSaleyardItem.getRoot().setVisibility(View.VISIBLE);
        UIUtils.showSaleyard(getContext(),mBinding.includeSaleyardItem,list,mSaleyardListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDataList = getArguments().getParcelableArrayList(ARG_LIST_DATA);
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false);
        mBinding.imageViewClose.setOnClickListener(l -> {
           // mBinding.frameLayoutAnimalCard.setVisibility(View.INVISIBLE);
        });
        refreshData(mDataList);
      //  mBinding.frameLayoutAnimalCard.setVisibility(mSelectList != null && mSelectList.size() != 0 ?
      //          View.VISIBLE : View.INVISIBLE);


        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSelectList != null && mSelectList.size() != 0) {
            presentCard(mSelectList);
        }
    }

    @Override
    public void onDestroy() {
        mBinding.frameLayoutAnimalCard.setVisibility(View.GONE);
        super.onDestroy();

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
        if (context instanceof SaleyardInterface) {
            mSaleyardListener = (SaleyardInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MapMarkListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMapListener = null;
        mSaleyardListener = null;
    }
}
