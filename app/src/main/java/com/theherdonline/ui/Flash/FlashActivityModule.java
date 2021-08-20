package com.theherdonline.ui.Flash;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.theherdonline.di.ActivityScoped;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.di.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class FlashActivityModule {

    @ActivityScoped
    @Binds
    @IntoMap
    @ViewModelKey(FlashActivityViewModel.class)
    abstract ViewModel bindHomeActivityViewModel(FlashActivityViewModel viewModelNew);

    @ActivityScoped
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory var1);
}
