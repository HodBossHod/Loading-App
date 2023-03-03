package com.udacity

import android.app.NotificationManager
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.utils.cancelNotifications
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar)
        val status=intent.getStringExtra("Status")
        val fileName=intent.getStringExtra("FileName")
        binding.details.status.text=status
        binding.details.fileName.text=fileName
        setDetailsColor(status)
        binding.details.okBtn.setOnClickListener {
            finish()
        }
        val notificationManager = ContextCompat.getSystemService(
            this.applicationContext,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotifications()
    }

    private fun setDetailsColor(status: String?) {
        if (intent.getStringExtra(status).equals("Fail")) {
            binding.details.status.setTextColor(getColor(R.color.red))
            binding.details.fileName.setTextColor(getColor(R.color.red))
        } else {
            binding.details.status.setTextColor(getColor(R.color.green))
            binding.details.fileName.setTextColor(getColor(R.color.green))
        }
    }

}
