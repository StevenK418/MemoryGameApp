package com.example.memorygameapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

public class HighScoreTable extends ListActivity {

    DatabaseManager db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_table);

        db = new DatabaseManager(this.getApplicationContext());
        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        //Get the top five scorers and display
        List<HighScore> values = db.GetTopFiveScores();

        ArrayAdapter<HighScore> adapter = new ArrayAdapter<HighScore>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    /**
     * Onclick event to return to the mainscreen
     * @param view
     */
    public void doReturnToMain(View view)
    {
        //Reset the game values for a new player or game
        ScoreManager.ResetValues();
        Intent mainScreen = new Intent(this, MainActivity.class);
        startActivity(mainScreen);
    }
}