package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R

private const val NOTIFICATION_ID = 0


fun NotificationManager.sendNotification(applicationContext: Context, downloadStatus:String, selectedFileName:String){
    val detailIntent=Intent(applicationContext,DetailActivity::class.java)
    detailIntent.putExtra("Status",downloadStatus)
    detailIntent.putExtra("FileName",selectedFileName)
    val detailPendingIntent= PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        detailIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notificationBuilder= NotificationCompat.Builder(
        applicationContext,
        MainActivity.CHANNEL_ID
    ).setSmallIcon(R.drawable.download_icon)
        .setAutoCancel(true)
        .setContentText(applicationContext.getString(R.string.notification_description))
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentIntent(detailPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(
            R.drawable.ic_baseline_check_24,
            applicationContext.getString(R.string.notification_button),
            detailPendingIntent
        )

    notify(NOTIFICATION_ID, notificationBuilder.build())

}


fun NotificationManager.cancelNotifications(){
    cancelAll()
}