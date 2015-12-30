package com.arcfighter.syndicateprojectgroup.battle;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.arcfighter.syndicateprojectgroup.MainGameActivity;
import com.arcfighter.syndicateprojectgroup.R;

import java.util.logging.LogRecord;

public class BattleActivity extends AppCompatActivity {

    final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("BATLEACTIVITY", "ENTERED BATTLE!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        Button attackbutton = (Button) findViewById(R.id.attackbutton);
        attackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BattleActivity.this,"ATTACK!",Toast.LENGTH_LONG).show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BattleActivity.this,"YOU WIN!",Toast.LENGTH_LONG).show();

                        Intent i = new Intent(BattleActivity.this, MainGameActivity.class);
//                        i.putExtra("monsterName", "SOME NAME");
                        startActivity(i);
                    }
                },5000);
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            }
        });
    }





}
