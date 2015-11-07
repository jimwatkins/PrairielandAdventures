package com.bigskyway.skyler.prairielandadventures2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigskyway.skyler.prairielandadventures.R;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class FirstLevel extends ActionBarActivity {
    int health = 100;
    int snakeHealth = 100;
    Map<String,String> wordMap;
    int timer = 5;
    Handler timeHandler;
    TextView correctView;
    int roundLength = 8;
    boolean matchOver = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_level);
        updateHealth();
        findViewById(R.id.ivsnake3).setOnClickListener(mSnakeClick);
        findViewById(R.id.ivsnake2).setOnClickListener(mSnakeClick);
        findViewById(R.id.ivsnake1).setOnClickListener(mSnakeClick);
        wordMap = getWords( ((MainScreenActivity) this.getParent()).sFileToRead);

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
        if (health == 0) {

            builder.setMessage("Oops, the snakes won")
                    .setTitle("Defeat")
                    .setPositiveButton("Ok", finishDialogueListener);
            dialog = builder.create();
            dialog.show();
        }
        else if (snakeHealth == 0) {
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

        ImageView ivSnake = (ImageView) findViewById(R.id.ivsnake1);
        ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
        ivSnake = (ImageView) findViewById(R.id.ivsnake2);
        ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));
        ivSnake = (ImageView) findViewById(R.id.ivsnake3);
        ivSnake.setBackground(getResources().getDrawable(R.drawable.startersnaketwo));

    }

    private void initializeWords() {
        String englishWord = "";
        englishWord = getRandomEnglishWord();

        String spanishWord;
        spanishWord = wordMap.get(englishWord);

        Log.i(spanishWord, englishWord);

        TextView englishTextView = (TextView) findViewById(R.id.txtEnglishAnswer);
        englishTextView.setText(englishWord);

        correctView = getRandomSnakeTextView();
        correctView.setText(spanishWord);

        // assign wrong words
        assignWrongWords(spanishWord,correctView);

    }


    private void updateTimer() {
        TextView txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtTimer.setText("Time: " + Integer.toString(timer));
    }

    private void updateHealth() {
        TextView txtHealth = (TextView) findViewById(R.id.TxtHealth);
        txtHealth.setText("Health: " + Integer.toString(health));
        TextView txtSnakeHealth = (TextView) findViewById(R.id.txtSnakeHealth);
        txtSnakeHealth.setText("Health: " + Integer.toString(snakeHealth));
    }

    final Runnable timeCheck = new Runnable()
    {
        @Override
        public void run() {
            // check if I should be running
            if (matchOver) return;

            if (timer > 0) {
                timer = timer - 1;
                updateTimer();
                moveSnake()
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
            health = health - 10;
        }
        else {
            snakeHealth = snakeHealth - 10;
        }

        if (health == 0 || snakeHealth == 0) {
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

        textView = (TextView) findViewById(R.id.textView1);
        if(textView != correctView) {
            textView.setText(getRandomSpanishWord(spanishWord));
        }

        textView = (TextView) findViewById(R.id.textView2);
        if(textView != correctView) {
            textView.setText(getRandomSpanishWord(spanishWord));
        }

        textView = (TextView) findViewById(R.id.textView3);
        if(textView != correctView) {
            textView.setText(getRandomSpanishWord(spanishWord));
        }
    }

    public String getRandomEnglishWord() {
        int len = wordMap.values().size();
        int i = randInt( 0, len - 1 );
        String word = (String) wordMap.keySet().toArray()[i];
        return word;
    }

    public String getRandomSpanishWord(String skipWord) {

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

    public TextView getRandomSnakeTextView() {
        int r = randInt (1,3);
        TextView txtView;

        if (r == 1)
            txtView = (TextView) findViewById(R.id.textView1);
        else if (r==2)
            txtView = (TextView) findViewById(R.id.textView2);
        else
            txtView = (TextView) findViewById(R.id.textView3);

        return txtView;
    }

    public void moveSnake() {
        int r = randInt (1,3);
        TextView txtView;

        if (r == 1)
            txtView = (TextView) findViewById(R.id.textView1);
        else if (r==2)
            txtView = (TextView) findViewById(R.id.textView2);
        else
            txtView = (TextView) findViewById(R.id.textView3);

        txtView.setPaddingRelative(5,5,0,0);
    }

    public static int randInt(int min, int max) {

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
            if (ivSnake.getId() == R.id.ivsnake1) {
                if ( R.id.textView1 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

            if (ivSnake.getId() == R.id.ivsnake2) {
                if ( R.id.textView2 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

            if (ivSnake.getId() == R.id.ivsnake3) {
                if ( R.id.textView3 == correctView.getId() ) {
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
            if (ivSnake.getId() == R.id.ivsnake1) {
                if ( R.id.textView1 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

            if (ivSnake.getId() == R.id.ivsnake2) {
                if ( R.id.textView2 == correctView.getId() ) {
                    roundOver(true);
                }
                else {
                    roundOver(false);
                }
            }

            if (ivSnake.getId() == R.id.ivsnake3) {
                if ( R.id.textView3 == correctView.getId() ) {
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
