package com.theherdonline.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;


@Module(includes = {AppModule.class})
public abstract class ApplicationModule {
    //expose Application as an injectable context
    @Binds
    abstract Context bindContext(Application application);

}

