package com.example.vd

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
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


        UsersignupDatamap = intent.getSerializableExtra("UserSignupData") as HashMap<String, Any?>

        Yes.setOnClickListener {


            Log.d("intent data ",UsersignupDatamap.toString())
            signupApiFunCall(UsersignupDatamap,this)

        }
    }


    fun signupApiFunCall(DATA:HashMap<String, Any?>, context: Context){


        val url = "https://vk-backend.herokuapp.com/users/auth/signup"

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

        Log.d("JSONOBJECT",jsonobj.toString())

        val req = JsonObjectRequest(Request.Method.POST,url,jsonobj, Response.Listener { response ->
            Log.d("success","REQUEST GET")
            Log.d("success",response.toString())
            Log.d("JSONOBJECT",response.toString())



            val sharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)

            sharepref.edit().putString("user_id",response["_id"].toString()).apply()
            sharepref.edit().putString("accessToken",response["accessToken"].toString()).apply()
            sharepref.edit().putString("refreshToken",response["refreshToken"].toString()).apply()
            sharepref.edit().putString("accesTokenExpiry",response["accesTokenExpiry"].toString()).apply()
            sharepref.edit().putString("login","true").apply()


            startActivity(Intent(this,HomeActivity::class.java))

            //sharepref.edit().putString("login", response["login"].toString()).apply()




        }, Response.ErrorListener {

            //signin.loadingFailed()

            //Toast.makeText(this,"Enter Valid Refferal", Toast.LENGTH_LONG).show()

            val alert = AlertView("An Error Arrive", "Please Check Your Internet", AlertStyle.DIALOG)
            alert.addAction(AlertAction("ok", AlertActionStyle.DEFAULT, { action -> }))
            alert.show(this)

            Log.d("ERROR",it.toString())
            Log.d("ERROR","REQUEST FAILD")

            ////////// only for response get------------
            //UserdetailLyt.visibility = View.GONE
            // BankdetailLyt.visibility = View.VISIBLE

        })

        que.add(req)

    }
}