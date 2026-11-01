package com.example.assignment2

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment

class AudioFragment : Fragment(R.layout.fragment_audio) {

    private var mediaPlayer: MediaPlayer? = null

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var seekBar: SeekBar
    private lateinit var audioTime: TextView

    private val updateSeekBar = object : Runnable {
        override fun run() {

            mediaPlayer?.let { player ->

                seekBar.progress = player.currentPosition

                val currentSeconds = player.currentPosition / 1000
                val totalSeconds = player.duration / 1000

                val currentTime = String.format(
                    "%02d:%02d",
                    currentSeconds / 60,
                    currentSeconds % 60
                )

                val totalTime = String.format(
                    "%02d:%02d",
                    totalSeconds / 60,
                    totalSeconds % 60
                )

                audioTime.text = "$currentTime / $totalTime"

                handler.postDelayed(this, 500)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton = view.findViewById<Button>(R.id.playButton)
        val pauseButton = view.findViewById<Button>(R.id.pauseButton)

        seekBar = view.findViewById(R.id.audioSeekBar)
        audioTime = view.findViewById(R.id.audioTime)

        // Load audio from raw folder
        mediaPlayer = MediaPlayer.create(
            requireContext(),
            R.raw.sample_audio
        )

        seekBar.max = mediaPlayer?.duration ?: 0

        // Play button
        playButton.setOnClickListener {

            mediaPlayer?.start()

            handler.removeCallbacks(updateSeekBar)
            handler.post(updateSeekBar)
        }

        // Pause button
        pauseButton.setOnClickListener {

            mediaPlayer?.pause()
        }

        // SeekBar control
        seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer?.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        // When audio finishes
        mediaPlayer?.setOnCompletionListener {

            seekBar.progress = 0
            mediaPlayer?.seekTo(0)
        }
    }

    override fun onPause() {
        super.onPause()

        // Pause audio when leaving the screen
        mediaPlayer?.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Stop updating SeekBar
        handler.removeCallbacks(updateSeekBar)

        // Release MediaPlayer resources
        mediaPlayer?.release()
        mediaPlayer = null
    }
}