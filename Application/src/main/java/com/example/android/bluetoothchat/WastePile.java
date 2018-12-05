package com.example.android.bluetoothchat;

import android.widget.ImageButton;

import java.util.ArrayList;

class WastePile extends Pile
{   
    WastePile(ImageButton image, NertsActivity table)
    {
        super(image, table);
    }    

    public void addThree(ArrayList<Card> cards)
    {
        for(int i = 0; i < cards.size(); i++)
        {
           pile.add(cards.get(i)); 
        }
    }  
    
    public ArrayList<Card> popWhenStuck()
    {
        Card temp = pile.get(pile.size()-1);
        pile.remove(pile.size()-1);
        pile.add(0, temp); 
        
        ArrayList<Card> res = new ArrayList<Card>(pile);
        pile.clear();
        return res;        
    }
    
}