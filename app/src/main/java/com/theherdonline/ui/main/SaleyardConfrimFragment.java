package com.theherdonline.ui.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.theherdonline.R;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.util.ActivityUtils;

import org.apache.commons.collections4.ListUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import dagger.Lazy;

public class SaleyardConfrimFragment extends SaleyardDetailFragmentV2 {



    public static SaleyardConfrimFragment newInstance(Lazy<ViewModelFactory> mLazyFactory,
                                                      Lazy<MapMarkerFragment> mMapMarkerFragmentLazy,
                                                      EntitySaleyard newEntitySaleyard, EntitySaleyard oldEntitySaleyard) {
        Bundle args = new Bundle();
        SaleyardConfrimFragment fragment = new SaleyardConfrimFragment();
        if (newEntitySaleyard != null)
        {
            args.putParcelable(ARG_TAG_saleyard_new_item, newEntitySaleyard);
        }
        if (oldEntitySaleyard != null)
        {
            args.putParcelable(ARG_TAG_saleyard_old_item, oldEntitySaleyard);
        }
        fragment.setArguments(args);
        fragment.mLazyFactory = mLazyFactory;
        fragment.mMapMarkerFragmentLazy = mMapMarkerFragmentLazy;
        return fragment;
    }




    public List<Media> mediaListNeedDeleted(EntitySaleyard newData, EntitySaleyard oldData)
    {
        List<Media> newMediaList = ListUtils.emptyIfNull(newData.getMedia());
        List<Media> mediaListToDelete = new ArrayList<>();

        for (Media oldMedia : ListUtils.emptyIfNull(oldData.getMedia()))
        {
            boolean add = true;
            for (Media newMedia : newMediaList)
            {
                if (newMedia.getUrl().equals(oldMedia))
                {
                    add = false;
                    break;
                }
            }
            if (add)
            {
                mediaListToDelete.add(oldMedia);
            }
        }
        return mediaListToDelete;
    }

    public List<Media> mediaListNeedUpload(EntitySaleyard newData, EntitySaleyard oldData)
    {

        List<Media> list = new ArrayList<>();
        for (Media media: ListUtils.emptyIfNull(newData.getMedia()))
        {
            if(new File(media.getUrl()).isFile())
            {
                list.add(media);
            }
        }
        return list;
    }

    public EntitySaleyard compareSaleyard(EntitySaleyard newData, EntitySaleyard oldData)
    {

        if (oldData == null)
        {
            // this is for creating a new saleyard
            return newData;
        }
        EntitySaleyard updateEntitySaleyard = new EntitySaleyard();
        if (newData.getName() != null && !newData.getName().equals(oldData.getName()))
        {
            updateEntitySaleyard.setName(newData.getName());
        }
        if (newData.getType() !=null && !newData.getType().equals(oldData.getType()))
        {
            updateEntitySaleyard.setType(newData.getType());
        }
        if (newData.getDescription()!= null && !newData.getDescription().equals(oldData.getDescription()))
        {
            updateEntitySaleyard.setDescription(newData.getDescription());
        }
        if (newData.getSale_numbers() != null && !newData.getSale_numbers().equals(oldData.getSale_numbers()))
        {
            updateEntitySaleyard.setSale_numbers(newData.getSale_numbers());
        }

        if (newData.getHeadcount() != null && !newData.getHeadcount().equals(oldData.getHeadcount()))
        {
            updateEntitySaleyard.setHeadcount(newData.getHeadcount());
        }


        if (newData.getStartsAt() != null && !newData.getStartsAt().equals(oldData.getStartsAt()))
        {
            updateEntitySaleyard.setStartsAt(newData.getStartsAt());
        }
        if (newData.getSaleyardStatusId() !=null && !newData.getSaleyardStatusId().equals(oldData.getSaleyardStatusId()))
        {
            updateEntitySaleyard.setSaleyardStatusId(newData.getSaleyardStatusId());
        }
        if (newData.getSaleyardCategoryId() != null && !newData.getSaleyardCategoryId().equals(oldData.getSaleyardCategoryId()))
        {
            updateEntitySaleyard.setSaleyardCategoryId(newData.getSaleyardCategoryId());
        }

        if (newData.getAddress() != null && !newData.getAddress().equals(oldData.getAddress()))
        {
            updateEntitySaleyard.setAddress(newData.getAddress());
        }

        if (newData.getAddressLineOne() != null && !newData.getAddressLineOne().equals(oldData.getAddressLineOne()))
        {
            updateEntitySaleyard.setAddressLineOne(newData.getAddressLineOne());
        }


        if (newData.getAddressLineTwo() != null && !newData.getAddressLineTwo().equals(oldData.getAddressLineTwo()))
        {
            updateEntitySaleyard.setAddressLineTwo(newData.getAddressLineTwo());
        }


        if (newData.getAddressSuburb() != null && !newData.getAddressSuburb().equals(oldData.getAddressSuburb()))
        {
            updateEntitySaleyard.setAddressSuburb(newData.getAddressSuburb());
        }


        if (newData.getAddressCity() != null && !newData.getAddressCity().equals(oldData.getAddressCity()))
        {
            updateEntitySaleyard.setAddressCity(newData.getAddressCity());
        }


        if (newData.getAddressPostcode() != null && !newData.getAddressPostcode().equals(oldData.getAddressPostcode()))
        {
            updateEntitySaleyard.setAddressPostcode(newData.getAddressPostcode());
        }

        if (newData.getLat() != null && !newData.getLat().equals(oldData.getLat()))
        {
            updateEntitySaleyard.setLat(newData.getLat());
        }

        if (newData.getLng() != null &&  !newData.getLng().equals(oldData.getLng()))
        {
            updateEntitySaleyard.setLng(newData.getLng());
        }

        if (newData.equals(updateEntitySaleyard))
        {
            return null;
        }
        else
        {
            return updateEntitySaleyard;
        }



    }


    @Override
    public CustomerToolbar getmCustomerToolbar() {
        View.OnClickListener mConfirmListener = v->{
            if (mNewEntitySaleyard.getId() == null) {
                // create a new saleyard
                mNewEntitySaleyard.setSaleyardStatusId(2);
                mViewModel.postCreateSaleyard(mNewEntitySaleyard).observe(this, new DataObserver<EntitySaleyard>(){
                    @Override
                    public void onSuccess(EntitySaleyard data) {
                        mNetworkListener.onLoading(false);
                        ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_submit_done), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ((MainActivity)getActivity()).onBackPressedTwice();

                                //getActivity().onBackPressed();
                                //getActivity().onBackPressed();

                            }
                        });
                    }

                    @Override
                    public void onError(Integer code, String msg) {
                        mNetworkListener.onLoading(false);
                        mNetworkListener.onFailed(code, msg);
                    }

                    @Override
                    public void onLoading() {

                        mNetworkListener.onLoading(true);
                    }

                    @Override
                    public void onDirty() {

                    }
                } );
            } else {
                // for Update
                EntitySaleyard updateEntitySaleyard = compareSaleyard(mNewEntitySaleyard, mOldEntitySaleyard);
                List<Media> mediaListUpload = mediaListNeedUpload(mNewEntitySaleyard, mOldEntitySaleyard);
                List<Media> mediaListDelete = mediaListNeedDeleted(mNewEntitySaleyard, mOldEntitySaleyard);

                if (updateEntitySaleyard == null && mediaListDelete.size() == 0  && mediaListDelete.size() == 0)
                {
                    // nothing needed to be updated
                    ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_submit_done), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // getActivity().getSupportFragmentManager().popBackStack();
                            // getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().onBackPressed();

                            //    ((MainActivity)getActivity()).mBinding.navigation.setCurrentItem(2);
                        }
                    });
                    return;
                }
                else
                {
                    // need to update something

                    mViewModel.updateSaleyard(mNewEntitySaleyard.getId(), updateEntitySaleyard,mediaListDelete,mediaListUpload).observe(this,
                            new DataObserver<EntitySaleyard>(){
                                @Override
                                public void onSuccess(EntitySaleyard data) {
                                    mNetworkListener.onLoading(false);
                                    ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_saleyard_update_done), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((MainActivity)getActivity()).onBackPressedTwice();


                                        }
                                    });
                                }

                                @Override
                                public void onError(Integer code, String msg) {
                                    mNetworkListener.onLoading(false);
                                    mNetworkListener.onFailed(code, msg);
                                }

                                @Override
                                public void onLoading() {
                                    mNetworkListener.onLoading(true);
                                }

                                @Override
                                public void onDirty() {
                                }
                            });
                }
            }
        };

        return   new CustomerToolbar(true, getString(R.string.txt_confirm_ad),
                getString(R.string.txt_publish), mConfirmListener,
                null, null,
                null, null,
                null, null);
    }

    @Override
    protected void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        mViewModel.refreshSaleyardDetail(null, mPresentEntitySaleyard);
        mViewModel.getmLDResponseSpecificSaleyard().observe(this, mObserverSaleyard);
    }
}
