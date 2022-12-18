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

import java.util.ArrayList;

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
    int counter = 0;

    ArrayList<Integer> gameSequence = new ArrayList<Integer>();

    TextView timeDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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


        Intent intent = new Intent();
        ArrayList<Integer> sequence = intent.getIntegerArrayListExtra("gameSequence");
        //Iterate through the game sequence array and back fill with the previous pattern
        if(sequence!=null)
        {
            for(int i: sequence)
            {
                gameSequence.add(sequence.get(i));
            }
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

    private void oneButton(int i)
    {
        Toast.makeText(this, "Number = " + n, Toast.LENGTH_SHORT).show();

        switch (n) {
            case 1:
                flashButton(btnRed);
                //gameSequence[counter++] = RED;
                gameSequence.add(RED);
                soundManager.PlaySound(1);
                break;
            case 2:
                flashButton(btnYellow);
                //gameSequence[counter++] = YELLOW;
                gameSequence.add(YELLOW);
                soundManager.PlaySound(2);
                break;
            case 3:
                flashButton(btnGreen);
                //gameSequence[counter++] = GREEN;
                gameSequence.add(GREEN);
                soundManager.PlaySound(3);
                break;
            case 4:
                flashButton(btnBlue);
                //gameSequence[counter++] = BLUE;
                gameSequence.add(BLUE);
                soundManager.PlaySound(4);
                break;
            default:
                break;
        }   // end switch

        counter++;
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

            if(gameSequence.size() <= 0 && gameSequence.size() < sequenceCount)
            {
                gameSequence.add(getRandom(sequenceCount));
            }
            else if(gameSequence.size() >= 4)
            {
                oneButton(gameSequence.get(counter));
            }
        }

        public void onFinish()
        {
            //mTextField.setText("done!");
            // we now have the game sequence

            for (int i = 0; i< counter; i++)
            {
                Log.d("game sequence", String.valueOf(gameSequence.get(i)));
            }

            //Switch to the player Game screen
            switchToPlayerGameScreen();
        }
    };


    public void switchToPlayerGameScreen()
    {
        //Create new intent instance for the high score table
        Intent playerGameScreen = new Intent(this, PlayerGameScreen.class);
        playerGameScreen.putIntegerArrayListExtra("gamesequence", gameSequence);
        //playerGameScreen.putExtra("gameSequence", gameSequence);
        startActivity(playerGameScreen);
    }

}