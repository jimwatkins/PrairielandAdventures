package com.bigskyway.skyler.prairielandadventures;

import com.bigskyway.skyler.prairielandadventures.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class MainScreenActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);

    }



public void launchFirstLevel (View view) {
    Log.i("Launching Screen", "Level One");
    Intent intent = new Intent(this, FirstLevel.class);
    startActivity(intent);
}


}
