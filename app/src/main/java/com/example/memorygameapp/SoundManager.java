package com.example.memorygameapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

public class SoundManager extends AppCompatActivity
{
    //Create a new mediaSource for the sound effect
    private MediaPlayer player;
    float globalVolume;

    //Store the application context
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    /**
     * Parameterised constructor
     * @param volume
     * @param context
     */
    public SoundManager(float volume, Context context)
    {
        this.context = context;
        this.globalVolume = volume;
    }

    /**
     * Plays a sound based on the id passed
     * @param id
     */
    public void PlaySound(int id)
    {
        switch(id)
        {
            case 1:
                //Initialize the mediaplayer here to avoid crashes.
                player = MediaPlayer.create(context, R.raw.red);
                break;
            case 2:
                //Initialize the mediaplayer here to avoid crashes.
                player = MediaPlayer.create(context, R.raw.yellow);
                break;
            case 3:
                //Initialize the mediaplayer here to avoid crashes.
                player = MediaPlayer.create(context, R.raw.green);
                break;
            case 4:
                //Initialize the mediaplayer here to avoid crashes.
                player = MediaPlayer.create(context, R.raw.blue);
                break;
            default:
                break;
        }
        //Set the volume on creation
        player.setVolume(globalVolume, globalVolume);
        //Disable the looping behaviour
        player.setLooping(false);
        //Play the sound effect when the phone is picked up
        player.start();
    }

    /**
     * Plays the You Win sound
     */
    public void PlayWinSound()
    {
        //Initialize the mediaplayer here to avoid crashes.
        player = MediaPlayer.create(context, R.raw.win);
        //Set the volume on creation
        player.setVolume(globalVolume, globalVolume);
        //Disable the looping behaviour
        player.setLooping(false);
        //Play the sound effect when the phone is picked up
        player.start();
    }

    /**
     * Plays the You Lose sound
     */
    public void PlayLoseSound()
    {
        //Initialize the mediaplayer here to avoid crashes.
        player = MediaPlayer.create(context, R.raw.lose);
        //Set the volume on creation
        player.setVolume(globalVolume, globalVolume);
        //Disable the looping behaviour
        player.setLooping(false);
        //Play the sound effect when the phone is picked up
        player.start();
    }

}
