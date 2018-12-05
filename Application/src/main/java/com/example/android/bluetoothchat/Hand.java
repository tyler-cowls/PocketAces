package com.example.android.bluetoothchat;

import android.widget.ImageView;

import java.util.ArrayList;

class Hand
{
    private ArrayList<Card> hand;
    private ArrayList<ImageView> images;
    private BlackjackActivity table;
    private int count;

    Hand(ArrayList<ImageView> images, BlackjackActivity table)
    {
        hand = new ArrayList<Card>();
        this.images = images;
        this.table = table;
        count = 0;
    }
    
    public void addCard(Card card)
    {
        hand.add(card);
    }
    
    public int getValue()
    {
        int val = 0;
        int numAces = 0;
        
        for(int i = 0; i < hand.size(); i++)
        {
            if(hand.get(i).getValue() == 1)
            {
                numAces++;
            }
            else
            {
                val += hand.get(i).getValue();
            }
        }
        
        while(numAces > 0)
        {
            numAces--;
            if((val+11 == 21 && numAces > 0) || val+11 > 21)
            {
                val++;
            }
            else
            {
                val += 11;
            }
        }
        
        return val;
    }
  
    public boolean isBust()
    {
        if(this.getValue() > 21)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
  
    public boolean isValue(int a)
    {
        if(this.getValue() == a)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean hasBlackjack()
    {
        if(this.getValue() == 21 && hand.size() == 2)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void showOne()
    {
        String path = "card" + hand.get(0).toString();
        int newCard = table.getResources().getIdentifier(path, "drawable", table.getPackageName());
        images.get(0).setImageResource(newCard);
        count = 1;
    }
    
    public void toImage()
    {
        for(int i = count; i < hand.size(); i++)
        {
            String path = "card" + hand.get(i).toString();
            int newCard = table.getResources().getIdentifier(path, "drawable", table.getPackageName());
            images.get(i).setImageResource(newCard);
        }
        count = hand.size();
    }
    
}