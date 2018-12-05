package com.example.android.bluetoothchat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageButton;
import android.view.View;

import com.example.android.common.activities.SampleActivityBase;
import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

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
        transaction.replace(R.id.sample_content_fragment, fragment);
        transaction.commit();

    }

    public void pressStock(View view)
    {
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
            fOne.showTop();
        }
    }

    public void pressFoundTwo(View view)
    {
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
            fTwo.showTop();
        }
    }

    public void pressFoundThree(View view)
    {
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
            fThree.showTop();
        }
    }

    public void pressFoundFour(View view)
    {
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
            fFour.showTop();
        }
    }

    public void pressFoundFive(View view)
    {
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
            fFive.showTop();
        }
    }

    public void pressFoundSix(View view)
    {
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
            fSix.showTop();
        }
    }

    public void pressFoundSeven(View view)
    {
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
            fSeven.showTop();
        }
    }

    public void pressFoundEight(View view)
    {
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
            fEight.showTop();
        }
    }

    public void pressNerts(View view)
    {
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
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
        if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
        {
            displayEnd();
        }

        if(userOne.isStuck())
        {
            userOne.setStuck(false);
            findViewById(R.id.stuckButton).setBackgroundColor(0xFF008577);
        }
        else
        {
            userOne.setStuck(true);
            findViewById(R.id.stuckButton).setBackgroundColor(0xFF00574B);
        }
    }

    public void pressDone(View view)
    {
        if(userOne.isDone())
        {
            userOne.setDone(false);
            findViewById(R.id.doneButton).setBackgroundColor(0xFF008577);
        }
        else
        {
            userOne.setDone(true);
            findViewById(R.id.doneButton).setBackgroundColor(0xFF00574B);

            if(userOne.hasNerts() || userTwo.hasNerts() || (userOne.isDone() && userTwo.isDone()))
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
        String userTwoScore = String.valueOf(userTwo.getScore());

        if(userOne.getScore() > userTwo.getScore())
        {
            result.setText(message + " won with " + userOneScore + " points!");
        }
        else if (userOne.getScore() == userTwo.getScore())
        {
            result.setText("Users tied with " + userOneScore + " points!");
        }
        else
        {
            result.setText("User Two won " + userTwoScore + " points!");
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
    }

    public void backToMainFromNerts(View view)
    {
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
    }
}

