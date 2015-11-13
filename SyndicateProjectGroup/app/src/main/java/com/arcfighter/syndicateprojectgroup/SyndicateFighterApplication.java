package com.arcfighter.syndicateprojectgroup;

import android.app.Application;
import com.firebase.client.Firebase;
/**
 * Created by Andrew on 11/12/15.
 */
public class SyndicateFighterApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }

}
