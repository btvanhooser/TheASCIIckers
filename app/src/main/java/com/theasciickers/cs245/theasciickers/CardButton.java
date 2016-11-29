package com.theasciickers.cs245.theasciickers;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Brian on 11/29/2016.
 */

public class CardButton extends Button {
    int row;
    int col;

    public CardButton(Context context, int row, int col) {
        super(context);
        this.row = row;
        this.col = col;
    }

    public int getRow(){return row;}
    public int getCol(){return col;}

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
