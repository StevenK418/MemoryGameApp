package com.example.memorygameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity
{
    //Create a new soundManager Instance
    SoundManager soundManager = new SoundManager(1.0f, MainActivity.this);

    private final int RED = 1;
    private final int YELLOW = 2;
    private final int GREEN = 3;
    private final int BLUE = 4;

    Button btnRed, btnYellow, btnGreen, btnBlue, activeButton;
    int sequenceCount = 4, n = 0;
    private Object mutex = new Object();
    int[] gameSequence = new int[120];
    int counter = 0;
    int sequenceLength = 4;


    TextView timeDisplay;

    ScoreManager scoremanager;
    DatabaseManager dbManager;

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

        Intent intent = getIntent();

        int[] previousSequence = intent.getIntArrayExtra("previousSequence");
        sequenceLength = intent.getIntExtra("sequenceLength", 0);

        if(sequenceLength == 0)
        {
            sequenceLength = sequenceCount;
        }

        for(int i = 0; i < sequenceCount; i++)
        {
            gameSequence[i] = getRandom(sequenceCount);
        }

        //Add some test data to db
        ScoreManager.db = new DatabaseManager(getApplicationContext());
        ScoreManager.AddTestScoreData();
    }

    public void doPlay(View view) {
        ct.start();
    }

    // return a number between 1 and maxValue
    private int getRandom(int maxValue)
    {
        return ((int) ((Math.random() * maxValue) + 1));
    }

    private void oneButton(int id)
    {
        Toast.makeText(this, "Number = " + n, Toast.LENGTH_SHORT).show();

        switch (id) {
            case 1:
                flashButton(btnRed);
                gameSequence[counter++] = RED;
                soundManager.PlaySound(1);
                break;
            case 2:
                flashButton(btnYellow);
                gameSequence[counter++] = YELLOW;
                soundManager.PlaySound(2);
                break;
            case 3:
                flashButton(btnGreen);
                gameSequence[counter++] = GREEN;
                soundManager.PlaySound(3);
                break;
            case 4:
                flashButton(btnBlue);
                gameSequence[counter++] = BLUE;
                soundManager.PlaySound(4);
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
    CountDownTimer ct = new CountDownTimer(6000,  1000)
    {
        public void onTick(long millisUntilFinished)
        {
            timeDisplay.setText("Time Left: " + millisUntilFinished / 1500);
            n = gameSequence[counter];
            oneButton(n);
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
        }
    };

    public void switchToPlayerGameScreen()
    {
        //Create new intent instance for the high score table
        Intent playerGameScreen = new Intent(this, PlayerGameScreen.class);
        playerGameScreen.putExtra("gameSequence", gameSequence);
        startActivity(playerGameScreen);
    }
}