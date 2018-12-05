package com.example.android.bluetoothchat;

import android.widget.ImageButton;

import java.util.ArrayList;

class WorkPile extends Pile
{
    
    WorkPile(ImageButton image, NertsActivity table)
    {
        super(image, table);
    }
    
    public boolean canAddPile(ArrayList<Card> input)
    {
        if(!canAddCard(input.get(0)))
        {
            return false;
        }
        else
        {            
            return true;
        }
    }
    
    public boolean canAddCard(Card card)
    {
        if(pile.size() == 0)
        {
            return true;
        }
        else
        {
            Card top = pile.get(pile.size()-1);
            
            //Check suit
            if(top.getSuit() == 0 || top.getSuit() == 2)
            {
                if(card.getSuit() == 0 || card.getSuit() == 2)
                {
                    return false;
                }
            }
            else
            {
                if(card.getSuit() == 1 || card.getSuit() == 3)
                {
                    return false;
                }            
            }
            
            //Check face
            if(card.getFace()+1 != top.getFace())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }        
}