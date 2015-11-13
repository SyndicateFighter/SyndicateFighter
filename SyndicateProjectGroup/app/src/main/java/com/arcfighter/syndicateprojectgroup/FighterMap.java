package com.arcfighter.syndicateprojectgroup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FighterMap extends AppCompatActivity {

    /* Request code used to invoke sign in user interactions for Google+ */
    public static final int RC_GOOGLE_LOGIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fighter_map);
    }
}
