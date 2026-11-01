package com.example.assignment2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment

class BroadcastFragment : Fragment(R.layout.fragment_broadcast) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(R.id.broadcastSpinner)
        val proceedButton = view.findViewById<Button>(R.id.proceedButton)

        val options = arrayOf(
            "Custom Broadcast Receiver",
            "System Battery Notification Receiver"
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            options
        )

        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spinner.adapter = adapter

        proceedButton.setOnClickListener {

            if (spinner.selectedItemPosition == 0) {

                val intent = Intent(
                    requireContext(),
                    CustomInputActivity::class.java
                )

                startActivity(intent)

            } else {

                val intent = Intent(
                    requireContext(),
                    BatteryActivity::class.java
                )

                startActivity(intent)
            }
        }
    }
}