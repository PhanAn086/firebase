package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var email : EditText = findViewById(R.id.email)
        var password : EditText = findViewById(R.id.password)

        var login : Button = findViewById(R.id.login)
        login.setOnClickListener {
            var auth : FirebaseAuth = FirebaseAuth.getInstance()
            auth.signInWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        var intent  = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "dang nhap that bai", Toast.LENGTH_SHORT).show()
                    }

                }

        }


        var back : Button = findViewById(R.id.back)

        back.setOnClickListener {
            var intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}