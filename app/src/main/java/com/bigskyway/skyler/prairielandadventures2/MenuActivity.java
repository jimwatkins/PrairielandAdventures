package com.bigskyway.skyler.prairielandadventures2;

import com.bigskyway.skyler.prairielandadventures2.util.SystemUiHider;

import android.app.Activity;
import android.app.admin.DeviceAdminInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

/**
 * This is the main Game Menu
 *
 */
public class MenuActivity extends Activity {
    private static final String TAG = "MenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Creating main menu" );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        Log.i(TAG, "MainMenu fetching ad" );
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
