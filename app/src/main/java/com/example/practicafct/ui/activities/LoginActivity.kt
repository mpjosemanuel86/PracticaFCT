package com.example.practicafct.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MotionEvent
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practicafct.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var checkBoxRemember: CheckBox
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            navigateToMainActivity()
            return
        }

        emailEditText = findViewById(R.id.et_correo_electrónico)
        passwordEditText = findViewById(R.id.et_contraseña)
        loginButton = findViewById(R.id.btIniciarSesión)
        signUpButton = findViewById(R.id.btnRegistrar)
        forgotPasswordTextView = findViewById(R.id.tvOlvidoContraseña)
        checkBoxRemember = findViewById(R.id.cbRecordarInicioSesión)

        checkIfRemembered()

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            if (checkBoxRemember.isChecked) {
                                rememberUser(email, password)
                            } else {
                                clearRememberedUser()
                            }
                            navigateToMainActivity()
                        } else {
                            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        passwordEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (passwordEditText.right - passwordEditText.compoundDrawables[2].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun rememberUser(email: String, password: String) {
        val sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("email", email)
        editor.putString("password", password)
        editor.apply()
    }

    private fun clearRememberedUser() {
        val sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.apply()
    }

    private fun checkIfRemembered() {
        val sharedPreferences = getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("email", null)
        val password = sharedPreferences.getString("password", null)

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            emailEditText.setText(email)
            passwordEditText.setText(password)
        } else {
            emailEditText.setText("")
            passwordEditText.setText("")
        }
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_off, 0)
        } else {
            passwordEditText.inputType =
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_eye_on, 0)
        }
        passwordEditText.setSelection(passwordEditText.text.length)
        isPasswordVisible = !isPasswordVisible
    }
}
