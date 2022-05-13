package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private var mAdapter : AdapterUser? =AdapterUser(this)
    private var mUser : MutableList<InfoUser>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var auth : FirebaseAuth = FirebaseAuth.getInstance()
        var userRef =  auth.currentUser

        title = userRef?.displayName.toString()

        getAllPerson()

        var logout : Button = findViewById(R.id.logout)
        logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            var intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
    }

    fun getAllPerson () {

        var userRef = FirebaseDatabase.getInstance().reference.child("users")
        var recyclerView : RecyclerView = findViewById(R.id.listUser)

        mUser = ArrayList<InfoUser>()
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    val fullName = data.child("fullname").getValue().toString()
                    val email = data.child("email").getValue().toString()
                    val password = data.child("password").getValue().toString()
                    val sdt = data.child("sdt").getValue().toString()
                    val diachi = data.child("diachi").getValue().toString()
                    val pq = data.child("pq").getValue().toString()

                   var user =  InfoUser(id = data.key,fullname = fullName,email = email,password =password,sdt = sdt,diachi = diachi,pq = pq.toInt() )

                   mUser?.add(user)
                }
                Log.e("user ",mUser.toString())
                mAdapter?.getList(mUser as ArrayList<InfoUser>)

                mAdapter?.notifyDataSetChanged()

            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = mAdapter

    }




}