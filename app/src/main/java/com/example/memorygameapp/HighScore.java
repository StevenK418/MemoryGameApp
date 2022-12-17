package com.example.memorygameapp;

public class HighScore <HighScore>
{
    int _id;
    String _name;
    int _highscore;

    public HighScore(){   }
    public HighScore(int id, String name, int highscore)
    {
        this._id = id;
        this._name = name;
        this._highscore = highscore;
    }

    public HighScore(String name, int highscore){
        this._name = name;
        this._highscore = highscore;
    }
    public int getID(){
        return this._id;
    }

    public void setID(int id){
        this._id = id;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public int getHighscore(){
        return this._highscore;
    }

    public void setHighscore(int highscore)
    {
        this._highscore = highscore;
    }
}
