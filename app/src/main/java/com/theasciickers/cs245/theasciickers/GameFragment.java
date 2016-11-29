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
    Button tryAgain;
    Button [][] board;
    int numCardsChosen;
    int numRows;
    int numCols;
    int clickCount;
    Button [] lastCards = new Button[2];
    String [] cardStrings = {"'\\0'","null",";-D","//","cout<<","printf()","&nbsp;","t('o't)","NaN","TRUMP"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.initial_game_screen,container,false);

        ListView list =(ListView) view.findViewById(R.id.listview);
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(list.getContext(),android.R.layout.simple_list_item_activated_1,NUM_CARDS);
        list.setAdapter(adapter);

        clickCount = 0;

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

        tryAgain = (Button) view.findViewById(R.id.tryAgainButton);
        tryAgain.setEnabled(false);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount = 0;
                tryAgain.setEnabled(false);
                lastCards[0].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));
                lastCards[1].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));
            }
        });

        setRetainInstance(true);
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
        board = new Button[numRows][numCols];


        gl.setRowCount(numRows);
        gl.setColumnCount(numCols);
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                board[i][j] = new Button(getActivity());
                board[i][j].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));
                final Button card = board[i][j];
                card.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        clickCount++;
                        if(clickCount == 1 ) {
                            card.setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_front_blank));
                            lastCards[clickCount-1] = card;
                        }
                        else if (clickCount ==  2){
                            card.setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_front_blank));
                            lastCards[clickCount-1] = card;
                            tryAgain.setEnabled(true);
                        }
                    }
               });
                GridLayout.LayoutParams itemLayoutParams = new GridLayout.LayoutParams(GridLayout.spec(i), GridLayout.spec(j));
                itemLayoutParams.width = 120;
                itemLayoutParams.height = 165;
                gl.addView(board[i][j],itemLayoutParams);
            }
        }

    }

}
