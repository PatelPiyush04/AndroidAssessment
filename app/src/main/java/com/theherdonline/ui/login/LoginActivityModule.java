package com.theherdonline.ui.login;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.theherdonline.di.ActivityScoped;
import com.theherdonline.di.FragmentScoped;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.di.ViewModelKey;
/*import au.twomatesmedia.theherd.ui.register.AddressFragment;
import au.twomatesmedia.theherd.ui.register.AgentFragment;
import au.twomatesmedia.theherd.ui.register.BasicFragment;
import au.twomatesmedia.theherd.ui.register.PaymentFragment;*/
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;


@Module
public abstract class LoginActivityModule {

    @ActivityScoped
    @Binds
    @IntoMap
    @ViewModelKey(LoginActivityViewModel.class)
    abstract ViewModel bindMainActivityViewModel(LoginActivityViewModel viewModelNew);

    @ActivityScoped
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory var1);

    @FragmentScoped
    @ContributesAndroidInjector
    abstract LoginFragment provideLoginFragment();

/*    @FragmentScoped
    @ContributesAndroidInjector
    abstract BasicFragment provideBasicFragment();*/
}