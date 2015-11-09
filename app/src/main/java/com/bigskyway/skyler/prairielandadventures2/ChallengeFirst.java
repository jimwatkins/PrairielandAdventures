package com.bigskyway.skyler.prairielandadventures2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigskyway.skyler.prairielandadventures.R;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class ChallengeFirst extends ActionBarActivity {
    int health = 100;
    int snakeHealth = 100;
    Map<String,String> wordMap;
    int timer = 4;
    Handler timeHandler;
    TextView correctView;
    int roundLength = 4;
    boolean matchOver = true;
    boolean bMoveBack = false;
    int iEngWordCount;
    int iDamageAmount = 2;
    int iSnakeAttackDamage = 15;

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
        if (health <= 0) {

            builder.setMessage("Oops, the snakes won!")
                    .setTitle("Defeat")
                    .setPositiveButton("Ok", finishDialogueListener);
            dialog = builder.create();
            dialog.show();
        }
        else if (snakeHealth <= 0) {
            builder.setMessage("Congratulations, you won!")
                    .setTitle("Victory")
                    .setPositiveButton("Ok", finishDialogueListener);
            dialog = builder.create();
            dialog.show();
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
                moveSnakes();
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
        int r = randInt (1,3);
         //TextView txtView;
        ImageView ivSnake;
        FrameLayout flSnakeWord;
        FrameLayout.LayoutParams fParams;
        //FrameLayout.LayoutParams fTxtParams;

        //get random integer to decide whether or not to attack (10% chance)
        int a = randInt(1,10);

        //set location for first snake/word pair
        flSnakeWord = (FrameLayout) findViewById(R.id.flaySnakeWord1);
        //txtView = (TextView) findViewById(R.id.txtVSpan1);
        ivSnake = (ImageView) findViewById(R.id.imgSnake1);
        if (a == 1) {
            ivSnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));
            //ivSnake.setImageDrawable(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));
            //health = health - iSnakeAttackDamage;
            }
        else
            ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
        fParams = (FrameLayout.LayoutParams) flSnakeWord.getLayoutParams();
        //fParams = (FrameLayout.LayoutParams) ivSnake.getLayoutParams();
        //fTxtParams = (FrameLayout.LayoutParams) txtView.getLayoutParams();
        r = randInt(1,3);
        if (r==1){
            fParams.gravity = Gravity.TOP | Gravity.LEFT;
            //fTxtParams.gravity = Gravity.TOP | Gravity.LEFT;
            }
        else if (r==2){
            fParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
            //fTxtParams.gravity = Gravity.CENTER_VERTICAL | Gravity.LEFT;
            }
        else {
            fParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
            //fTxtParams.gravity = Gravity.BOTTOM | Gravity.LEFT;
        }

        //set location for second snake/word pair
        flSnakeWord = (FrameLayout) findViewById(R.id.flaySnakeWord2);
        //txtView = (TextView) findViewById(R.id.txtVSpan1);
        ivSnake = (ImageView) findViewById(R.id.imgSnake2);
        if (a == 1)
            ivSnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));
        else
            ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
        //ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
        fParams = (FrameLayout.LayoutParams) flSnakeWord.getLayoutParams();
        //fParams = (FrameLayout.LayoutParams) ivSnake.getLayoutParams();
        //fTxtParams = (FrameLayout.LayoutParams) txtView.getLayoutParams();
        r = randInt(1,2);
        if (r==1){
            fParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            //fTxtParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        }
        else if (r==2){
            fParams.gravity = Gravity.CENTER;
            //fTxtParams.gravity = Gravity.CENTER;
            }

        //set location for third snake/word pair
        flSnakeWord = (FrameLayout) findViewById(R.id.flaySnakeWord3);
        //txtView = (TextView) findViewById(R.id.txtVSpan1);
        ivSnake = (ImageView) findViewById(R.id.imgSnake3);
        if (a == 1)
            ivSnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthreetwo));
        else
            ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
        //ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
        fParams = (FrameLayout.LayoutParams) flSnakeWord.getLayoutParams();
        //fParams = (FrameLayout.LayoutParams) ivSnake.getLayoutParams();
        //fTxtParams = (FrameLayout.LayoutParams) txtView.getLayoutParams();
        r = randInt(1,3);
        if (r==1){
            fParams.gravity = Gravity.RIGHT | Gravity.TOP;
            //fTxtParams.gravity = Gravity.RIGHT | Gravity.TOP;
        }
        else if (r==2){
            fParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
            //fTxtParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        }
        else {
            fParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
            //fTxtParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;

        }

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
}
