package com.bigskyway.skyler.prairielandadventures;

import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class FirstLevel extends ActionBarActivity {
    int health = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_level);
        TextView txtHealth = (TextView) findViewById(R.id.TxtHealth);
        txtHealth.setText("Health: " + Integer.toString(health));
        findViewById(R.id.ivsnake3).setOnTouchListener(mSnakeTouch);

        Map<String,String> test = getWords("spanish_words.txt");
        Log.i("hola", test.get("hola"));

    }


    View.OnTouchListener mSnakeTouch = new View.OnTouchListener()  {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ImageView ivSnake = (ImageView) v;
          ivSnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthree));
//                toggleAlpha(v);
  //              shakeImage(v);
    //            changeImage (v);

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
            words.put(wordList[i], wordList[i+1]);
        }

        return words;
    }
}
