package com.theherdonline.di;



import com.theherdonline.ui.Flash.FlashActivity;
import com.theherdonline.ui.Flash.FlashActivityModule;
import com.theherdonline.ui.herdLive.GoLiveStreamActivity;
import com.theherdonline.ui.login.LoginActivity;
import com.theherdonline.ui.login.LoginActivityModule;
import com.theherdonline.ui.makebid.MakeBidActivity;
import com.theherdonline.ui.makebid.MakeBidActivityModule;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module ActivityBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */
@Module
public abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = FlashActivityModule.class)
    abstract FlashActivity provideFlashActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity provideMainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract LoginActivity provideLoginActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MakeBidActivityModule.class)
    abstract MakeBidActivity provideMakeBidActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract GoLiveStreamActivity provideGoLiveStreamActivity();

}
