package com.bigskyway.skyler.prairielandadventures;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class FirstLevel extends ActionBarActivity {
    int health = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_level);
        TextView txtHealth = (TextView) findViewById(R.id.TxtHealth);
        txtHealth.setText("Health: " + Integer.toString(health));
        findViewById(R.id.ivsnake3).setOnTouchListener(mSnakeTouch);

    }
    View.OnTouchListener mSnakeTouch = new View.OnTouchListener()  {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ImageView ivsnake = (ImageView) v;
          ivsnake.setBackground(getResources().getDrawable(R.drawable.attackingsnakeposthree));
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
}
