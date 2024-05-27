package com.example.practicafct.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.example.practicafct.databinding.ActivitySmartSolarBinding
import com.example.practicafct.ui.model.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class SmartSolarActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySmartSolarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySmartSolarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.tableLayoutSmart
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Mi instalación"
                1 -> "Energía"
                else -> "Detalles"
            }
        }.attach()
    }
}

