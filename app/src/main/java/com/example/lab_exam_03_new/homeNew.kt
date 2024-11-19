package com.example.lab_exam_03_new

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.lab_exam_03_new.databinding.ActivityHomeNewBinding
import com.example.yourapp.CalenderNew

class homeNew : AppCompatActivity() {

    private lateinit var binding: ActivityHomeNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set status bar to transparent and ensure icons are visible on your background color
        setCustomStatusBar()

        binding = ActivityHomeNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(TodoNew())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(TodoNew())
                R.id.Calender -> replaceFragment(CalenderNew())
                R.id.Time -> replaceFragment(TimeNew())
                else -> {
                    // Do nothing
                }
            }
            true
        }
    }

    // Method to set the status bar to transparent and ensure system icons are visible
    private fun setCustomStatusBar() {
        // Set the status bar to transparent
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = getColor(R.color.backgroundColor)  // Use transparent or an appropriate color

        // Ensure system icons are visible
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = true  // Or false, depending on your background color
    }



    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.addToBackStack(null)  // Optional: Add to back stack if you want to handle back navigation
        fragmentTransaction.commit()
    }
}
