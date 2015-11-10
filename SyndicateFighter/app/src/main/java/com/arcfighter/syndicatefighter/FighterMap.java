package com.arcfighter.syndicatefighter;

import android.location.Location;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.esri.android.geotrigger.GeotriggerBroadcastReceiver;
import com.esri.android.geotrigger.GeotriggerService;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FighterMap extends AppCompatActivity implements
        GeotriggerBroadcastReceiver.LocationUpdateListener,
        GeotriggerBroadcastReceiver.ReadyListener {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private View mContentView;
    //private View mControlsView;
    private boolean mVisible;

    private boolean initialzoom = true;
    public static final String TAG = "FighterMap";
    private static final String AGO_CLIENT_ID = "Hi6hVNDbrnAzC0aE";
    private static final String GCM_SENDER_ID = "502999115026 ";

    private static final String[] TAGS = new String[] {"explorer"};

    private GeotriggerBroadcastReceiver mGeotriggerBroadcastReceiver;


    MapView fighterMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fighter_map);

        mVisible = true;
        //mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fighterMap);

//
//        // Set up the user interaction to manually show or hide the system UI.
//        mContentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                toggle();
//            }
//        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        //findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);

        mGeotriggerBroadcastReceiver = new GeotriggerBroadcastReceiver();

        GeotriggerHelper.startGeotriggerService(this, AGO_CLIENT_ID, GCM_SENDER_ID, TAGS,
                GeotriggerService.TRACKING_PROFILE_ADAPTIVE);

        fighterMapView = (MapView) findViewById(R.id.fighterMap);
        ArcGISTiledMapServiceLayer grayTiled = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/arcgis/rest/services/Canvas/World_Dark_Gray_Base/MapServer");
        //ArcGISTiledMapServiceLayer grayReference = new ArcGISTiledMapServiceLayer("http://services.arcgisonline.com/arcgis/rest/services/Canvas/World_Dark_Gray_Reference/MapServer");
        fighterMapView.addLayer(grayTiled);
        //fighterMapView.addLayer(grayReference);


    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the receiver. Activity will no longer respond to
        // GeotriggerService intents. Tracking and push notification handling
        // will continue in the background.
        unregisterReceiver(mGeotriggerBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register the receiver. The default intent filter listens for all
        // intents that the receiver can handle. If you need to handle events
        // while the app is in the background, you must register the receiver
        // in the manifest.
        registerReceiver(mGeotriggerBroadcastReceiver,
                GeotriggerBroadcastReceiver.getDefaultIntentFilter());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
//    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            if (AUTO_HIDE) {
//                delayedHide(AUTO_HIDE_DELAY_MILLIS);
//            }
//            return false;
//        }
//    };

//    private void toggle() {
//        if (mVisible) {
//            hide();
//        } else {
//            show();
//        }
//    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        //mHideHandler.removeCallbacks(mShowPart2Runnable);
        //mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

//    private final Runnable mHidePart2Runnable = new Runnable() {
//        @SuppressLint("InlinedApi")
//        @Override
//        public void run() {
//            // Delayed removal of status and navigation bar
//
//            // Note that some of these constants are new as of API 16 (Jelly Bean)
//            // and API 19 (KitKat). It is safe to use them, as they are inlined
//            // at compile-time and do nothing on earlier devices.
////            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
////                    | View.SYSTEM_UI_FLAG_FULLSCREEN
////                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
////                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//        }
//    };

//    @SuppressLint("InlinedApi")
//    private void show() {
//        // Show the system bar
////        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
////        mVisible = true;
//
//        // Schedule a runnable to display UI elements after a delay
//        mHideHandler.removeCallbacks(mHidePart2Runnable);
//        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
//    }

//    private final Runnable mShowPart2Runnable = new Runnable() {
//        @Override
//        public void run() {
//            // Delayed display of UI elements
////            ActionBar actionBar = getSupportActionBar();
////            if (actionBar != null) {
////                actionBar.show();
////            }
//            //mControlsView.setVisibility(View.VISIBLE);
//        }
//    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
//
//    /**
//     * Schedules a call to hide() in [delay] milliseconds, canceling any
//     * previously scheduled calls.
//     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onLocationUpdate(Location location, boolean b) {
        Toast.makeText(this, "Location Update Received!",
                Toast.LENGTH_SHORT).show();
        Log.d(TAG, String.format("Location update received: (%f, %f)",
                location.getLatitude(), location.getLongitude()));


        //Log.d(TAG, fighterMapView.getCenter().getX()+" "+fighterMapView.getCenter().getY());

        if(initialzoom && fighterMapView.isLoaded()) {
            //fighterMapView.centerAndZoom(location.getLatitude(), location.getLongitude(), );
            SpatialReference sr = SpatialReference.create(3857);
            Point p = GeometryEngine.project(location.getLongitude(), location.getLatitude(), sr);
            Log.d(TAG, p.getX()+ " "+ p.getY());
            fighterMapView.zoomToScale(p, 13);
            initialzoom = false;
        }

    }

    @Override
    public void onReady() {
        Toast.makeText(this, "GeotriggerService ready!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "GeotriggerService ready!");

    }
}
