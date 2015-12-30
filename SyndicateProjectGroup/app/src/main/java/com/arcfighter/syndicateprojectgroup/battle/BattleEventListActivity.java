package com.arcfighter.syndicateprojectgroup.battle;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.arcfighter.syndicateprojectgroup.R;
import com.arcfighter.syndicateprojectgroup.battle.dummy.DummyContent;

public class BattleEventListActivity extends FragmentActivity implements BattleEventFragment.OnListFragmentInteractionListener {

    private String TAG = "BATTLEEVENTLISTACTIVITY";
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG,"launched?");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_battle_event_list);

        if(getIntent().getExtras() != null){
            Uid = getIntent().getExtras().getString("Uid");
        }

        loadFragment();
    }

    private void loadFragment() {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment listFragment = BattleEventFragment.newInstance(1, Uid);
        ft.replace(R.id.flContainer, listFragment);
        ft.commit();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Intent i = new Intent(this,BattleActivity.class);
        i.putExtra("monsterName", "SOME NAME");
        startActivity(i);

    }
}
