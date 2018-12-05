package com.example.android.bluetoothchat;

import android.widget.ImageButton;

import java.util.ArrayList;

class Pile
{
    protected ArrayList<Card> pile;
    private ImageButton image;
    private NertsActivity table;

    Pile(ImageButton image, NertsActivity table)
    {
        this.image = image;
        this.table = table;
        pile = new ArrayList<Card>();
    }    
    
    public void addCard(Card card)
    {
        pile.add(card);
    }    

    public void addPile(ArrayList<Card> input)
    {
        for(int i = 0; i < input.size(); i++)
        {
            pile.add(input.get(i));
        }
    }
    
    public ArrayList<Card> getPile()
    {
        return this.pile;
    }

    public Card getCard(int index)
    {
        return pile.get(index);
    }

    public Card getTop()
    {
        if(pile.size() == 0)
        {
            return new Card(-1, -1, -1);
        }
        else
        {
            return pile.get(pile.size()-1);
        }
    }

    public void showTop()
    {
        if(pile.size() == 0)
        {
            image.setImageResource(android.R.color.transparent);
        }
        else
        {
            String path = "card" + pile.get(pile.size() - 1).toString();
            int newCard = table.getResources().getIdentifier(path, "drawable", table.getPackageName());
            image.setImageResource(newCard);
        }
    }   
    
    public Card popTop()
    {
        Card temp = pile.get(pile.size()-1);
        pile.remove(pile.size()-1);
        return temp;
    }
    
    public ArrayList<Card> popPile(int index)
    {
        ArrayList<Card> res = new ArrayList<Card>(pile.subList(index,pile.size()));
        pile.subList(index, pile.size()).clear();
        return res;
    }
    
    public boolean isEmpty()
    {
        return (pile.size() == 0);
    }
    
    public String toString()
    {
        String res = "";
        for(int i = 0; i < pile.size(); i++)
        {
            res += pile.get(i).toString() + " ";
        }
        return res;
    }
}  