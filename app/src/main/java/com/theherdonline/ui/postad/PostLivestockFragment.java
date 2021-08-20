package com.theherdonline.ui.postad;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentPostAdBinding;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.DataConverter;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.GenderEntity;
import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.LivestockSubCategory;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.PregnancyEntity;
import com.theherdonline.db.entity.User;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.ADType;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DatePickerFragment;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.PickupAnimalDialogFragment;
import com.theherdonline.ui.general.PickupBreedDialogFragment;
import com.theherdonline.ui.general.PickupDurationDayDialogFragment;
import com.theherdonline.ui.general.PickupGenderDialogFragment;
import com.theherdonline.ui.general.PickupPregnancyDialogFragment;
import com.theherdonline.ui.general.PickupStringDialogFragment;
import com.theherdonline.ui.general.PickupUserDialogFragment;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.poddocksearch.AutoLocationInterface;
import com.theherdonline.ui.makebid.BidDetailFragment;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UserType;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.Lazy;

public class PostLivestockFragment extends HerdFragment
        implements PickupAnimalDialogFragment.OnSelectedItemListener,
        PickupBreedDialogFragment.OnSelectedItemListener,
        PickupGenderDialogFragment.OnSelectedItemListener,
        PickupUserDialogFragment.OnSelectedItemListener,
        PickupDurationDayDialogFragment.OnSelectedItemListener,
        PickupPregnancyDialogFragment.OnSelectedItemListener {


    public static String AGR_DATA = "data-livestock";
    public static String AGR_MODE = "data-mode";
    public static Integer AGR_MODE_NEW = 1;
    public static Integer AGR_MODE_UPDATE = 2;
    private final List<Media> mMediaListToDelete = new ArrayList<>();
    public Integer[] mDurationList = {1, 2, 3, 4, 5, 6, 7};
    @Inject
    PlacesClient mPlaceClient;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    @Inject
    Lazy<BidDetailFragment> mBidDetailFragmentProvider;
    @Inject
    AppUtil mAppUtil;
    @Inject
    AppDatabase mDatabase;
    LivestockInterface mListener;
    FragmentPostAdBinding mBinding;
    MainActivityViewModel mMainActivityViewModel;
    LivestockInterface mLivestockInterface;
    Boolean mIsAgent = false;
    MainActivityViewModel mViewModel;
    AutoLocationInterface mLocationListener;
    private Integer mUiMode = AGR_MODE_NEW;
    private EntityLivestock mLivestock = null;
    private Place mPlace;
    private DateTime mStartTime;
    private DateTime mAvailableTime;
    private DateTime mEarliestTime;
    private DateTime mLatestTime;
    private List<LivestockCategory> mAnimalList;
    private List<LivestockSubCategory> mBreedList;
    private LivestockCategory mSelectAnimal;
    private LivestockSubCategory mSelectBreed;
    private List<User> mProducerList;
    private List<GenderEntity> mGenderList;
    private List<PregnancyEntity> mPregnancyList;

    private GenderEntity mSelectGender;
    private PregnancyEntity mSelectPregnancy;

    String  toolbarTitle;

    @Inject
    public PostLivestockFragment() {
    }

    public static PostLivestockFragment newInstance(Lazy<PostLivestockFragment> lazy, EntityLivestock livestock) {
        Bundle args = new Bundle();
        PostLivestockFragment fragment = lazy.get();
        args.putParcelable(AGR_DATA, livestock == null ? new EntityLivestock() : livestock);
        args.putInt(AGR_MODE, livestock == null ? AGR_MODE_NEW : AGR_MODE_UPDATE);
        fragment.setArguments(args);
        return fragment;
    }

    public void setmAppUtil(AppUtil mAppUtil) {
        this.mAppUtil = mAppUtil;
    }

    public void setmLazyFactory(Lazy<ViewModelFactory> mLazyFactory) {
        this.mLazyFactory = mLazyFactory;
    }


    @Override
    public Boolean getmShowNavigationBar() {
        return false;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_post_ad, container, false);
        if (getArguments() != null) {
            mUiMode = getArguments().getInt(AGR_MODE);
            mLivestock = getArguments().getParcelable(AGR_DATA);
            if (mUiMode == AGR_MODE_UPDATE) {

            } else {

            }
        }


        mIsAgent = mAppUtil.getUserType() == UserType.AGENT;
//        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mBinding.list.setLayoutManager(new GridLayoutManager(getContext(), 4));

        mAnimalList = mDatabase.livestockCategoryDao().loadAllExceptAll();
        if (mAnimalList != null && mAnimalList.size() > 0) {
            LivestockCategory livestockCategory = mBinding.tvSpinnerAnimal.getTag() == null ? mAnimalList.get(0) : (LivestockCategory) mBinding.tvSpinnerAnimal.getTag();
            mBinding.tvSpinnerAnimal.setText(livestockCategory.getName());
            mBinding.tvSpinnerAnimal.setTag(livestockCategory);

            List<LivestockSubCategory> subCategoryList = mDatabase.livestockSubCategoryDao().loadSpecificArray(livestockCategory.getId());
            if (subCategoryList != null && subCategoryList.size() > 0) {
                LivestockSubCategory selectSubCategory = mBinding.tvSpinnerBreed.getTag() == null ? subCategoryList.get(0) : (LivestockSubCategory) mBinding.tvSpinnerBreed.getTag();
                mBinding.tvSpinnerBreed.setText(selectSubCategory.getName());
                mBinding.tvSpinnerBreed.setTag(selectSubCategory);
            }
        }

        mBinding.tvSpinnerAnimal.setOnClickListener(l -> {
            if (mAnimalList != null && mAnimalList.size() > 0) {
                PickupAnimalDialogFragment fragment = PickupAnimalDialogFragment.newInstance("add", mAnimalList, PostLivestockFragment.this);
                fragment.show(getChildFragmentManager(), "photoh");
            }
        });


        mBinding.tvSpinnerBreed.setOnClickListener(l -> {
            LivestockCategory selectAnimal = (LivestockCategory) mBinding.tvSpinnerAnimal.getTag();
            List<LivestockSubCategory> subCategoryList = mDatabase.livestockSubCategoryDao().loadSpecificArray(selectAnimal.getId());
            if (subCategoryList != null && subCategoryList.size() > 1) {
                PickupBreedDialogFragment fragment = PickupBreedDialogFragment.newInstance("add", subCategoryList, PostLivestockFragment.this);
                fragment.show(getChildFragmentManager(), "photoh");
            }
        });

/*        mBinding.tvStartDate.setOnClickListener(l -> {
            DateTimePicker newFragment = DateTimePicker.newInstance(new DateTimePicker.InterfaceTimeDate() {
                @Override
                public void setTimeDate(DateTime dateTime) {
                    mStartTime = dateTime;
                    mBinding.tvStartDate.setTag(TimeUtils.time2ApiString(mStartTime));
                    mBinding.tvStartDate.setText(TimeUtils.time2String(mStartTime));

                }
            }, mStartTime);
            newFragment.show(getChildFragmentManager(), "dialog");
        });*/


        mBinding.tvAvailableDate.setOnClickListener(l -> {

            DialogFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //int minute = endDate.getMinuteOfHour();
                    //int hourOfDay = endDate.getHourOfDay();
                    mAvailableTime = new DateTime(year, month + 1, dayOfMonth, 0, 0);
                    mBinding.tvAvailableDate.setTag(TimeUtils.time2String(mAvailableTime, "yyyy-MM-dd"));
                    mBinding.tvAvailableDate.setText(TimeUtils.time2String(mAvailableTime, "dd MMMM yy"));
                }
            }, new DateTime());
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

        });
        mBinding.tvEarliestDate.setOnClickListener(l -> {

            DialogFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //int minute = endDate.getMinuteOfHour();
                    //int hourOfDay = endDate.getHourOfDay();
                    mEarliestTime = new DateTime(year, month + 1, dayOfMonth, 0, 0);
                    mBinding.tvEarliestDate.setTag(TimeUtils.time2String(mEarliestTime, "yyyy-MM-dd"));
                    mBinding.tvEarliestDate.setText(TimeUtils.time2String(mEarliestTime, "dd MMMM yy"));
                }
            }, new DateTime());
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

        });
        mBinding.tvLatestDate.setOnClickListener(l -> {

            DialogFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    //int minute = endDate.getMinuteOfHour();
                    //int hourOfDay = endDate.getHourOfDay();
                    mLatestTime = new DateTime(year, month + 1, dayOfMonth, 0, 0);
                    mBinding.tvLatestDate.setTag(TimeUtils.time2String(mLatestTime, "yyyy-MM-dd"));
                    mBinding.tvLatestDate.setText(TimeUtils.time2String(mLatestTime, "dd MMMM yy"));
                }
            }, new DateTime());
            newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");

        });


        mBinding.tvChooseALocation.setOnClickListener(l -> {
            mLocationListener.updateCreateLivestockLocation();

        });


        mBinding.tvSpinnerSex.setOnClickListener(l -> {
            PickupGenderDialogFragment fragment = PickupGenderDialogFragment.newInstance(getString(R.string.txt_select_gender), mGenderList, PostLivestockFragment.this);
            fragment.show(getChildFragmentManager(), "photoh");
        });


        mBinding.tvAgeUnit.setOnClickListener(l -> {
            PickupStringDialogFragment fragment = PickupStringDialogFragment.newInstance("add",
                    new PickupStringDialogFragment.OnSelectedUnitListener() {
                        @Override
                        public void OnSelectedItem(String ageunit) {
                            mBinding.tvAgeUnit.setTag(ageunit);
                            mBinding.tvAgeUnit.setText(ageunit);
                        }
                    }, R.array.array_age_unit);
            fragment.show(getChildFragmentManager(), "photoh");
        });

        mBinding.tvSpinnerPriceType.setOnClickListener(l -> {
            PickupStringDialogFragment fragment = PickupStringDialogFragment.newInstance("add",
                    new PickupStringDialogFragment.OnSelectedUnitListener() {
                        @Override
                        public void OnSelectedItem(String ageunit) {
                            mBinding.tvSpinnerPriceType.setTag(ageunit);
                            mBinding.tvSpinnerPriceType.setText(ageunit);
                        }
                    }, R.array.array_price_type);
            fragment.show(getChildFragmentManager(), "photoh");
        });


        mBinding.tvSpinnerOfferType.setOnClickListener(l -> {
            PickupStringDialogFragment fragment = PickupStringDialogFragment.newInstance("abc",
                    offerType -> {
                        mBinding.tvSpinnerOfferType.setTag(offerType);
                        mBinding.tvSpinnerOfferType.setText(offerType);
                        if ((getResources().getStringArray(R.array.array_offer_type)[0]).equals(offerType)) {
                            mBinding.checkboxOno.setVisibility(View.VISIBLE);
                            mBinding.llPriceView.setVisibility(View.VISIBLE);
                            mBinding.llLowPriceView.setVisibility(View.GONE);
                            mBinding.llHighPriceView.setVisibility(View.GONE);
                        } else if ((getResources().getStringArray(R.array.array_offer_type)[1]).equals(offerType)) {
                            mBinding.llLowPriceView.setVisibility(View.VISIBLE);
                            mBinding.llHighPriceView.setVisibility(View.VISIBLE);
                            mBinding.checkboxOno.setVisibility(View.GONE);
                            mBinding.llPriceView.setVisibility(View.GONE);
                        } else {
                            mBinding.checkboxOno.setVisibility(View.GONE);
                            mBinding.llLowPriceView.setVisibility(View.GONE);
                            mBinding.llHighPriceView.setVisibility(View.GONE);
                        }
                    }, R.array.array_offer_type);
            fragment.show(getChildFragmentManager(), "pickupOfferType");
        });

        mBinding.checkboxASAP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    mBinding.llAsapView.setVisibility(View.GONE);
                } else {
                    mBinding.llAsapView.setVisibility(View.VISIBLE);
                }
            }
        });
        mBinding.tvSpinnerPregnancyStatus.setOnClickListener(l -> {

            PickupPregnancyDialogFragment fragment = PickupPregnancyDialogFragment.newInstance(getString(R.string.txt_select_pregnancy), mPregnancyList, PostLivestockFragment.this);
            fragment.show(getChildFragmentManager(), "photoh");
        });


/*        //producer
        mBinding.frameLayoutProducer.setVisibility(mIsAgent ? View.VISIBLE : View.GONE);
        if (mIsAgent) {
            if (mProducerList == null) {
                mProducerList = mDatabase.userDao().getProducerUsers(mAppUtil.getUserId());
            }
            if (mBinding.tvSpinnerProducer.getTag() != null) {
                User selectProducer = (User) mBinding.tvSpinnerProducer.getTag();
                mBinding.tvSpinnerProducer.setText(ActivityUtils.trimTextEmpty(selectProducer.getFullName()));
            }
            mBinding.tvSpinnerProducer.setOnClickListener(l -> {
                if (mProducerList != null && mProducerList.size() > 0) {
                    PickupUserDialogFragment fragment = PickupUserDialogFragment.newInstance("add", mProducerList, PostLivestockFragment.this);
                    fragment.show(getChildFragmentManager(), "photoh");
                } else {
                    ActivityUtils.showToast(getContext(), getString(R.string.txt_producer_not_available), false);
                }
            });
        }*/

        mBinding.tvDuration.setOnClickListener(l -> {
            PickupDurationDayDialogFragment fragment = PickupDurationDayDialogFragment.newInstance("add", mDurationList, PostLivestockFragment.this);
            fragment.show(getChildFragmentManager(), "photoh");
        });

        mBinding.buttonConfirm.setOnClickListener(l -> {
            EntityLivestock newLivestock = updateLivestock();
            if (newLivestock == null) {
                return;
            } else {
                newLivestock.setId(mLivestock != null ? mLivestock.getId() : null);
                newLivestock.setAdvertisementStatusId(DataConverter.ADType2Int(mAppUtil.getUserType() == UserType.AGENT ? ADType.SCHEDULED : ADType.REQUEST));
                mViewModel.updatePostLivestocks(newLivestock);
                Boolean hasChanged = newLivestock.equals(mLivestock);
                mListener.onConfirmLivestock(newLivestock, mViewModel.getmMediaListToDelete(), true);
            }
        });

        return mBinding.getRoot();
    }


    public void initUI(EntityLivestock livestock) {

        if (StringUtils.isEmpty(livestock.getName()))
        {
            toolbarTitle="Create Ad";
        }else{
            toolbarTitle="Edit Ad";
        }
        mBinding.editTitle.setText(ActivityUtils.trimTextEmpty(livestock.getName()));
        mBinding.editDescription.setText(ActivityUtils.trimTextEmpty(livestock.getDescription()));

        if (livestock.getProducerId() != null) {
            User userProducer = mDatabase.userDao().getUserById(livestock.getProducerId());
            if (userProducer == null) {
                mBinding.tvSpinnerProducer.setText("");
                mBinding.tvSpinnerProducer.setTag(null);
            } else {
                mBinding.tvSpinnerProducer.setText(ActivityUtils.trimFullName(userProducer.getFirstName(), userProducer.getLastName()));
                mBinding.tvSpinnerProducer.setTag(userProducer);
            }
        } else {
            mBinding.tvSpinnerProducer.setText("");
            mBinding.tvSpinnerProducer.setTag(null);
        }

        // Location
        mPlace = livestock.getmPlace();
        if (mPlace != null) {
            mBinding.tvChooseALocation.setText(mPlace.getAddress());
        } else if (livestock.getAddress() != null) {
            mBinding.tvChooseALocation.setText(livestock.getAddress());
        } else {
            mBinding.tvChooseALocation.setText("");
        }

        // Start date
/*        if (livestock.getStart_date() != null) {
            mStartTime = TimeUtils.apiString2Time(livestock.getStart_date());
            mBinding.tvStartDate.setText(TimeUtils.time2String(mStartTime));
            mBinding.tvStartDate.setTag(TimeUtils.time2ApiString(mStartTime));
        } else {
            mBinding.tvStartDate.setText("");
            mStartTime = null;
            mBinding.tvStartDate.setTag(null);
        }*/


        // price type
        if (livestock.getPrice_type() != null) {
            mBinding.tvSpinnerPriceType.setTag(livestock.getPrice_type());
            mBinding.tvSpinnerPriceType.setText(livestock.getPrice_type());
        } else {
            mBinding.tvSpinnerPriceType.setText("");
            mBinding.tvSpinnerPriceType.setTag(null);
        }


        String[] stringsOfferType = getResources().getStringArray(R.array.array_offer_type);
        if ((getResources().getStringArray(R.array.array_offer_type_sever_side)[0]).equals(livestock.getOffer_type())) {
            mBinding.tvSpinnerOfferType.setText(stringsOfferType[0]);
            mBinding.tvSpinnerOfferType.setTag(stringsOfferType[0]);
            mBinding.checkboxOno.setVisibility(View.VISIBLE);
            mBinding.llPriceView.setVisibility(View.VISIBLE);
            mBinding.llLowPriceView.setVisibility(View.GONE);
            mBinding.llHighPriceView.setVisibility(View.GONE);
            mBinding.checkboxOno.setChecked(livestock.getOno() != null && !livestock.getOno().equals(0));
            mBinding.editPrice.setText(ActivityUtils.trimTextEmpty(livestock.getDollar_price()));

        } else if ((getResources().getStringArray(R.array.array_offer_type_sever_side)[1]).equals(livestock.getOffer_type())) {
            mBinding.tvSpinnerOfferType.setText(stringsOfferType[1]);
            mBinding.tvSpinnerOfferType.setTag(stringsOfferType[1]);
            mBinding.checkboxOno.setVisibility(View.GONE);
            mBinding.llPriceView.setVisibility(View.GONE);
            mBinding.llLowPriceView.setVisibility(View.VISIBLE);
            mBinding.llHighPriceView.setVisibility(View.VISIBLE);
            mBinding.editLowprice.setText(ActivityUtils.trimTextEmpty(livestock.getPriceLow()));
            mBinding.editHighprice.setText(ActivityUtils.trimTextEmpty(livestock.getPriceHigh()));

        } else {
            mBinding.checkboxOno.setVisibility(View.GONE);
            mBinding.tvSpinnerOfferType.setText("");
            mBinding.tvSpinnerOfferType.setTag(null);
        }
        mBinding.checkboxUnerOffer.setChecked(livestock.getUnder_offer() != null && !livestock.getUnder_offer().equals(0));

        // Available date
        if (livestock.getAvailable_at() != null) {
            mAvailableTime = TimeUtils.string2Time(livestock.getAvailable_at(), "yyyy-MM-dd");
            mBinding.tvAvailableDate.setTag(TimeUtils.time2String(mAvailableTime, "yyyy-MM-dd"));
            mBinding.tvAvailableDate.setText(TimeUtils.time2String(mAvailableTime, "dd MMMM yy"));
            //    mBinding.tvAvailableDate.setText(TimeUtils.time2String(mAvailableTime));
            //    mBinding.tvAvailableDate.setTag(TimeUtils.time2ApiString(mAvailableTime));
        } else {
            mBinding.tvAvailableDate.setText("");
            mAvailableTime = null;
            mBinding.tvAvailableDate.setTag(null);
        }

        mBinding.checkboxASAP.setChecked(livestock.getAsap() != null && !livestock.getAsap().equals(0));
        if (livestock.getEarliest_at() != null) {
            livestock.setEarliest_at(TimeUtils.dateTimeSpiltGetDate(livestock.getEarliest_at()));
            mEarliestTime = TimeUtils.string2Time(livestock.getEarliest_at(), "yyyy-MM-dd");
            mBinding.tvEarliestDate.setTag(TimeUtils.time2String(mEarliestTime, "yyyy-MM-dd"));
            mBinding.tvEarliestDate.setText(TimeUtils.time2String(mEarliestTime, "dd MMMM yy"));
        } else {

            mBinding.tvEarliestDate.setText("");
            mEarliestTime = null;
            mBinding.tvEarliestDate.setTag(null);
        }
        if (livestock.getLatest_at() != null) {
            livestock.setLatest_at(TimeUtils.dateTimeSpiltGetDate(livestock.getLatest_at()));
            mLatestTime = TimeUtils.string2Time(livestock.getLatest_at(), "yyyy-MM-dd");
            mBinding.tvLatestDate.setTag(TimeUtils.time2String(mLatestTime, "yyyy-MM-dd"));
            mBinding.tvLatestDate.setText(TimeUtils.time2String(mLatestTime, "dd MMMM yy"));
        } else {
            mBinding.tvLatestDate.setText("");
            mLatestTime = null;
            mBinding.tvLatestDate.setTag(null);
        }

        // Animal Category
        if (livestock.getLivestockCategoryId() != null) {
            LivestockCategory animal = mDatabase.livestockCategoryDao().getItemById(livestock.getLivestockCategoryId());
            mBinding.tvSpinnerAnimal.setTag(animal);
            mBinding.tvSpinnerAnimal.setText(animal.getName());
            Boolean isHorse = animal.getName().toLowerCase().equals("horse")
                    || animal.getName().toLowerCase().equals("dog");
            isHourseOrDog(isHorse);

        } else {
            mBinding.tvSpinnerAnimal.setText("");
            mBinding.tvSpinnerAnimal.setTag(null);
        }

        // Animal breed
        if (livestock.getLivestockSubCategoryId() != null) {
            LivestockSubCategory breed = mDatabase.livestockSubCategoryDao().getItemById(livestock.getLivestockSubCategoryId());
            mBinding.tvSpinnerBreed.setTag(breed);
            mBinding.tvSpinnerBreed.setText(breed.getName());
        } else {
            mBinding.tvSpinnerBreed.setTag(null);
            mBinding.tvSpinnerBreed.setText("");
        }


        // Gender
        mBinding.tvSpinnerSex.setTag(null);
        mBinding.tvSpinnerSex.setText("");
        if (livestock.getGender_id() != null) {
            GenderEntity genderEntity = mDatabase.genderEntityDao().getItemById(livestock.getGender_id());
            if (genderEntity != null) {
                mBinding.tvSpinnerSex.setTag(genderEntity);
                mBinding.tvSpinnerSex.setText(genderEntity.getName());
            }
            mBinding.frameLayoutPregnancy.setVisibility(View.VISIBLE);
        } else {
            mBinding.frameLayoutGender.setVisibility(View.GONE);
        }

        // pregnancy
        mBinding.tvSpinnerPregnancyStatus.setTag(null);
        mBinding.tvSpinnerPregnancyStatus.setText("");
        if (livestock.getPregnancy_status_id() != null) {
            PregnancyEntity pregnancyEntity = mDatabase.pregnancyEntityDao().getItemById(livestock.getPregnancy_status_id());
            if (pregnancyEntity != null) {
                mBinding.tvSpinnerPregnancyStatus.setTag(pregnancyEntity);
                mBinding.tvSpinnerPregnancyStatus.setText(pregnancyEntity.getName());
            }
            mBinding.frameLayoutPregnancy.setVisibility(View.VISIBLE);
        } else {
            mBinding.frameLayoutPregnancy.setVisibility(View.GONE);
        }


        if (livestock.getAge() != null) {
            mBinding.editMinAge.setText(String.valueOf(livestock.getAge()));
        } else {
            mBinding.editMinAge.setText("");
        }

        if (livestock.getMax_age() != null) {
            mBinding.editMaxAge.setText(String.valueOf(livestock.getMax_age()));
        } else {
            mBinding.editMaxAge.setText("");
        }

        if (livestock.getAverage_age() != null) {
            mBinding.editAverageAge.setText(String.valueOf(livestock.getAverage_age()));
        } else {
            mBinding.editAverageAge.setText("");
        }


        if (livestock.getAge_type() != null) {
            mBinding.tvAgeUnit.setText(livestock.getAge_type());
            mBinding.tvAgeUnit.setTag(livestock.getAge_type());
        } else {
            mBinding.tvAgeUnit.setText("");
            mBinding.tvAgeUnit.setTag(null);
        }

        // Quantity
        if (livestock.getQuantity() != null) {
            mBinding.editQuantity.setText(String.valueOf(livestock.getQuantity()));
        } else {
            mBinding.editQuantity.setText("");
        }

/*        // GenderEntity
        if (livestock.getSex() != null) {
            mBinding.tvSpinnerSex.setText(String.valueOf(livestock.getSex()));
            mBinding.tvSpinnerSex.setTag(livestock.getSex());
            if (livestock.getSex().toLowerCase().equals(getString(R.string.txt_female).toLowerCase())) {
                mBinding.frameLayoutPregnancy.setVisibility(View.VISIBLE);
                mBinding.tvSpinnerPregnancyStatus.setVisibility(View.VISIBLE);
                mBinding.tvPregnancy.setVisibility(View.VISIBLE);
                if (livestock.getPregnancyStatus() != null) {
                    mBinding.tvSpinnerPregnancyStatus.setText(livestock.getPregnancyStatus());
                    mBinding.tvSpinnerPregnancyStatus.setTag(livestock.getPregnancyStatus());
                } else {
                    mBinding.tvSpinnerPregnancyStatus.setText("");
                    mBinding.tvSpinnerPregnancyStatus.setTag(null);

                }
            } else {
                mBinding.frameLayoutPregnancy.setVisibility(View.GONE);
                mBinding.tvSpinnerPregnancyStatus.setVisibility(View.GONE);
                mBinding.tvPregnancy.setVisibility(View.GONE);
                mBinding.tvSpinnerPregnancyStatus.setText("");
                mBinding.tvSpinnerPregnancyStatus.setTag(null);
            }
        } else {
            mBinding.tvSpinnerSex.setText("");
            mBinding.tvSpinnerSex.setTag(null);
            mBinding.frameLayoutPregnancy.setVisibility(View.GONE);
            mBinding.tvSpinnerPregnancyStatus.setVisibility(View.GONE);
            mBinding.tvPregnancy.setVisibility(View.GONE);
            mBinding.tvSpinnerPregnancyStatus.setText("");
            mBinding.tvSpinnerPregnancyStatus.setTag(null);
        }*/


        if (livestock.getLowestWeight() != null) {
            mBinding.editWeightMin.setText(String.valueOf(livestock.getLowestWeight()));
        } else {
            mBinding.editWeightMin.setText("");
        }

        if (livestock.getHighestWeight() != null) {
            mBinding.editWeightMax.setText(String.valueOf(livestock.getHighestWeight()));
        } else {
            mBinding.editWeightMax.setText("");
        }

        if (livestock.getAverageWeight() != null) {
            mBinding.editWeightAverage.setText(String.valueOf(livestock.getAverageWeight()));
        } else {
            mBinding.editWeightAverage.setText("");
        }

        if (livestock.getDuration() != null) {
            mBinding.tvDuration.setText(ActivityUtils.trimTextEmpty(livestock.getDuration()));
            mBinding.tvDuration.setTag(livestock.getDuration());
        }
        // mBinding.checkboxDetition.setChecked(DataConverter.Inter2Boolean(livestock.getDentition()));
        mBinding.checkboxWeighed.setChecked(DataConverter.Inter2Boolean(livestock.getWeighed()));
        mBinding.checkboxEu.setChecked(DataConverter.Inter2Boolean(livestock.getEu()));
        mBinding.checkboxMouthed.setChecked(DataConverter.Inter2Boolean(livestock.getMouthed()));
        mBinding.checkboxMsa.setChecked(DataConverter.Inter2Boolean(livestock.getMsa()));
        mBinding.checkboxLpa.setChecked(DataConverter.Inter2Boolean(livestock.getLpa()));
        mBinding.checkboxOrganic.setChecked(DataConverter.Inter2Boolean(livestock.getOrganic()));
        mBinding.checkboxVendorBred.setChecked(DataConverter.Inter2Boolean(livestock.getVendorBred()));
        mBinding.checkboxPcas.setChecked(DataConverter.Inter2Boolean(livestock.getPcas()));

        mBinding.checkboxGrassFed.setChecked(DataConverter.Inter2Boolean(livestock.getGrassFed()));
        mBinding.checkboxAntibioticFree.setChecked(DataConverter.Inter2Boolean(livestock.getAntibioticFree()));
        mBinding.checkboxLifeTimeTraceable.setChecked(DataConverter.Inter2Boolean(livestock.getLifeTimeTraceable()));
        mBinding.checkboxOneMark.setChecked(DataConverter.Inter2Boolean(livestock.getOneMark()));
        mBinding.checkboxHgpFree.setChecked(DataConverter.Inter2Boolean(livestock.getHgpFree()));

        mBinding.editAccessedBy.setText(ActivityUtils.trimTextEmpty(livestock.getAssessedBy()));
        // Initialize the Media of delete list
        if (livestock.getMedia() != null) {
            mMediaListToDelete.addAll(livestock.getMedia());
        }
    }

    private void isHourseOrDog(Boolean isHorse) {
        mBinding.tvAge.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.editMinAge.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.tvMaxAge.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.editMaxAge.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.tvWeightMinKg.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.editWeightMin.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.tvWeightMaxKg.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.editWeightMax.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.tvWeightAverage.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.editWeightAverage.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.tvWeightMaxKg.setVisibility(isHorse ? View.GONE : View.VISIBLE);
        mBinding.frameLayoutPregnancy.setVisibility(isHorse ? View.GONE : View.VISIBLE);
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        //getString(R.string.txt_create_ad)
        return new CustomerToolbar(toolbarTitle, mExitListener,
                null, null,
                null, null);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        if (mViewModel.getmLiveDataPostLivestock().getValue() == null) {
            mViewModel.updatePostLivestocks(mLivestock);
        }
        subscription();
    }

    public void subscription() {
        mViewModel.getmLiveDataMediaList().observe(this, new Observer<List<Media>>() {
            @Override
            public void onChanged(@Nullable List<Media> mediaList) {
                if (mediaList != null) {
                    if (mBinding.list.getAdapter() != null) {
                        mBinding.list.getAdapter().notifyDataSetChanged();
                    } else {
                        mBinding.list.setAdapter(new PhotoRecyclerViewAdapter((MainActivity) getActivity(), mediaList, new PhotoRecyclerViewAdapter.OnClickPhotoInterface() {
                            @Override
                            public void deletePhoto(Media media) {
                                mViewModel.removePhoto(media);
                            }

                            @Override
                            public void setPrimaryImage(Media media) {
                                mViewModel.setPrimaryPhoto(media);
                            }
                        }));
                    }
                }
            }
        });

        mViewModel.getmLDPostLivestockLocation().observe(this, new Observer<Place>() {
            @Override
            public void onChanged(@Nullable Place place) {
                mPlace = place;
                if (place != null) {
                    mBinding.tvChooseALocation.setText(place.getAddress());
                } else {
                    mBinding.tvChooseALocation.setText("");
                }
            }
        });

        mViewModel.getmLiveDataPostLivestock().observe(this, new Observer<EntityLivestock>() {
            @Override
            public void onChanged(@Nullable EntityLivestock advertisement) {
                mViewModel.setMediaList(advertisement == null || advertisement.getMedia() == null ? new ArrayList<>() : advertisement.getMedia());
                if (advertisement != null) {
                    initUI(advertisement);
                }
            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LivestockInterface) {
            mListener = (LivestockInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof AutoLocationInterface) {
            mLocationListener = (AutoLocationInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AutoLocationInterface");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mLocationListener = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.updatePostLivestocks(new EntityLivestock());
        // Delete all cached  photo file
        for (Media media : mViewModel.getmLiveDataMediaList().getValue()) {
            AppUtil.deleteFile(media.getUrl());
        }
        mViewModel.setMediaList(null);
        mViewModel.clearDeleteMediaList();
        mMediaListToDelete.clear();
        mLivestock = null;
    }

    public EntityLivestock updateLivestock() {
        EntityLivestock newLivestock = new EntityLivestock();
        String title = mBinding.editTitle.getText().toString();
        String des = mBinding.editDescription.getText().toString();
        if (!ActivityUtils.checkInput(getContext(), title, getString(R.string.txt_title))) {
            return null;
        }
        newLivestock.setName(title);

        newLivestock.setDescription(StringUtils.isEmpty(des) ? null : des);


        if (mBinding.tvSpinnerProducer.getTag() != null) {
            newLivestock.setProducerId(((User) mBinding.tvSpinnerProducer.getTag()).getId());
        }
/*        String strStartDate = mBinding.tvStartDate.getText().toString();
        if (StringUtils.isEmpty(strStartDate)) {
            Toast.makeText(getContext(), getString(R.string.txt_start_time_is_required), Toast.LENGTH_SHORT).show();
            return null;
        }
        if (mStartTime.isBefore(new DateTime())) {
            showWarningDialog(getContext(), getContext().getString(R.string.app_name), getString(R.string.txt_start_before_than_now));
            return null;
        }
        newLivestock.setStart_date((String) mBinding.tvStartDate.getTag());*/

        newLivestock.setUnder_offer(mBinding.checkboxUnerOffer.isChecked() ? 1 : 0);

        String strAvailableDate = mBinding.tvAvailableDate.getText().toString();
        String strEarliestDate = mBinding.tvEarliestDate.getText().toString();
        String strLatestDate = mBinding.tvLatestDate.getText().toString();

        if (!StringUtils.isEmpty(strAvailableDate)) {
            newLivestock.setAvailable_at((String) mBinding.tvAvailableDate.getTag());
        }
        if (!StringUtils.isEmpty(strEarliestDate)) {
            newLivestock.setEarliest_at((String) mBinding.tvEarliestDate.getTag());
        }
        if (!StringUtils.isEmpty(strLatestDate)) {
            newLivestock.setLatest_at((String) mBinding.tvLatestDate.getTag());
        }

        newLivestock.setAsap(mBinding.checkboxASAP.isChecked() ? 1 : 0);
        if (mBinding.checkboxASAP.isChecked()) {

            newLivestock.setEarliest_at(null);
            newLivestock.setLatest_at(null);
        }
        String strPriceType = mBinding.tvSpinnerPriceType.getText().toString();
        if (!StringUtils.isEmpty(strPriceType)) {
            newLivestock.setPrice_type(strPriceType);
        }


        String[] offerTypeArray = getResources().getStringArray(R.array.array_offer_type);
        String[] offerTypeArraySever = getResources().getStringArray(R.array.array_offer_type_sever_side);
        String price = mBinding.editPrice.getText().toString();
        String strOfferType = mBinding.tvSpinnerOfferType.getText().toString();
        if (StringUtils.isEmpty(strOfferType)) {
            newLivestock.setOffer_type(null);
            newLivestock.setOno(null);
            newLivestock.setPriceLow(null);
            newLivestock.setPriceHigh(null);
        } else if (strOfferType.equals(offerTypeArray[0])) {
            newLivestock.setOffer_type(offerTypeArraySever[0]);
            newLivestock.setOno(mBinding.checkboxOno.isChecked() ? 1 : 0);
            newLivestock.setPrice(StringUtils.isEmpty(price) ? null : price);
            newLivestock.setDollar_price(StringUtils.isEmpty(price) ? null : price);
            newLivestock.setPriceLow(null);
            newLivestock.setPriceHigh(null);
        } else if (strOfferType.equals(offerTypeArray[1])) {
            newLivestock.setOffer_type(offerTypeArraySever[1]);
            newLivestock.setOno(null);
            newLivestock.setPrice("0.00");
            newLivestock.setDollar_price(null);
            String strPriceLow = mBinding.editLowprice.getText().toString();
            String strPriceHigh = mBinding.editHighprice.getText().toString();
            if (!StringUtils.isEmpty(strPriceLow) || !StringUtils.isEmpty(strPriceHigh)) {
                newLivestock.setPriceLow(strPriceLow);
                newLivestock.setPriceHigh(strPriceHigh);
            }

        } else {
            newLivestock.setOffer_type(null);
            newLivestock.setOno(null);
            newLivestock.setPrice("0.00");
            newLivestock.setDollar_price(null);
        }


        if (mUiMode.equals(AGR_MODE_NEW) && mLivestock.getId() == null) {
            DateTime dateTime = new DateTime();
            newLivestock.setStart_date(TimeUtils.LocalDateTime2UTC(dateTime, "yyyy-MM-dd HH:mm:ss"));
            newLivestock.setEnd_date(TimeUtils.LocalDateTime2UTC(dateTime.plusDays(AppConstants.DEFAULT_DURATION_DAY), "yyyy-MM-dd HH:mm:ss"));
        } else {
            newLivestock.setStart_date(mLivestock.getStart_date());
            newLivestock.setEnd_date(mLivestock.getEnd_date());
            newLivestock.setDuration(mLivestock.getDuration());
        }




/*

        String strDurationDay = mBinding.tvDuration.getText().toString();
        if (!ActivityUtils.checkInput(getContext(), strDurationDay, getString(R.string.txt_ad_duration))) {
            return null;
        }
        newLivestock.setDuration((Integer) mBinding.tvDuration.getTag());
*/

        //newLivestock.setDuration(AppConstants.DEFAULT_DURATION_DAY);


        //newLivestock.setEnd_date(TimeUtils.time2ApiString(mStartTime.plusDays(AppConstants.DEFAULT_DURATION_DAY)));


        String strAnimal = mBinding.tvSpinnerAnimal.getText().toString();
        if (!ActivityUtils.checkInput(getContext(), strAnimal, getString(R.string.txt_animal))) {
            return null;
        }
        newLivestock.setLivestockCategoryId(((LivestockCategory) mBinding.tvSpinnerAnimal.getTag()).getId());

        if (!ActivityUtils.checkInput(getContext(), mBinding.tvSpinnerBreed.getText().toString(), getString(R.string.txt_breed))) {
            return null;
        }
        LivestockSubCategory subCategory = (LivestockSubCategory) mBinding.tvSpinnerBreed.getTag();

        newLivestock.setLivestockSubCategoryId(subCategory == null ? null : subCategory.getId());


        String strAgeType = mBinding.tvAgeUnit.getText().toString();
/*        if (!ActivityUtils.checkInput(getContext(), strAgeType, getString(R.string.txt_age_unit))) {
            return null;
        }*/
        newLivestock.setAge_type(StringUtils.isEmpty(strAgeType) ? null : strAgeType);


        String strMinAge = mBinding.editMinAge.getText().toString();
        /*if (!ActivityUtils.checkInput(getContext(), strMinAge, getString(R.string.txt_min_age))) {
            return null;
        }*/
        try {
            newLivestock.setAge(Integer.valueOf(strMinAge));
        } catch (NumberFormatException e) {
            newLivestock.setAge(0);
        }


        String strMaxAge = mBinding.editMaxAge.getText().toString();
        /*if (!ActivityUtils.checkInput(getContext(), strMaxAge, getString(R.string.txt_max_age))) {
            return null;
        }*/
        try {
            newLivestock.setMax_age(Integer.valueOf(strMaxAge));
        } catch (NumberFormatException e) {
            newLivestock.setMax_age(0);
        }

        String strAverageAge = mBinding.editAverageAge.getText().toString();
/*        if (!ActivityUtils.checkInput(getContext(), strAverageAge, getString(R.string.txt_average_age))) {
            return null;
        }*/
        try {
            newLivestock.setAverage_age(Integer.valueOf(strAverageAge));
        } catch (NumberFormatException e) {
            newLivestock.setAverage_age(null);
        }

        String strQuantity = mBinding.editQuantity.getText().toString();
/*        if (!ActivityUtils.checkInput(getContext(), strQuantity, getString(R.string.txt_quantity))) {
            return null;
        }*/
        try {
            newLivestock.setQuantity(Integer.valueOf(strQuantity));
        } catch (NumberFormatException e) {
            newLivestock.setQuantity(null);
        }


        newLivestock.setGender_id(mSelectGender == null ? null : mSelectGender.getId());
        newLivestock.setPregnancy_status_id(mSelectPregnancy == null ? null : mSelectPregnancy.getId());

        String strMinWeight = mBinding.editWeightMin.getText().toString();
/*        if (!ActivityUtils.checkInput(getContext(), strMinWeight, getString(R.string.txt_weight_min_kg))) {
            return null;
        }*/
        try {
            newLivestock.setLowestWeight(Float.valueOf(strMinWeight));
        } catch (NumberFormatException e) {
            newLivestock.setLowestWeight(null);
        }


        String strMaxWeight = mBinding.editWeightMax.getText().toString();
/*        if (!ActivityUtils.checkInput(getContext(), strMaxWeight, getString(R.string.txt_weight_max_kg))) {
            return null;
        }*/
        try {
            newLivestock.setHighestWeight(Float.valueOf(strMaxWeight));
        } catch (NumberFormatException e) {
            newLivestock.setHighestWeight(null);
        }


        String strAvarageWeight = mBinding.editWeightAverage.getText().toString();
/*        if (!ActivityUtils.checkInput(getContext(), strAvarageWeight, getString(R.string.txt_weight_average))) {
            return null;
        }*/
        try {
            newLivestock.setAverageWeight(Float.valueOf(strAvarageWeight));
        } catch (NumberFormatException e) {
            newLivestock.setAverageWeight(null);
        }

        //newLivestock.setDentition(mBinding.checkboxDetition.isChecked() ? 1 : 0);
        newLivestock.setMouthed(mBinding.checkboxMouthed.isChecked() ? 1 : 0);
        newLivestock.setMsa(mBinding.checkboxMsa.isChecked() ? 1 : 0);
        newLivestock.setLpa(mBinding.checkboxLpa.isChecked() ? 1 : 0);
        newLivestock.setOrganic(mBinding.checkboxOrganic.isChecked() ? 1 : 0);
        newLivestock.setPcas(mBinding.checkboxPcas.isChecked() ? 1 : 0);
        newLivestock.setWeighed(mBinding.checkboxWeighed.isChecked() ? 1 : 0);
        newLivestock.setEu(mBinding.checkboxEu.isChecked() ? 1 : 0);

        newLivestock.setVendorBred(mBinding.checkboxVendorBred.isChecked() ? 1 : 0);
        newLivestock.setGrassFed(mBinding.checkboxGrassFed.isChecked() ? 1 : 0);
        newLivestock.setAntibioticFree(mBinding.checkboxAntibioticFree.isChecked() ? 1 : 0);
        newLivestock.setLifeTimeTraceable(mBinding.checkboxLifeTimeTraceable.isChecked() ? 1 : 0);
        newLivestock.setOneMark(mBinding.checkboxOneMark.isChecked() ? 1 : 0);
        newLivestock.setHgpFree(mBinding.checkboxHgpFree.isChecked() ? 1 : 0);


        if (mPlace != null) {
            newLivestock.setmPlace(mPlace);
            newLivestock.setAddress(mPlace.getAddress());
            newLivestock.setAddressLineOne(mPlace.getAddress());
            if (mPlace.getLatLng() != null) {
                newLivestock.setLat(mPlace.getLatLng().latitude);
                newLivestock.setLng(mPlace.getLatLng().longitude);
            }
            Geocoder mGeocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                List<Address> addresses = mGeocoder.getFromLocation(mPlace.getLatLng().latitude, mPlace.getLatLng().longitude, 1);
                if (addresses.get(0).getPostalCode() != null) {
                    String zipCode = addresses.get(0).getPostalCode();
                    Log.d(TAG, "updateLivestock: " + zipCode);
                    if (zipCode != null) {
                        newLivestock.setAddressPostcode(zipCode);
                    }
                }

                if (addresses.get(0).getLocality() != null) {
                    String city = addresses.get(0).getLocality();
                    if (city != null) {
                        newLivestock.setAddressCity(city);
                    }
                }

                if (addresses.get(0).getAdminArea() != null) {
                    String state = addresses.get(0).getAdminArea();
                    state = "";
                }

                if (addresses.get(0).getCountryName() != null) {
                    String country = addresses.get(0).getCountryName();

                }
            } catch (Exception e) {

            }
        } else {
            if (mLivestock != null) {
                newLivestock.setAddress(mLivestock.getAddress());
                newLivestock.setLat(mLivestock.getLat());
                newLivestock.setLng(mLivestock.getLng());
                newLivestock.setAddressPostcode(mLivestock.getAddressPostcode());
                newLivestock.setAddressLineOne(mLivestock.getAddressLineOne());
                newLivestock.setAddressLineTwo(mLivestock.getAddressLineTwo());
            }
        }

        if (!StringUtils.isEmpty(mBinding.editAccessedBy.getText().toString())) {
            newLivestock.setAssessedBy(mBinding.editAccessedBy.getText().toString());
        }

        UserType userType = mAppUtil.getUserType();
        if (userType == UserType.AGENT) {
            newLivestock.setAgentId(mAppUtil.getUserId());
            /*User producer = (User) mBinding.tvSpinnerProducer.getTag();
            if (producer != null) {
                newLivestock.setProducerId(producer.getId());
            } else {

            }*/
            User user = mDatabase.userDao().getUserById(mAppUtil.getUserId());
            newLivestock.setAgent(user);
        } else {
            newLivestock.setAgentId(mAppUtil.getAgentId());
            newLivestock.setProducerId(mAppUtil.getUserId());
            User user = mDatabase.userDao().getUserById(mAppUtil.getAgentId());
            newLivestock.setAgent(user);
        }
        List<Media> mediaList = mViewModel.getmLiveDataMediaList().getValue();
        if (mediaList != null) {
            newLivestock.setMedia(mediaList);
        }
        for (Media media : mediaList) {
            for (Media mediaOld : mMediaListToDelete) {
                if (mediaOld.getUrl().equals(media.getUrl())) {
                    mMediaListToDelete.remove(mediaOld);
                    break;
                }
            }
        }
        return newLivestock;
    }


    @Override
    public void OnSelectedItem(LivestockCategory animal) {
        if (!animal.equals(mSelectAnimal)) {
            mBinding.tvSpinnerAnimal.setText(animal.getName());
            mBinding.tvSpinnerAnimal.setTag(animal);
            mBinding.tvSpinnerBreed.setText("");
            mBinding.tvSpinnerBreed.setTag(null);
            /*
            List<LivestockSubCategory> subCategoryList = mDatabase.livestockSubCategoryDao().loadSpecificArray(animal.getId());
            if (subCategoryList != null && subCategoryList.size() > 0) {
                LivestockSubCategory selectSubCategory = subCategoryList.get(0);
                mBinding.tvSpinnerBreed.setText(selectSubCategory.getName());
                mBinding.tvSpinnerBreed.setTag(selectSubCategory);
            }*/
        }
        mSelectAnimal = animal;

        mGenderList = mDatabase.genderEntityDao().loadAllByCategoryExceptAll(mSelectAnimal.getId());
        if (mGenderList == null || mGenderList.size() == 0) {
            mBinding.frameLayoutGender.setVisibility(View.GONE);
            mBinding.tvSpinnerSex.setText("");
            mBinding.tvSpinnerSex.setTag(null);
        } else {
            mBinding.frameLayoutGender.setVisibility(mGenderList == null || mGenderList.size() == 0 ? View.GONE : View.VISIBLE);
            mBinding.tvSpinnerSex.setText("");
            mBinding.tvSpinnerSex.setTag(null);
        }
        mPregnancyList = mDatabase.pregnancyEntityDao().loadAllByCategoryExceptAll(mSelectAnimal.getId());

        if (mPregnancyList == null || mPregnancyList.size() == 0) {
            mBinding.frameLayoutPregnancy.setVisibility(View.GONE);
            mBinding.tvSpinnerPregnancyStatus.setText("");
            mBinding.tvSpinnerPregnancyStatus.setTag(null);
        } else {
            mBinding.frameLayoutPregnancy.setVisibility(View.VISIBLE);
            mBinding.tvSpinnerPregnancyStatus.setText("");
            mBinding.tvSpinnerPregnancyStatus.setTag(null);
        }
        mSelectGender = null;
        mSelectPregnancy = null;
        Boolean isHorse = mSelectAnimal.getName().toLowerCase().equals("horse")
                || mSelectAnimal.getName().toLowerCase().equals("dog");
        isHourseOrDog(isHorse);


    }

    @Override
    public void OnSelectedItem(LivestockSubCategory breed) {
        mBinding.tvSpinnerBreed.setText(breed.getName());
        mBinding.tvSpinnerBreed.setTag(breed);
        mSelectBreed = breed;
    }

    @Override
    public void OnSelectedItem(GenderEntity genderEntity) {
        mBinding.tvSpinnerSex.setText(genderEntity.getName());
        mBinding.tvSpinnerSex.setTag(genderEntity);
        mSelectGender = genderEntity;
    }

    @Override
    public void OnSelectedItem(PregnancyEntity pregnancyEntity) {
        mBinding.tvSpinnerPregnancyStatus.setText(pregnancyEntity.getName());
        mBinding.tvSpinnerPregnancyStatus.setTag(pregnancyEntity);
        mSelectPregnancy = pregnancyEntity;
    }

    @Override
    public void OnSelectedItem(User user) {
        mBinding.tvSpinnerProducer.setText(ActivityUtils.trimTextEmpty(user.getFullName()));
        mBinding.tvSpinnerProducer.setTag(user);
    }

    @Override
    public void OnSelectedItem(Integer day) {
        mBinding.tvDuration.setText(ActivityUtils.trimTextEmpty(day));
        mBinding.tvDuration.setTag(day);
    }

}
