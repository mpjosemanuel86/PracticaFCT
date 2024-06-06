package com.example.practicafct.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.practicafct.R

class NavegacionActivity : AppCompatActivity() {

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navegacion)

        webView = findViewById(R.id.fragmentContainer)

        findViewById<Button>(R.id.btnAbrirNavegador).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iberdrola.es"))
            startActivity(intent)
        }

        findViewById<Button>(R.id.btnAbrirWebview).setOnClickListener {
            webView.apply {
                webViewClient = WebViewClient()
                loadUrl("https://www.iberdrola.es")
            }
        }
    }
}
