package com.bigskyway.skyler.prairielandadventures2;

import com.bigskyway.skyler.prairielandadventures2.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        PublisherAdView mPublisherAdView = (PublisherAdView) findViewById(R.id.publisherAdView);
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mPublisherAdView.loadAd(adRequest);

    }

    public void launchGame(View view) {
        Log.i("Launching Screen", "Main Game Screen");
        Intent intent = new Intent(this, MainScreenActivity.class);
        startActivity(intent);
    }
    public void launchShop(View view) {
        Log.i("Launching Screen", "Shop");
        Intent intent = new Intent(this, Shop.class);
        startActivity(intent);
    }

    public void launchMultiplayer(View view) {
//        Toast toast = Toast.makeText(getApplicationContext(), "Functionality not available yet", Toast.LENGTH_LONG);
 //       toast.show();
        Log.i("Launching Screen", "Multiplayer Main Menu");
        Intent intent = new Intent(this, MultiplayerMenuActivity.class);
        startActivity(intent);

    }

    public void quitGame(View view) {
        finish();
    }


}
