package com.bigskyway.skyler.prairielandadventures2;

import android.app.Application;

/**
 * Created by jim on 11/21/2015.
 */
public class PrairieLandsApplication extends Application {



    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the singletons so their instances
        // are bound to the application process.
        initSingletons();
    }

    protected void initSingletons()
    {
        // Initialize the instance of MySingleton
//        MySingleton.initInstance();
    }


    @Override
    public void onTerminate() {
        // Do your application wise Termination task
        super.onTerminate();
    }
}
