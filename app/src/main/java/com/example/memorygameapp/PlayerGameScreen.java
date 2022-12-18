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
import android.widget.TextView;

public class PlayerGameScreen extends AppCompatActivity {

    private final int RED = 1;
    private final int YELLOW = 2;
    private final int GREEN = 3;
    private final int BLUE = 4;

    Button btnRed, btnYellow, btnGreen, btnBlue, activeButton;
    int sequenceCount = 4, n = 0;
    int[] gameSequence = new int[120];
    int[] userGameSequence = new int[120];
    int counter = 0;

    TextView timeDisplay;
    ScoreManager scoremanager;
    DatabaseManager dbManager;

    boolean isTimeUp = false;
    boolean isInPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_game_screen);
        getSupportActionBar ().hide ();

        //Database setup
        Context mContext = getApplicationContext();

        btnRed = findViewById(R.id.btnRed);
        btnYellow = findViewById(R.id.btnYellow);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);

        timeDisplay = findViewById(R.id.txt_Time);

//        //TODO: testing db
//        scoremanager = new ScoreManager(mContext);
//        scoremanager.buildNewHighScore("TestUser", 1500);
//        scoremanager.AddTestScoreData();

        //Create new intent instance
        Intent intent = getIntent();

        //Get and store the gamesequence from the main activity
        gameSequence = intent.getIntArrayExtra("gameSequence");

        for (int i=0;i < counter;i++)
        {
            Log.d("sequence", String.valueOf(gameSequence[i]));
        }

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
            if(isInPlay == false)
            {
                ct.start();
                isInPlay = true;
            }


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
                flashButton(btnYellow);
                userGameSequence[counter++] = RED;
                break;
            case 2:
                flashButton(btnRed);
                userGameSequence[counter++] = YELLOW;
                break;
            case 3:
                flashButton(btnGreen);
                userGameSequence[counter++] = GREEN;
                break;
            case 4:
                flashButton(btnBlue);
                userGameSequence[counter++] = BLUE;
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
            timeDisplay.setText("seconds remaining: " + millisUntilFinished / 1500);
        }

        public void onFinish()
        {
            timeDisplay.setText("TIME'S UP!");
            PrintResult();
            isTimeUp = true;
            isInPlay = false;
        }
    };

    public void doHighScoreTable(View view)
    {
        //Create new intent instance for the high score table
        Intent summaryPage = new Intent(this, HighScoreTable.class);
        startActivity(summaryPage);
    }

    public void PrintResult()
    {

        for (int i = 0; i< userGameSequence.length; i++)
        {
            Log.d("ATTENTION:", "Entered the onFinishLoop");
            Log.d("You entered: ", String.valueOf(userGameSequence[i]));
            Log.d("game sequence", String.valueOf(gameSequence[i]));
            timeDisplay.setText("Entered the For loop in onFinish");
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