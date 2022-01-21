package com.jaylangkung.brainnet_staff.utils

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jaylangkung.dht.utils.NotificationHelper

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val title = remoteMessage.data["title"]
        val body = remoteMessage.data["message"]

        NotificationHelper(applicationContext).displayNotification(title!!, body!!)
    }
}