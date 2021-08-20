package com.theherdonline.ui.main;

import android.content.Context;

import com.theherdonline.ui.herdinterface.NetworkInterface;
import dagger.android.support.DaggerFragment;

public class MainFragment extends DaggerFragment {

    protected NetworkInterface mNetworkListener;

    protected void subscription(){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NetworkInterface) {
            mNetworkListener = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNetworkListener = null;
    }

}
