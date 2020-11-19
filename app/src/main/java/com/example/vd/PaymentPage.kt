package com.example.vd

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.vd.Apiconfig.APIconfigure
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import kotlinx.android.synthetic.main.activity_payment_page.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject


class PaymentPage : AppCompatActivity() {

    var UsersignupDatamap = hashMapOf<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_page)
        getSupportActionBar()?.hide()

        UsersignupDatamap = intent.getSerializableExtra("UserSignupData") as HashMap<String, Any?>
        Yes.setOnClickListener {
            Log.d("intent data ",UsersignupDatamap.toString())
            signupApiFunCall(UsersignupDatamap,this)
        }
    }


    fun signupApiFunCall(DATA:HashMap<String, Any?>, context: Context){

        val API = APIconfigure()
        val que = Volley.newRequestQueue(context)

        val jsonobj = JSONObject()
        jsonobj.put("full_name",DATA["full_name"])
        jsonobj.put("gender",DATA["gender"])
        jsonobj.put("phone_no",DATA["phone_no"])
        jsonobj.put("email",DATA["email"])
        jsonobj.put("address",DATA["address"])
        jsonobj.put("password",DATA["password"])
        jsonobj.put("parentID",DATA["parentID"])
        jsonobj.put("bankName",DATA["bankName"])
        jsonobj.put("account_no",DATA["account_no"])
        jsonobj.put("account_holder_name",DATA["account_holder_name"])
        jsonobj.put("ifsc_code",DATA["ifsc_code"])

        Log.d("SIGNUP-JSONOBJECT",jsonobj.toString())

        val req = JsonObjectRequest(Request.Method.POST,API.BASEURL+API.SIGNUP,jsonobj, Response.Listener { response ->
            Log.d("success","REQUEST GET")
            Log.d("success",response.toString())
            Log.d("JSONOBJECT",response.toString())

            val sharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)

            sharepref.edit().putString("user_id",response["_id"].toString()).apply()
            sharepref.edit().putString("accessToken",response["accessToken"].toString()).apply()
            sharepref.edit().putString("refreshToken",response["refreshToken"].toString()).apply()
            sharepref.edit().putString("accesTokenExpiry",response["accesTokenExpiry"].toString()).apply()
            sharepref.edit().putString("login",response["login"].toString()).apply()
            sharepref.edit().putString("refferal_code", response["self_refferal_code"].toString()).apply()

            startActivity(Intent(this,HomeActivity::class.java))
            //sharepref.edit().putString("login", response["login"].toString()).apply()

        },
        Response.ErrorListener {

            val alert = AlertView("An Error Arrive", "Please Check Your Internet", AlertStyle.DIALOG)
            alert.addAction(AlertAction("ok", AlertActionStyle.DEFAULT, { action -> }))
            alert.show(this)

            Log.d("ERROR",it.toString())
            Log.d("ERROR","REQUEST FAILD")

        })

        que.add(req)
        req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))


    }
}