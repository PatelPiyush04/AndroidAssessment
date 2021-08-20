package com.theherdonline.ui.profile;

import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.espresso.SimpleIdlingResource;
import com.theherdonline.repository.PagerListRepository;
import com.theherdonline.databinding.FragmentPageListBinding;
import com.theherdonline.di.MyApplication;
import com.theherdonline.ui.general.AutoLoadingDataObserver;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.SwipeController;
import com.theherdonline.ui.general.SwipeControllerActions;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.util.ActivityUtils;

public abstract class PagerListFragment<DataType> extends Fragment {

    public NetworkInterface mNetworkInterface;
    public Boolean mRefreshOnResume = false;
    public OnDataChangedListener mOnDataChangedListener;
    public OnDateDeleteListerner mOnDateDeleteListerner;
    public Boolean mSwipeable = false;
    public Boolean mEnableSwipe = false;
    public String  mDeleteWarningText;



    FragmentPageListBinding mBinding;
    SwipeController mSwipeController;
    List<DataType> mDataList;

    public Boolean getmEnableSwipe() {
        return mEnableSwipe;
    }

    public String getmDeleteWarningText() {
        return mDeleteWarningText;
    }

    public void setmDeleteWarningText(String mDeleteWarningText) {
        this.mDeleteWarningText = mDeleteWarningText;
    }

    protected AutoLoadingDataObserver<List<DataType>> mListDataObserver = new AutoLoadingDataObserver<List<DataType>>() {
        @Override
        public void onLoadingMore() {
            mBinding.swiperefresh.setRefreshing(false);
            setEspressoTestingIdling(false);
            // showProgressBar(false,true);
        }

        @Override
        public void onSuccess(List<DataType> data) {
            mBinding.swiperefresh.setRefreshing(false);
            mDataList = data;
            // showProgressBar(false,false);
            mBinding.list.getAdapter().notifyDataSetChanged();
            ((CustomRecyclerViewAdapter) mBinding.list.getAdapter()).setTotalItemNumber(getViewModel().totalItemNumber());
            mBinding.frameLayoutAvailableData.setVisibility(data.size() == 0 ? View.VISIBLE : View.INVISIBLE);
            if (mOnDataChangedListener != null) {
                ArrayList<DataType> ne = new ArrayList<>();
                ne.addAll(data);
                mOnDataChangedListener.onChanged(ne);
            }
            setEspressoTestingIdling(true);

        }

        @Override
        public void onError(Integer code, String msg) {
            mBinding.swiperefresh.setRefreshing(false);
            // showProgressBar(false,false);
            if (code == AppConstants.DATA_ERROR_END) {
                //Toast.makeText(getContext(), getString(R.string.error_no_more_data), Toast.LENGTH_LONG).show();
                getAdapter().showNowMoreDataText(true);
            }
            else
            {
                mNetworkInterface.onFailed(code,msg);
            }
            setEspressoTestingIdling(false);

        }

        @Override
        public void onLoading() {
            showProgressBar(true, false);

            setEspressoTestingIdling(false);


        }

        @Override
        public void onDirty() {
            setEspressoTestingIdling(true);

        }
    };





    public PagerListFragment() {
    }

    public abstract CustomRecyclerViewAdapter getAdapter();

    public abstract PagerViewModel getViewModel();

    private void showProgressBar(boolean firstLoading, boolean loadingMore) {
        mBinding.swiperefresh.setRefreshing(firstLoading && !loadingMore);
        //mBinding.progressBarFirstLoading.setVisibility(firstLoading ? View.VISIBLE : View.GONE);
        mBinding.frameLayoutProgressBar.setVisibility(loadingMore ? View.VISIBLE : View.GONE);
        //mNetworkInterface.onLoading(firstLoading);
    }

    public void setmRfreshOnResume(Boolean mRfreshOnResume) {
        this.mRefreshOnResume = mRfreshOnResume;
    }

    public List<DataType> getData() {
        return mDataList;
    }

    public void setmOnDataChangedListener(OnDataChangedListener mOnDataChangedListener) {
        this.mOnDataChangedListener = mOnDataChangedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_page_list, container, false);
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        return mBinding.getRoot();
    }




    public void setmSwipeController(SwipeController mSwipeController) {
        this.mSwipeController = mSwipeController;
    }

    public int getItemId(DataType dataType) {
        return 0;
    }

    public void setEnableSwipe(Boolean enable) {
        mSwipeable = enable;
    }


    public String deleteDescription(DataType dataType)
    {
        return "Are you sure?";
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.frameLayoutAvailableData.setVisibility(mDataList != null && mDataList.size() == 0 ? View.VISIBLE : View.GONE );

        mBinding.list.setAdapter(getAdapter());
        if (mSwipeable) {
            SwipeController swipeController = new SwipeController(getContext(), new SwipeControllerActions() {
                @Override
                public void onRightClicked(int position) {
                    if (position < mDataList.size()) {
                        DataType selectedCard = mDataList.get(position);
                        String msg = deleteDescription(selectedCard);
                        ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), msg,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Integer sourceId = getItemId(selectedCard);
                                        getViewModel().deleteItem(sourceId).observe(PagerListFragment.this,
                                                new DataObserver<Void>() {
                                                    @Override
                                                    public void onSuccess(Void data) {
                                                        //mNetworkListener.onLoading(false);
                                                        mDataList.remove(position);
                                                        RecyclerView.Adapter adapter = mBinding.list.getAdapter();
                                                        adapter.notifyItemRemoved(position);
                                                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                                                    }

                                                    @Override
                                                    public void onError(Integer code, String msg) {
                                                        //mNetworkListener.onFailed(code,msg);
                                                        // mNetworkListener.onLoading(false);
                                                    }

                                                    @Override
                                                    public void onLoading() {
                                                        //mNetworkListener.onLoading(true);
                                                    }

                                                    @Override
                                                    public void onDirty() {
                                                        // mNetworkListener.onLoading(false);
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


        if (getActivity() != null && mSwipeable == false) {
            RecyclerView.ItemDecoration a = ((MyApplication) getActivity().getApplication()).getmAppUtil().provideDividerItemDecoration();
            mBinding.list.addItemDecoration(a);

        }
        mBinding.list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastPosition == mBinding.list.getAdapter().getItemCount() - 1) {
                    PagerListRepository repository = getViewModel().mRepository;
                    if (repository.getTotalPage() == null) {
                    } else {
                        getViewModel().refreshRepository(true);
                    }
                }
            }
        });

        mBinding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(getContext(), getString(R.string.txt_refreshing), Toast.LENGTH_SHORT).show();
                getViewModel().resetRepository();
                getViewModel().refreshRepository(true);
                getAdapter().showNowMoreDataText(false);
            }
        });

        if (getViewModel().getmRepository().getDataList().size() == 0) {
            getViewModel().refreshRepository(false);
            mBinding.swiperefresh.setRefreshing(true);
        }

        if (mRefreshOnResume) {
            getViewModel().resetRepository();
            getViewModel().refreshRepository(false);
            getAdapter().showNowMoreDataText(false);
        }
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NetworkInterface) {
            mNetworkInterface = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NetworkInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNetworkInterface = null;
    }


    public void setEspressoTestingIdling(Boolean b)
    {
        /*if (getActivity() instanceof MainActivity)
        {
            if (((MainActivity)getActivity()).getIdlingResource() instanceof SimpleIdlingResource)
            {
                SimpleIdlingResource simpleIdlingResource = (SimpleIdlingResource) ((MainActivity)getActivity()).getIdlingResource();
                simpleIdlingResource.setIdleState(b);
            }
        }*/
    }


    public interface OnDataChangedListener {
        void onChanged(ArrayList dataTypeList);
    }

    public interface OnDateDeleteListerner{
        void onDeleteDataSuccess(ArrayList dataTypelList, int position);
        void onDeleteFail(ArrayList dataTypelList, int position);
    }

}
