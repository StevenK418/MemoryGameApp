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

        //Get and store the game sequence from the main activity
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
            //Check if the game is in play and only start timer if it is not
            if(isInPlay == false)
            {
                ct.start();
            }

            //Get the full id nof the button that was pressed
            String elementName = view.getResources().getResourceName(view.getId());

            //Check the value of the Button's id and pass the correct value to the onebutton method
            switch(elementName)
            {
                case "com.example.memorygameapp:id/btnRed":
                    oneButton(1);
                    break;
                case "com.example.memorygameapp:id/btnYellow":
                    oneButton(2);
                    break;
                case "com.example.memorygameapp:id/btnGreen":
                    oneButton(3);
                    break;
                case "com.example.memorygameapp:id/btnBlue":
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
                userGameSequence[counter] = RED;
                break;
            case 2:
                userGameSequence[counter] = YELLOW;
                break;
            case 3:
                userGameSequence[counter] = GREEN;
                break;
            case 4:
                userGameSequence[counter] = BLUE;
                break;
            default:
                break;
        }   // end switch
        //Increment the counter by 1 each run of the method
        counter++;
    }

    private void flashButton(Button button)
    {
        activeButton = button;
        activeButton.setPressed(true);
        activeButton.invalidate();
        activeButton.performClick();
    }

    /**
     * Count down game timer
     */
    CountDownTimer ct = new CountDownTimer(6000,  1500)
    {

        public void onTick(long millisUntilFinished)
        {
            isInPlay = true;
            timeDisplay.setText("seconds remaining: " + millisUntilFinished / 1500);
        }

        public void onFinish()
        {
            timeDisplay.setText("TIME'S UP!");
            PrintResult();
            isTimeUp = true;
            isInPlay = false;
            //Show the result of the game based on the user's input
            ShowGameResult(userGameSequence);
        }
    };


    /**
     * Prints the results back to the user via logcat
     */
    public void PrintResult()
    {
        for (int i = 0; i< userGameSequence.length; i++)
        {
            //Build string based on the input and the sequence value passed
            String message = "You entered: " + String.valueOf(userGameSequence[i]) + " And Sequence was: " + String.valueOf(gameSequence[i]);
            //Display this result in the Logcat result
            Log.d("GameInfo: ", message);
        }
    }

    /**
     * Checks the sequence input by the user matches
     * the sequence shown
     * @param userGameSequence
     */
    public boolean CheckPlayersInputSequence(int[] userGameSequence)
    {
        //Flag set to true by default unless false detected
        boolean doesMatch = true;

        //Array to store boolean results during check
        boolean[] results = new boolean[gameSequence.length];

        //Iterate through the game sequence and check against user input
        for (int i = 0; i < gameSequence.length; i++)
        {
            //If user's input matches index, store True, else false
            if (gameSequence[i] == userGameSequence[i])
            {
                results[i] = true;
            }
            else
            {
                results[i] = false;
            }
        }

        /**
         * For all results in results, if one or more False's
         * are detected, set the doesmatch flag to false
         */
        for (int i = 0; i < results.length; i++)
        {
            Log.d("RESULT", String.valueOf((results[i])));
            if(results[i] == true)
            {
                continue;
            }
            else
            {
                doesMatch=false;
                break;
            }
        }
        //Return the end result
        return doesMatch;
    }

    /**
     * Shows the End game condition and acts accordingly
     * @param userGameSequence
     */

    public void ShowGameResult(int[] userGameSequence)
    {

        //Reset the values
        ResetGame();

        //Check the sequence input against the one from the main activity
        boolean result =  CheckPlayersInputSequence(userGameSequence);

        if(result == true)
        {
            //Display the end game condition to the user
            timeDisplay.setText("YOU WIN ROUND");
            //Switch back to the main game screen
            switchBackToMainScreen();
        }
        else
        {
            //Display the end game condition back to the user
            timeDisplay.setText("YOU LOSE!");
            //Switch to the high score screen instead
            switchBackToHighScoreScreen();
        }
    }

    /**
     * Switches back to the main screen for a new round
     */
    public void switchBackToMainScreen()
    {
        //Create new intent instance for the high score table
        Intent mainGameScreen = new Intent(this, MainActivity.class);
        startActivity(mainGameScreen);
    }

    /**
     * Switches to the highscore results screen
     */
    public void switchBackToHighScoreScreen()
    {
        //Create new intent instance for the high score table
        Intent highScoreScreen = new Intent(this, HighScoreTable.class);
        startActivity(highScoreScreen);
    }

    /**
     * Resets all the sequence values and the displayed text for a new round
     */
    public void ResetGame()
    {
        //Iterate through the user's inputs and set to zero
        for (int i : userGameSequence)
        {
            i=0;
        }

        //Iterate through the game sequence's inputs and reset to zero
        for (int j : gameSequence)
        {
            j=0;
        }

        timeDisplay.setText("Player's turn!");
    }
}