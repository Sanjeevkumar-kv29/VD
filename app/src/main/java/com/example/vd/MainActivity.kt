package com.example.vd

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       if (supportActionBar != null)

        setContentView(R.layout.activity_main)

        signin.setOnClickListener {
           signin.startLoading() //start loading
           signin.loadingSuccessful();

            startActivity(Intent(this,HomeActivity::class.java))
            finish()

        }


    }



    }
