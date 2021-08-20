package com.theherdonline.ui.herdLive;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.theherdonline.R;
import com.theherdonline.databinding.FragmentCreateSaleyardStreamBinding;
import com.theherdonline.db.entity.HerdLive;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.general.InterfacePermissionCheck;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import dagger.Lazy;

public class CreateSaleyardStreamFragment extends HerdFragment {

    BackPressInterface mActivityListener;
    NetworkInterface mNetworkListener;
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    MainActivityViewModel mMainActivityViewModel;
    FragmentCreateSaleyardStreamBinding mBinding;
    SingleDateAndTimePickerDialog.Builder singleBuilder;
    SimpleDateFormat simpleDateFormat;
    String mDateTime = "";
    @Inject
    Lazy<HerdLiveFragment> mHerdLiveFragmentProvider;

    @Inject
    public CreateSaleyardStreamFragment() {
        // Required empty public constructor
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(true, getString(R.string.txt_create_saleyard_stream), null, null, null, null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_saleyard_stream, container, false);
        this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        mBinding.btnCreateSaleyard.setOnClickListener(l -> {

            String strSaleyardName = mBinding.etSaleyardName.getText().toString();
            //String strDate = mBinding.tvSelectDate.getText().toString();
            if (StringUtils.isEmpty(strSaleyardName)) {
                ActivityUtils.showWarningDialog(requireActivity(), "Missing Fields", "\n Please enter all fields.");
            } else if (StringUtils.isEmpty(mDateTime)) {
                ActivityUtils.showWarningDialog(requireActivity(), "Missing Fields", "\n Please enter all fields.");
            } else {
                // Call API to post Data..
                CreateSaleyardStream(strSaleyardName, mDateTime);
            }
        });


        mBinding.tvSelectDate.setOnClickListener(l -> {
            openDateTimePicker();
        });

        return mBinding.getRoot();
    }

    private void CreateSaleyardStream(String saleyardName, String dateTime) {


        mMainActivityViewModel.postSaleyardStream(saleyardName, dateTime).observe(requireActivity(), new DataObserver<ResHerdLive>() {

            @Override
            public void onSuccess(ResHerdLive data) {
                mNetworkListener.onLoading(false);
                mBinding.etSaleyardName.setText("");
                mBinding.tvSelectDate.setText("");
                mDateTime="";
                ActivityUtils.showWarningDialog(getContext(), getString(R.string.txt_saleyard_stream_created), getString(R.string.txt_saleyard_stream_created_msg), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mActivityListener.OnClickGoBackButton();
                    }
                });
            }

            @Override
            public void onError(Integer code, String msg) {

                ActivityUtils.showWarningDialog(requireActivity(),"","\n"+msg);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onDirty() {

            }
        });
    }

    private void openDateTimePicker() {

        final Calendar calendar = Calendar.getInstance();
        final Date defaultDate = calendar.getTime();

        singleBuilder = new SingleDateAndTimePickerDialog.Builder(requireActivity())
                .setTimeZone(TimeZone.getDefault())
                .bottomSheet()
                .curved()

                //.titleTextColor(Color.GREEN)
                //.backgroundColor(Color.BLACK)
                //.mainColor(Color.GREEN)

                .displayHours(true)
                .displayMinutes(true)
                .displayDays(true)

                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        Log.d(TAG, "Dialog displayed");
                    }
                })

                .title("Select Date")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        mDateTime = simpleDateFormat.format(date);
                        mBinding.tvSelectDate.setText(TimeUtils.BackendUTC2LocalUTC(mDateTime));
                    }
                });
        singleBuilder.display();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainActivityViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
    }

    public Lazy<ViewModelFactory> getmLazyFactory() {
        return mLazyFactory;
    }

    public void setmLazyFactory(Lazy<ViewModelFactory> mLazyFactory) {
        this.mLazyFactory = mLazyFactory;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mActivityListener = null;
        mNetworkListener = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BackPressInterface) {
            mActivityListener = (BackPressInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


        if (context instanceof NetworkInterface) {
            mNetworkListener = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NetworkInterface");
        }

    }

}