package com.example.vd

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.vd.Apiconfig.APIconfigure
import kotlinx.android.synthetic.main.activity_contactus.*
import org.json.JSONObject

class Contactus : AppCompatActivity() {
    private var pD: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contactus)
        getSupportActionBar()?.hide()

        pD = ProgressDialog(this)

        contactussubmit.setOnClickListener {

            if (contactname.text.toString().isNullOrEmpty()){
                contactname.setError("Enter name")

            }
            else if (contactusemail.text.toString().isNullOrEmpty()){
                contactusemail.setError("Enter email id")

            }
            else if (contactcontact.text.toString().isNullOrEmpty()){
                contactcontact.setError("Enter contact no")

            }else if (message.text.toString().isNullOrEmpty()){
                message.setError("Enter your message")

            }
           else{

                ContactusAPI(this)
            }


        }


    }


    fun ContactusAPI(context: Context) {

        pD?.setMessage("Sending Message")
        pD?.show()

        val que = Volley.newRequestQueue(context)
        val apiP= APIconfigure()


        val jsonobj = JSONObject()
        jsonobj.put("name",contactname.text)
        jsonobj.put("email",contactusemail.text)
        jsonobj.put("phoneNumber",contactcontact.text)
        jsonobj.put("message",message.text)
        Log.d("ContactUs-JSONOBJECT",jsonobj.toString())


        val req = JsonObjectRequest(Request.Method.POST,apiP.BASEURL+apiP.CONTACTUS,jsonobj, Response.Listener { response ->
            Log.d("MessageSent","REQUEST GET")
            Log.d("RESPONSEdata",response.toString())

            pD?.dismiss()

            Toast.makeText(this,"Message sent successfully", Toast.LENGTH_LONG).show()


        }, Response.ErrorListener {

            pD?.dismiss()

            Log.d("error",it.toString())
            Log.d("GETOTPERROR","REQUEST FAILD")
            Log.d("ErrorCode",it.networkResponse.statusCode.toString())

            Toast.makeText(this,"An Error Occur Please Try Again Later", Toast.LENGTH_SHORT).show()

        })

        que.add(req)
        req.setRetryPolicy(
            DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )
    }
}