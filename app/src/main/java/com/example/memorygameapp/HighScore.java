package com.example.memorygameapp;

/**
 * Class defining a Highscore model
 * @param <HighScore>
 */
public class HighScore <HighScore>
{
    int scoreId;
    String playerName;
    int playerHighScore;

    /**
     * Default constructor
     */
    public HighScore()
    {

    }
    /**
     * Parameterised constructor to build Highscore model
     * as used by DB.
     * @param id
     * @param name
     * @param highscore
     */
    public HighScore(int id, String name, int highscore)
    {
        this.scoreId = id;
        this.playerName = name;
        this.playerHighScore = highscore;
    }

    /**
     * Parameterised constructor to build user highscore
     * @param name
     * @param highscore
     */
    public HighScore(String name, int highscore)
    {
        this.playerName = name;
        this.playerHighScore = highscore;
    }

    //Mutator methods
    public int getID()
    {
        return this.scoreId;
    }

    public void setID(int id)
    {
        this.scoreId = id;
    }

    public String getName()
    {
        return this.playerName;
    }

    public void setName(String name)
    {
        this.playerName = name;
    }

    public int getHighscore()
    {
        return this.playerHighScore;
    }

    public void setHighscore(int highscore)
    {
        this.playerHighScore = highscore;
    }

    @Override
    public String toString()
    {
        return "Name: " + playerName + " Score: "  + String.valueOf(playerHighScore);
    }
}
