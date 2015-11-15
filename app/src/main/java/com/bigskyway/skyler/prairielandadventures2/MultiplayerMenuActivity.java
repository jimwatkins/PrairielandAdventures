package com.bigskyway.skyler.prairielandadventures2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.games.Player;


public class MultiplayerMenuActivity extends FragmentActivity
        implements EnableGooglePlayFragment.Listener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    final String TAG = "MultiplayerMenuActivity";



    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Automatically start the sign-in flow when the Activity starts
    private boolean mAutoStartSignInFlow = true;

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiplayer_main_screen);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        EnableGooglePlayFragment pbf = (EnableGooglePlayFragment) getFragmentManager().findFragmentById(R.id.fragmentGooglePlaySwitch);
        pbf.setListener(this);


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
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected(): connected to Google APIs");
        // Show sign-out button on main menu
//        mMainMenuFragment.setShowSignInButton(false);

        // Show "you are signed in" message on win screen, with no sign in button.
//        mWinFragment.setShowSignInButton(false);

        // Set the greeting appropriately on main menu
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String displayName;
        if (p == null) {
            Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
            displayName = "???";
        } else {
            displayName = p.getDisplayName();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): attempting to connect");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed(): attempting to resolve");
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed(): already resolving");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;
//            if (!BaseGameUtils.resolveConnectionFailure(this, mGoogleApiClient, connectionResult,
//                    RC_SIGN_IN, getString(R.string.signin_other_error))) {
//                mResolvingConnectionFailure = false;
//            }
        }

    }

    @Override
    public void onConnectToGooglePlay() {
        Log.i(TAG, "Connecting to google play");
        mGoogleApiClient.connect();
    }
    @Override
    public void onDisconnectFromGooglePlay() {
        Log.i(TAG, "Disconnecting from google play");
        Games.signOut(mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
