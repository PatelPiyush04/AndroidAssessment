package com.theherdonline.ui.general;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.main.MainActivity;

public class GeneralFragment extends Fragment {
    protected String TAG = HerdFragment.class.getName();
    protected BackPressInterface mGobackListener;
    protected NetworkInterface mNetworkListener;
    protected Boolean mIsActive = false;
    protected Boolean mShowNavigationBar = true;
    protected MainActivity mMainActivity;
    protected String mToolbarTitle;
    protected Boolean mShowBackbotton;
    protected Integer mToolbarRightImage1;
    protected Integer mToolbarRightImage2;
    protected View.OnClickListener mClickToolbarRightButton1;
    protected View.OnClickListener mClickToolbarRightButton2;
    protected CustomerToolbar mCustomerToolbar;

    public CustomerToolbar getmCustomerToolbar() {
        return mCustomerToolbar;
    }

    protected void setToolBar(String title, Boolean  isShowBackbutton, Integer image1, Integer image2,
                              View.OnClickListener mClickImage1, View.OnClickListener mClickImage2)
    {
        mToolbarTitle = title;
        mShowBackbotton = isShowBackbutton;
        mToolbarRightImage1 = image1;
        mToolbarRightImage2 = image2;
        mClickToolbarRightButton1 = mClickImage1;
        mClickToolbarRightButton2 = mClickImage2;
        return;
    }


    public Boolean getmShowNavigationBar() {
        return mShowNavigationBar;
    }

    protected View.OnClickListener mExitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGobackListener.OnClickGoBackButton();
        }
    };





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BackPressInterface) {
            mGobackListener = (BackPressInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BackPressInterface");
        }

        if (context instanceof NetworkInterface) {
            mNetworkListener = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NetworkInterface");
        }

        if (context instanceof MainActivity) {
            mMainActivity = (MainActivity) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement MainActivity");
        }
    }






    @Override
    public void onDetach() {
        super.onDetach();
        mMainActivity.showNavigationBar(true);
        mGobackListener = null;
        mMainActivity = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        mIsActive = true;
        mCustomerToolbar = getmCustomerToolbar();
        mShowNavigationBar = getmShowNavigationBar();
        if (mMainActivity != null)
        {
            mMainActivity.setToolBar(mCustomerToolbar);
            mMainActivity.showNavigationBar(mShowNavigationBar);
        }
    }

    protected void setToolBar(CustomerToolbar customerToolbar)
    {
        if (mMainActivity != null) {
            mMainActivity.setToolBar(customerToolbar);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsActive = false;
    }
}
