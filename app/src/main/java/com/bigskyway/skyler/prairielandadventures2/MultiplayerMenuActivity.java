package com.bigskyway.skyler.prairielandadventures2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.bigskyway.skyler.prairielandadventures2.util.BaseGameActivity;
import com.bigskyway.skyler.prairielandadventures2.util.BaseGameUtils;
import com.bigskyway.skyler.prairielandadventures2.util.GameHelper;
import com.google.android.gms.games.Games;


public class MultiplayerMenuActivity extends BaseGameActivity implements EnableGooglePlayFragment.Listener {

    final String TAG = "MultiplayerMenuActivity";

    private boolean mSignInClicked;

    private EnableGooglePlayFragment enableGooglePlayFragment;
    private int RC_UNUSED = 5001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiplayer_main_screen);

        enableGooglePlayFragment = (EnableGooglePlayFragment) getFragmentManager().findFragmentById(R.id.fragmentGooglePlaySwitch);
        enableGooglePlayFragment.setListener(this);

    }

    public void launchUnit21FirstChallenge(View view) {
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

    public void showAchievements(View view) {
        if (isSignedIn()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.achievements_not_available)).show();
        }
    }

    public void showLeaderboards(View view) {
        if (isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()),
                    RC_UNUSED);
        } else {
            BaseGameUtils.makeSimpleDialog(this, getString(R.string.leaderboards_not_available)).show();
        }
    }


    @Override
    public void onConnectToGooglePlay() {
        Log.i(TAG, "onConnectToGooglePlay");
        mSignInClicked = true;

        if (!getApiClient().isConnected())
            getGameHelper().beginUserInitiatedSignIn();

    }

    @Override
    public void onDisconnectFromGooglePlay() {
        Log.i(TAG, "onDisconnectingFromGooglePlay:");
        getGameHelper().signOut();
        mSignInClicked = false;
    }


    @Override
    protected void onStart() {
        super.onStart();
        enableGooglePlayFragment.setIsPlayEnabled(getApiClient().isConnected());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onSignInFailed() {
        Log.i(TAG, "onSignInFailed:");
        enableGooglePlayFragment.setIsPlayEnabled(getApiClient().isConnected());
    }

    @Override
    public void onSignInSucceeded() {

        Log.i(TAG, "onSignInSucceeded:");
        enableGooglePlayFragment.setIsPlayEnabled(getApiClient().isConnected());
    }

}
