package com.himanshu.osfeaturendkdemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.himanshu.osfeaturendkdemo.databinding.ActivityStartupBinding

class StartupActivity : AppCompatActivity() {
    lateinit var binding: ActivityStartupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_startup)

        binding.btnImageProcessor.setOnClickListener {
            val intent = Intent(this, ImageFilterActivity::class.java)
            startActivity(intent)
        }
        binding.btnNsfwProcessor.setOnClickListener {
            val intent = Intent(this, NsfwActivity::class.java);
            startActivity(intent)
        }
    }
}