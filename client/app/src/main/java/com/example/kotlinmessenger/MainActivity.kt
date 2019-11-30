package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val signInButton: Button = findViewById(R.id.sign_in_button)
        //log_in_link.setOnClickListener{
        //    val intent = Intent(this, LogInActivity::class.java)
        //    startActivity(intent)
        //}



        signInButton.setOnClickListener{ showUserData()}


    }

    private fun showUserData() {
        Toast.makeText(this,"You are in", Toast.LENGTH_SHORT).show()
    }

    private fun getUserInfo(): List<String> {
        val password = password_field.text.toString()
        val userName = userName_field.text.toString()
        return listOf(userName, password)
    }
}
