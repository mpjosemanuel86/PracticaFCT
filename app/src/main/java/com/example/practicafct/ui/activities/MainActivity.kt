package com.example.practicafct.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.practicafct.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class MainActivity : AppCompatActivity() {

    private lateinit var button1: ImageButton
    private lateinit var button2: ImageButton
    private lateinit var button3: ImageButton
    private lateinit var ibLogout: ImageButton

    private lateinit var auth: FirebaseAuth
    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        // Inicializa Firebase Remote Config
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(1)
            .build()
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        // Establece valores predeterminados
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        // Obtén los valores de configuración remota
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Valores de configuración remota activados
                    applyRemoteConfig()
                } else {
                    // Manejar el error
                }
            }

        initComponent()
        initListeners()
    }

    private fun applyRemoteConfig() {
        val showFacturaOption = mFirebaseRemoteConfig.getBoolean("show_invoice_menu")
        val useAlternateTheme = mFirebaseRemoteConfig.getBoolean("use_alternate_style")

        // Mostrar u ocultar la opción de factura
        button1.isVisible = showFacturaOption

        // Aplicar el tema alternativo si es necesario
        if (useAlternateTheme) {
            setTheme(R.style.DefaultTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

    }

    private fun initComponent() {
        button1 = findViewById(R.id.button1)
        button2 = findViewById(R.id.button2)
        button3 = findViewById(R.id.button3)
        ibLogout = findViewById(R.id.ibLogout)
    }

    private fun initListeners() {
        button1.setOnClickListener { navigateToPractica1() }
        button2.setOnClickListener { navigateToPractica2() }
        button3.setOnClickListener { navigateToPractica3() }
        ibLogout.setOnClickListener { logout() }
    }

    private fun navigateToPractica1() {
        intent = Intent(this, FacturaActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToPractica2() {
        intent = Intent(this, SmartSolarActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToPractica3() {
        intent = Intent(this, NavegacionActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        auth.signOut()

        // Eliminar las credenciales almacenadas
        val sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.apply()

        // Redirigir a LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
