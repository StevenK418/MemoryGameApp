package com.example.memorygameapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "highscoreDatabase";
    private static final String TABLE_HIGHSCORE = "highscore";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_HIGHSCORE = "highscore";

    /**
     * Default Constructor
     * @param context
     * */
    public DatabaseManager(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_HIGHSCORE_TABLE = "CREATE TABLE " + TABLE_HIGHSCORE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_HIGHSCORE + " TEXT" + ")";
        db.execSQL(CREATE_HIGHSCORE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORE);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    void addRecordToDatabase(HighScore highscore)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highscore.getName()); // Contact Name
        values.put(KEY_HIGHSCORE, highscore.getHighscore()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_HIGHSCORE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to update the single contact
    public int updateHighscore(HighScore highscore)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, highscore.getName());
        values.put(KEY_HIGHSCORE, highscore.getHighscore());

        // updating row
        return db.update(TABLE_HIGHSCORE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(highscore.getID()) });
    }

    // Deleting single contact
    public void deleteHighScore(HighScore highscore)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIGHSCORE, KEY_ID + " = ?",
                new String[] { String.valueOf(highscore.getID()) });
        db.close();
    }

    /**
     * Returns the number of records held within the database
     * @return
     */
    // Getting contacts Count
    public int getRecordCount()
    {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_HIGHSCORE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }
}
