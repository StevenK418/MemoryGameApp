package com.example.memorygameapp;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ScoreManager extends AppCompatActivity
{
    HighScore highScore = new HighScore();
    List<HighScore> highscores;
    DatabaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public ScoreManager(Context context)
    {
        db = new DatabaseManager(context);
    }

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

    public void AddTestScoreData()
    {
        db.addRecordToDatabase(new HighScore<HighScore>("Frodo", 12));
        db.addRecordToDatabase(new HighScore<HighScore>("Dobby", 16));
        db.addRecordToDatabase(new HighScore<HighScore>("DarthV", 20));
        db.addRecordToDatabase(new HighScore<HighScore>("Bob", 18));
        db.addRecordToDatabase(new HighScore<HighScore>( "Gemma", 22));
        db.addRecordToDatabase(new HighScore<HighScore>( "Joe", 30));
        db.addRecordToDatabase(new HighScore<HighScore>("DarthV", 22));
        db.addRecordToDatabase(new HighScore<HighScore>("Gandalf", 132));

        //Display the results
        DisplayScores();
    }

    public void DisplayScores()
    {

        //Get and store all the records in the db:
        highscores = db.getAllHighscore();

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
