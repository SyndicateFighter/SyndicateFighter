package com.arcfighter.syndicateprojectgroup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.arcfighter.syndicateprojectgroup.Triggers.Triggers;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.map.Graphic;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

public class MainGameActivity extends AppCompatActivity {

    private MapView fighterMapView;
    private Location mLastSignificantLocation;

    private MaterialSheetFab materialSheetFab;

    private LocationDisplayManager locationDisplayManager;

    private SharedPreferences mainPreference;
    private SharedPreferences.Editor mainPreferenceEditor;

    private final String MAIN_PREF = "MAIN_PREF_FILE";

    private static final String TAG = "SYNMAIN";

    private Firebase mFirebaseRef;

    private GraphicsLayer triggerGraphicsLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_game);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);




        mainPreference = getSharedPreferences(MAIN_PREF,0);
        mainPreferenceEditor = mainPreference.edit();

        /* Create the Firebase ref that is used for all authentication with Firebase */
        mFirebaseRef = new Firebase("https://amber-fire-1309.firebaseio.com/");

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            //Check which intent started this activity

            //if the signin page started this activity
            if(extras.getString("fromactivity").equals("InitActivity")){

                String token = extras.getString("oauth_token");
                mFirebaseRef.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Log.e(TAG, "Authenticated with FIREBASE");
                            Toast.makeText(MainGameActivity.this, "Authenticated with FIREBASE", Toast.LENGTH_SHORT).show();
                            //TODO someone do something with firebase?
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Log.e(TAG, "NOT Authenticated with FIREBASE");
                            Toast.makeText(MainGameActivity.this, "NOT Authenticated with FIREBASE", Toast.LENGTH_SHORT).show();
                        }
                    });


                //TODO save the mlocation to secured location on device - DONE
                //TODO update 11/19 this part with mLastSignificantLocation may not be necessary? not sure yet
                //since android cannot store double, use long http://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
                //get from long to double by using:
                //Double.longBitsToDouble(XXX);
                mLastSignificantLocation = extras.getParcelable("location");

                mainPreferenceEditor.putLong("lastSigLat", Double.doubleToLongBits(mLastSignificantLocation.getLatitude()));
                mainPreferenceEditor.putLong("lastSigLong", Double.doubleToLongBits(mLastSignificantLocation.getLongitude()));

            }

        }

        fighterMapView = (MapView) findViewById(R.id.fightermap);
        triggerGraphicsLayer = new GraphicsLayer();
        fighterMapView.addLayer(triggerGraphicsLayer);


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
                    //if(mLocation!=null) {
                        //fighterMapView.centerAndZoom(mLocation.getLatitude(), mLocation.getLongitude(), 15);
                        locationDisplayManager = fighterMapView.getLocationDisplayManager();
                        locationDisplayManager.setLocationListener(new LocationListener() {
                            boolean locationChanged = false;

                            @Override
                            public void onLocationChanged(Location location) {
                                if(!locationChanged) {
                                    //on first launch of the locationchanged

                                    //TODO display the nearby triggers
                                    showNearByTriggers(location);

                                    Log.e(TAG, "first location log");
                                    double lastLat = Double.longBitsToDouble(mainPreference.getLong("lastSigLat",0));
                                    double lastLong = Double.longBitsToDouble(mainPreference.getLong("lastSigLong",0));

                                    //TODO need comparison to see if current location is significantly further away than the last point
                                    //if it is, then update the Preference for the location

                                    locationChanged = true;
                                    fighterMapView.centerAndZoom(location.getLatitude(), location.getLongitude(), 18);
                                    locationDisplayManager.setAutoPanMode(LocationDisplayManager.AutoPanMode.OFF);
                                }
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {

                            }

                            @Override
                            public void onProviderEnabled(String provider) {

                            }

                            @Override
                            public void onProviderDisabled(String provider) {

                            }
                        });
                        locationDisplayManager.start();

                    //}else{
                        ////TODO get mlocation from local storage, the map should always zoom back to the last known location of the user
                        ////TODO update 11/19 may not be needed?
                    //}
                }
            }
        });
    }

    private void showNearByTriggers(Location location) {
        Triggers triggers = new Triggers(location);
        Graphic[] nearbyTriggers = triggers.getNearbyTriggers();

        //TODO make triggergraphiclayer pretty either here or in triggers java, here is probably better
        triggerGraphicsLayer.addGraphics(nearbyTriggers);
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
        findViewById(R.id.fab_sheet_item_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainGameActivity.this,SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        findViewById(R.id.fab_sheet_item_backpack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainGameActivity.this,TeamActivity.class);
                startActivity(settingsIntent);
            }
        });

        findViewById(R.id.fab_sheet_item_team).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(MainGameActivity.this, TeamActivity.class);
                startActivity(settingsIntent);
            }
        });
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
