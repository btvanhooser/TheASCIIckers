package com.theasciickers.cs245.theasciickers;

/**
 * Created by Melanie on 11/25/16.
 */
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HighScoresFragment extends android.support.v4.app.Fragment
{
   // public static HighScoresFragment newInstance() {return new HighScoresFragment();}

    // initizliaze essential components of frag you want to retain when frag is paused/stopped then resumed
    /*public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);
    }*/
    // Globals

    Spinner spinner;
    TextView firstName;
    TextView secondName;
    TextView thirdName;
    TextView firstScore;
    TextView secondScore;
    TextView thirdScore;
    TextView[] nameList;
    TextView[] scoreList;
    HashMap<Integer,ArrayList<String>> highscores;
    Activity main;
    // draws ui returns view for new ui, null if no ui change
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.high_scores,container,false);
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        // Added setup that will run whenever the fragment is made visible

        main = getActivity();
        setUpArrays();
        retrieveHighScores();
        setUpSpinner();
    }

    private void setUpArrays() {
        //Set up for arrays to hold the textviews that will need to be updated

        firstName = (TextView) main.findViewById(R.id.player1Name);
        firstScore = (TextView) main.findViewById(R.id.player1Score);
        secondName = (TextView) main.findViewById(R.id.player2Name);
        secondScore = (TextView) main.findViewById(R.id.player2Score);
        thirdName = (TextView) main.findViewById(R.id.player3Name);
        thirdScore = (TextView) main.findViewById(R.id.player3Score);
        nameList = new TextView[]{firstName, secondName, thirdName};
        scoreList = new TextView[]{firstScore, secondScore, thirdScore};
    }

    private void retrieveHighScores() {
        //File is located in "raw" folder under res
        //Entries being stored as (number of tiles) => (top 3 high scores)

        HashMap<Integer,ArrayList<String>> temp = new HashMap<>();

        InputStream is = this.getResources().openRawResource(R.raw.highscores);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr, 8192);
        while (true){
            String line = "";
            try {
                line = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (null == line){
                try {
                    isr.close();
                    is.close();
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            int tileNumber = Integer.parseInt(line);
            temp.put(tileNumber,new ArrayList<String>());
            for (int i = 0; i < 3; i++) {
                try {
                    temp.get(tileNumber).add(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        highscores = temp;
    }

    private void setUpSpinner() {
        spinner = ((Spinner) main.findViewById(R.id.spinner2));
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                int selected = Integer.parseInt((String) parent.getItemAtPosition(i));
                Log.d("selected",selected + "");
                ArrayList<String> temp = highscores.get(selected);
                for (int j = 0; j < 3; j++){
                    String[] split = temp.get(j).split(",");
                    nameList[j].setText(split[0]);
                    scoreList[j].setText(split[1]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner.setSelection(0);
    }
    // Add onPause - commit any changes that need to be saved for future use
}
