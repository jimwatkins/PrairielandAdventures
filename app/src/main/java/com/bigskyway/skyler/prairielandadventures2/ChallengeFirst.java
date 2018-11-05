package com.bigskyway.skyler.prairielandadventures2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigskyway.skyler.prairielandadventures2.util.BaseGameActivity;
import com.bigskyway.skyler.prairielandadventures2.util.BaseGameUtils;
import com.bigskyway.skyler.prairielandadventures2.util.GameHelper;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ChallengeFirst extends BaseGameActivity   {
    final String TAG = "ChallengeFirst";

    int health = 100;
    int snakeHealth = 100;
    Map<String,String> wordMap;
    int timer;
    Handler timeHandler;
    TextView correctView;
    int roundLength = 10;
    boolean matchOver = true;
    boolean bMoveBack = false;
    int iEngWordCount;
    int iDamageAmount = 25;
    int iSnakeAttackDamage = 15;
    int iTotalTime;
    int iTotalPoints;
    int iBeginPoints = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge_first_ach);
        updateHealth();
        findViewById(R.id.imgSnake3).setOnClickListener(mSnakeClick);
        findViewById(R.id.imgSnake2).setOnClickListener(mSnakeClick);
        findViewById(R.id.imgSnake1).setOnClickListener(mSnakeClick);
        String sWords = this.getIntent().getStringExtra("fileToLoad");
        wordMap = getWords( sWords );
//        wordMap = getWords("spanish_verbs.csv");



        // start time handler
        timeHandler = new Handler();
        timeHandler.postDelayed(timeCheck, 1000);
        startMatch();
    }


    private void startMatch() {
        matchOver = false;
        health = 100;
        snakeHealth = 100;
        resetRound();
    }

    private void gameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = null;
        matchOver = true;
        if (health <= 0) {

            builder.setMessage("Oops, the snakes won in " + iTotalTime + " seconds with " + snakeHealth + " health!")
                    .setTitle("Defeat")
                    .setPositiveButton("Ok", finishDialogueListener);
            dialog = builder.create();
            dialog.show();
        }
        else if (snakeHealth <= 0) {
            iTotalPoints = (iBeginPoints-iTotalTime) + health;
            if (health == 100) {
                iTotalPoints = iTotalPoints+100;
                builder.setMessage("Congratulations, you won in " + iTotalTime + " seconds and earned a 100 point bonus for losing no health!  Total Points:" + iTotalPoints)
                        .setTitle("Victory")
                        .setNeutralButton("Post Score", postScoreListener)
                        .setPositiveButton("Ok", finishDialogueListener);
                dialog = builder.create();
                dialog.show();
            }
            else {
                    builder.setMessage("Congratulations, you won in " + iTotalTime + " seconds with " + health + " health!  Total Points:" + iTotalPoints)
                            .setTitle("Victory")
                            .setNeutralButton("Post Score", postScoreListener)
                            .setPositiveButton("Ok", finishDialogueListener);
                    dialog = builder.create();
                    dialog.show();
                }
        }
    }

    protected DialogInterface.OnClickListener postScoreListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            postScore();
        }
    };
    

    private void postScore() {

        if (!getGameHelper().isSignedIn()) {
            getGameHelper().beginUserInitiatedSignIn();
        }

        if (getApiClient().isConnected()) {
            Games.Leaderboards.submitScore(getApiClient(), "CgkIoJniztIdEAIQCA", iTotalPoints);
            Toast toast = Toast.makeText(getApplicationContext(), "Score published to leaderboard.", Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(), "Unable to published to leaderboard, please check connection", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected DialogInterface.OnClickListener finishDialogueListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finishMatch();
        }
    };

    private void finishMatch() {
        matchOver = true;
        finish();
    }


    private void resetRound() {

        timer = roundLength;
        updateTimer();
        initializeWords();

        ImageView ivSnake = (ImageView) findViewById(R.id.imgSnake1);
        ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
        ivSnake = (ImageView) findViewById(R.id.imgSnake2);
        ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
        ivSnake = (ImageView) findViewById(R.id.imgSnake3);
        ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));

    }

    private void initializeWords() {
        String englishWord = "";
        englishWord = getEnglishWord();

        String spanishWord;
        spanishWord = wordMap.get(englishWord);

        Log.i(spanishWord, englishWord);

        TextView englishTextView = (TextView) findViewById(R.id.txtEnglishAnswer);
        englishTextView.setText(englishWord);

        correctView = getRandomSnakeTextView();
        correctView.setText(spanishWord);

        // assign wrong words
        assignWrongWords(spanishWord, correctView);

    }


    private void updateTimer() {
        TextView txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtTimer.setText("Time: " + Integer.toString(timer));
    }

    private void updateHealth() {
        TextView txtHealth = (TextView) findViewById(R.id.TxtHealth);
        txtHealth.setText("Your Health: " + Integer.toString(health));
        TextView txtSnakeHealth = (TextView) findViewById(R.id.txtSnakeHealth);
        txtSnakeHealth.setText("Snake Health: " + Integer.toString(snakeHealth));
    }

    final Runnable timeCheck = new Runnable()
    {
        @Override
        public void run() {
            // check if I should be running

            bMoveBack = ! bMoveBack;

            if (matchOver) return;

            if (timer > 0) {
                timer = timer - 1;
                updateTimer();
                if (timer%2==0) {
                    moveSnakes();
                }
                timeHandler.postDelayed(timeCheck, 1000);
            }
            else if (timer == 0 ) {
                roundOver(false);
                timeHandler.postDelayed(timeCheck, 1000);
            }
        }
    };

    private void roundOver(boolean didPlayerWin) {
        if (didPlayerWin == false) {
            health = health - iDamageAmount;
        }
        else {
            snakeHealth = snakeHealth - iDamageAmount;
        }

        iTotalTime = iTotalTime + (roundLength - timer);
        if (health <= 0 || snakeHealth <= 0) {
            updateHealth();
            gameOver();
        }
        else {
            updateHealth();
            resetRound();
        }
    }

    private void assignWrongWords(String spanishWord, TextView correctView) {

        TextView textView;

        textView = (TextView) findViewById(R.id.txtVSpan1);
        if(textView != correctView) {
            textView.setText(getRandomSpanishWord(spanishWord));
        }

        textView = (TextView) findViewById(R.id.txtVSpan2);
        if(textView != correctView) {
            textView.setText(getRandomSpanishWord(spanishWord));
        }

        textView = (TextView) findViewById(R.id.txtVSpan3);
        if(textView != correctView) {
            textView.setText(getRandomSpanishWord(spanishWord));
        }
    }

    private String getEnglishWord() {
        int len = wordMap.values().size();
        //int i = randInt(0, len - 1);
        //String word = (String) wordMap.keySet().toArray()[i];
        String word = (String) wordMap.keySet().toArray()[iEngWordCount];
        if (iEngWordCount == len -1)
            iEngWordCount = 0;
        else
            iEngWordCount = iEngWordCount + 1;
        return word;
    }

    private String getRandomSpanishWord(String skipWord) {

        int i = 0;
        i = randInt( 0, wordMap.values().size() - 1 );

        String word = new String();
        word = (String) wordMap.values().toArray()[i];

        while (word.equals(skipWord)) {
            i = randInt( 0, wordMap.values().size() - 1 );
            word = (String) wordMap.values().toArray()[i];
        }

        return word;
    }

    private TextView getRandomSnakeTextView() {
        int r = randInt (1,3);
        TextView txtView;

        if (r == 1)
            txtView = (TextView) findViewById(R.id.txtVSpan1);
        else if (r==2)
            txtView = (TextView) findViewById(R.id.txtVSpan2);
        else
            txtView = (TextView) findViewById(R.id.txtVSpan3);

        return txtView;
    }

    private void moveSnakes() {
        int p = 0;
        int as = 0;
        int iFirstPos = 0;
        int iSecondPos = 0;
        int iAttackSnake = 0;
        boolean bAlreadyAttacked = false;
        //TextView txtView;
        ImageView ivSnake;
        FrameLayout flSnakeWord;
        FrameLayout.LayoutParams fParams;
        //FrameLayout.LayoutParams fTxtParams;

        //get random integer to decide whether or not to attack (10% chance); attack if = 1
        int a = randInt(1,10);
        //if attack, randomly determine attacking snake (1,2, or 3)
        if (a == 1) {
            iAttackSnake = randInt(1, 3);
        }

        for(int i=1; i<=3; i++) {

            //get random integer to represent position for each snake
            p = randInt(1, 8);
            //if random # was the same as the position for the 1st or 2nd snake, get another #
            while (p == iFirstPos || p == iSecondPos) {
                p = randInt(1, 8);
            }
            //obtain frame object containing snake and word based on loop index
            if (i == 1) {
                iFirstPos = p;
                flSnakeWord = (FrameLayout) findViewById(R.id.flaySnakeWord1);
                //txtView = (TextView) findViewById(R.id.txtVSpan1);
                ivSnake = (ImageView) findViewById(R.id.imgSnake1);
            }
            else if (i == 2) {
                iSecondPos = p;
                flSnakeWord = (FrameLayout) findViewById(R.id.flaySnakeWord2);
                ivSnake = (ImageView) findViewById(R.id.imgSnake2);
            }
            else  {
                flSnakeWord = (FrameLayout) findViewById(R.id.flaySnakeWord3);
                ivSnake = (ImageView) findViewById(R.id.imgSnake3);
            }

            //if current snake object is attacking snake, change image and subtract points from health
            if (iAttackSnake == i) {
                ivSnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));
                //ivSnake.setImageDrawable(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));
                //health = health - iSnakeAttackDamage;
            }

            //now get position parameters object for frame object
            fParams = (FrameLayout.LayoutParams) flSnakeWord.getLayoutParams();

            //position frame object inside parent frame based on random position (1-8)
            switch (p) {
                case 1:
                    p = 1;
                    fParams.gravity = Gravity.TOP | Gravity.LEFT;
                    break;
                case 2:
                    p = 1;
                    fParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                    break;
                case 3:
                    p = 1;
                    fParams.gravity = Gravity.RIGHT | Gravity.TOP;
                    break;
                case 4:
                    p = 1;
                    fParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
                    break;
                case 5:
                    p = 1;
                    fParams.gravity = Gravity.CENTER;
                    break;
                case 6:
                    p = 1;
                    fParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                    break;
                case 7:
                    p = 1;
                    fParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
                    break;
                case 8:
                    p = 1;
                    fParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
                    break;
            } //end switch case
        } //end for snakes 1 to 3

//        r = randInt(1,3);
//        if (r==1){
//            fParams.gravity = Gravity.TOP | Gravity.LEFT;
//            //fTxtParams.gravity = Gravity.TOP | Gravity.LEFT;
//            }
//        else if (r==2){
//            fParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
//            //fTxtParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
//            }
//        else {
//            fParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
//            //fTxtParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
//        }
//
//        //set location for second snake/word pair
//        flSnakeWord = (FrameLayout) findViewById(R.id.flaySnakeWord2);
//        //txtView = (TextView) findViewById(R.id.txtVSpan1);
//        ivSnake = (ImageView) findViewById(R.id.imgSnake2);
//        if (a == 1)
//            ivSnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));
//        else
//            ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
//        //ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
//        fParams = (FrameLayout.LayoutParams) flSnakeWord.getLayoutParams();
//        //fParams = (FrameLayout.LayoutParams) ivSnake.getLayoutParams();
//        //fTxtParams = (FrameLayout.LayoutParams) txtView.getLayoutParams();
//        r = randInt(1,2);
//        if (r==1){
//            fParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
//            //fTxtParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
//        }
//        else if (r==2){
//            fParams.gravity = Gravity.CENTER;
//            //fTxtParams.gravity = Gravity.CENTER;
//            }
//
//        //set location for third snake/word pair
//        flSnakeWord = (FrameLayout) findViewById(R.id.flaySnakeWord3);
//        //txtView = (TextView) findViewById(R.id.txtVSpan1);
//        ivSnake = (ImageView) findViewById(R.id.imgSnake3);
//        if (a == 1)
//            ivSnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));
//        else
//            ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
//        //ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
//        fParams = (FrameLayout.LayoutParams) flSnakeWord.getLayoutParams();
//        //fParams = (FrameLayout.LayoutParams) ivSnake.getLayoutParams();
//        //fTxtParams = (FrameLayout.LayoutParams) txtView.getLayoutParams();
//        r = randInt(1,3);
//        if (r==1){
//            fParams.gravity = Gravity.RIGHT | Gravity.TOP;
//            //fTxtParams.gravity = Gravity.RIGHT | Gravity.TOP;
//        }
//        else if (r==2){
//            fParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
//            //fTxtParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
//        }
//        else {
//            fParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
//            //fTxtParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
//
//        }

    }



    private static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    View.OnClickListener mSnakeClick = new View.OnClickListener()  {

        @Override
        public void onClick(View v) {
            ImageView ivSnake = (ImageView) v;
            ivSnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));

            // is the snake clicked the right snake ?
            if (ivSnake.getId() == R.id.imgSnake1) {
                if ( R.id.txtVSpan1 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

            if (ivSnake.getId() == R.id.imgSnake2) {
                if ( R.id.txtVSpan2 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

            if (ivSnake.getId() == R.id.imgSnake3) {
                if ( R.id.txtVSpan3 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

        }
    };



    View.OnTouchListener mSnakeTouch = new View.OnTouchListener()  {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ImageView ivSnake = (ImageView) v;
            ivSnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));

            // is the snake clicked the right snake ?
            if (ivSnake.getId() == R.id.imgSnake1) {
                if ( R.id.txtVSpan1 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

            if (ivSnake.getId() == R.id.imgSnake2) {
                if ( R.id.txtVSpan2 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

            if (ivSnake.getId() == R.id.imgSnake3) {
                if ( R.id.txtVSpan3 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

            return true;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first_level, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Map<String, String> getWords(String file) {
        Map<String,String> words = new HashMap<String,String>();

        AssetManager assets = getAssets();
        String line = "";

        try {
            InputStream is;
            is = assets.open(file);


            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // byte buffer into a string
             line = new String(buffer);
        }

        catch (Exception ex) {
            Log.i(ex.toString(), ex.getMessage());
        }

        String[]wordList = line.split(",");

        for (int i = 0; i < wordList.length; i = i+2) {
            words.put(wordList[i+1], wordList[i]);
        }


        return words;
    }

    @Override
    protected void onStop() {
        finishMatch();
        super.onStop();
    }

    @Override
    public void onSignInFailed() {
        BaseGameUtils.makeSimpleDialog(this, "Connecting to google playe services failed.");
    }

    @Override
    public void onSignInSucceeded() {
        Log.i(TAG, "onSignInSucceeded");
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        getGameHelper().onActivityResult(requestCode, resultCode, data);
//    }

}
