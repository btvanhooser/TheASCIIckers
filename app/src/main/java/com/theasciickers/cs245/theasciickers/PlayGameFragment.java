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
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Melanie on 11/28/2016.
 */

public class PlayGameFragment extends android.support.v4.app.Fragment{

    final Integer [] NUM_CARDS = {4,6,8,10,12,14,16,18,20};
    View view;
    TextView scoreText;
    int score;
    Button tryAgain;
    CardButton [][] board;
    String [][] boardStrings;
    int numCardsChosen;
    int numRows;
    int numCols;
    int clickCount;
    int numTiles;
    boolean gameBegan;
    Button [] lastCards = new Button[2];
    String [] cardStrings = {"'\\0'","null",";-D","//","cout<<","printf()","&nbsp;","t('o't)","NaN","TRUMP"};
    int pairsRemaining;

    public PlayGameFragment (){}
    public PlayGameFragment(int n){
        numTiles = n;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.playable_game_screen,container,false);

        scoreText = (TextView) view.findViewById(R.id.currentGameScore);
        score = 0;
        clickCount = 0;
        gameBegan = false;
        determineCardLayout();
        constructBoard();
        tryAgain = (Button) view.findViewById(R.id.tryAgainButton);
        tryAgain.setEnabled(false);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount = 0;
                tryAgain.setEnabled(false);
                lastCards[0].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));
                lastCards[1].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));
                lastCards[0].setText("");
                lastCards[1].setText("");
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
        pairsRemaining = ((numRows * numCols) / 2);
    }


    public void constructBoard(){
        GridLayout gl = (GridLayout) view.findViewById(R.id.gridLayoutTilePicker);
        board = new CardButton[numRows][numCols];
        boardStrings = new String[numRows][numCols];
        shuffleArray(cardStrings);
        String[] stringsOnBoard = new String[numCols*numRows];
        for (int i = 0; i < stringsOnBoard.length; i++){
            stringsOnBoard[i] = cardStrings[i/2];
        }
        shuffleArray(stringsOnBoard);
        for (int i = 0; i < boardStrings.length; i++){
            for (int j = 0; j < boardStrings[i].length; j++){
                boardStrings[i][j] = stringsOnBoard[(i * boardStrings[i].length) + j];
            }
        }

        gl.setRowCount(numRows);
        gl.setColumnCount(numCols);
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                board[i][j] = new CardButton(getActivity(), i, j);
                board[i][j].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));
                final CardButton card = board[i][j];
                card.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v){
                        if ((getResources().getDrawable(R.drawable.ascii_playing_card_back).getConstantState()).equals(card.getBackground().getConstantState())) {
                            clickCount++;
                            if (clickCount == 1) {
                                card.setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_front_blank));
                                card.setText(boardStrings[card.getRow()][card.getCol()]);
                                lastCards[clickCount - 1] = card;
                            } else if (clickCount == 2) {
                                card.setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_front_blank));
                                card.setText(boardStrings[card.getRow()][card.getCol()]);
                                lastCards[clickCount - 1] = card;
                                if ((lastCards[0].getText()).equals(lastCards[1].getText())){
                                    score += 2;
                                    scoreText.setText(score + "");
                                    clickCount = 0;
                                    pairsRemaining--;
                                    if (pairsRemaining == 0){
                                        Log.d("Test","Game ends here");
                                        //end the game
                                        //call high score check -- I have this, I just need to add (Brian)
                                    }
                                }
                                else {
                                    tryAgain.setEnabled(true);
                                    score = (score == 0) ? 0 : score - 1;
                                    scoreText.setText(score + "");
                                }
                            }
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

    static void shuffleArray(String[] ar)
    {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState){
        super.onSaveInstanceState(outState);
        View view;
        TextView scoreText;
        Button tryAgain;
        CardButton [][] board;
        Button [] lastCards = new Button[2];

        if(gameBegan){
            outState.putInt("score",score);
            outState.putInt("numRows",numRows);
            outState.putInt("numCols",numCols);
            outState.putInt("numCardsChosen",numCardsChosen);
            outState.putInt("clickCount",clickCount);
            outState.putInt("pairsRemaining",pairsRemaining);
//            for(int i = 0 ; i < boardStrings.length;i++){
//                String [] tempBoardString = new String [boardStrings[i].length];
//                for(int j = 0; i < boardStrings[i].length;j++){
//                    tempBoardString[j] = boardStrings[i][j];
//                }
//                outState.putStringArray("boardStringsR"+i,tempBoardString);
//            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){
            //mydata = savedInstanceState.getdata
            score = savedInstanceState.getInt("score");
            numRows = savedInstanceState.getInt("numRows");
            numCols = savedInstanceState.getInt("numCols");
            numCardsChosen = savedInstanceState.getInt("numCardsChosen");
            clickCount = savedInstanceState.getInt("clickCount");
            pairsRemaining = savedInstanceState.getInt("pairsRemaining");
//            for(int i = 0 ; i < boardStrings.length;i++){
//                String [] tempBoardString = new String [boardStrings[i].length];
//                for(int j = 0; i < boardStrings[i].length;j++){
//                    tempBoardString[j] = boardStrings[i][j];
//                }
//                outState.putStringArray("boardStringsR"+i,tempBoardString);
//            }
        }
    }

}
