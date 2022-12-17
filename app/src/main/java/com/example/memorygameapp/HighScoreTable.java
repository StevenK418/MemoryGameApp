package com.example.memorygameapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
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
        List<HighScore> values = db.getAllHighscore();

        ArrayAdapter<HighScore> adapter = new ArrayAdapter<HighScore>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

//    // Will be called via the onClick attribute
//    // of the buttons in main.xml
//    public void onClick(View view) {
//        @SuppressWarnings("unchecked")
//        ArrayAdapter<HighScore> adapter = (ArrayAdapter<HighScore>) getListAdapter();
//        HighScore highscore = null;
//        switch (view.getId()) {
//            case R.id.add:
//                String inStr = userText.getText().toString();
//                // save the new comment to the database
//                HighScore = datasource.createComment(inStr);
//                adapter.add(comment);
//                break;
//            case R.id.delete:
//                if (getListAdapter().getCount() > 0) {
//                    comment = (Comment) getListAdapter().getItem(0);
//                    datasource.deleteComment(comment);
//                    adapter.remove(comment);
//                }
//                break;
//        }
//        adapter.notifyDataSetChanged();
//    }
}