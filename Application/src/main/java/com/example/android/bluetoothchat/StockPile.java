package com.example.android.bluetoothchat;

import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collections;

class StockPile extends Pile
{
    StockPile(ImageButton image, NertsActivity table)
    {
        super(image, table);
    }

    public void addPile(ArrayList<Card> input)
    {
        Collections.reverse(input);
        for(int i = 0; i < input.size(); i++)
        {
            pile.add(input.get(i));
        }
    }
    
    public ArrayList<Card> popThree()
    {
        ArrayList<Card> res = new ArrayList<Card>();
        int curSize = pile.size();
        for(int i = curSize-1; i >= curSize-3 && i >= 0; i--)
        {
            res.add(pile.get(i));
            pile.remove(i);
        }
        return res;
    }
}