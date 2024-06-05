package com.example.practicafct.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practicafct.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var resetButton: Button
    private lateinit var backButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        auth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.et_forgot_email)
        resetButton = findViewById(R.id.btn_forgot_remind_password)
        backButton = findViewById(R.id.btn_atras)

        resetButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(applicationContext, "Por favor ingresa tu dirección de correo electrónico", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Correo de restablecimiento enviado correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "No se pudo enviar el correo de restablecimiento", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
