package com.example.demouser.scarnesdice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Random random;
    int player1Total;
    int player2Total;
    int currentTurn;
    Players currentPlayer;
    Button rollButton;
    Button holdButton;
    ImageView dieView;
    public static String userScore;
    public static String compScore;
    private int roll;

    private TextView otherInfo;
    private ScarnesDiceGame game;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private String mUserEmail;
    private DatabaseReference mFirebaseDatabase;
    private PlayerState state;

    int numRolls = 0;
    final Handler timerHandler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if(mFirebaseUser == null)
        {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }
        else
        {
            mUserEmail = mFirebaseUser.getEmail();
            configureDatabase();
        }

        setContentView(R.layout.activity_main);
        rollButton = (Button)findViewById(R.id.rollButton);
        holdButton = (Button)findViewById(R.id.holdButton);
        dieView = (ImageView)findViewById(R.id.dieView);
        otherInfo = ((TextView)findViewById(R.id.otherInfo));


        random = new Random();
        currentPlayer = Players.PLAYER1;

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

    private void startGame(PlayerState newState)
    {
        game = new ScarnesDiceGame(state.getEmail(), newState.getEmail(), (random.nextBoolean() ? Players.PLAYER1 : Players.PLAYER2) );

        mFirebaseDatabase.child("games").child(game.getId()).setValue(game);

        mFirebaseDatabase.child("games").child(game.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                game = dataSnapshot.getValue(ScarnesDiceGame.class);
                updateGameView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void updateGameView()
    {

        if (roll == 1)
        {
            otherInfo.setText("Changing players!");
            changePlayers();
        }
        else if (currentPlayer())
        {
            otherInfo.setText("Your turn!");
        }
        else
        {
            otherInfo.setText("Waiting for other player...");
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
        }

        if (roll>0)
        {
            roll = game.getLastRoll();
            showDie(roll);
            checkWinners();
        }

        player1Total = game.getPlayer1Score();
        player2Total = game.getPlayer2Score();
        currentTurn = game.getCurrentTurn();

        updateCurrentTurn();
        updatePlayerScores();
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

    private boolean currentPlayer()
    {
        return true;
    }




    public void roll()
    {

        int roll = random.nextInt(6)+1;

//
//
          showDie(roll);
//        numRolls +=1;
//
        if (roll == 1)
        {
            currentTurn = 0;
            changePlayers();
        }
        else
        {
            currentTurn += roll;
        }
        updateGameView();


        checkWinners();
    }

    private void checkWinners() {

        if (currentPlayer.equals(Players.PLAYER1) && player1Total+currentTurn >= 50)
        {
            Intent intent = new Intent(this, WinActivity.class);
            intent.putExtra(userScore, String.valueOf((player1Total+currentTurn)));
            startActivity(intent);
            reset();

        }

        else if (currentPlayer.equals(Players.PLAYER2) && player2Total+currentTurn >= 50)
        {
            Intent intent = new Intent(this, LoseActivity.class);
            intent.putExtra(compScore, String.valueOf((player2Total+currentTurn)));
            startActivity(intent);
            reset();
        }
        updateGameView();


//        if (currentPlayer ==1 && player1Total+currentTurn >= 50)
//        {
//            Intent intent = new Intent(this, WinActivity.class);
//            intent.putExtra(userScore, String.valueOf((player1Total+currentTurn)));
//            startActivity(intent);
//            reset();
//
//        }
//
//        else if (currentPlayer == 2 && player2Total+currentTurn >= 50)
//        {
//            Intent intent = new Intent(this, LoseActivity.class);
//            intent.putExtra(compScore, String.valueOf((player2Total+currentTurn)));
//            startActivity(intent);
//            reset();
//        }

    }


    public void hold()
    {

        if (currentPlayer.equals(Players.PLAYER1))
        {
            player1Total += currentTurn;
        }

        else
        {
            player2Total += currentTurn;
        }
        updateGameView();
//        if (currentPlayer == 1)
//        {
//            player1Total += currentTurn;
//        }
//
//        else
//        {
//            player2Total += currentTurn;
//        }
        currentTurn =0;
       updateCurrentTurn();
//        updatePlayerScores();
//        changePlayers();
    }


    public void reset()
    {
//        currentTurn = 0;
//        player1Total = 0;
//        player2Total = 0;
//        currentPlayer=1;
//        ((TextView)findViewById(R.id.otherInfo)).setText(String.format(getString(R.string.turnText), currentPlayer));
//        updatePlayerScores();
//        updateCurrentTurn();
//        ((TextView)findViewById(R.id.otherInfo)).setText("");
//        rollButton.setEnabled(true);
//        holdButton.setEnabled(true);

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
        if (currentPlayer.equals(Players.PLAYER1)) {
            currentPlayer = Players.PLAYER2;
        }

        else if (currentPlayer.equals(Players.PLAYER2)) {
            currentPlayer = Players.PLAYER1;
        }




//        if (currentPlayer==1) {
//            currentPlayer = 2;
//            computerTurnin500();
//            rollButton.setEnabled(false);
//            holdButton.setEnabled(false);
//
//        }
//
//        else if (currentPlayer==2) {
//            currentPlayer = 1;
//            rollButton.setEnabled(true);
//            holdButton.setEnabled(true);
//        }
//
//
//        numRolls=0;
//        ((TextView)findViewById(R.id.otherInfo)).setText(String.format(getString(R.string.turnText), currentPlayer));
    }

//    private boolean shouldHold()
//    {
//        if((((double)currentTurn)/((double)numRolls) >= 4) && numRolls >3)
//            return true;
//        else if (currentTurn>5)
//            return true;
//        else
//            return false;
//    }

//    private void computerTurnin500()
//    {
//        timerHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                computerTurn();
//                if (currentPlayer==2)
//                {
//                    computerTurnin500();
//                }
//            }
//        },1000);
//    }
//
//    private void computerTurn() {
//        roll();
//        if (shouldHold())
//        {
//            hold();
//        }
//    }
//
    private void configureDatabase()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference();

        mFirebaseDatabase.child("players").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                PlayerState newState = dataSnapshot.getValue(PlayerState.class);
                if (newState != dataSnapshot.getValue(PlayerState.class) && newState.getStatus() == PlayerState.PlayerStatus.READY && state.getStatus() == PlayerState.PlayerStatus.READY)
                {
                    state.setStatus(PlayerState.PlayerStatus.IN_GAME);
                    newState.setStatus(PlayerState.PlayerStatus.IN_GAME);
                    mFirebaseDatabase.child("players").child(state.getId()).setValue(state);
                    mFirebaseDatabase.child("players").child(newState.getId()).setValue(newState);
                    startGame(newState);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        state = new PlayerState(mUserEmail);
        mFirebaseDatabase.child("players").push().setValue(state);
    }

    enum Players
    {
        PLAYER1, PLAYER2
    }


}
