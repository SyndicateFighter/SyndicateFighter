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
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

public class MainGameActivity extends AppCompatActivity {

    private MapView fighterMapView;
    private Location mLocation;

    private MaterialSheetFab materialSheetFab;

    //TODO move firebase auth to here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            //Check which intent started this activity

            //if the signin page started this activity
            if(extras.getString("fromactivity").equals("InitActivity")){
                mLocation = extras.getParcelable("location");

                //TODO save the mlocation to secured location on device

                //TODO firebase auth

            }

        }

        fighterMapView = (MapView) findViewById(R.id.fightermap);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        setupFab();


        fighterMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if(status == STATUS.INITIALIZED){
                    fighterMapView.centerAndZoom(mLocation.getLatitude(),mLocation.getLongitude(),15);
                }
            }
        });
    }

    private void setupFab(){
        Fab fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = getResources().getColor(R.color.background_card);
        int fabColor = getResources().getColor(R.color.theme_accent);

        materialSheetFab = new MaterialSheetFab<>(fab,sheetView,overlay,sheetColor,fabColor);
        materialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                super.onShowSheet();
            }

            @Override
            public void onHideSheet() {
                super.onHideSheet();
            }
        });

        // TODO Set material sheet item click listeners
        //findViewById(R.id.fab_sheet_item_setting).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        //TODO need to implement what happens when back button is pressed
        if(materialSheetFab.isSheetVisible()){
            materialSheetFab.hideSheet();
        }else{
            super.onBackPressed();
        }
    }
}
