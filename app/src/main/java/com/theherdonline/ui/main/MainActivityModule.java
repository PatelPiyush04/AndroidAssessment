package com.theherdonline.ui.main;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.theherdonline.di.ActivityScoped;
import com.theherdonline.di.FragmentScoped;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.di.ViewModelKey;
import com.theherdonline.ui.herdLive.CreateSaleyardStreamFragment;
import com.theherdonline.ui.herdLive.GoLiveLoadingFragment;
import com.theherdonline.ui.herdLive.GoLiveStreamingFragment;
import com.theherdonline.ui.herdLive.HerdLiveDetailFragment;
import com.theherdonline.ui.herdLive.HerdLiveFragment;
import com.theherdonline.ui.main.myad.AdTopFragment;
import com.theherdonline.ui.main.myad.CreateLivestockFragment;
import com.theherdonline.ui.main.myad.MyAdFragment;
import com.theherdonline.ui.main.myad.PrimeSaleyardsFragment;
import com.theherdonline.ui.main.payment.AddCardFragment;
import com.theherdonline.ui.main.payment.PaymentFragment;
import com.theherdonline.ui.main.poddocksearch.FilterDistanceFragment;
import com.theherdonline.ui.main.poddocksearch.LivestockListFragment;
import com.theherdonline.ui.main.poddocksearch.PoddockSearchFilterFragment;
import com.theherdonline.ui.main.poddocksearch.SearchLivestockResultFragment;
import com.theherdonline.ui.main.saleyardsearch.SaleyardListFragment;
import com.theherdonline.ui.main.saleyardsearch.SaleyardSearchFilterFragment;
import com.theherdonline.ui.main.saleyardsearch.SearchResultSaleyardFragment;
import com.theherdonline.ui.makebid.BidDetailFragment;
import com.theherdonline.ui.makebid.MakeBidFragment;
import com.theherdonline.ui.marketReport.CreateMarketReportFragment;
import com.theherdonline.ui.marketReport.CreateMarketReportViewModel;
import com.theherdonline.ui.marketReport.MarketReportDetailFragment;
import com.theherdonline.ui.marketReport.MarketReportFragment;
import com.theherdonline.ui.marketReport.SelectSyleyardFragment;
import com.theherdonline.ui.notification.backup.NotificationBackupFragment;
import com.theherdonline.ui.notification.normal.NotificationFragment;
import com.theherdonline.ui.notification.normal.NotificationPagerViewModel;
import com.theherdonline.ui.organisation.OrganisationFragment;
import com.theherdonline.ui.postad.PostLivestockFragment;
import com.theherdonline.ui.postad.PostPrimSaleyardFragment;
import com.theherdonline.ui.profile.AccountFragment;
import com.theherdonline.ui.profile.BidListFragment;
import com.theherdonline.ui.profile.EditProfileFragment;
import com.theherdonline.ui.profile.ProfileOptionsFragment;
import com.theherdonline.ui.profile.UserProfileFragment;
import com.theherdonline.ui.profile.UserProfileFragmentViewModel;
import com.theherdonline.ui.profile.mybid.MyBidFragment;
import com.theherdonline.ui.stream.CreatePostFragment;
import com.theherdonline.ui.stream.StreamFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;


@Module
public abstract class MainActivityModule {

    @ActivityScoped
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MainActivityViewModel viewModelNew);




   /* @ActivityScoped
    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModelNew.class)
    abstract ViewModel bindChatViewModelNew(ChatViewModelNew viewModelNew);*/


    @ActivityScoped
    @Binds
    @IntoMap
    @ViewModelKey(UserProfileFragmentViewModel.class)
    abstract ViewModel bindUserProfileFragmentViewModel(UserProfileFragmentViewModel viewModelNew);


    @ActivityScoped
    @Binds
    @IntoMap
    @ViewModelKey(NotificationPagerViewModel.class)
    abstract ViewModel bindNotificationPagerViewModelV1(NotificationPagerViewModel viewModelNew);


    @ActivityScoped
    @Binds
    @IntoMap
    @ViewModelKey(CreateMarketReportViewModel.class)
    abstract ViewModel bindCreateMarketReportViewModel(CreateMarketReportViewModel viewModelNew);


    @ActivityScoped
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory var1);


    @FragmentScoped
    @ContributesAndroidInjector
    abstract ReportFragment provideReportFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract StreamFragment provideStreamFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MeFragment provideMeFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract RequestLoginFragment provideRequestLoginFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract NetworkErrorFragment provideNetworkErrorFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HomeTopFragment provideSearchCategoryFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MapLivestockFragment provideMapLivestoceFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MapSaleyardFragment provideMapSaleyardFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract BidListFragment provideBidListFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract UserProfileFragment provideUserProfileFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract MarketReportFragment provideMarketReportFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MarketReportDetailFragment provideMarketReportDetailFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract CreateMarketReportFragment provideCreateMarketReportFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract GoLiveLoadingFragment provideGoLiveLoadingFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract GoLiveStreamingFragment provideGoLiveStreamingFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HerdLiveFragment provideHerdLiveFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract HerdLiveDetailFragment provideHerdLiveDetailFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract CreateSaleyardStreamFragment provideCreateSaleyardStreamFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract SelectSyleyardFragment provideSelectSyleyardFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract BidDetailFragment provideBidDetailFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract MakeBidFragment provideMakeBidFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PaymentFragment providePaymentFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract AddCardFragment provideAddCardFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyAdFragment provideMyAdFragmentNew();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract CreateLivestockFragment provideCreateLivestockFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchLivestockResultFragment provideSearchLivestockResultFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract FilterDistanceFragment provideFilterDistanceFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract PoddockSearchFilterFragment providePoddockSearchFilterFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract SearchResultSaleyardFragment provideSearchResultSaleyardFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract SaleyardSearchFilterFragment provideSaleyardSearchFilterFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract SaleyardListFragment provideSaleyardListFragmentNew();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract SaleyardDetailFragment provideSaleyardDetailFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract EditProfileFragment provideEditProfileFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract MyBidFragment provideMyBidFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract PostLivestockFragment providePostLivestockFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract CreatePostFragment provideCreatePostFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract AccountFragment provideAccountFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract ProfileOptionsFragment provideProfileOptionsFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract MapMarkerFragment provideMapMarkerFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract LivestockListFragment provideLivestockListFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract OrganisationFragment provideOrganisationFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract AdTopFragment AdTopFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract PrimeSaleyardsFragment PrimeSaleyardsFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract PostPrimSaleyardFragment PostPrimSaleyardFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract NotificationBackupFragment NotificationFragment();


    @FragmentScoped
    @ContributesAndroidInjector
    abstract NotificationFragment NotificationFragmentV1();


}