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
        implements EnableGooglePlayFragment.Listener, GameHelper.GameHelperListener,
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

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_multiplayer_main_screen);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
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
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingConnectionFailure) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingConnectionFailure = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingConnectionFailure = true;
        }
    }

    @Override
    public void onConnectToGooglePlay() {
        Log.i(TAG, "Connecting to google play");
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    @Override
    public void onDisconnectFromGooglePlay() {
        Log.i(TAG, "Disconnecting from google play");
//        Games.signOut(mGoogleApiClient);
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        mSignInClicked = false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onSignInFailed() {
        Log.i(TAG, "Sign in failed");
    }

    @Override
    public void onSignInSucceeded() {
        Log.i(TAG, "Sign in succeeded");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingConnectionFailure) {  // more about this later
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MultiplayerMenuActivity) getActivity()).onDialogDismissed();
        }
    }


}
