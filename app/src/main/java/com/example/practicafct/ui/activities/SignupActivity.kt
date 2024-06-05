package com.example.practicafct.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.practicafct.R
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var repeatPasswordEditText: EditText
    private lateinit var registerButton: Button
    private var isPasswordVisible = false
    private lateinit var mAuth: FirebaseAuth

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Referencias a los views
        emailEditText = findViewById(R.id.et_signup_email)
        passwordEditText = findViewById(R.id.et_signup_password)
        repeatPasswordEditText = findViewById(R.id.et_signup_password_repeat)
        registerButton = findViewById(R.id.btn_signup_register)

        // Listener para el icono del ojo
        passwordEditText.setOnTouchListener { _, event ->
            val drawableEnd = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (passwordEditText.right - passwordEditText.compoundDrawables[drawableEnd].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Listener para el botón de registro
        registerButton.setOnClickListener {
            registerUser()
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0)
        } else {
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_on, 0)
        }
        isPasswordVisible = !isPasswordVisible
        passwordEditText.setSelection(passwordEditText.text.length)
    }

    private fun registerUser() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val repeatPassword = repeatPasswordEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != repeatPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
                    // TODO: Navigate to another activity or update UI
                } else {
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
