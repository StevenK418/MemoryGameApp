package com.example.memorygameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayerGameScreen extends AppCompatActivity implements SensorEventListener
{
    //Create a new soundManager Instance
    SoundManager soundManager = new SoundManager(1.0f, PlayerGameScreen.this);

    //Configure  the sensor inputs
    private SensorManager sensorManager;
    private Sensor sensor;
    boolean highLimit = false;

    //Predefine floor and ceiling on x axis (forward and backwards)
    // Ceiling
    private final double NORTH_MOVE_FORWARD = -7.0;
    // Floor
    private final double NORTH_MOVE_BACKWARD = 8.0;

    //Predefine floor and ceiling on y axis (sideways)
    // Ceiling
    private final double EAST_MOVE_FORWARD = 8.0;
    // Floor
    private final double EAST_MOVE_BACKWARD = -7.0;

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
        //Disable the top bar as it is not needed.
        getSupportActionBar ().hide ();

        btnRed = findViewById(R.id.btnRed);
        btnYellow = findViewById(R.id.btnYellow);
        btnGreen = findViewById(R.id.btnGreen);
        btnBlue = findViewById(R.id.btnBlue);

        timeDisplay = findViewById(R.id.txt_Time);

//        //TODO: testing db
//          scoremanager = new ScoreManager();
//        scoremanager.buildNewHighScore("TestUser", 1500);
//        scoremanager.AddTestScoreData();

        //Create new intent instance
        Intent intent = getIntent();

        //Get and store the game sequence from the main activity
        gameSequence = intent.getIntArrayExtra("gameSequence");

        Log.d("Sequence length passed: ", String.valueOf(gameSequence.length));
        for (int i=0;i < gameSequence.length;i++)
        {
            Log.d("sequence", String.valueOf(gameSequence[i]));
        }

        //Initialize our sensors
        // we are going to use the sensor service
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void doInput(View view)
    {
            //Check if the game is in play and only start timer if it is not
            if(isInPlay == false)
            {
                ScoreManager.round++;
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
                soundManager.PlaySound(1);
                break;
            case 2:
                userGameSequence[counter] = YELLOW;
                soundManager.PlaySound(2);
                break;
            case 3:
                userGameSequence[counter] = GREEN;
                soundManager.PlaySound(3);
                break;
            case 4:
                userGameSequence[counter] = BLUE;
                soundManager.PlaySound(4);
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
            timeDisplay.setText("Time Left: " + millisUntilFinished / 1500);
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

        boolean isHighScore = ScoreManager.CheckIfHighScore();
        Log.d("IsHighScore is: ", String.valueOf(isHighScore));

       if(result == true && isHighScore == true)
        {
            //Display the end game condition to the user
            timeDisplay.setText("YOU WIN ROUND");
            //Play the win sound
            soundManager.PlayWinSound();
            //Increment the user's score by 100
            ScoreManager.userScore += 100;
            //Switch back to the main game screen
            switchToNewHighScoreScreen();
        }
//       else if(result == true && isHighScore == true)
//       {
//           //Display the end game condition to the user
//           timeDisplay.setText("YOU WIN ROUND");
//           //Play the win sound
//           soundManager.PlayWinSound();
//           //Increment the user's score by 100
//           ScoreManager.userScore += 100;
//           //Switch back to the main game screen
//           switchBackToMainScreen();
//       }
        else if(result == true && isHighScore == false)
        {
            //Display the end game condition to the user
            timeDisplay.setText("YOU WIN ROUND");
            //Play the win sound
            soundManager.PlayWinSound();
            //Increment the user's score by 100
            ScoreManager.userScore += 100;
            //Switch back to the main game screen
            switchBackToMainScreen();
        }
        else
        {
            //Display the end game condition back to the user
            timeDisplay.setText("YOU LOSE!");
            //Play the lose sound
            soundManager.PlayLoseSound();
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
        mainGameScreen.putExtra("previousSequence", gameSequence);
        //Double the sequence length for the next game
        mainGameScreen.putExtra("sequenceLength", counter*2);
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
     * Switches to the highscore results screen
     */
    public void switchToNewHighScoreScreen()
    {
        //Create new intent instance for the new high score screen
        Intent newHighScoreScreen = new Intent(this, NewHighScore.class);
        newHighScoreScreen.putExtra("scoreValue", ScoreManager.userScore);
        startActivity(newHighScoreScreen);
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

        timeDisplay.setText("Player's turn!");
    }

    // regionSensor management
    /**
     * Main sensor event detection
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //Failsafe, return if no sensors have been found
        if (event.sensor != sensor)
        {
            return;
        }

        //Store our x and Y axes
        float x = event.values[0];
        float y = event.values[1];

        String sensorsMessage = "X: " + x + " Y: " + y;

        if ((x <= NORTH_MOVE_FORWARD && x < NORTH_MOVE_BACKWARD) && (highLimit == false))
        {
            highLimit = true;
            doInput(findViewById(R.id.btnRed));
        }
        else if ((x >= NORTH_MOVE_BACKWARD && x > NORTH_MOVE_FORWARD) && (highLimit == true))
        {
            highLimit = false;
            doInput(findViewById(R.id.btnGreen));
        }
        else if ((y >= EAST_MOVE_FORWARD && y > EAST_MOVE_BACKWARD) && (highLimit == true))
        {
            highLimit = false;
            doInput(findViewById(R.id.btnYellow));
            Log.d("FORWARD Sensor values: ", sensorsMessage);
        }
        else if ((y <= EAST_MOVE_BACKWARD && y < EAST_MOVE_FORWARD) && (highLimit == false))
        {
            highLimit = true;
            doInput(findViewById(R.id.btnBlue));
            Log.d("BACKWARD Sensor values: ", sensorsMessage);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    /**
     * When the app is brought to the foreground - using app on screen
     */
    protected void onResume()
    {
        super.onResume();
        // turn on the sensor
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * App running but not on screen - in the background
     */
    protected void onPause()
    {
        super.onPause();
        //Switch off the sensor to save energy
        sensorManager.unregisterListener(this);
    }

    /**
     * Do some rounding of the mag value
     */
    public static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    //endregion

}