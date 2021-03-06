package com.arcfighter.syndicateprojectgroup;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alexander.syndicatefighter.Battle.BattleEventItem;
import com.alexander.syndicatefighter.Battle.BattleEventList;
import com.alexander.syndicatefighter.Player.Player;
import com.arcfighter.syndicateprojectgroup.Triggers.Triggers;
import com.arcfighter.syndicateprojectgroup.geofence.GeofenceTransitionsIntentService;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.games.Games;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.gordonwong.materialsheetfab.MaterialSheetFabEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainGameActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,ResultCallback<Status> {

    // Client used to interact with Google APIs
    private GoogleApiClient mainGoogleApiClient;

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

    private Triggers nearByTriggers;

    private com.esri.core.geometry.Point[] nearbyTriggerPointsWGS;

    private ArrayList<Geofence> mGeofenceList;

    private PendingIntent mGeofencePendingIntent;

    private boolean needToUpdateTriggers = false;

    private boolean startedFromNotification = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Layout may change depending on how activity is launched
        setContentView(R.layout.activity_main_game);


        //getting the user preferences
        mainPreference = getSharedPreferences(MAIN_PREF, 0);
        mainPreferenceEditor = mainPreference.edit();

        // Empty list for storing geofences.
        mGeofenceList = new ArrayList<Geofence>();

        mainGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(LocationServices.API)
                .build();


        /* Create the Firebase ref that is used for all authentication with Firebase */
        //TODO doing some more reading, not sure if firebaseref should be implemented at activity level or application level, currently at application level for now
        //TODO Constant for URL
        mFirebaseRef = new Firebase("https://amber-fire-1309.firebaseio.com/");

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            //Check which intent started this activity

            //if the signin page started this activity
            //TODO constant for fromactivity, initactivity, GeofenceTransitionsIntentService
            if(extras.getString("fromactivity").equals("InitActivity")){

//                needToUpdateTriggers = true;//TODO need to consider if this is the best approach (sometimes google sign in is quick and does not allow map location to be loaded first)

                String token = extras.getString("oauth_token");
                mFirebaseRef.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Log.e(TAG, "Authenticated with FIREBASE");
//                            Toast.makeText(MainGameActivity.this, "Authenticated with FIREBASE", Toast.LENGTH_SHORT).show();
                            //TODO someone do something with firebase?
                            String provider = authData.getProvider();
                            String dispname = "No Name";
                            String avatarUrl = "http://i.imgur.com/2OwLvMw.png";
                            if(authData.getProviderData().containsKey("displayName")) {
                                dispname = authData.getProviderData().get("displayName").toString();
                            }
                            if(authData.getProviderData().containsKey("profileImageURL")){
                                avatarUrl = authData.getProviderData().get("profileImageURL").toString();
                            }
                            Player newPlayer = new Player(authData.getUid(),provider,dispname, avatarUrl);
                            mFirebaseRef.child("users").child(authData.getUid()).setValue(newPlayer);

                            //TODO check if first time
                            BattleEventItem bei = new BattleEventItem("123","trainer","andrew");
                            mFirebaseRef.child("users").child(authData.getUid()).child("battleEventList").push().setValue(bei);

                            //should look into why this is not working
                            //BattleEventList bel = new BattleEventList();
//                            Map<String, Object> belMap = new HashMap<String, Object>();
//                            belMap.put("battleEventList", bel);
//                            mFirebaseRef.child("users").child(authData.getUid()).updateChildren(belMap);


//                            Map<String, String> map = new HashMap<String, String>();tinteni
//                            map.put("provider", authData.getProvider());
//                            if(authData.getProviderData().containsKey("displayName")) {
//                                map.put("displayName", authData.getProviderData().get("displayName").toString());
//                            }
//                            mFirebaseRef.child("users").child(authData.getUid()).setValue(map);


                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            Log.e(TAG, "NOT Authenticated with FIREBASE");
//                            Toast.makeText(MainGameActivity.this, "NOT Authenticated with FIREBASE", Toast.LENGTH_SHORT).show();
                        }
                    });


//                //TODO save the mlocation to secured location on device - DONE
//                //TODO update 11/19 this part with mLastSignificantLocation may not be necessary? not sure yet
//                //since android cannot store double, use long http://stackoverflow.com/questions/16319237/cant-put-double-sharedpreferences
//                //get from long to double by using:
//                //Double.longBitsToDouble(XXX);
//                mLastSignificantLocation = extras.getParcelable("location");
//                mainPreferenceEditor.putLong("lastSigLat", Double.doubleToLongBits(mLastSignificantLocation.getLatitude()));
//                mainPreferenceEditor.putLong("lastSigLong", Double.doubleToLongBits(mLastSignificantLocation.getLongitude()));

            }else if(extras.getString("fromactivity").equals("GeofenceTransitionsIntentService")){

                //NOTE this probably will be taken care of in a different activity in the future!!!
                needToUpdateTriggers = false;
                startedFromNotification = true;
                Log.i(TAG, "FROM NOTIFICATION");
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
                                    //on first launch of the locationchanged, everytime the activity is launched

                                    if(!startedFromNotification) {
                                        needToUpdateTriggers = true;

                                        getNearByTriggers(location);
                                        showNearByTriggers();
                                        createGeofencesfromTriggerPoints();

                                        //get current location, and store it into lastSigLat and lastSigLong
                                        mainPreferenceEditor.putLong("lastSigLat", Double.doubleToLongBits(location.getLatitude()));
                                        mainPreferenceEditor.putLong("lastSigLong", Double.doubleToLongBits(location.getLongitude()));

                                        mainPreferenceEditor.apply();//might want to use commit in the future

                                    }else{

                                        //Don't need to udpate, probably from notification
                                        Log.i(TAG, "currentlatlog"+location.getLatitude()+ ", "+location.getLongitude());

                                        double lastLat = Double.longBitsToDouble(mainPreference.getLong("lastSigLat",0));
                                        double lastLong = Double.longBitsToDouble(mainPreference.getLong("lastSigLong",0));
                                        Log.i(TAG,"test"+lastLat+" "+lastLong);

                                        Location oldlocation = new Location(location);
                                        oldlocation.setLatitude(lastLat);
                                        oldlocation.setLongitude(lastLong);


                                        Log.i(TAG, "currentlatlog" + location.getLatitude() + ", " + location.getLongitude());
                                        Log.i(TAG, "oldlatlog"+oldlocation.getLatitude()+ ", "+oldlocation.getLongitude());

                                        Point currentpoint = (Point) GeometryEngine.project(location.getLongitude(), location.getLatitude(), SpatialReference.create(3857));
                                        Point oldpoint = (Point) GeometryEngine.project(oldlocation.getLongitude(), oldlocation.getLatitude(), SpatialReference.create(3857));


                                        double xdiff = Math.abs(currentpoint.getX() - oldpoint.getX());
                                        double ydiff =  Math.abs(currentpoint.getY()-oldpoint.getY());

                                        Log.i(TAG, "diffs"+ xdiff+" "+ydiff);


                                        //TODO not able to test the accuracy of math here, faking my location doesn't seem to register correctly, still taking my real position
                                        if(Math.pow(xdiff, 2)+Math.pow(ydiff,2)>9000000) {
                                            Log.i(TAG, "hypo greater");
                                            needToUpdateTriggers = true;
                                        }else if(xdiff >3200){
                                            Log.i(TAG, "X greater");
                                            needToUpdateTriggers = true;

                                        }else if(ydiff>3200){
                                            Log.i(TAG, "Y greater");
                                            needToUpdateTriggers = true;
                                        }


                                        //create new if needed, else keep the old ones
                                        if(needToUpdateTriggers){
                                            //get current location, and store it into lastSigLat and lastSigLong
                                            mainPreferenceEditor.putLong("lastSigLat", Double.doubleToLongBits(location.getLatitude()));
                                            mainPreferenceEditor.putLong("lastSigLong", Double.doubleToLongBits(location.getLongitude()));
                                            mainPreferenceEditor.apply();//might want to use commit in the future

                                            getNearByTriggers(location);
                                            showNearByTriggers();

                                            createGeofencesfromTriggerPoints();
                                        }else{

                                            //get current location, and store it into lastSigLat and lastSigLong
                                            mainPreferenceEditor.putLong("lastSigLat", Double.doubleToLongBits(oldlocation.getLatitude()));
                                            mainPreferenceEditor.putLong("lastSigLong", Double.doubleToLongBits(oldlocation.getLongitude()));
                                            mainPreferenceEditor.apply();//might want to use commit in the future

                                            getNearByTriggers(oldlocation);
                                            showNearByTriggers();
                                        }

                                    }

                                    double lastLat = Double.longBitsToDouble(mainPreference.getLong("lastSigLat",0));
                                    double lastLong = Double.longBitsToDouble(mainPreference.getLong("lastSigLong",0));
                                    Log.i(TAG,"test"+lastLat+" "+lastLong);


                                    //--IGNORE
//                                    Log.e(TAG, "first location log");
//                                    double lastLat = Double.longBitsToDouble(mainPreference.getLong("lastSigLat",0));
//                                    double lastLong = Double.longBitsToDouble(mainPreference.getLong("lastSigLong",0));
                                    //TODO need comparison to see if current location is significantly further away than the last point
                                    //TODO now it might unnecessary as a new set of triggers will be feteched everytime MainGameActivity is launched
                                    //if it is, then update the Preference for the location
                                    //--END IGNORE





                                    locationChanged = true;
                                    fighterMapView.centerAndZoom(location.getLatitude(), location.getLongitude(), 17);
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

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "attempting to connect!");
        mainGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainGoogleApiClient.disconnect();
    }

    //Start the GeofenceTransitionIntentService so MainGameActivity is not always monitoring for triggers
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    //use nearbyTriggerPointsWGS to create a list of geofences (mGeofenceList)
    private void createGeofencesfromTriggerPoints() {
        //Created a list of Geofences to populate mGeofenceList, which is passed to GeoFencingRequest
        for(int i=0; i<nearbyTriggerPointsWGS.length; i++) {

            //TODO create constants for 400 and 100*60*60*24
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(String.valueOf(i))
                    .setCircularRegion(nearbyTriggerPointsWGS[i].getY(), nearbyTriggerPointsWGS[i].getX(), 400)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .setExpirationDuration(1000*60*60*24)
                    .build());
        }
    }


    //actually initializes the geofences with the google api
    public void addGeofence(){
        //remove any old ones
        removeGeofence();
//        ArrayList<String> removalList = new ArrayList<String>();
//        for(int i=0; i<25; ++i){
//            removalList.add(String.valueOf(i));
//        }
//        LocationServices.GeofencingApi.removeGeofences(mainGoogleApiClient,removalList);

        //add new ones
        LocationServices.GeofencingApi.addGeofences(mainGoogleApiClient, getGeofenceingRequest(), getGeofencePendingIntent()).setResultCallback(this);

    }

    //stop monitoring (if the user turns off the notifications)
    //also may be used when we want to only trigger for changes every hour or so
    public void removeGeofence(){
        LocationServices.GeofencingApi.removeGeofences(mainGoogleApiClient, getGeofencePendingIntent()).setResultCallback(this);
    }




    //use the list of geofences (mGeofenceList) to initiaze the Geofencing request
    private GeofencingRequest getGeofenceingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


    //use the current location to generate closest 25 geofences
    private void getNearByTriggers(Location location){
        nearByTriggers = new Triggers(location);
        nearbyTriggerPointsWGS = nearByTriggers.getNearbyTriggerPointsWGS();
    }

    //display the closest 25 geofences from the nearbyTriggers
    private void showNearByTriggers() {
        Graphic[] nearbyTriggers = nearByTriggers.getNearbyTriggers();

        //TODO make triggergraphiclayer pretty either here or in nearByTriggers java, here is probably better
        triggerGraphicsLayer.addGraphics(nearbyTriggers);
    }

    //setting up that floating android button
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

    @Override
    public void onResult(Status status) {
        if(status.isSuccess()){
            Log.i(TAG, "Result Success");
        }else {
            Log.e(TAG, "Result Error Message: " + status.getStatusMessage());
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "connected here");

        if(needToUpdateTriggers) {
            addGeofence();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
}
