package com.bigskyway.skyler.prairielandadventures2;

import android.app.Application;
import android.content.Context;

/**
 * Created by jim on 11/21/2015.
 */
public class PrairieLandsApplication extends Application {

    private static PrairieLandsApplication application;


    @Override
    public void onCreate()
    {
        super.onCreate();
        application=this;
    }

    @Override
    public void onTerminate() {
        // Do your application wise Termination task
        super.onTerminate();
    }

    public static Application getApplication() {
        return application;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

}
