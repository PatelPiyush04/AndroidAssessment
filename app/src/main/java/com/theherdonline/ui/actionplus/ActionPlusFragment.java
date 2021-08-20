package com.theherdonline.ui.actionplus;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.main.myad.LivestockListFragment;
import com.theherdonline.ui.main.myad.MyAdRecyclerViewAdapter;
import com.theherdonline.ui.profile.CustomRecyclerViewAdapter;
import com.theherdonline.ui.profile.PagerListFragment;
import com.theherdonline.ui.profile.PagerViewModel;

public class ActionPlusFragment extends PagerListFragment<EntityLivestock> {

    private ActionPlusPagerViewModel mActionPlusViewModel;
    private LivestockInterface mLivestockListener;

    @Override
    public CustomRecyclerViewAdapter getAdapter() {
        return new MyAdRecyclerViewAdapter(getContext(), mActionPlusViewModel.getDataList(), mLivestockListener, LivestockListFragment.ARG_VIEW_TYPE_REQUEST);
    }



    @Override
    public PagerViewModel getViewModel() {
        return mActionPlusViewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ActionPlusPagerViewModel.Factory factoryProfile = new ActionPlusPagerViewModel.Factory(getActivity().getApplication());
        mActionPlusViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(ActionPlusPagerViewModel.class);
        mActionPlusViewModel.getmLiveDataStreamList().observe(this, mListDataObserver);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LivestockInterface) {
            mLivestockListener = (LivestockInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LivestockInterface");
        }
    }

}
