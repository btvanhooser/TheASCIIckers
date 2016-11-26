package com.theasciickers.cs245.theasciickers;

/**
 * Created by Melanie on 11/25/16.
 */
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HighScoresFragment
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.high_scores,container,false);
        return view;
    }
}
