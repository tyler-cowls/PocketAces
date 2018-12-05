package com.example.android.bluetoothchat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageButton;
import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.concurrent.atomic.AtomicInteger;
import com.example.android.common.activities.SampleActivityBase;

public class NertsActivity extends SampleActivityBase {

    ImageButton curPressed;
    int isPressed = 0;

    NertsPlayer userOne;
    NertsPlayer userTwo;
    FoundationPile fOne;
    FoundationPile fTwo;
    FoundationPile fThree;
    FoundationPile fFour;
    FoundationPile fFive;
    FoundationPile fSix;
    FoundationPile fSeven;
    FoundationPile fEight;

    AtomicInteger userTwoStuck = new AtomicInteger(0);
    AtomicInteger userTwoDone = new AtomicInteger(0);
    AtomicInteger userTwoNerts = new AtomicInteger(0);
    AtomicInteger userTwoScore = new AtomicInteger(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nerts);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.workPilesText);
        textView.setText(message + "'s Work Piles");

        userOne = new NertsPlayer((ImageButton)findViewById(R.id.work1Button), (ImageButton)findViewById(R.id.work2Button),
                (ImageButton)findViewById(R.id.work3Button), (ImageButton)findViewById(R.id.work4Button),
                (ImageButton)findViewById(R.id.wasteButton), (ImageButton)findViewById(R.id.stockButton),
                (ImageButton)findViewById(R.id.nertsButton), this);
        userTwo = new NertsPlayer(null, null, null, null, null, null, null, null);
        fOne = new FoundationPile((ImageButton)findViewById(R.id.found1Button), this);
        fTwo = new FoundationPile((ImageButton)findViewById(R.id.found2Button), this);
        fThree = new FoundationPile((ImageButton)findViewById(R.id.found3Button), this);
        fFour = new FoundationPile((ImageButton)findViewById(R.id.found4Button), this);
        fFive = new FoundationPile((ImageButton)findViewById(R.id.found5Button), this);
        fSix = new FoundationPile((ImageButton)findViewById(R.id.found6Button), this);
        fSeven = new FoundationPile((ImageButton)findViewById(R.id.found7Button), this);
        fEight = new FoundationPile((ImageButton)findViewById(R.id.found8Button), this);

        userOne.getWorkOne().showTop();
        userOne.getWorkTwo().showTop();
        userOne.getWorkThree().showTop();
        userOne.getWorkFour().showTop();
        userOne.getNerts().showTop();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        BluetoothChatFragment fragment = new BluetoothChatFragment();
        fragment.setFoundations(fOne, 1);
        fragment.setFoundations(fTwo, 2);
        fragment.setFoundations(fThree, 3);
        fragment.setFoundations(fFour, 4);
        fragment.setFoundations(fFive, 5);
        fragment.setFoundations(fSix, 6);
        fragment.setFoundations(fSeven, 7);
        fragment.setFoundations(fEight, 8);
        fragment.setNertzBooleans(userTwoStuck, 1);
        fragment.setNertzBooleans(userTwoDone, 2);
        fragment.setNertzBooleans(userTwoNerts, 3);
        fragment.setNertzBooleans(userTwoScore, 4);
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();

    }

    public void pressStock(View view)
    {
        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                Button mSendButton = (Button) findViewById(R.id.button_send);
                TextView sendText = (TextView) findViewById(R.id.edit_text_out);
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.stockButton);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                if(userOne.getStock().isEmpty())
                {
                    curPressed.setImageResource(android.R.color.transparent);
                    int newCard = this.getResources().getIdentifier("blue_back_bicycle", "drawable", this.getPackageName());
                    ((ImageButton)findViewById(R.id.stockButton)).setImageResource(newCard);
                }
                userOne.switchWasteToStock();
            }
        }
    }

    public void pressWaste(View view)
    {
        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                Button mSendButton = (Button) findViewById(R.id.button_send);
                TextView sendText = (TextView) findViewById(R.id.edit_text_out);
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.wasteButton);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.stockButton))
            {
                userOne.drawFromStock();
                userOne.getWaste().showTop();

                if(userOne.getStock().isEmpty())
                {
                    curPressed.setImageResource(android.R.color.transparent);
                }
            }
        }
    }

    public void pressWorkOne(View view)
    {
        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                Button mSendButton = (Button) findViewById(R.id.button_send);
                TextView sendText = (TextView) findViewById(R.id.edit_text_out);
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.work1Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToWork(1, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToWork(1, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToWork(1, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToWork(1, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToWork(1, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            userOne.getWorkOne().showTop();
        }
    }

    public void pressWorkTwo(View view)
    {
        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                Button mSendButton = (Button) findViewById(R.id.button_send);
                TextView sendText = (TextView) findViewById(R.id.edit_text_out);
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.work2Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToWork(2, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToWork(2, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToWork(2, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToWork(2, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToWork(2, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            userOne.getWorkTwo().showTop();
        }
    }

    public void pressWorkThree(View view)
    {
        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                Button mSendButton = (Button) findViewById(R.id.button_send);
                TextView sendText = (TextView) findViewById(R.id.edit_text_out);
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.work3Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToWork(3, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToWork(3, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToWork(3, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToWork(3, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToWork(3, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            userOne.getWorkThree().showTop();
        }
    }

    public void pressWorkFour(View view)
    {
        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                Button mSendButton = (Button) findViewById(R.id.button_send);
                TextView sendText = (TextView) findViewById(R.id.edit_text_out);
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.work4Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToWork(4, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToWork(4, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToWork(4, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToWork(4, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToWork(4, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            userOne.getWorkFour().showTop();
        }
    }

    public void pressFoundOne(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.found1Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToFoundation(fOne, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToFoundation(fOne, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToFoundation(fOne, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToFoundation(fOne, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToFoundation(fOne, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToFoundation(fOne, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            JSONObject sendPacket = makeJSON("Nertz", "", "", "1", fOne.getTop().toString(), "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            fOne.showTop();
        }
    }

    public void pressFoundTwo(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.found2Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToFoundation(fTwo, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToFoundation(fTwo, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToFoundation(fTwo, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToFoundation(fTwo, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToFoundation(fTwo, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToFoundation(fTwo, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            JSONObject sendPacket = makeJSON("Nertz", "", "", "2", fTwo.getTop().toString(), "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            fTwo.showTop();
        }
    }

    public void pressFoundThree(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.found3Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToFoundation(fThree, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToFoundation(fThree, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToFoundation(fThree, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToFoundation(fThree, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToFoundation(fThree, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToFoundation(fThree, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            JSONObject sendPacket = makeJSON("Nertz", "", "", "3", fThree.getTop().toString(), "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            fThree.showTop();
        }
    }

    public void pressFoundFour(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.found4Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToFoundation(fFour, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToFoundation(fFour, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToFoundation(fFour, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToFoundation(fFour, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToFoundation(fFour, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToFoundation(fFour, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            JSONObject sendPacket = makeJSON("Nertz", "", "", "4", fFour.getTop().toString(), "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            fFour.showTop();
        }
    }

    public void pressFoundFive(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.found5Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToFoundation(fFive, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToFoundation(fFive, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToFoundation(fFive, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToFoundation(fFive, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToFoundation(fFive, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToFoundation(fFive, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            JSONObject sendPacket = makeJSON("Nertz", "", "", "5", fFive.getTop().toString(), "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            fFive.showTop();
        }
    }

    public void pressFoundSix(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.found6Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToFoundation(fSix, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToFoundation(fSix, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToFoundation(fSix, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToFoundation(fSix, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToFoundation(fSix, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToFoundation(fSix, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            JSONObject sendPacket = makeJSON("Nertz", "", "", "6", fSix.getTop().toString(), "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            fSix.showTop();
        }
    }

    public void pressFoundSeven(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.found7Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToFoundation(fSeven, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToFoundation(fSeven, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToFoundation(fSeven, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToFoundation(fSeven, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToFoundation(fSeven, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToFoundation(fSeven, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }
            JSONObject sendPacket = makeJSON("Nertz", "", "", "7", fSeven.getTop().toString(), "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            fSeven.showTop();
        }
    }

    public void pressFoundEight(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.found8Button);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            isPressed = 0;
            curPressed.setBackgroundColor(0x00000000);
            if(curPressed == findViewById(R.id.wasteButton))
            {
                userOne.addCardToFoundation(fEight, userOne.getWaste());
                userOne.getWaste().showTop();
            }
            else if(curPressed == findViewById(R.id.nertsButton))
            {
                userOne.addCardToFoundation(fEight, userOne.getNerts());
                userOne.getNerts().showTop();
            }
            else if(curPressed == findViewById(R.id.work1Button))
            {
                userOne.addCardToFoundation(fEight, userOne.getWorkOne());
                userOne.getWorkOne().showTop();
            }
            else if(curPressed == findViewById(R.id.work2Button))
            {
                userOne.addCardToFoundation(fEight, userOne.getWorkTwo());
                userOne.getWorkTwo().showTop();
            }
            else if(curPressed == findViewById(R.id.work3Button))
            {
                userOne.addCardToFoundation(fEight, userOne.getWorkThree());
                userOne.getWorkThree().showTop();
            }
            else if(curPressed == findViewById(R.id.work4Button))
            {
                userOne.addCardToFoundation(fEight, userOne.getWorkFour());
                userOne.getWorkFour().showTop();
            }

            JSONObject sendPacket = makeJSON("Nertz", "", "", "8", fEight.getTop().toString(), "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
            fEight.showTop();
        }
    }

    public void pressNerts(View view)
    {
        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                Button mSendButton = (Button) findViewById(R.id.button_send);
                TextView sendText = (TextView) findViewById(R.id.edit_text_out);
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(isPressed == 0)
        {
            curPressed = findViewById(R.id.nertsButton);
            curPressed.setBackgroundColor(0xFF00574B);
            isPressed = 1;
        }
        else
        {
            curPressed.setBackgroundColor(0x00000000);
            isPressed = 0;
        }
    }

    public void pressStuck(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
        {
            if(userOne.hasNerts())
            {
                JSONObject sendPacket = makeJSON("Nertz", "", "", "", "", "1", userOne.getScore() + "");
                sendText.setText(sendPacket.toString());
                mSendButton.performClick();
            }
            displayEnd();
        }

        if(userOne.isStuck())
        {
            userOne.setStuck(false);
            findViewById(R.id.stuckButton).setBackgroundColor(0xFF008577);
            //TODO: Finish
            JSONObject sendPacket = makeJSON("Nertz", "", "0", "", "", "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
        }
        else
        {
            userOne.setStuck(true);
            findViewById(R.id.stuckButton).setBackgroundColor(0xFF00574B);
            //TODO: Finish
            JSONObject sendPacket = makeJSON("Nertz", "", "1", "", "", "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
        }
    }

    public void pressDone(View view)
    {
        Button mSendButton = (Button) findViewById(R.id.button_send);
        TextView sendText = (TextView) findViewById(R.id.edit_text_out);

        if(userOne.isDone())
        {
            userOne.setDone(false);
            findViewById(R.id.doneButton).setBackgroundColor(0xFF008577);
            //TODO: Finish
            JSONObject sendPacket = makeJSON("Nertz", "0", "", "", "", "", "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();
        }
        else
        {
            userOne.setDone(true);
            findViewById(R.id.doneButton).setBackgroundColor(0xFF00574B);
            //TODO: Finish
            JSONObject sendPacket = makeJSON("Nertz", "1", "", "", "", "", userOne.getScore() + "");
            sendText.setText(sendPacket.toString());
            mSendButton.performClick();

            if(userOne.hasNerts() || (userTwoNerts.get() == 1) || (userOne.isDone() && (userTwoDone.get() == 1)))
            {
                displayEnd();
            }
        }
    }

    public void displayEnd()
    {
        TextView result = findViewById(R.id.result);
        result.setVisibility(View.VISIBLE);

        RelativeLayout relativeLayout = findViewById(R.id.endGameLayout);
        relativeLayout.setVisibility(View.VISIBLE);

        Button doneButton = findViewById(R.id.doneButton);
        doneButton.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // noticed result is not displayed
        String userOneScore = String.valueOf(userOne.getScore());

        if(userOne.getScore() > userTwoScore.get())
        {
            result.setText(message + " won with " + userOneScore + " points!");
        }
        else if (userOne.getScore() == userTwo.getScore())
        {
            result.setText("Users tied with " + userOneScore + " points!");
        }
        else
        {
            result.setText("User Two won " + userTwoScore.get() + " points!");
        }
    }

    public void playAgainNerts(View view)
    {
        String EXTRA_MESSAGE = "com.example.blackjack.MESSAGE";
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Intent newIntent = new Intent(this, NertsActivity.class);
        newIntent.putExtra(EXTRA_MESSAGE, message); // an extra is a key-value pair: EXTRA_MESSAGE is the key, message is the value
        startActivity(newIntent);
        finish();
    }

    public void backToMainFromNerts(View view)
    {
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
        finish();
    }

    private JSONObject makeJSON(String game, String done, String stuck, String pile, String card, String nertz, String score)
    {
        JSONObject jo = new JSONObject();
        try {
            jo.put("Game", game);
            jo.put("Done", done);
            jo.put("Stuck", stuck);
            jo.put("Pile", pile);
            jo.put("Card", card);
            jo.put("Nertz", nertz);
            jo.put("Score", score);
        }
        catch (JSONException e)
        {

        }
        return jo;
    }
}

