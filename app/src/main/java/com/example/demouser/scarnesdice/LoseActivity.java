package com.example.demouser.scarnesdice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);

        Intent intent = getIntent();
        String score = intent.getStringExtra(MainActivity.compScore);

        ((TextView)findViewById(R.id.scoreText)).setText(String.format("The computer scored %s", score));

        ((Button)findViewById(R.id.playAgainButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
