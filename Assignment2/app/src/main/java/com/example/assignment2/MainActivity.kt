package com.example.assignment2

import android.os.Bundle
import android.graphics.Color
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Fix status bar overlap for main content
        val mainContent = findViewById<android.view.View>(R.id.mainContent)

        ViewCompat.setOnApplyWindowInsetsListener(mainContent) { view, insets ->

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

        // Connect the toolbar and navigation drawer
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val drawerLayout = findViewById<DrawerLayout>(R.id.main)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        setSupportActionBar(toolbar)

        // Create the hamburger menu button
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Make hamburger icon match App title color
        toggle.drawerArrowDrawable.color = Color.parseColor("#333333")

        // Handle navigation menu selections
        navigationView.setNavigationItemSelectedListener { item ->

            val selectedFragment = when (item.itemId) {
                R.id.nav_broadcast -> BroadcastFragment()
                R.id.nav_image_scale -> ImageScaleFragment()
                R.id.nav_video -> VideoFragment()
                R.id.nav_audio -> AudioFragment()
                else -> null
            }

            if (selectedFragment != null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            }

            drawerLayout.closeDrawers()
            true
        }

        // Show Broadcast screen when the app first opens
        if (savedInstanceState == null) {

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BroadcastFragment())
                .commit()

            navigationView.setCheckedItem(R.id.nav_broadcast)
        }
    }
}