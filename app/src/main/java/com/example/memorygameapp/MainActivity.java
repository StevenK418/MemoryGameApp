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


    private final int BLUE = 1;
    private final int RED = 2;
    private final int YELLOW = 3;
    private final int GREEN = 4;

    Button bRed, bBlue, bYellow, bGreen, fb;
    int sequenceCount = 4, n = 0;
    private Object mutex = new Object();
    int[] gameSequence = new int[120];
    int counter = 0;

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

        bRed = findViewById(R.id.btnRed);
        bBlue = findViewById(R.id.btnBlue);
        bYellow = findViewById(R.id.btnYellow);
        bGreen = findViewById(R.id.btnGreen);

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
                flashButton(bBlue);
                gameSequence[counter++] = BLUE;
                break;
            case 2:
                flashButton(bRed);
                gameSequence[counter++] = RED;
                break;
            case 3:
                flashButton(bYellow);
                gameSequence[counter++] = YELLOW;
                break;
            case 4:
                flashButton(bGreen);
                gameSequence[counter++] = GREEN;
                break;
            default:
                break;
        }   // end switch
    }

    private void flashButton(Button button)
    {
        fb = button;
        String buttonText = String.valueOf(fb.getText());
        Handler handler = new Handler();
        Runnable r = new Runnable()
        {
            public void run()
            {
                fb.setPressed(true);
                fb.invalidate();
                fb.performClick();
                Handler handler1 = new Handler();
                Runnable r1 = new Runnable()
                {
                    public void run()
                    {
                        fb.setPressed(false);
                        fb.invalidate();
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

    public void doHighScoreTable(View view)
    {
        //Create new intent instance for the high score table
        Intent summaryPage = new Intent(this, HighScoreTable.class);
        //Pass all the analysis data to the new intent
//        summaryPage.putExtra("date", currentDate);
//        summaryPage.putExtra("distance", distance);
//        summaryPage.putExtra("calories", calories);
//        //Lastly, pass over the time
//        summaryPage.putExtra("time", time);
//        //Load up the new activity
        startActivity(summaryPage);
    }
}