package com.example.assignment2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class BatteryActivity : AppCompatActivity() {

    private lateinit var batteryText: TextView

    // Battery Broadcast Receiver
    private val batteryReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

            val level = intent?.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1
            ) ?: -1

            val scale = intent?.getIntExtra(
                BatteryManager.EXTRA_SCALE, -1
            ) ?: -1

            if (level >= 0 && scale > 0) {

                val percentage = level * 100 / scale

                batteryText.text = "Battery Percentage: $percentage%"
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Transparent status bar over white background
        window.statusBarColor = Color.TRANSPARENT

        // Dark status bar icons
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        // Dark navigation bar icons
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightNavigationBars = true

        // White background behind system bars
        window.decorView.setBackgroundColor(Color.WHITE)

        setContentView(R.layout.activity_battery)

        // Apply system bar padding
        val mainLayout = findViewById<android.view.View>(
            R.id.batteryMainLayout
        )

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // Setup toolbar
        val toolbar = findViewById<MaterialToolbar>(
            R.id.batteryToolbar
        )

        toolbar.setNavigationIcon(
            androidx.appcompat.R.drawable.abc_ic_ab_back_material
        )

        // Dark gray back arrow
        toolbar.setNavigationIconTint(
            Color.parseColor("#333333")
        )

        toolbar.setNavigationOnClickListener {
            finish()
        }

        // Battery percentage TextView
        batteryText = findViewById(R.id.batteryText)
    }

    override fun onStart() {
        super.onStart()

        // Register battery receiver
        registerReceiver(
            batteryReceiver,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
    }

    override fun onStop() {
        super.onStop()

        // Unregister battery receiver
        unregisterReceiver(batteryReceiver)
    }
}