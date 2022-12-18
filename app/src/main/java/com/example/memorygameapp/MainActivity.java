package com.example.memorygameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {


    private final int RED = 1;
    private final int YELLOW = 2;
    private final int GREEN = 3;
    private final int BLUE = 4;

    Button btnRed, btnYellow, btnGreen, btnBlue, activeButton;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar ().hide ();

        //Database setup
        Context mContext = getApplicationContext();

        btnRed = findViewById(R.id.btnRed);
        btnYellow = findViewById(R.id.btnYellow);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);

        timeDisplay = findViewById(R.id.txt_Time);

        //TODO: testing db
        scoremanager = new ScoreManager(mContext);
        scoremanager.buildNewHighScore("TestUser", 1500);
        scoremanager.AddTestScoreData();



    }

    public void doPlay(View view) {
        ct.start();
    }

    // return a number between 1 and maxValue
    private int getRandom(int maxValue)
    {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    private void oneButton()
    {
        n = getRandom(sequenceCount);

        Toast.makeText(this, "Number = " + n, Toast.LENGTH_SHORT).show();

        switch (n) {
            case 1:
                flashButton(btnRed);
                gameSequence[counter++] = RED;
                break;
            case 2:
                flashButton(btnYellow);
                gameSequence[counter++] = YELLOW;
                break;
            case 3:
                flashButton(btnGreen);
                gameSequence[counter++] = GREEN;
                break;
            case 4:
                flashButton(btnBlue);
                gameSequence[counter++] = BLUE;
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
            oneButton();
            //here you can have your logic to set text to edittext
        }

        public void onFinish()
        {
            //mTextField.setText("done!");
            // we now have the game sequence

            for (int i = 0; i< counter; i++)
            {
                Log.d("game sequence", String.valueOf(gameSequence[i]));
            }

            //Switch to the player Game screen
            switchToPlayerGameScreen();

            // start next activity

            // put the sequence into the next activity
            // stack overglow https://stackoverflow.com/questions/3848148/sending-arrays-with-intent-putextra
            //Intent i = new Intent(A.this, B.class);
            //i.putExtra("numbers", array);
            //startActivity(i);

            // start the next activity
            // int[] arrayB = extras.getIntArray("numbers");
        }
    };

    public void switchToPlayerGameScreen()
    {
        //Create new intent instance for the high score table
        Intent playerGameScreen = new Intent(this, PlayerGameScreen.class);
        playerGameScreen.putExtra("gameSequence", gameSequence);
        startActivity(playerGameScreen);
    }

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