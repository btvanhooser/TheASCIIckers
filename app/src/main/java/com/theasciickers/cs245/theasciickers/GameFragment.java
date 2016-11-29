package com.theasciickers.cs245.theasciickers;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Button;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;

/**
 * Created by Melanie on 11/28/2016.
 */

public class GameFragment extends android.support.v4.app.Fragment{

    final Integer [] NUM_CARDS = {4,6,8,10,12,14,16,18,20};
    View view;
    int numCardsChosen;
    int numRows;
    int numCols;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.initial_game_screen,container,false);

        ListView list =(ListView) view.findViewById(R.id.listview);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(list.getContext(),android.R.layout.simple_list_item_activated_1,NUM_CARDS);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                Log.e("" + NUM_CARDS[(int)itemId],"CLICKED");
                numCardsChosen = NUM_CARDS[(int)itemId];
                determineCardLayout();
                GridLayout gl = (GridLayout) view.findViewById(R.id.gridLayoutTilePicker);
                gl.removeView(view.findViewById(R.id.listview));
                gl.removeView(view.findViewById(R.id.selectTileView));
                constructBoard();
            }
        });

        return view;
    }

    public void determineCardLayout(){
        List<Integer> factors = new ArrayList<>();
        for(int i = 1; i <= (int)Math.sqrt(numCardsChosen); i++){
                if(numCardsChosen%i == 0){
                    factors.add(i);
                    factors.add(numCardsChosen/i);
                }
        }
        Collections.sort(factors);

        if(factors.size()%2==0){
            numRows = factors.get(factors.size()/2);
            numCols = factors.get((factors.size()/2)-1);
        }
        else{
            numRows = factors.get(factors.size()/2);
            numRows = numCols;
        }

        System.out.println("mxn: " + numRows +"x" + numCols);
    }


    public void constructBoard(){
        GridLayout gl = (GridLayout) view.findViewById(R.id.gridLayoutTilePicker);


        gl.setRowCount(numRows);
        gl.setColumnCount(numCols);
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                Button card = new Button(getActivity());
                card.setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));

                GridLayout.LayoutParams itemLayoutParams = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j));
                itemLayoutParams.width = 150;
                itemLayoutParams.height = 185;
                gl.addView(card,itemLayoutParams);
            }
        }

    }

}
