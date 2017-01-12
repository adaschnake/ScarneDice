package com.example.demouser.scarnesdice;

import android.content.Intent;
import android.os.Handler;
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
    public static String userScore;
    public static String compScore;

    int numRolls = 0;
    final Handler timerHandler = new Handler();



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
                roll();
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hold();
            }
        });

        ((Button)findViewById(R.id.resetButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

    }


    public void roll()
    {
        int roll = random.nextInt(6)+1;


        showDie(roll);
        numRolls +=1;

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


        checkWinners();
    }

    private void showDie(int roll) {

        if (roll == 1) {
            dieView.setImageResource(R.drawable.dice1);
            dieView.setContentDescription("Die face showing 1");
        } else if (roll == 2)
        {
            dieView.setImageResource(R.drawable.dice2);
            dieView.setContentDescription("Die face showing 2");
        }
        else if (roll==3)
        {
            dieView.setImageResource(R.drawable.dice3);
            dieView.setContentDescription("Die face showing 3");
        }
            else if (roll==4)
        {
            dieView.setImageResource(R.drawable.dice4);
            dieView.setContentDescription("Die face showing 4");
        }
            else if (roll==5)
        {
            dieView.setImageResource(R.drawable.dice5);
            dieView.setContentDescription("Die face showing 5");
        }
            else
        {
            dieView.setImageResource(R.drawable.dice6);
            dieView.setContentDescription("Die face showing 6");
        }


        }

    private void checkWinners() {

        if (currentPlayer ==1 && player1Total+currentTurn >= 50)
        {
            Intent intent = new Intent(this, WinActivity.class);
            intent.putExtra(userScore, String.valueOf((player1Total+currentTurn)));
            startActivity(intent);
            reset();

        }

        else if (currentPlayer == 2 && player2Total+currentTurn >= 50)
        {
            Intent intent = new Intent(this, LoseActivity.class);
            intent.putExtra(compScore, String.valueOf((player2Total+currentTurn)));
            startActivity(intent);
            reset();
        }

    }


    public void hold()
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


    public void reset()
    {
        currentTurn = 0;
        player1Total = 0;
        player2Total = 0;
        currentPlayer=1;
        ((TextView)findViewById(R.id.otherInfo)).setText(String.format(getString(R.string.turnText), currentPlayer));
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
        if (currentPlayer==1) {
            currentPlayer = 2;
            computerTurnin500();
            rollButton.setEnabled(false);
            holdButton.setEnabled(false);

        }

        else if (currentPlayer==2) {
            currentPlayer = 1;
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
        }


        numRolls=0;
        ((TextView)findViewById(R.id.otherInfo)).setText(String.format(getString(R.string.turnText), currentPlayer));
    }

    private boolean shouldHold()
    {
        if((((double)currentTurn)/((double)numRolls) >= 4) && numRolls >3)
            return true;
        else if (currentTurn>5)
            return true;
        else
            return false;
    }

    private void computerTurnin500()
    {
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                computerTurn();
                if (currentPlayer==2)
                {
                    computerTurnin500();
                }
            }
        },1000);
    }

    private void computerTurn() {
        roll();
        if (shouldHold())
        {
            hold();
        }
    }

}
