package com.example.android.bluetoothchat;

public class Card
{
    private int face;
    private int suit;
    private int val;

    private String[] faces = {"Ace", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King"};
    private String[] suits = {"Spades", "Diamonds", "Clubs", "Hearts"};

    Card(int face, int suit, int val)
    {
        this.face = face;
        this.suit = suit;
        this.val = val;
    }

    public int getValue()
    {
        return val;
    }

    public int getSuit()
    {
        return suit;
    }

    public int getFace()
    {
        return face;
    }

    public String toString()
    {
        if(face == -1)
        {
            return "";
        }
        else
        {
            return "" + (suit*13+face);
        }
    }
}
