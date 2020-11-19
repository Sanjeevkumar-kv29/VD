package com.example.vd

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat.recreate
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.vd.Apiconfig.APIconfigure
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_redeem.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import org.json.JSONObject

class Redeem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeem)
        getSupportActionBar()?.hide()

        val Getsharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)
        val id=Getsharepref.getString("user_id","")

        buttonredeem.setOnClickListener {

            if ((redeemamnt.text.isNullOrEmpty()) or (redeemamnt.text.toString()<1.toString()))
            {
                redeemamnt.setError("enter valid amount")
            }
            else{
                redeemApi(id.toString(),redeemamnt.text.toString())
            }
        }

        donewlt.setOnClickListener {

            finish()

        }
    }


    fun redeemApi(uid:String,wltamount:String){

        val API = APIconfigure()
        val que = Volley.newRequestQueue(this)

        val jsonobj = JSONObject()
        jsonobj.put("uid",uid)
        jsonobj.put("wallet_amount_input",wltamount)

        Log.d("JSONOBJECT",jsonobj.toString())

        val req = JsonObjectRequest(Request.Method.POST,API.BASEURL+API.REDEEM,jsonobj, Response.Listener { response ->
            Log.d("success","REQUEST GET")
            Log.d("successFetchData",response.toString())

            redeemamnt.visibility = View.GONE
            buttonredeem.visibility = View.GONE
            check.check()
            donewlt.visibility = View.VISIBLE



        }, Response.ErrorListener {

            Toast.makeText(this,"An error Occured or In-sufficient Wallat balance", Toast.LENGTH_LONG).show()
            Log.d("ERROR",it.toString())
            Log.d("ERROR","REQUEST FAILD")

        })

        que.add(req)
        req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))


    }
}