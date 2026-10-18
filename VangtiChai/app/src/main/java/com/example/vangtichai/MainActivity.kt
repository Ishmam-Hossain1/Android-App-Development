package com.example.vangtichai

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    // Store the amount entered by the user
    private var amount = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Handle system bars (status bar and navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Restore the amount when the screen rotates
        if (savedInstanceState != null) {
            amount = savedInstanceState.getLong("amount", 0L)
        }

        // Find all numeric buttons
        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2,
            R.id.btn3, R.id.btn4, R.id.btn5,
            R.id.btn6, R.id.btn7, R.id.btn8,
            R.id.btn9
        )

        // Set click listeners for buttons 0 to 9
        for (i in 0..9) {
            val button = findViewById<Button>(buttons[i])

            button.setOnClickListener {
                addDigit(i)
            }
        }

        // CLEAR button
        val clearButton = findViewById<Button>(R.id.btnClear)

        clearButton.setOnClickListener {
            amount = 0L
            updateDisplay()
        }

        // Update the interface when the app starts
        updateDisplay()
    }

    // Add a digit to the current amount
    private fun addDigit(digit: Int) {

        // Prevent the number from becoming too large
        if (amount > (Long.MAX_VALUE - digit) / 10) {
            return
        }

        amount = amount * 10 + digit

        updateDisplay()
    }

    // Update the amount and calculate the change
    private fun updateDisplay() {

        // Display the entered amount
        val takaDisplay = findViewById<TextView>(R.id.takaDisplay)
        takaDisplay.text = "Taka: $amount"

        // Available Taka denominations
        val notes = listOf(500, 100, 50, 20, 10, 5, 2, 1)

        // TextViews for displaying note counts
        val noteViews = listOf(
            R.id.note500,
            R.id.note100,
            R.id.note50,
            R.id.note20,
            R.id.note10,
            R.id.note5,
            R.id.note2,
            R.id.note1
        )

        var remainingAmount = amount

        // Calculate how many notes are needed
        for (i in notes.indices) {

            val note = notes[i]

            // Integer division gives the number of notes
            val count = remainingAmount / note

            // Find the amount still remaining
            remainingAmount = remainingAmount % note

            // Show the result
            val textView = findViewById<TextView>(noteViews[i])
            textView.text = "$note: $count"
        }
    }

    // Save the amount before the activity is recreated
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong("amount", amount)
        super.onSaveInstanceState(outState)
    }
}
