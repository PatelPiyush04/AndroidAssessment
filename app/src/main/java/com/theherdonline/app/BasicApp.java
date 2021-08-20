package com.theherdonline.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;


/**
 * Created by freddie on 14/05/18.
 */

public class BasicApp extends Application implements ContextProvider {

    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                BasicApp.this.currentActivity = activity;
            }

            @Override
            public void onActivityStarted(Activity activity) {
                BasicApp.this.currentActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                BasicApp.this.currentActivity = activity;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                BasicApp.this.currentActivity = null;
            }

            @Override
            public void onActivityStopped(Activity activity) {
                // don't clear current activity because activity may get stopped after
                // the new activity is resumed
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                // don't clear current activity because activity may get destroyed after
                // the new activity is resumed
            }
        });

    }

    @Override
    public Context getActivityContext() {
        return currentActivity;
    }


}
