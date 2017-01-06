package com.example.demouser.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random random;
    int player1Total;
    int player2Total;
    int currentTurn;
    int currentPlayer;
    Button rollButton;
    Button holdButton;
    ImageView dieView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rollButton = (Button)findViewById(R.id.rollButton);
        holdButton = (Button)findViewById(R.id.holdButton);
        dieView = (ImageView)findViewById(R.id.dieView);


        random = new Random();
        currentPlayer = 1;

        rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roll(v);
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hold(v);
            }
        });

        ((Button)findViewById(R.id.resetButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset(v);
            }
        });

    }


    public void roll(View view)
    {
        int roll = random.nextInt(6)+1;

        //((TextView)findViewById(R.id.dieRoll)).setText(""+roll);
        showDie(roll);

        if (roll == 1)
        {
            currentTurn = 0;
            changePlayers();
        }
        else
        {
            currentTurn += roll;
        }
        updateCurrentTurn();


        checkWinners(view);
    }

    private void showDie(int roll) {

        if (roll==1)
            dieView.setImageResource(R.drawable.dice1);
        else if (roll==2)
            dieView.setImageResource(R.drawable.dice2);
        else if (roll==3)
            dieView.setImageResource(R.drawable.dice3);
        else if (roll==4)
            dieView.setImageResource(R.drawable.dice4);
        else if (roll==5)
            dieView.setImageResource(R.drawable.dice5);
        else
            dieView.setImageResource(R.drawable.dice6);


    }

    private void checkWinners(View v) {

        if (currentPlayer ==1 && player1Total+currentTurn >= 50)
        {
            ((TextView)findViewById(R.id.otherInfo)).setText("Player 1 wins!");
            rollButton.setEnabled(false);
            holdButton.setEnabled(false);
            currentPlayer=0;
        }

        else if (currentPlayer == 2 && player2Total+currentTurn >= 50)
        {
            ((TextView)findViewById(R.id.otherInfo)).setText("Player 2 wins!");
            rollButton.setEnabled(false);
            holdButton.setEnabled(false);
            currentPlayer = 0;
        }

    }


    public void hold(View view)
    {
        if (currentPlayer == 1)
        {
            player1Total += currentTurn;
        }

        else
        {
            player2Total += currentTurn;
        }
        currentTurn =0;
        updateCurrentTurn();
        updatePlayerScores();
        changePlayers();
    }


    public void reset(View view)
    {
        currentTurn = 0;
        player1Total = 0;
        player2Total = 0;
        currentPlayer=1;
        updatePlayerScores();
        updateCurrentTurn();
        ((TextView)findViewById(R.id.otherInfo)).setText("");
        rollButton.setEnabled(true);
        holdButton.setEnabled(true);

    }

    private void updatePlayerScores()
    {
        ((TextView)findViewById(R.id.player1Score)).setText(""+player1Total);
        ((TextView)findViewById(R.id.player2Score)).setText(""+player2Total);

    }

    private void updateCurrentTurn()
    {
        ((TextView)findViewById(R.id.turnScore)).setText(""+currentTurn);
    }

    private void changePlayers()
    {
        if (currentPlayer==1)
            currentPlayer=2;

        else if (currentPlayer==2)
            currentPlayer=1;

        ((TextView)findViewById(R.id.otherInfo)).setText("Player "+currentPlayer+"'s turn!");
    }

    private boolean shouldHold()
    {

    }

    private void computerTurnin500()
    {
        
    }

}
