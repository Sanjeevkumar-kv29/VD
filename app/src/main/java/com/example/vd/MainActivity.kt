package com.example.vd

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.vd.Apiconfig.APIconfigure
import com.example.vd.Apiconfig.APIconfigure.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.email
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide()

        val Getsharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)
        val logstatus=Getsharepref.getString("login","")

        if (logstatus == "true") { Toast.makeText(this,"WELCOME BACK...",Toast.LENGTH_LONG).show()
            startActivity(Intent(this,HomeActivity::class.java))
            finish()}

        signin.setOnClickListener {if(email.text.isNullOrEmpty() or pass.text.isNullOrEmpty()){email.setError("This should be filled")
                                                                                                pass.setError("This should be filled")}
                                    else{signin.startLoading()
                                         LoginApiFunCall(email.text.toString().trim(),pass.text.toString().trim(),this)} }


        signup.setOnClickListener { startActivity(Intent(this,SignUpActivity::class.java)) }


        forgetpass.setOnClickListener { startActivity(Intent(this,ForgetPassword::class.java))  }

        contactus.setOnClickListener { startActivity(Intent(this,Contactus::class.java))  }

    }




    fun LoginApiFunCall(email:String , password:String , context: Context){

        val que = Volley.newRequestQueue(context)
        val apiP=APIconfigure()


        val jsonobj = JSONObject()
        jsonobj.put("email",email)
        jsonobj.put("password",password)
        Log.d("LOGIN-JSONOBJECT",jsonobj.toString())


        val req = JsonObjectRequest(Request.Method.POST,apiP.BASEURL+apiP.LOGINDIR,jsonobj, Response.Listener { response ->
            Log.d("LOGINsuccess","REQUEST GET")
            Log.d("LOGINdata",response.toString())

            val sharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)

            sharepref.edit().putString("user_id",response["_id"].toString()).apply()
            sharepref.edit().putString("accessToken",response["accessToken"].toString()).apply()
            sharepref.edit().putString("refreshToken",response["refreshToken"].toString()).apply()
            sharepref.edit().putString("accesTokenExpiry",response["accesTokenExpiry"].toString()).apply()
            sharepref.edit().putString("login", response["login"].toString()).apply()
            sharepref.edit().putString("refferal_code", response["self_refferal_code"].toString()).apply()

            Toast.makeText(this,"WELCOME BACK...",Toast.LENGTH_LONG).show()

                signin.loadingSuccessful()
                startActivity(Intent(this,HomeActivity::class.java))
                finish()


        }, Response.ErrorListener {

            signin.loadingFailed()

            Log.d("LOGINERROR",it.toString())
            Log.d("LOGINERROR","REQUEST FAILD")
            //Log.d("ErrorCode",it.networkResponse.statusCode.toString())

            if (it.networkResponse.statusCode.toString()=="503"){

                Toast.makeText(this,"Server Error Please Try Again Later",Toast.LENGTH_SHORT).show()
            }
            else{


            val responseBody = String(it.networkResponse.data, charset("utf-8"))
            val data = JSONObject(responseBody)
            Log.d("LoginErrorResponse",data.toString())
            val message = data.optString("err")
                Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
            Log.d("LoginErrormsg",message)

            }
        })

        que.add(req)
        req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))

    }




/*fun sharedPrefData(LoginAPIfetchDetails :HashMap<String, Any?>){

    val sharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)

    sharepref.edit().putString("Loginstatus",LoginAPIfetchDetails["IN"].toString()).apply()
    sharepref.edit().putString("user_id",LoginAPIfetchDetails["user_id"].toString()).apply()
    sharepref.edit().putString("accessToken",LoginAPIfetchDetails["accessToken"].toString()).apply()
    sharepref.edit().putString("refreshToken",LoginAPIfetchDetails["refreshToken"].toString()).apply()
    sharepref.edit().putString("accesTokenExpiry",LoginAPIfetchDetails["accesTokenExpiry"].toString()).apply()
    sharepref.edit().putString("login",LoginAPIfetchDetails["login"].toString()).apply()

    Toast.makeText(this,"WELCOME BACK...",Toast.LENGTH_LONG).show()


}*/


}
