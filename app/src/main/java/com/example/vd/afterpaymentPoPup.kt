package com.example.vd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class afterpaymentPoPup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_afterpayment_po_pup)
    }

    override fun onBackPressed() {


        val intent = Intent(this,afterpaymentPoPup::class.java)
        intent.putExtra("walletstatus","true")
        startActivity(intent)
        finish()


    }
}