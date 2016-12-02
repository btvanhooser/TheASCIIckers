package com.theasciickers.cs245.theasciickers;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class GameFragment extends android.support.v4.app.Fragment{

    final Integer [] NUM_CARDS = {4,6,8,10,12,14,16,18,20};
    View view;
    TextView scoreText;
    int score;
    Button tryAgain;
    Button endGame;
    CardButton [][] board;
    String [][] boardStrings;
    boolean [][] flipped;
    int numCardsChosen;
    int numRows;
    int numCols;
    int clickCount;
    boolean card0;
    boolean card1;
    CardButton [] lastCards = new CardButton[2];
    String [] cardStrings = {"'\\0'","null",";-D","//","cout<<","printf()","&nbsp;","t('o't)","NaN","TRUMP"};
    int pairsRemaining;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.initial_game_screen,container,false);

        scoreText = (TextView) view.findViewById(R.id.currentGameScore);
        score = 0;
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
        endGame = (Button) view.findViewById(R.id.endGameButton);
        tryAgain.setEnabled(false);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount = 0;
                tryAgain.setEnabled(false);
                board[lastCards[0].getRow()][lastCards[0].getCol()].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));
                board[lastCards[1].getRow()][lastCards[1].getCol()].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));
                board[lastCards[0].getRow()][lastCards[0].getCol()].setText("");
                board[lastCards[1].getRow()][lastCards[1].getCol()].setText("");
                Log.d("Test",lastCards[0].getRow() + " " + lastCards[0].getCol());
                Log.d("Test",lastCards[1].getRow() + " " + lastCards[1].getCol());
            }
        });
        endGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswers();
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
        endGame.setEnabled(true);
        GridLayout gl = (GridLayout) view.findViewById(R.id.gridLayoutTilePicker);
        board = new CardButton[numRows][numCols];
        boardStrings = new String[numRows][numCols];
        flipped = new boolean[numRows][numCols];
        for (int i = 0; i < flipped.length; i++){
            for (int j = 0; j < flipped[i].length; j++){
                flipped[i][j] = false;
            }
        }
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
                                    flipped[lastCards[0].getRow()][lastCards[0].getCol()] = true;
                                    flipped[lastCards[1].getRow()][lastCards[1].getCol()] = true;
                                    score += 2;
                                    scoreText.setText(score + "");
                                    clickCount = 0;
                                    pairsRemaining--;
                                    if (pairsRemaining == 0){
                                        endGame.setEnabled(false);
                                        tryAgain.setEnabled(false);
                                        askForName();
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

    private void askForName() {
        String[] test = getSpecificHighScores(numCols * numRows);
        String[] lastHighScore = test[2].split(",");
        int lastScore = Integer.parseInt(lastHighScore[1]);
        if (score < lastScore){
            //no high score
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.no_high_score);
            Button oKButton = (Button) dialog.findViewById(R.id.okayButton);
            oKButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            TextView scoreView = (TextView) dialog.findViewById(R.id.scoreTextForEndGame);
            scoreView.setText("Score: " + score);
            dialog.show();
        }
        else {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.high_score_entry);
            dialog.setTitle("High Score Reached");
            Button enterButton = (Button) dialog.findViewById(R.id.enterButton);
            enterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText editText = (EditText) dialog.findViewById(R.id.newHighScoreName);
                    String text = editText.getText().toString().replace(',', ' ');
                    String[] test = getSpecificHighScores(numCols * numRows);
                    String result = (numCols * numRows) + "\n";
                    Boolean scoreAdded = false;
                    for (int i = 0, tot = 0; tot < 3; tot++) {
                        if (!scoreAdded && score > Integer.parseInt(test[i].split(",")[1])) {
                            result += (text + "," + score + "\n");
                            scoreAdded = true;
                        } else {
                            result += (test[i] + "\n");
                            i++;
                        }
                    }
                    plugInNewHighScoresAtLocation((numCols * numRows), result);

                    dialog.dismiss();

                }
            });
            Button cancelButton = (Button) dialog.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    private void plugInNewHighScoresAtLocation(int num, String newScoreSection) {
        try {
            FileInputStream is = getActivity().openFileInput("highscore.txt");
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr, 8192);
            String newHighScoreList = "";
            while (true){
                String line = "";
                try {
                    line = br.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (null == line || line.equals("")){
                    try {
                        isr.close();
                        is.close();
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                if (line.equals(num + "")){
                    try {
                        br.readLine();
                        br.readLine();
                        br.readLine();
                        Log.d("Test",newScoreSection);
                        newHighScoreList += newScoreSection;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        String newString = line + "\n" + br.readLine() + "\n" + br.readLine() + "\n" + br.readLine() + "\n";
                        Log.d("Test",newString);
                        newHighScoreList += (newString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            FileOutputStream fos = getActivity().openFileOutput("highscore.txt", Context.MODE_PRIVATE);
            fos.write(newHighScoreList.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] getSpecificHighScores(int num) {
        String[] result = new String[3];
        try {
            FileInputStream is = getActivity().openFileInput("highscore.txt");
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
                if (line.equals(num + "")){
                    try {
                        result[0] = br.readLine();
                        result[1] = br.readLine();
                        result[2] = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        br.readLine();
                        br.readLine();
                        br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void showAnswers(){
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                board[i][j].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_front_blank));
                board[i][j].setText(boardStrings[i][j]);
                flipped[i][j] = true;
            }
        }
        clickCount = 2;
        pairsRemaining = 0;
        tryAgain.setEnabled(false);
        endGame.setEnabled(false);
    }

    public void drawBoard(){
        GridLayout gl = (GridLayout) view.findViewById(R.id.gridLayoutTilePicker);
        gl.removeAllViews();
        gl.setRowCount(numRows);
        gl.setColumnCount(numCols);
        board = new CardButton[numRows][numCols];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                board[i][j] = new CardButton(getActivity(), i, j);
                if (flipped[i][j]) {
                    board[i][j].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_front_blank));
                    board[i][j].setText(boardStrings[i][j]);
                }
                else {
                    board[i][j].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_back));
                }
                final CardButton card = board[i][j];
                System.out.println("card0: " + card0 + "card1: " + card1);
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
                                    flipped[lastCards[0].getRow()][lastCards[0].getCol()] = true;
                                    flipped[lastCards[1].getRow()][lastCards[1].getCol()] = true;
                                    score += 2;
                                    scoreText.setText(score + "");
                                    clickCount = 0;
                                    pairsRemaining--;
                                    if (pairsRemaining == 0){
                                        endGame.setEnabled(false);
                                        tryAgain.setEnabled(false);
                                        askForName();
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
        if(card0){
            board[lastCards[0].getRow()][lastCards[0].getCol()].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_front_blank));
            board[lastCards[0].getRow()][lastCards[0].getCol()].setText(lastCards[0].getText());
            System.out.println("in card 0");
        }
        if(card1){
            board[lastCards[1].getRow()][lastCards[1].getCol()].setBackground(getResources().getDrawable(R.drawable.ascii_playing_card_front_blank));
            board[lastCards[1].getRow()][lastCards[1].getCol()].setText(lastCards[1].getText());
            System.out.println("in card 1");
        }
    }

    static void shuffleArray(String[] ar)
    {
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
        TextView scoreText;

        if(numRows > 0){
            outState.putInt("score",score);
            outState.putInt("numRows",numRows);
            outState.putInt("numCols",numCols);
            outState.putInt("numCardsChosen",numCardsChosen);
            outState.putInt("clickCount",clickCount);
            outState.putInt("pairsRemaining",pairsRemaining);
            outState.putBoolean("tryAgainIsEnabled",tryAgain.isEnabled());
            outState.putBoolean("endGameIsEnabled",endGame.isEnabled());
            card0 = false;
            card1 = false;
            if(lastCards[0] != null){
                outState.putString("lastCard0",lastCards[0].getText().toString());
                outState.putInt("lastCard0Row",lastCards[0].getRow());
                outState.putInt("lastCard0Col",lastCards[0].getCol());
                card0 = true;
                outState.putBoolean("card0",card0);
            }
            if(lastCards[1] != null){

                outState.putString("lastCard1",lastCards[1].getText().toString());
                outState.putInt("lastCard1Row",lastCards[1].getRow());
                outState.putInt("lastCard1Col",lastCards[1].getCol());
                card1 = true;
                outState.putBoolean("card0",card1);
            }

            for(int i = 0 ; i < numRows;i++){
                String [] tempBoardString = new String [numCols];
                boolean [] tempFlipped = new boolean[numCols];
                for(int j = 0; j < numCols;j++){
                    tempBoardString[j] = boardStrings[i][j];
                    tempFlipped[j] = flipped[i][j];
                    System.out.println(boardStrings[i][j]);
                }
                outState.putStringArray("boardStringsR"+i,tempBoardString);
                outState.putBooleanArray("flippedR"+i,tempFlipped);
            }
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
            tryAgain.setEnabled(savedInstanceState.getBoolean("tryAgainIsEnabled"));
            endGame.setEnabled(savedInstanceState.getBoolean("endGameIsEnabled"));
            clickCount = savedInstanceState.getInt("clickCount");
            System.out.println("clickcount: " +clickCount);
            System.out.println("numCardsChosen: " + numCardsChosen);
            pairsRemaining = savedInstanceState.getInt("pairsRemaining");
            for(int i = 0 ; i < numRows;i++){
                boardStrings[i] = savedInstanceState.getStringArray("boardStringsR"+i);
                flipped[i] = savedInstanceState.getBooleanArray("flippedR"+i);
            }
            if(numRows > 0){
                drawBoard();
                if(savedInstanceState.getBoolean("card0")){
                    lastCards[0] = board[savedInstanceState.getInt("lastCard0Row")][savedInstanceState.getInt("lastCard0Col")];
                    lastCards[0].setText(savedInstanceState.getString("lastCard0"));
                    card0 = true;
                }
                if(savedInstanceState.getBoolean("card1")){
                    lastCards[1] = board[savedInstanceState.getInt("lastCard1Row")][savedInstanceState.getInt("lastCard1Col")];
                    lastCards[1].setText(savedInstanceState.getString("lastCard1"));
                    card1 = true;
                }
                scoreText.setText(score + "");
            }

        }
    }

}
