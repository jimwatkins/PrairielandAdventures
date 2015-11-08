package com.bigskyway.skyler.prairielandadventures2;

import com.bigskyway.skyler.prairielandadventures.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);

    }

    public void launchFirstLevelNouns (View view) {
        Log.i("Launching Screen", "Level One Step 2");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_AllNouns.csv");
        startActivity(intent);
    }


    public void launchFirstLevel (View view) {
        Log.i("Launching Screen", "Level One");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "spanish_verbs.csv");
        startActivity(intent);
    }

}
