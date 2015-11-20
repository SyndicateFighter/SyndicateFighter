package com.arcfighter.syndicateprojectgroup;

import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.arcfighter.basegameutil.BaseGameUtils;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;

import java.io.IOException;

public class InitActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;
    private boolean mAutoStartSignInflow = true;
    private boolean mResolvingConnectionFailure = false;

    // Request code to use when launching the resolution activity
    private static int RC_SIGN_IN = 9001;




    /* A flag indicating that a PendingIntent is in progress and prevents us from starting further intents. */
    private boolean mGoogleIntentInProgress;

    private static final String TAG = InitActivity.class.getSimpleName();
    public static final String INIT_LAUNCH= "InitialLaunch";


    private Location mLocation;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);




        // Create the Google API Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(LocationServices.API)
                .build();

        if(mAutoStartSignInflow) {
            mGoogleApiClient.connect();
        }



    }

    @Override
    public void onConnected(Bundle bundle) {


        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        //Player is signed in, proceed to game load and fighter map activity
        Toast.makeText(this,"SIGNED IN!",Toast.LENGTH_SHORT).show();
        getGoogleOAuthTokenAndLogin();


    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(mResolvingConnectionFailure){
            return;
        }
        if(mAutoStartSignInflow){
            mAutoStartSignInflow = false;
            mResolvingConnectionFailure = true;

            if(BaseGameUtils.resolveConnectionFailure(this,mGoogleApiClient,connectionResult,RC_SIGN_IN, getString(R.string.signin_other_error))){
                mGoogleIntentInProgress = true;
                mResolvingConnectionFailure = false;

            }else{
                mGoogleIntentInProgress = false;
                mResolvingConnectionFailure = true;
            }
        }
        //Display Signin Button
    }

    private void getGoogleOAuthTokenAndLogin() {
        /* Get OAuth token in Background */
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(InitActivity.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!mGoogleIntentInProgress) {

                        //TODO this needs to be modified in the future!!!
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        //startActivityForResult(recover, FighterMap.RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }

                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                //mGoogleLoginClicked = false;
                Intent resultIntent = new Intent(InitActivity.this,MainGameActivity.class);
                if (token != null) {


//                    //TODO this will need to be moved to appropriate section, most likely main game activity in the future
//                    mFirebaseRef.authWithOAuthToken("google", token, new Firebase.AuthResultHandler() {
//                        @Override
//                        public void onAuthenticated(AuthData authData) {
//                            Log.e(TAG, "Authenticated with FIREBASE");
//                            Toast.makeText(InitActivity.this, "Authenticated with FIREBASE", Toast.LENGTH_SHORT).show();
//
//
//                        }
//
//                        @Override
//                        public void onAuthenticationError(FirebaseError firebaseError) {
//                            Log.e(TAG, "NOT Authenticated with FIREBASE");
//                            Toast.makeText(InitActivity.this, "NOT Authenticated with FIREBASE", Toast.LENGTH_SHORT).show();
//                        }
//                    });

                    //TODO in the future keys will need to be obatined from the xml
                    resultIntent.putExtra("fromactivity", "InitActivity");
                    resultIntent.putExtra("oauth_token", token);
                    resultIntent.putExtra("location", mLocation);
//                    startService(resultIntent);
                    startActivity(resultIntent);

                } else if (errorMessage != null) {
                    Log.e(TAG, "no token");
                    //resultIntent.putExtra("error", errorMessage);
                }
                //setResult(FighterMap.RC_GOOGLE_LOGIN, resultIntent);
                finish();
            }
        };
        task.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }
}
