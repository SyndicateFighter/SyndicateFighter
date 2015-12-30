package com.arcfighter.syndicateprojectgroup.geofence;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.util.Log;

import com.alexander.syndicatefighter.Battle.BattleEventItem;
import com.alexander.syndicatefighter.Battle.BattleEventList;
import com.arcfighter.syndicateprojectgroup.MainGameActivity;
import com.arcfighter.syndicateprojectgroup.R;
import com.arcfighter.syndicateprojectgroup.battle.BattleEventListActivity;
import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class GeofenceTransitionsIntentService extends IntentService {
//    // TODO: Rename actions, choose action names that describe tasks that this
//    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
//    private static final String ACTION_FOO = "com.arcfighter.syndicateprojectgroup.geofence.action.FOO";
//    private static final String ACTION_BAZ = "com.arcfighter.syndicateprojectgroup.geofence.action.BAZ";
//
//    // TODO: Rename parameters
//    private static final String EXTRA_PARAM1 = "com.arcfighter.syndicateprojectgroup.geofence.extra.PARAM1";
//    private static final String EXTRA_PARAM2 = "com.arcfighter.syndicateprojectgroup.geofence.extra.PARAM2";


    private Firebase mFirebaseRef;
    private String Uid;

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

//    /**
//     * Starts this service to perform action Foo with the given parameters. If
//     * the service is already performing a task this action will be queued.
//     *
//     * @see IntentService
//     */
//    // TODO: Customize helper method
//    public static void startActionFoo(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
//        intent.setAction(ACTION_FOO);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }
//
//    /**
//     * Starts this service to perform action Baz with the given parameters. If
//     * the service is already performing a task this action will be queued.
//     *
//     * @see IntentService
//     */
//    // TODO: Customize helper method
//    public static void startActionBaz(Context context, String param1, String param2) {
//        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
//        intent.setAction(ACTION_BAZ);
//        intent.putExtra(EXTRA_PARAM1, param1);
//        intent.putExtra(EXTRA_PARAM2, param2);
//        context.startService(intent);
//    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mFirebaseRef = new Firebase("https://amber-fire-1309.firebaseio.com/");

        AuthData authData = mFirebaseRef.getAuth();
        if (authData != null) {
            // user authenticated
            Log.i("Intent", "authenticated");

            BattleEventItem bei = new BattleEventItem("456","wild","alexander");
            Uid = authData.getUid();
            mFirebaseRef.child("users").child(authData.getUid()).child("battleEventList").push().setValue(bei);


        }else{
            //TODO bug where if on launch user is within trigger, it will fail here, sometimes
            Log.i("Intent", "NOT authenticated");
        }


        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            //TODO implement error logging/catching here
//            String errorMessage = GeofenceErrorMessages.getErrorString(this,
//                    geofencingEvent.getErrorCode());
//            Log.e(TAG, errorMessage);
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            String geofenceTransitionDetails = getGeofenceTransitionDetails(this, geofenceTransition, triggeringGeofences);

            //send off the notification
            sendNotification(geofenceTransitionDetails);
        }

    }

    private String getGeofenceTransitionDetails(Context context, int geofenceTransition, List<Geofence> triggeringGeofences){
        //String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        //TODO "Entered Area" needs to go into one of those R.string
        return "Encountered Monster" + ": " + triggeringGeofencesIdsString;

    }


    /**
    * Posts a notification in the notification bar when a transition is detected.
            * If the user clicks the notification, control goes to the MainActivity.
            */
    private void sendNotification(String notificationDetails) {

        // Create an explicit content Intent that starts the main game Activity.
        //TODO consider maybe a new activity to hold a list of all events or challenges??
        Intent notificationIntent = new Intent(getApplicationContext(), BattleEventListActivity.class);

        notificationIntent.putExtra("fromactivity", "GeofenceTransitionsIntentService");
        notificationIntent.putExtra("Uid", Uid);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainGameActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        //TODO update the graphics and text here!!!
        // Define the notification settings.
        builder.setSmallIcon(R.drawable.cast_ic_notification_0)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.cast_ic_notification_0))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText("Prepare to Battle!!")
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }

//    /**
//     * Handle action Foo in the provided background thread with the provided
//     * parameters.
//     */
//    private void handleActionFoo(String param1, String param2) {
//        // TODO: Handle action Foo
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    /**
//     * Handle action Baz in the provided background thread with the provided
//     * parameters.
//     */
//    private void handleActionBaz(String param1, String param2) {
//        // TODO: Handle action Baz
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
}
