package com.example.memorygameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;

public class NewHighScore extends AppCompatActivity {

    TextView scoreValue;
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_high_score);
        getSupportActionBar ().hide ();

        scoreValue = findViewById(R.id.txtScoreValue);
        name = findViewById(R.id.txtName);
        Intent intent = getIntent();
        int score = intent.getIntExtra("scoreValue", 0);
        scoreValue.setText(String.valueOf(score));
    }

    //Save the high score to the database
    public void doSaveHighScore(View view)
    {
        //Get the name from the user
        ScoreManager.userName = String.valueOf(name.getText());
        ScoreManager.AddHighscore();

        //Load the high score table:
        Intent highscorescreen = new Intent(this, HighScoreTable.class);
        startActivity(highscorescreen);
    }
}