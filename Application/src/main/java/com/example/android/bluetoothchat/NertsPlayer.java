package com.example.android.bluetoothchat;

import android.widget.ImageButton;

import java.util.ArrayList;

class NertsPlayer
{
    private WorkPile workOne;
    private WorkPile workTwo;
    private WorkPile workThree;
    private WorkPile workFour;
    private WastePile waste;
    private StockPile stock;
    private Pile nerts;
    private Deck deck;
    private int numFoundationPlayed;
    private boolean stuck = false;
    private boolean done = false;
    
    NertsPlayer(ImageButton workOne, ImageButton workTwo, ImageButton workThree, ImageButton workFour, ImageButton waste, ImageButton stock, ImageButton nerts, NertsActivity table)
    {
        numFoundationPlayed = 0;
        deck = new Deck();
        
        this.workOne = new WorkPile(workOne, table);
        this.workOne.addCard(deck.deal());
        this.workTwo = new WorkPile(workTwo, table);
        this.workTwo.addCard(deck.deal());
        this.workThree = new WorkPile(workThree, table);
        this.workThree.addCard(deck.deal());
        this.workFour = new WorkPile(workFour, table);
        this.workFour.addCard(deck.deal());

        this.nerts = new Pile(nerts, table);
        for(int i = 0; i < 13; i++)
        {
            this.nerts.addCard(deck.deal());
        }

        this.waste = new WastePile(waste, table);
        this.stock = new StockPile(stock, table);
        for(int i = 0; i < 35; i++)
        {
            this.stock.addCard(deck.deal());
        }
    }
    
    public void setStuck(boolean b)
    {
        stuck = b;
    }

    public boolean isStuck()
    {
        return stuck;
    }    

    public void setDone(boolean b)
    {
        done = b;
    }

    public boolean isDone()
    {
        return done;
    }
    
    public WorkPile getWorkOne()
    {
        return workOne;
    }
 
    public WorkPile getWorkTwo()
    {
        return workTwo;
    }

    public WorkPile getWorkThree()
    {
        return workThree;
    }
 
    public WorkPile getWorkFour()
    {
        return workFour;
    }
 
    public WastePile getWaste()
    {
        return waste;
    }

    public StockPile getStock()
    {
        return stock;
    }

    public Pile getNerts()
    {
        return nerts;
    }

    public int getScore()
    {
        return numFoundationPlayed - (2*nerts.getPile().size());
    }
    
    public void addCardToWork(int pileNum, Pile pile)
    {
        Card card = pile.getTop();
        if(pileNum == 1)
        {
            if(workOne.canAddCard(card))
            {
                workOne.addCard(card);
                pile.popTop();
            }
        }
        else if(pileNum == 2)
        {
            if(workTwo.canAddCard(card))
            {
                workTwo.addCard(card);
                pile.popTop();
            }          
        }
        else if(pileNum == 3)
        {
            if(workThree.canAddCard(card))
            {
                workThree.addCard(card);
                pile.popTop();
            }            
        }
        else
        {
            if(workFour.canAddCard(card))
            {
                workFour.addCard(card);
                pile.popTop();
            }            
        }
    }
 
    public void addPileToWork(int pileNum, Pile pile, int index)
    {
        Card temp = pile.getCard(index);
        if(pileNum == 1)
        {
            if(workOne.canAddCard(temp))
            {
                workOne.addPile(pile.popPile(index));
            }
        }
        else if(pileNum == 2)
        {
            if(workTwo.canAddCard(temp))
            {
                workTwo.addPile(pile.popPile(index));
            }          
        }
        else if(pileNum == 3)
        {
            if(workThree.canAddCard(temp))
            {
                workThree.addPile(pile.popPile(index));
            }            
        }
        else
        {
            if(workFour.canAddCard(temp))
            {
                workFour.addPile(pile.popPile(index));
            }            
        }
    }

    public void addCardToFoundation(FoundationPile dest, Pile pile)
    {
        Card card = pile.getTop();
        if(dest.canAddCard(card))
        {
            numFoundationPlayed++;
            dest.addCard(card);
            pile.popTop();
        }
    }

    public void drawFromStock()
    {
        ArrayList<Card> temp = stock.popThree();
        waste.addThree(temp);
    }
    
    public void switchWasteToStock()
    {
        if(stock.isEmpty())
        {
            if(stuck)
            {
                stock.addPile(waste.popWhenStuck());
            }
            else
            {
                stock.addPile(waste.popPile(0));
            }
        }
    }
    
    public boolean hasNerts()
    {
        if(nerts.isEmpty())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}