package com.example.assignment2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.net.URL
import kotlin.concurrent.thread

class ImageScaleFragment : Fragment(R.layout.fragment_image_scale) {

    private var scaleFactor = 1.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageView = view.findViewById<ImageView>(R.id.zoomImageView)
        val progressBar = view.findViewById<ProgressBar>(R.id.imageProgressBar)

        // Detect pinch gestures
        val scaleDetector = ScaleGestureDetector(
            requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

                override fun onScale(detector: ScaleGestureDetector): Boolean {

                    scaleFactor *= detector.scaleFactor

                    // Minimum zoom = 1x, Maximum zoom = 5x
                    scaleFactor = scaleFactor.coerceIn(1.0f, 5.0f)

                    imageView.scaleX = scaleFactor
                    imageView.scaleY = scaleFactor

                    return true
                }
            }
        )

        // Handle touch gestures on the image
        imageView.setOnTouchListener { _, event ->

            scaleDetector.onTouchEvent(event)

            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                imageView.performClick()
            }

            true
        }

        // Image URL
        val imageUrl = "https://picsum.photos/600/400"

        // Download image on a background thread
        thread {

            var bitmap: Bitmap? = null

            try {
                val connection = URL(imageUrl).openConnection()
                connection.connectTimeout = 10000
                connection.readTimeout = 10000

                connection.getInputStream().use { input ->
                    bitmap = BitmapFactory.decodeStream(input)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Update UI on main thread
            activity?.runOnUiThread {

                // Avoid updating a fragment view that was destroyed
                if (viewLifecycleOwnerLiveData.value == null) {
                    return@runOnUiThread
                }

                progressBar.visibility = View.GONE

                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to load image. Check internet connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}