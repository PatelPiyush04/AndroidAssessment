package com.theherdonline.ui.makebid;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.theherdonline.di.ActivityScoped;
import com.theherdonline.di.FragmentScoped;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.di.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;


@Module
public abstract class MakeBidActivityModule {

    @ActivityScoped
    @Binds
    @IntoMap
    @ViewModelKey(MakeBidActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(MakeBidActivityViewModel viewModelNew);

    @ActivityScoped
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory var1);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract MakeBidFragment provideMakeBidFragment();

    @FragmentScoped
    @ContributesAndroidInjector
    abstract BidDetailFragment provideBidDetailFragment();


}