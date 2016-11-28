package com.theasciickers.cs245.theasciickers;

/**
 * Created by Melanie on 11/25/16.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HighScoresFragment extends android.support.v4.app.Fragment
{
   // public static HighScoresFragment newInstance() {return new HighScoresFragment();}

    // initizliaze essential components of frag you want to retain when frag is paused/stopped then resumed
    /*public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);
    }*/

    // draws ui returns view for new ui, null if no ui change
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.high_scores,container,false);
        return view;
    }

    // Add onPause - commit any changes that need to be saved for future use
}
