package com.example.assignment2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class CustomInputActivity : AppCompatActivity() {

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

        setContentView(R.layout.activity_custom_input)

        // Apply system bar padding to the actual root layout
        val mainLayout = findViewById<android.view.View>(R.id.inputMainLayout)

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
        val toolbar = findViewById<MaterialToolbar>(R.id.inputToolbar)

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

        // Message input
        val messageInput = findViewById<EditText>(R.id.messageInput)
        val sendButton = findViewById<Button>(R.id.sendButton)

        sendButton.setOnClickListener {

            val message = messageInput.text.toString()

            if (message.isNotEmpty()) {

                val intent = Intent(
                    this,
                    CustomReceiverActivity::class.java
                )

                intent.putExtra("message", message)
                startActivity(intent)

            } else {
                messageInput.error = "Enter a message"
            }
        }
    }
}