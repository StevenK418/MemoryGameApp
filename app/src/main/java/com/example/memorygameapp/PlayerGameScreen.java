package com.example.memorygameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerGameScreen extends AppCompatActivity {

    private final int BLUE = 1;
    private final int RED = 2;
    private final int YELLOW = 3;
    private final int GREEN = 4;

    Button bRed, bBlue, bYellow, bGreen, activeButton;
    int sequenceCount = 4, n = 0;
    private Object mutex = new Object();
    int[] gameSequence = new int[120];
    int[] userGameSequence = new int[120];
    int counter = 0;

    TextView timeDisplay;


    ScoreManager scoremanager;

    DatabaseManager dbManager;

    boolean isCpuPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_game_screen);
        getSupportActionBar ().hide ();

        //Database setup
        Context mContext = getApplicationContext();

        bRed = findViewById(R.id.btnRed);
        bBlue = findViewById(R.id.btnBlue);
        bYellow = findViewById(R.id.btnYellow);
        bGreen = findViewById(R.id.btnGreen);

        timeDisplay = findViewById(R.id.txt_Time);

        //TODO: testing db
        scoremanager = new ScoreManager(mContext);
        scoremanager.buildNewHighScore("TestUser", 1500);
        scoremanager.AddTestScoreData();

        //Create new intent instance
        Intent intent = getIntent();

        //Get and store the gamesequence from the main activity
        gameSequence = intent.getIntArrayExtra("gameSequence");
    }

    public void doPlay(View view) {
        ct.start();
    }

    // return a number between 1 and maxValue
    private int getRandom(int maxValue)
    {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    public void doInput(View view)
    {
        ct.start();

        int id = view.getId();

        switch(id)
        {
            case 1:
                oneButton(1);
                break;
            case 2:
                oneButton(2);
                break;
            case 3:
                oneButton(3);
                break;
            case 4:
                oneButton(4);
                break;
            default:
                break;
        }
    }

    private void oneButton(int buttonID)
    {
        switch (buttonID) {
            case 1:
                flashButton(bBlue);
                userGameSequence[counter++] = BLUE;
                break;
            case 2:
                flashButton(bRed);
                userGameSequence[counter++] = RED;
                break;
            case 3:
                flashButton(bYellow);
                userGameSequence[counter++] = YELLOW;
                break;
            case 4:
                flashButton(bGreen);
                userGameSequence[counter++] = GREEN;
                break;
            default:
                break;
        }   // end switch
    }

    private void flashButton(Button button)
    {
        activeButton = button;
        String buttonText = String.valueOf(activeButton.getText());
        Handler handler = new Handler();
        Runnable r = new Runnable()
        {
            public void run()
            {
                activeButton.setPressed(true);
                activeButton.invalidate();
                activeButton.performClick();
                Handler handler1 = new Handler();
                Runnable r1 = new Runnable()
                {
                    public void run()
                    {
                        activeButton.setPressed(false);
                        activeButton.invalidate();
                    }
                };
                handler1.postDelayed(r1, 600);

            } // end runnable
        };
        handler.postDelayed(r, 600);
    }

    /*Timer*/
    CountDownTimer ct = new CountDownTimer(6000,  1500)
    {
        public void onTick(long millisUntilFinished)
        {
            isCpuPlaying = true;
            timeDisplay.setText("seconds remaining: " + millisUntilFinished / 1500);
        }

        public void onFinish()
        {

            for (int i = 0; i< counter; i++)
            {
                Log.d("ATTENTION:", "Entered the onFinishLoop");
                Log.d("You entered: ", String.valueOf(userGameSequence[i]));
                Log.d("game sequence", String.valueOf(gameSequence[i]));
            }

            //Check the sequence input against the one from the main activity

            boolean result =  CheckPlayersInputSequence(userGameSequence);

            if(result == true)
            {
                timeDisplay.setText("YOU WIN");
            }
            else
            {
                timeDisplay.setText("YOU LOSE!");
            }
        }
    };

    public void doHighScoreTable(View view)
    {
        //Create new intent instance for the high score table
        Intent summaryPage = new Intent(this, HighScoreTable.class);
        startActivity(summaryPage);
    }

    /**
     * Checks the sequence inputby the user matches
     * the sequence shown
     * @param userGameSequence
     */
    public boolean CheckPlayersInputSequence(int[] userGameSequence)
    {
        boolean doesMatch = false;
        boolean[] results = new boolean[gameSequence.length];

        for (int i = 0; i < gameSequence.length; i++) {
            for (int j = 0; j < userGameSequence.length; j++) {
                if (gameSequence[i] == userGameSequence[j]) {
                    results[i] = true;
                } else {
                    results[i] = false;
                }
            }
        }

        for (int i = 0; i < results.length; i++)
        {
            if(results[i] == true)
            {
                doesMatch = true;
                continue;
            }
            else
            {
                doesMatch=false;
                break;
            }
        }
        return doesMatch;
    }

    /**
     * Adds the user's input to the game sequence
     * @param input
     */
    public void RegisterInput(int input)
    {
        userGameSequence[counter++] = input;
    }
}