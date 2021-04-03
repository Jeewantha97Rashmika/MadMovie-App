package com.chathura.madmovie.classes;


import android.content.Intent;
import android.util.Log;

import com.chathura.madmovie.About;
import com.chathura.madmovie.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.annotation.NonNull;

public class FirebasePushNotificationClass extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.d("TAG", "Refreshed token: " + s);
    }
}
