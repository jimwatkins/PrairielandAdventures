package com.bigskyway.skyler.prairielandadventures2;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.bigskyway.skyler.prairielandadventures2.util.BaseGameActivity;
import com.bigskyway.skyler.prairielandadventures2.util.BaseGameUtils;
import com.bigskyway.skyler.prairielandadventures2.util.GameHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.games.Player;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;

public class MultiplayerMenuActivity extends FragmentActivity
        implements EnableGooglePlayFragment.Listener, GameHelper.GameHelperListener
    {

    final String TAG = "MultiplayerMenuActivity";

    // The game helper object. This class is mainly a wrapper around this object.
    protected GameHelper mHelper;

    private boolean mDebugLog = true;
        private boolean mSignInClicked = false;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiplayer_main_screen);

        EnableGooglePlayFragment pbf = (EnableGooglePlayFragment) getFragmentManager().findFragmentById(R.id.fragmentGooglePlaySwitch);
        pbf.setListener(this);

        if (mHelper == null) {
            getGameHelper();
        }
        mHelper.setup(this);

        enableDebugLog(true);

}

    public GameHelper getGameHelper() {
        if (mHelper == null) {
            mHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
            mHelper.enableDebugLog(mDebugLog);
        }
        return mHelper;
    }

    protected GoogleApiClient getApiClient() {
        return mHelper.getApiClient();
    }


    public void launchUnit21FirstChallenge (View view) {
        Log.i("Launching Screen", "Challenge One: Vocabulary");
        Intent intent = new Intent(this, ChallengeFirst.class);
        intent.putExtra("fileToLoad", "SpanishVocab_Unit2_1.csv");
        startActivity(intent);
    }


//    public void launchUnit21SecondChallenge (View view) {
//        Log.i("Launching Screen", "Challenge Two: Demonstrative Adjectives and Pronouns");
//        Intent intent = new Intent(this, FirstLevel.class);
//        intent.putExtra("fileToLoad", "SpanishVocab_demon_adj_pro.csv");
//        startActivity(intent);
//    }
//
//    public void launchUnit21ThirdChallenge (View view) {
//        Log.i("Launching Screen", "Challenge Three: Preterite Tense");
//        Intent intent = new Intent(this, FirstLevel.class);
//        intent.putExtra("fileToLoad", "SpanishVocab_verbs_conjugation_past.csv");
//        startActivity(intent);
//    }


    @Override
    public void onConnectToGooglePlay() {
        Log.i(TAG, "onConnectToGooglePlay");
        mSignInClicked = true;

        // Make sure the app is not already connected or attempting to connect
        mHelper.beginUserInitiatedSignIn();
    }

    @Override
    public void onDisconnectFromGooglePlay() {
        Log.i(TAG, "onDisconnectingFromGooglePlay:");
        mHelper.signOut();
        mSignInClicked = false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onSignInFailed() {  Log.i(TAG, "onSignInFailed:");    }

    @Override
    public void onSignInSucceeded() {
        Log.i(TAG, "onSignInSucceeded:");
    }

    private void enableDebugLog(boolean enabled) {
        mDebugLog = true;
        if (mHelper != null) {
            mHelper.enableDebugLog(enabled);
        }
    }

}
