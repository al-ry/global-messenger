package com.example.global_messenger_reworked.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.global_messenger_reworked.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavView : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavView, navController)
    }

    fun hideBottomNavigation() {
        bottomNavView.visibility = View.GONE
    }

    fun showBottomNavigation()
    {
        bottomNavView.visibility = View.VISIBLE
    }
}


