package com.example.assignment2

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class CustomReceiverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enable edge-to-edge layout
        WindowCompat.setDecorFitsSystemWindows(window, false)

        // Make status bar transparent
        window.statusBarColor = Color.TRANSPARENT

        // Make status bar icons dark
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightStatusBars = true

        // Make navigation bar icons dark
        WindowCompat.getInsetsController(window, window.decorView)
            .isAppearanceLightNavigationBars = true

        // White background behind system bars
        window.decorView.setBackgroundColor(Color.WHITE)

        setContentView(R.layout.activity_custom_receiver)

        // Apply system bar padding to root layout
        val mainLayout = findViewById<android.view.View>(R.id.receiverMainLayout)

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
        val toolbar = findViewById<MaterialToolbar>(R.id.receiverToolbar)

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

        // Display received message
        val receivedMessage = findViewById<TextView>(R.id.receivedMessage)

        val message = intent.getStringExtra("message") ?: ""

        receivedMessage.text = "Received Message:\n$message"
    }
}