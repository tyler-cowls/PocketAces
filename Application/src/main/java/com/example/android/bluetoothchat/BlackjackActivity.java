package com.example.android.bluetoothchat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Button;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.android.common.activities.SampleActivityBase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BlackjackActivity extends SampleActivityBase {

    int curState = 0;
    ArrayList<ImageView> dealerImages = new ArrayList<ImageView>();
    ArrayList<ImageView> userOneImages = new ArrayList<ImageView>();
    ArrayList<ImageView> userTwoImages = new ArrayList<ImageView>();

    //Initial set up
    Deck deck = new Deck();
    Hand dealer = new Hand(dealerImages, this);
    Hand userOne = new Hand(userOneImages, this);
    Hand userTwo = new Hand(userTwoImages, this);

    AtomicInteger otherRandNum = new AtomicInteger(0);
    int myRandNum = 0;

    String myName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blackjack);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textViewP1 = findViewById(R.id.textViewP1);
        textViewP1.setText(message);

        TextView textViewTurn = findViewById(R.id.textViewTurn);
        TextView textResult1 = findViewById(R.id.result1);
        TextView textResult2 = findViewById(R.id.result2);

        // adjust layout settings
        textViewP1.bringToFront();
        textViewTurn.bringToFront();

        textViewTurn.setText("Dealing Cards");

        dealerImages.add((ImageView)findViewById(R.id.dcard1));
        dealerImages.add((ImageView)findViewById(R.id.dcard2));
        dealerImages.add((ImageView)findViewById(R.id.dcard3));
        dealerImages.add((ImageView)findViewById(R.id.dcard4));
        dealerImages.add((ImageView)findViewById(R.id.dcard5));
        dealerImages.add((ImageView)findViewById(R.id.dcard6));
        dealerImages.add((ImageView)findViewById(R.id.dcard7));
        dealerImages.add((ImageView)findViewById(R.id.dcard8));
        dealerImages.add((ImageView)findViewById(R.id.dcard9));
        dealerImages.add((ImageView)findViewById(R.id.dcard10));

        userOneImages.add((ImageView)findViewById(R.id.p1card1));
        userOneImages.add((ImageView)findViewById(R.id.p1card2));
        userOneImages.add((ImageView)findViewById(R.id.p1card3));
        userOneImages.add((ImageView)findViewById(R.id.p1card4));
        userOneImages.add((ImageView)findViewById(R.id.p1card5));
        userOneImages.add((ImageView)findViewById(R.id.p1card6));
        userOneImages.add((ImageView)findViewById(R.id.p1card7));
        userOneImages.add((ImageView)findViewById(R.id.p1card8));
        userOneImages.add((ImageView)findViewById(R.id.p1card9));
        userOneImages.add((ImageView)findViewById(R.id.p1card10));

        userTwoImages.add((ImageView)findViewById(R.id.p2card1));
        userTwoImages.add((ImageView)findViewById(R.id.p2card2));
        userTwoImages.add((ImageView)findViewById(R.id.p2card3));
        userTwoImages.add((ImageView)findViewById(R.id.p2card4));
        userTwoImages.add((ImageView)findViewById(R.id.p2card5));
        userTwoImages.add((ImageView)findViewById(R.id.p2card6));
        userTwoImages.add((ImageView)findViewById(R.id.p2card7));
        userTwoImages.add((ImageView)findViewById(R.id.p2card8));
        userTwoImages.add((ImageView)findViewById(R.id.p2card9));
        userTwoImages.add((ImageView)findViewById(R.id.p2card10));

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BluetoothChatFragment fragment = new BluetoothChatFragment();
        fragment.setBJVars(otherRandNum,1);
        fragment.setBJViews(dealerImages, 1);
        fragment.setBJViews(userOneImages, 2);
        fragment.setBJViews(userTwoImages, 3);
        fragment.setBJActivity(this);
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();
    }

    public void nameSend()
    {
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);
        /*
        JSONObject sendPacket = makeJSON("Blackjack", "", "", "", myName, "");
        sendText.setText(sendPacket.toString());
        mSendButton.performClick();
        */
        if(message.equals("bob"))
        {
            TextView textViewTurn = findViewById(R.id.textViewTurn);
            TextView textResult1 = findViewById(R.id.result1);
            TextView textResult2 = findViewById(R.id.result2);

            TextView testText = findViewById(R.id.testText);
            testText.setText("MASTER");

            //Initial hand set up
            Card someCard = deck.deal();
            JSONObject sendPacket = makeJSON("Blackjack", "", "0", someCard.toString(), "", "1");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            userOne.addCard(someCard);

            someCard = deck.deal();
            sendPacket = makeJSON("Blackjack", "", "0", someCard.toString(), "", "2");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            userTwo.addCard(someCard);

            someCard = deck.deal();
            sendPacket = makeJSON("Blackjack", "", "0", someCard.toString(), "", "3");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            dealer.addCard(someCard);

            someCard = deck.deal();
            sendPacket = makeJSON("Blackjack", "", "0", someCard.toString(), "", "1");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            userOne.addCard(someCard);

            someCard = deck.deal();
            sendPacket = makeJSON("Blackjack", "", "0", someCard.toString(), "", "2");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            userTwo.addCard(someCard);

            someCard = deck.deal();
            dealer.addCard(someCard);

            dealer.showOne();
            userOne.toImage();
            userTwo.toImage();

            //Check if anyone has blackjack yet
            if(dealer.hasBlackjack() && userOne.hasBlackjack() && userTwo.hasBlackjack()) // All has Blackjack
            {
                dealer.toImage();
                textResult1.setText(message + " tied!");
                textResult2.setText("User Two tied!");
                textResult1.setVisibility(View.VISIBLE);
                textResult2.setVisibility(View.VISIBLE);

                View view1 = findViewById(R.id.view1);
                View view2 = findViewById(R.id.view2);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);

                curState = 3;
                displayEnd();
            }
            else if(dealer.hasBlackjack() && userOne.hasBlackjack() && !userTwo.hasBlackjack()) // Only Dealer and User One has Blackjack
            {
                dealer.toImage();
                textResult1.setText(message + " tied!");
                textResult2.setText("User Two lost!");
                textResult1.setVisibility(View.VISIBLE);
                textResult2.setVisibility(View.VISIBLE);

                View view1 = findViewById(R.id.view1);
                View view2 = findViewById(R.id.view2);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);

                curState = 3;
                displayEnd();
            }
            else if(dealer.hasBlackjack() && !userOne.hasBlackjack() && userTwo.hasBlackjack()) // Only Dealer and User Two has Blackjack
            {
                dealer.toImage();
                textResult1.setText(message + " lost!");
                textResult2.setText("User Two tied!");
                textResult1.setVisibility(View.VISIBLE);
                textResult2.setVisibility(View.VISIBLE);

                View view1 = findViewById(R.id.view1);
                View view2 = findViewById(R.id.view2);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);

                curState = 3;
                displayEnd();
            }
            else if(!dealer.hasBlackjack() && userOne.hasBlackjack() && userTwo.hasBlackjack()) // Only User One and User Two has Blackjack
            {
                dealer.toImage();
                textResult1.setText(message + " won!");
                textResult2.setText("User Two won!");
                textResult1.setVisibility(View.VISIBLE);
                textResult2.setVisibility(View.VISIBLE);

                View view1 = findViewById(R.id.view1);
                View view2 = findViewById(R.id.view2);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);

                curState = 3;
                displayEnd();
            }
            else if(dealer.hasBlackjack() && !userOne.hasBlackjack() && !userTwo.hasBlackjack()) // Only Dealer has Blackjack
            {
                dealer.toImage();
                textResult1.setText(message + " lost!");
                textResult2.setText("User Two lost!");
                textResult1.setVisibility(View.VISIBLE);
                textResult2.setVisibility(View.VISIBLE);

                View view1 = findViewById(R.id.view1);
                View view2 = findViewById(R.id.view2);
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.VISIBLE);

                curState = 3;
                displayEnd();
            }
            else if(!dealer.hasBlackjack() && userOne.hasBlackjack() && !userTwo.hasBlackjack()) //Skip User One's turn
            {
                curState = 2;
                textViewTurn.setText("User Two's Turn");
            }
            else if(!dealer.hasBlackjack() && !userOne.hasBlackjack() && userTwo.hasBlackjack()) //Skip User Two's turn
            {
                curState++;
                textViewTurn.setText(message + "'s Turn");
            }
            else
            {
                curState++;
                textViewTurn.setText(message + "'s Turn");
            }
        }
    }

    public void tableInit(String someMessage)
    {
        try
        {
            JSONObject obj = new JSONObject(someMessage);
            String otherName = obj.getString("Name");
            TextView tempView = findViewById(R.id.testText);
            tempView.setText("Received:" + otherName + " mine:" + myName);

            if(myName.compareTo(otherName) < 0)
            {
                Intent intent = getIntent();
                String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

                TextView textViewTurn = findViewById(R.id.textViewTurn);
                TextView textResult1 = findViewById(R.id.result1);
                TextView textResult2 = findViewById(R.id.result2);

                Button mSendButton = (Button) findViewById(R.id.button_send);
                TextView sendText = (TextView) findViewById(R.id.edit_text_out);

                //Initial hand set up
                Card someCard = deck.deal();
                JSONObject sendPacket = makeJSON("Blackjack", "", "0", "" + someCard.toString(), "", "1");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
                userOne.addCard(someCard);

                someCard = deck.deal();
                sendPacket = makeJSON("Blackjack", "", "0", "" + someCard.toString(), "", "2");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
                userTwo.addCard(someCard);

                someCard = deck.deal();
                sendPacket = makeJSON("Blackjack", "", "0", "" + someCard.toString(), "", "3");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
                dealer.addCard(someCard);

                someCard = deck.deal();
                sendPacket = makeJSON("Blackjack", "", "0", "" + someCard.toString(), "", "1");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
                userOne.addCard(someCard);

                someCard = deck.deal();
                sendPacket = makeJSON("Blackjack", "", "0", "" + someCard.toString(), "", "2");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
                userTwo.addCard(someCard);

                someCard = deck.deal();
                sendPacket = makeJSON("Blackjack", "", "0", "" + someCard.toString(), "", "3");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
                dealer.addCard(someCard);

                dealer.showOne();
                userOne.toImage();
                userTwo.toImage();

                //Check if anyone has blackjack yet
                if(dealer.hasBlackjack() && userOne.hasBlackjack() && userTwo.hasBlackjack()) // All has Blackjack
                {
                    dealer.toImage();
                    textResult1.setText(message + " tied!");
                    textResult2.setText("User Two tied!");
                    textResult1.setVisibility(View.VISIBLE);
                    textResult2.setVisibility(View.VISIBLE);

                    View view1 = findViewById(R.id.view1);
                    View view2 = findViewById(R.id.view2);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);

                    curState = 3;
                    displayEnd();
                }
                else if(dealer.hasBlackjack() && userOne.hasBlackjack() && !userTwo.hasBlackjack()) // Only Dealer and User One has Blackjack
                {
                    dealer.toImage();
                    textResult1.setText(message + " tied!");
                    textResult2.setText("User Two lost!");
                    textResult1.setVisibility(View.VISIBLE);
                    textResult2.setVisibility(View.VISIBLE);

                    View view1 = findViewById(R.id.view1);
                    View view2 = findViewById(R.id.view2);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);

                    curState = 3;
                    displayEnd();
                }
                else if(dealer.hasBlackjack() && !userOne.hasBlackjack() && userTwo.hasBlackjack()) // Only Dealer and User Two has Blackjack
                {
                    dealer.toImage();
                    textResult1.setText(message + " lost!");
                    textResult2.setText("User Two tied!");
                    textResult1.setVisibility(View.VISIBLE);
                    textResult2.setVisibility(View.VISIBLE);

                    View view1 = findViewById(R.id.view1);
                    View view2 = findViewById(R.id.view2);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);

                    curState = 3;
                    displayEnd();
                }
                else if(!dealer.hasBlackjack() && userOne.hasBlackjack() && userTwo.hasBlackjack()) // Only User One and User Two has Blackjack
                {
                    dealer.toImage();
                    textResult1.setText(message + " won!");
                    textResult2.setText("User Two won!");
                    textResult1.setVisibility(View.VISIBLE);
                    textResult2.setVisibility(View.VISIBLE);

                    View view1 = findViewById(R.id.view1);
                    View view2 = findViewById(R.id.view2);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);

                    curState = 3;
                    displayEnd();
                }
                else if(dealer.hasBlackjack() && !userOne.hasBlackjack() && !userTwo.hasBlackjack()) // Only Dealer has Blackjack
                {
                    dealer.toImage();
                    textResult1.setText(message + " lost!");
                    textResult2.setText("User Two lost!");
                    textResult1.setVisibility(View.VISIBLE);
                    textResult2.setVisibility(View.VISIBLE);

                    View view1 = findViewById(R.id.view1);
                    View view2 = findViewById(R.id.view2);
                    view1.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.VISIBLE);

                    curState = 3;
                    displayEnd();
                }
                else if(!dealer.hasBlackjack() && userOne.hasBlackjack() && !userTwo.hasBlackjack()) //Skip User One's turn
                {
                    curState = 2;
                    textViewTurn.setText("User Two's Turn");
                }
                else if(!dealer.hasBlackjack() && !userOne.hasBlackjack() && userTwo.hasBlackjack()) //Skip User Two's turn
                {
                    curState++;
                    textViewTurn.setText(message + "'s Turn");
                }
                else
                {
                    curState++;
                    textViewTurn.setText(message + "'s Turn");
                }
            }
        }
        catch (JSONException e)
        {

        }
    }

    public void hitMethod(View view)
    {
        TextView textViewTurn = findViewById(R.id.textViewTurn);

        if(curState == 1)
        {
            userOne.addCard(deck.deal());
            userOne.toImage();

            if(userOne.isBust() || userOne.isValue(21) )
            {
                if(userTwo.hasBlackjack())
                {
                    curState = 3;
                    textViewTurn.setText("Dealer's Turn");
                    dealerPlay();
                }
                else {
                    textViewTurn.setText("User Two's Turn");
                    curState++;
                }
            }
        }
        else if(curState == 2)
        {
            userTwo.addCard(deck.deal());
            userTwo.toImage();

            if(userTwo.isBust() || userTwo.isValue(21) )
            {
                textViewTurn.setText("Dealer's Turn");
                dealerPlay();
                curState++;
            }
        }
    }

    public void stayMethod(View view)
    {
        TextView textViewTurn = findViewById(R.id.textViewTurn);

        if(curState == 1)
        {
            if(userTwo.hasBlackjack())
            {
                curState = 3;
                textViewTurn.setText("Dealer's Turn");
                dealerPlay();
            }
            else {
                textViewTurn.setText("User Two's Turn");
                curState++;
            }
        }
        else if (curState == 2)
        {
            curState++;
            textViewTurn.setText("Dealer's Turn");
            dealerPlay();
        }
    }

    public void dealerPlay()
    {
        dealer.toImage();
        while(dealer.getValue() <= 16)
        {
            dealer.addCard(deck.deal());
            dealer.toImage();
        }

        displayWinner();
    }

    public void displayWinner()
    {
        TextView textResult1 = findViewById(R.id.result1);
        TextView textResult2 = findViewById(R.id.result2);
        View view1 = findViewById(R.id.view1);
        View view2 = findViewById(R.id.view2);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        if(dealer.isBust() && userOne.isBust() && userTwo.isBust())
        {
            textResult1.setText(message + " tied!");
            textResult2.setText("User Two tied!");
        }
        else if(dealer.isBust() && !userOne.isBust() && userTwo.isBust())
        {
            textResult1.setText(message + " won!");
            textResult2.setText("User Two tied!");
        }
        else if(!dealer.isBust() && userOne.isBust() && userTwo.isBust())
        {
            textResult1.setText(message + " lost!");
            textResult2.setText("User Two lost!");
        }
        else if(dealer.isBust() && userOne.isBust() && !userTwo.isBust())
        {
            textResult1.setText(message + " tied!");
            textResult2.setText("User Two won!");
        }
        else if(dealer.isBust() && !userOne.isBust() && !userTwo.isBust())
        {
            textResult1.setText(message + " won!");
            textResult2.setText("User Two won!");
        }
        else if(!dealer.isBust() && !userOne.isBust() && userTwo.isBust())
        {
            if(dealer.getValue() == userOne.getValue())
            {
                textResult1.setText(message + " tied!");
            }
            else if (dealer.getValue() > userOne.getValue())
            {
                textResult1.setText(message + " lost!");
            }
            else
            {
                textResult1.setText(message + " won!");
            }
            textResult2.setText("User Two lost!");
        }
        else if(!dealer.isBust() && userOne.isBust() && !userTwo.isBust())
        {
            textResult1.setText(message + " lost!");
            if(dealer.getValue() == userTwo.getValue())
            {
                textResult2.setText("User Two tied!");
            }
            else if (dealer.getValue() > userTwo.getValue())
            {
                textResult2.setText("User Two lost!");
            }
            else
            {
                textResult2.setText("User Two won!");
            }
        }
        else
        {
            if(dealer.getValue() == userOne.getValue())
            {
                textResult1.setText(message + " tied!");
            }
            else if (dealer.getValue() > userOne.getValue())
            {
                textResult1.setText(message + " lost!");
            }
            else
            {
                textResult1.setText(message + " won!");
            }

            if(dealer.getValue() == userTwo.getValue())
            {
                textResult2.setText("User Two tied!");
            }
            else if (dealer.getValue() > userTwo.getValue())
            {
                textResult2.setText("User Two lost!");
            }
            else
            {
                textResult2.setText("User Two won!");
            }
        }
        textResult1.setVisibility(View.VISIBLE);
        textResult2.setVisibility(View.VISIBLE);
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
        displayEnd();
    }

    public void displayEnd()
    {
        RelativeLayout relativeLayout = findViewById(R.id.endGameLayout);
        Button stayButton = findViewById(R.id.stayButton);
        Button hitButton = findViewById(R.id.hitButton);
        relativeLayout.setVisibility(View.VISIBLE);
        hitButton.setVisibility(View.INVISIBLE);
        stayButton.setVisibility(View.INVISIBLE);
    }

    public void playAgain(View view)
    {
        String EXTRA_MESSAGE = "com.example.blackjack.MESSAGE";
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent newIntent = new Intent(this, BlackjackActivity.class);
        newIntent.putExtra(EXTRA_MESSAGE, message); // an extra is a key-value pair: EXTRA_MESSAGE is the key, message is the value
        startActivity(newIntent);
        finish();
    }

    public void backToMain(View view)
    {
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
        finish();
    }

    private JSONObject makeJSON(String game, String hitOrStay, String state, String card, String name, String user)
    {
        JSONObject jo = new JSONObject();
        try {
            jo.put("Game", game);
            jo.put("HitOrStay", hitOrStay);
            jo.put("User", user);
            jo.put("State", state);
            jo.put("Card", card);
            jo.put("Name", name);
        }
        catch (JSONException e)
        {

        }
        return jo;
    }
}

