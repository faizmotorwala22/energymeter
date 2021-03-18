package com.example.uom_app;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Belal on 12/8/2017.
 */

//the class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIdService extends FirebaseMessagingService {


    //this method will be called
    //when the token is generated

    @Override
    public void onNewToken(String mToken) {
        super.onNewToken(mToken);
        Log.e("TOKEN",mToken);
        sendRegistrationToServer(mToken);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }




    private void sendRegistrationToServer(String refreshedToken) {
        Log.d("TOKEN ", refreshedToken.toString());
    }
}
