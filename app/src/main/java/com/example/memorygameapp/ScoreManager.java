package com.example.memorygameapp;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ScoreManager extends AppCompatActivity
{
    private static ScoreManager _scoreManager = null;
    HighScore highScore = new HighScore();
    static int round = 0;
    static int userScore;

    static List<HighScore> highscores;
    static DatabaseManager db;

    static String userName;


    Context context = this.getApplicationContext();
    public static ScoreManager getInstance()
    {
        //lazy initialization
        if(_scoreManager == null)
        {
            _scoreManager = new ScoreManager();
        }

        return _scoreManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //db = new DatabaseManager(this.getApplicationContext());
    }

    public ScoreManager() {}

    public void buildNewHighScore(String name, int highScore)
    {
        this.highScore.playerName = name;
        this.highScore.playerHighScore = highScore;
    }


    /**
     * Saves the high score object to the db
     */
    public void SaveToDatabase()
    {
        db.addRecordToDatabase(highScore);
    }

    public static void AddTestScoreData()
    {
        db.addRecordToDatabase(new HighScore<HighScore>("JIM", 10));
        db.addRecordToDatabase(new HighScore<HighScore>("ACE", 20));

        //Display the results
        DisplayScores();
    }

    /**
     * Displays the high scores in the log
     */
    public static void DisplayScores()
    {
        //Get and store all the records in the db:
        highscores = db.getHighScores();

        if(highscores != null)
        {
            for (HighScore cn : highscores)
            {
                String log = "Id: " + cn.getID() + " ,Name: " + cn.getName() + " ,Highscore: " +
                        cn.getHighscore();
                Log.i("Name: ", log);

            }

            Log.i("divider", "====================");

            HighScore singleUser = db.getHighScoreWithID(5);
            Log.i("contact 5 is ", singleUser.getName());

            Log.i("divider", "====================");

            // Calling SQL statement
            int userCount = db.getRecordCount();
        }
    }

    /**
     * Checks if the user's score is a high score
     * @return
     */
    public static boolean CheckIfHighScore()
    {
        List<HighScore> userScores = db.GetTopFiveScores();
        boolean result = false;

        if(userScores != null)
        {
            for(HighScore score : userScores)
            {
                if(userScore > score.getHighscore())
                {
                    result = true;
                    break;
                }
            }
        }
        else
        {
            result = true;
        }
        return result;
    }

    /**
     * Saves a new high score to the database
     */
    public static void AddHighscore()
    {
        db.addRecordToDatabase(new HighScore<HighScore>(userName, userScore));
    }
}
