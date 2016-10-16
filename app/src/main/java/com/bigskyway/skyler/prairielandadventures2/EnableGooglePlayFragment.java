package com.bigskyway.skyler.prairielandadventures2;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnableGooglePlayFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnableGooglePlayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnableGooglePlayFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "isPlayEnabled";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String isPlayEnabled;
    private String mParam2;

    private Switch swGooglePlay;

    private Listener mListener = null;

//    GoogleApiClient mGoogleApiClient = new GoogleApiClient
//            .Builder(PrairieLandsApplication.getContext())
//            .addApi(Games.API)
//            .addConnectionCallbacks(this)
//            .build();


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public interface Listener {
        public void onConnectToGooglePlay();
        public void onDisconnectFromGooglePlay();
//        public void onStartGameRequested(boolean hardMode);
//        public void onShowLeaderboardsRequested();
//        public void onSignInButtonClicked();
//        public void onSignOutButtonClicked();
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EnableGooglePlayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EnableGooglePlayFragment newInstance(String param1, String param2) {
        EnableGooglePlayFragment fragment = new EnableGooglePlayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EnableGooglePlayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isPlayEnabled = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    public void setIsPlayEnabled(boolean b) {
            swGooglePlay.setChecked(b);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enable_google_play, container, false);
        swGooglePlay = (Switch) view.findViewById(R.id.switchGooglePlay);
        if ( Boolean.getBoolean(isPlayEnabled) )
            swGooglePlay.setChecked(true);


        swGooglePlay.setOnClickListener(this);
        return view;
    }

    public void setListener(Listener l) {
        mListener = l;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        isPlayEnabled = Boolean.toString(swGooglePlay.isChecked());
        Log.i("Google Play State", isPlayEnabled);

//        if (swGooglePlay.isChecked() )
//            tryLogin();
//        else
//            disconnect();

        if (swGooglePlay.isChecked())
            mListener.onConnectToGooglePlay();
        else
            mListener.onDisconnectFromGooglePlay();

    }

//    private void tryLogin() {
//        mGoogleApiClient.connect();
//    }
//
//    private void disconnect() {
//        mGoogleApiClient.disconnect();
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
