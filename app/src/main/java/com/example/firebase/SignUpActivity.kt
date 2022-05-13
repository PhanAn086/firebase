package com.example.firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var email : EditText = findViewById(R.id.emaildk)
        var password : EditText = findViewById(R.id.passworddk)
        var fullname : EditText = findViewById(R.id.fullname)
        var diachi : EditText = findViewById(R.id.address)
        var sdt : EditText = findViewById(R.id.sdt)

        var signUp : Button = findViewById(R.id.signUp)



        signUp.setOnClickListener {
            var auth : FirebaseAuth = FirebaseAuth.getInstance()
            auth.createUserWithEmailAndPassword(email.text.toString(),password.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        saveUser(email,password,fullname,diachi,sdt)
                        var intent = Intent(this,LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this, "that bai", Toast.LENGTH_SHORT).show()
                    }
                }


        }

        var back : Button = findViewById(R.id.back)

        back.setOnClickListener {
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun saveUser(email: EditText, password: EditText, fullname: EditText, diachi: EditText, sdt: EditText) {
        val user = FirebaseAuth.getInstance().currentUser

        var currentUserID = user!!.uid
        var userRef : DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")


        var userMap = HashMap<String,Any>()
        userMap["email"] = email.text.toString()
        userMap["password"] = password.text.toString()
        userMap["fullname"] = fullname.text.toString()
        userMap["diachi"] = diachi.text.toString()
        userMap["sdt"] = sdt.text.toString()
        userMap["pq"] = 1
        val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(fullname.text.toString()).build()

        user!!.updateProfile(profileUpdates)

        userRef.child(currentUserID).setValue(userMap)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "thanh cong save - info", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "that bai save - info", Toast.LENGTH_SHORT).show()
                }
            }
    }
}