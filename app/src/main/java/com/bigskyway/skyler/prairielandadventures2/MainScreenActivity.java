package com.bigskyway.skyler.prairielandadventures2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class MainScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);

    }

    public void launchFirstLevel (View view) {
        Log.i("Launching Screen", "Level One Step 1: Verbs");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_verbs.csv");
        startActivity(intent);
    }

    public void launchFirstLevelNounsCat1 (View view) {
        Log.i("Launching Screen", "Level One Step 2: Animals");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_Nouns_animal.csv");
        startActivity(intent);
    }

    public void launchFirstLevelNounsCat2 (View view) {
        Log.i("Launching Screen", "Level One Step 3: Clothing");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_Nouns_clothing.csv");
        startActivity(intent);
    }

    public void launchFirstLevelNounsCat3 (View view) {
        Log.i("Launching Screen", "Level One Step 4: Days");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_Nouns_days.csv");
        startActivity(intent);
    }

    public void launchFirstLevelNounsCat4 (View view) {
        Log.i("Launching Screen", "Level One Step 4: Family");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_Nouns_family.csv");
        startActivity(intent);
    }

    public void launchFirstLevelNounsCat5 (View view) {
        Log.i("Launching Screen", "Level One Step 6: General Nouns");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_Nouns_general.csv");
        startActivity(intent);
    }

    public void launchFirstLevelNounsCat6 (View view) {
        Log.i("Launching Screen", "Level One Step 7: Home");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_Nouns_home.csv");
        startActivity(intent);
    }

    public void launchFirstLevelNounsCat7 (View view) {
        Log.i("Launching Screen", "Level One Step 8: Meals");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_Nouns_meal.csv");
        startActivity(intent);
    }

    public void launchFirstLevelAdv (View view) {
        Log.i("Launching Screen", "Level One Step 9: Adverbs");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_adv.csv");
        startActivity(intent);
    }

    public void launchFirstLevelNumbers (View view) {
        Log.i("Launching Screen", "Level One Step 10: Numbers");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_numbers.csv");
        startActivity(intent);
    }

    public void launchFirstLevelAdj (View view) {
        Log.i("Launching Screen", "Level One Step 11: Adjectives");
        Intent intent = new Intent(this, FirstLevel.class);
        intent.putExtra("fileToLoad", "SpanishVocab_adj.csv");
        startActivity(intent);
    }


}
