package com.example.android.bluetoothchat;

import android.widget.ImageButton;

class FoundationPile extends Pile
{
    FoundationPile(ImageButton image, NertsActivity table)
    {
        super(image, table);
    }    
    
    public boolean canAddCard(Card card)
    {
        if(pile.size() == 0)
        {
            if(card.getFace()-1 != 0)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
        else
        {
            Card top = pile.get(pile.size()-1);
            
            //Check suit
            if(top.getSuit() != card.getSuit())
            {
                return false;
            }
            
            //Check face
            if(card.getFace()-1 != top.getFace())
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }  
    
    public boolean isFull()
    {
        if(pile.size() == 13)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
}