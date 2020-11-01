package edu.bluejack20_1.gogames

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

class ReminderBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val builder : NotificationCompat.Builder = NotificationCompat.Builder(context!!, "notifyPromo")
            .setSmallIcon(R.drawable.ic_mail)
            .setContentTitle("Promo Notification")
            .setContentText("Check out your new game promo")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager : NotificationManagerCompat = NotificationManagerCompat.from(context)

        notificationManager.notify(200, builder.build())
    }


}