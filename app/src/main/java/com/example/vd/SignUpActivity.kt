package com.example.vd

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        signup.setOnClickListener {

            if (name.text.isNullOrEmpty()){
                name.setError("Please enter name")
            }
            else if (email.text.isNullOrEmpty()){
                email.setError("Please enter Email")
            }
            else if (pass.text.isNullOrEmpty()){
                email.setError("Please enter Password")
            }
            else if (referral.text.isNullOrEmpty()){
                referral.setError("Please enter Referral")
            }
            else if (contact.text.isNullOrEmpty()){
                contact.setError("Please enter Contact")
            }
            else if (address.text.isNullOrEmpty()){
                address.setError("Please enter Address")
            }
            else if (pincode.text.isNullOrEmpty()){
                pincode.setError("Please enter Pincode")
            }
            else{

                signupApiFunCall(referral.text.toString(),this)

            }





        }

        signin.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }


    fun signupApiFunCall(Parentid:String, context: Context){


        val url = "https://vk-backend.herokuapp.com/users/verifyReferralID"

        val que = Volley.newRequestQueue(context)

        val jsonobj = JSONObject()
        jsonobj.put("parentID",Parentid)

        Log.d("JSONOBJECT",jsonobj.toString())

        val req = JsonObjectRequest(Request.Method.POST,url,jsonobj, Response.Listener { response ->
            Log.d("success","REQUEST GET")
            Log.d("success",response.toString())
            Log.d("JSONOBJECT",response["response"].toString())

        }, Response.ErrorListener {

            Log.d("ERROR",it.toString())
            Log.d("ERROR","REQUEST FAILD")

        })

        que.add(req)

    }



}