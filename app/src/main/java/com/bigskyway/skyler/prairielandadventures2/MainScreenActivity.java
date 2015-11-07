package com.bigskyway.skyler.prairielandadventures2;

import com.bigskyway.skyler.prairielandadventures.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainScreenActivity extends Activity {

    public String sFileToRead;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);

    }



public void launchFirstLevel (View view) {
    Log.i("Launching Screen", "Level One");
    sFileToRead = "spanish_verbs.csv";
    Intent intent = new Intent(this, FirstLevel.class);
    startActivity(intent);
}

    public void launchFirstLevel_Nouns (View view) {
        Log.i("Launching Screen", "Level One, Step 2");
        sFileToRead = "SpanishVocab_AllNouns.csv";
        Intent intent = new Intent(this, FirstLevel.class);
        startActivity(intent);
    }

}
