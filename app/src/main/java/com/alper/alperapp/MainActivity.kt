package com.alper.alperapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mRegBtn = findViewById<Button>(R.id.main_reg_btn);
        mRegBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity :: class.java);
            startActivity(intent);
        }

        val mLoginBtn = findViewById<Button>(R.id.main_login_btn);
        mLoginBtn.setOnClickListener {
            val intent = Intent(this, StartActivity :: class.java);
            startActivity(intent);
        }

    }
}
