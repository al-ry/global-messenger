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

        log_in_link.setOnClickListener{
            val intent = Intent(this, SignInActivity::class.java)
          startActivity(intent)
        }

        signInButton.setOnClickListener{ showUserData() }
    }

    private fun showUserData() {
        val info = getUserInfo()
        var result = String()

        if (info[0].isEmpty() || info[1].isEmpty() || info[2].isEmpty())
        {
            result = "You should fill all the fields"
        }
        else
        {
            result = info[0] + "/n" + info[1] + "/n" + info[2]
        }

        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    private fun getUserInfo(): List<String> {
        val password = password_field.text.toString()
        val userName = userName_field.text.toString()
        val phoneNumber = phone_number_field.text.toString()

        return listOf(userName, phoneNumber, password)
    }
}
