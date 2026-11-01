package com.example.assignment2

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import java.util.Locale

class VideoFragment : Fragment(R.layout.fragment_video) {

    private var videoView: VideoView? = null

    private val handler = Handler(Looper.getMainLooper())

    private lateinit var seekBar: SeekBar
    private lateinit var videoTime: TextView

    // Update SeekBar and timer every 500 milliseconds
    private val updateSeekBar = object : Runnable {

        override fun run() {

            videoView?.let { video ->

                val currentPosition = video.currentPosition
                val duration = video.duration.coerceAtLeast(0)

                seekBar.progress = currentPosition
                seekBar.max = duration

                val currentSeconds = currentPosition / 1000
                val totalSeconds = duration / 1000

                val currentTime = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    currentSeconds / 60,
                    currentSeconds % 60
                )

                val totalTime = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    totalSeconds / 60,
                    totalSeconds % 60
                )

                videoTime.text = "$currentTime / $totalTime"

                handler.postDelayed(this, 500)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoView = view.findViewById(R.id.videoView)

        seekBar = view.findViewById(R.id.videoSeekBar)
        videoTime = view.findViewById(R.id.videoTime)

        val playButton = view.findViewById<Button>(R.id.videoPlayButton)
        val pauseButton = view.findViewById<Button>(R.id.videoPauseButton)

        // Load existing video from res/raw
        val videoUri = Uri.parse(
            "android.resource://${requireContext().packageName}/${R.raw.sample_video}"
        )

        videoView?.setVideoURI(videoUri)

        // Wait until video is ready
        videoView?.setOnPreparedListener { mediaPlayer ->

            seekBar.max = mediaPlayer.duration

            updateVideoTime(0, mediaPlayer.duration)

            // Start updating the SeekBar
            handler.removeCallbacks(updateSeekBar)
            handler.post(updateSeekBar)
        }

        // Play button
        playButton.setOnClickListener {

            videoView?.start()

            handler.removeCallbacks(updateSeekBar)
            handler.post(updateSeekBar)
        }

        // Pause button
        pauseButton.setOnClickListener {

            videoView?.pause()
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
                        videoView?.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        // When video finishes
        videoView?.setOnCompletionListener {

            videoView?.seekTo(0)
            seekBar.progress = 0

            val duration = videoView?.duration ?: 0
            updateVideoTime(0, duration)
        }
    }

    // Format and display the video timer
    private fun updateVideoTime(currentPosition: Int, duration: Int) {

        val currentSeconds = currentPosition / 1000
        val totalSeconds = duration / 1000

        val currentTime = String.format(
            Locale.getDefault(),
            "%02d:%02d",
            currentSeconds / 60,
            currentSeconds % 60
        )

        val totalTime = String.format(
            Locale.getDefault(),
            "%02d:%02d",
            totalSeconds / 60,
            totalSeconds % 60
        )

        videoTime.text = "$currentTime / $totalTime"
    }

    override fun onPause() {
        super.onPause()

        // Pause video when leaving the screen
        videoView?.pause()
    }

    override fun onDestroyView() {

        // Stop SeekBar updates
        handler.removeCallbacks(updateSeekBar)

        // Release video resources
        videoView?.stopPlayback()
        videoView = null

        super.onDestroyView()
    }
}