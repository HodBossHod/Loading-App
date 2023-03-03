package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var binding:ActivityMainBinding
    private var fileName=""
    private lateinit var notificationManager: NotificationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        createChannel(CHANNEL_ID, getString(R.string.notification_channel_description))
        okButtonClicked()
    }

    private fun okButtonClicked() {
        custom_button.setOnClickListener {
            custom_button.buttonState = ButtonState.Clicked;
            when (radioGroup.checkedRadioButtonId) {
                R.id.loadAppOption -> {
                    custom_button.buttonState = ButtonState.Loading;
                    download(LOAD_APP_URL)
                    fileName = getString(R.string.load_app_option)
                }
                R.id.glideOption -> {
                    custom_button.buttonState = ButtonState.Loading;
                    download(GLIDE_URL)
                    fileName = getString(R.string.glide_option)
                }
                R.id.retrofitOption -> {
                    custom_button.buttonState = ButtonState.Loading;
                    download(RETROFIT_URL)
                    fileName = getString(R.string.retrofit_option)
                }
                else -> {
                    Toast.makeText(
                        this,
                        getString(R.string.download_toast),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context?.apply{
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                id?.apply {
                    val downloadManager=context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                    val query=DownloadManager.Query()
                    query.setFilterById(id)
                    val record=downloadManager.query(query)
                    if (record.moveToFirst()){
                        val columnIndex=record.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        val status=record.getInt(columnIndex)
                        notificationManager = ContextCompat.getSystemService(
                            context,
                            NotificationManager::class.java
                        ) as NotificationManager
                        when(status){
                            DownloadManager.STATUS_SUCCESSFUL->{
                                Toast.makeText(context,getString(R.string.success_toast),Toast.LENGTH_SHORT).show()
                                notificationManager.sendNotification(context,getString(R.string.success_status),fileName)
                            }
                            else->{
                                Toast.makeText(context,getString(R.string.fail_toast),Toast.LENGTH_SHORT).show()
                                notificationManager.sendNotification(context,getString(R.string.fail_status),fileName)
                            }
                        }

                    }
                }
            }
            custom_button.buttonState=ButtonState.Completed
        }
    }
    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_channel_description)
            notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    private fun download(url:String) {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverRoaming(true)
                .setAllowedOverMetered(true)
        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID = downloadManager.enqueue(request)// enqueue puts the download request in the queue.

    }


    companion object {
        private const val GLIDE_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val LOAD_APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"

        const val CHANNEL_ID = "channel ID"
    }

}
