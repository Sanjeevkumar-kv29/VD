package com.example.vd

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.email
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       if (supportActionBar != null)
        setContentView(R.layout.activity_main)


        val Getsharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)
        val logstatus=Getsharepref.getString("login","")

        if (logstatus == "true") {
            Toast.makeText(this,"WELCOME BACK...",Toast.LENGTH_LONG).show()
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }

        signin.setOnClickListener {

            if (email.text.isNullOrEmpty() or pass.text.isNullOrEmpty()){

                email.setError("This should be filled")
                pass.setError("This should be filled")
            }
            else{

                 //start loading
                signin.startLoading()
                LoginApiFunCall(email.text.toString().trim(),pass.text.toString().trim(),this)
                }

        }

        signup.setOnClickListener {

            startActivity(Intent(this,SignUpActivity::class.java))

        }


    }




    fun LoginApiFunCall(email:String , password:String , context: Context){

        val url = "https://vk-backend.herokuapp.com/users/auth/login_user"
        val que = Volley.newRequestQueue(context)

        val jsonobj = JSONObject()
        jsonobj.put("email",email)
        jsonobj.put("password",password)
        Log.d("JSONOBJECT",jsonobj.toString())

        val req = JsonObjectRequest(Request.Method.POST,url,jsonobj, Response.Listener {response ->
            Log.d("success","REQUEST GET")
            Log.d("successFetchData",response.toString())

            val sharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)

            sharepref.edit().putString("user_id",response["_id"].toString()).apply()
            sharepref.edit().putString("accessToken",response["accessToken"].toString()).apply()
            sharepref.edit().putString("refreshToken",response["refreshToken"].toString()).apply()
            sharepref.edit().putString("accesTokenExpiry",response["accesTokenExpiry"].toString()).apply()
            sharepref.edit().putString("login", response["login"].toString()).apply()

            Toast.makeText(this,"WELCOME BACK...",Toast.LENGTH_LONG).show()

                signin.loadingSuccessful()
                startActivity(Intent(this,HomeActivity::class.java))
                finish()


        }, Response.ErrorListener {


            signin.loadingFailed()
            Toast.makeText(this,"Invalid credentials",Toast.LENGTH_LONG).show()

            Log.d("ERROR",it.toString())
            Log.d("ERROR","REQUEST FAILD")

        })

        que.add(req)


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
