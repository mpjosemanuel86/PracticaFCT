package com.example.practicafct

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var button1: ImageButton
    private lateinit var button2: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initComponent()
        initListeners()


    }

    private fun initComponent() {
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
    }

    private fun initListeners() {
        button1.setOnClickListener { navigateToPractica1() }
        button2.setOnClickListener { navigateToPractica2() }
    }

    private fun navigateToPractica1() {
        intent = Intent(this, FacturasActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToPractica2() {
        intent = Intent(this, Practica_2::class.java)
        startActivity(intent)
    }
}