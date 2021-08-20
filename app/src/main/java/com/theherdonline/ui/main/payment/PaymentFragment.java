package com.theherdonline.ui.main.payment;

import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.db.entity.PaymentCard;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.databinding.FragmentPaymentBinding;
import com.theherdonline.ui.general.SwipeController;
import com.theherdonline.ui.general.SwipeControllerActions;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import dagger.Lazy;


public class PaymentFragment extends HerdFragment {

    FragmentPaymentBinding mBinding;
    MainActivityViewModel mViewModel;

    List<PaymentCard> mPaymentCardList = null;


    DataObserver<List<PaymentCard>> mRefreshObser = new DataObserver<List<PaymentCard>>() {
        @Override
        public void onSuccess(List<PaymentCard> data) {
            mBinding.swiperefreshContainer.setRefreshing(false);
            mPaymentCardList = data;
         //   mNetworkListener.onLoading(false);
            PaymentRecyclerViewAdapter adapter = new PaymentRecyclerViewAdapter(getContext(),data);
            mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
            mBinding.list.setAdapter(adapter);


            SwipeController swipeController = new SwipeController(getContext(), new SwipeControllerActions() {
                @Override
                public void onRightClicked(int position) {
                    if (position < mPaymentCardList.size())
                    {
                        PaymentCard selectedCard = mPaymentCardList.get(position);
                        String msg = getString(R.string.txt_are_you_sure_delete_credit,selectedCard.getLastFour());
                        ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), msg,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Integer sourceId = mPaymentCardList.get(position).getId();
                                        mViewModel.deletePaymentCreditCard(mAppUtil.getUserId(), sourceId).observe(PaymentFragment.this,
                                                new DataObserver<Void>() {
                                                    @Override
                                                    public void onSuccess(Void data) {
                                                        mNetworkListener.onLoading(false);
                                                        mPaymentCardList.remove(position);
                                                        adapter.notifyItemRemoved(position);
                                                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                                    }

                                                    @Override
                                                    public void onError(Integer code, String msg) {
                                                        mNetworkListener.onFailed(code,msg);
                                                        mNetworkListener.onLoading(false);
                                                    }

                                                    @Override
                                                    public void onLoading() {
                                                        mNetworkListener.onLoading(true);
                                                    }

                                                    @Override
                                                    public void onDirty() {
                                                        mNetworkListener.onLoading(false);
                                                    }
                                                });
                                    }
                                },
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                    }
                }
            });

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(mBinding.list);

            mBinding.list.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    swipeController.onDraw(c);
                }
            });


        }

        @Override
        public void onError(Integer code, String msg) {
            mBinding.swiperefreshContainer.setRefreshing(false);

           // mNetworkListener.onLoading(false);
            mNetworkListener.onFailed(code,msg);

        }

        @Override
        public void onLoading() {

            //mNetworkListener.onLoading(true);
        }

        @Override
        public void onDirty() {
            mBinding.swiperefreshContainer.setRefreshing(false);

           // mNetworkListener.onLoading(false);
        }
    };

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;


    @Inject
    Lazy<AddCardFragment> mLazyAddCardFragment;


    @Inject
    AppUtil mAppUtil;

    @Inject
    public PaymentFragment() {
    }


    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar( getString(R.string.txt_payment_details), mExitListener, R.drawable.ic_add_circle, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                {
                    ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                            mLazyAddCardFragment.get(),
                            R.id.frameLayout_container);
                }
            }
        }, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false);

/*        UIUtils.showToolbar(getContext(), mBinding.frameLayoutToolbar, getString(R.string.txt_payment_details), mExitListener, R.drawable.ic_add_circle, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null)
                {
                    ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                            mLazyAddCardFragment.get(),
                            R.id.frameLayout_container);
                }
            }
        }, null, null);*/

        mBinding.swiperefreshContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mViewModel.getmLDPaymentCardList().observe(PaymentFragment.this,mRefreshObser);
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.swiperefreshContainer.setRefreshing(true);
        mViewModel.getmLDPaymentCardList().observe(PaymentFragment.this,mRefreshObser);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        //mViewModel.getmLDPaymentCardList().observe(this,mRefreshObser);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
