package com.example.android.bluetoothchat;

import java.util.ArrayList;
import java.util.Collections;

class Deck
{
    private ArrayList<Card> deck;
    
    Deck()
    {
        deck = new ArrayList<Card>();
        
        for(int i = 0; i < 4; i++)
        {
            for(int j = 1; j <= 13; j++)
            {
                if(j > 10)
                {
                    deck.add(new Card(j, i, 10));
                }
                else
                {
                    deck.add(new Card(j, i, j));
                }
            }
        }
        
        Collections.shuffle(deck);
    }
    
    public Card deal()
    {
        return deck.remove(0);
    }
}