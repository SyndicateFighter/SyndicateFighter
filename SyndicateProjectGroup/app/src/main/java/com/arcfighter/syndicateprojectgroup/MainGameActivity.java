package com.arcfighter.syndicateprojectgroup;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;

public class MainGameActivity extends AppCompatActivity {

    private MapView fighterMapView;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            if(extras.getString("fromactivity").equals("InitActivity")){
                mLocation = extras.getParcelable("location");

            }

        }

        fighterMapView = (MapView) findViewById(R.id.fightermap);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        fighterMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED){
                    fighterMapView.centerAndZoom(mLocation.getLatitude(),mLocation.getLongitude(),15);
                }
            }
        });
    }

}
