package com.example.vd

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.vd.Apiconfig.APIconfigure
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class ForgetPassword : AppCompatActivity() {
    lateinit var userid :String
    private var pD: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        getSupportActionBar()?.hide()

        pD = ProgressDialog(this)

        SendOtp.setOnClickListener { if (frgtemail.text.toString().isNullOrEmpty()){frgtemail.setError("This should not be emply")} else{ Getotp(frgtemail.text.toString(),this) } }

        VerifyOtp.setOnClickListener { if (otpforverify.text.toString().isNullOrEmpty()){otpforverify.setError("This should not be emply")} else{ verifyOTP(otpforverify.text.toString(),this) } }

        submitPassBtn.setOnClickListener { if (newpass.text.toString().isNullOrEmpty() and cnewpass.text.toString().isNullOrEmpty()){newpass.setError("Some feilds are empty")}else if (newpass.text.toString() != cnewpass.text.toString()){Toast.makeText(this,"Password and confirm Password are not same", Toast.LENGTH_LONG).show()} else{ ResetPass(newpass.text.toString(),this) } }


    }



    fun Getotp(email:String, context: Context){

        pD?.setMessage("Checking Details...")
        pD?.show()

        val que = Volley.newRequestQueue(context)
        val apiP= APIconfigure()


        val jsonobj = JSONObject()
        jsonobj.put("email",email)
        Log.d("GETOTP-JSONOBJECT",jsonobj.toString())


        val req = JsonObjectRequest(Request.Method.POST,apiP.BASEURL+apiP.GETOTP,jsonobj, Response.Listener { response ->
            Log.d("GETOTPsuccess","REQUEST GET")
            Log.d("RESPONSEdata",response.toString())

            pD?.dismiss()

            Toast.makeText(this,response["message"].toString(), Toast.LENGTH_LONG).show()
            if (response["message"].toString()=="sent"){

                getOTPlyt.visibility = View.GONE
                verifyOTPlyt.visibility = View.VISIBLE
                userid = response["user_id"].toString()

            }



        }, Response.ErrorListener {

            pD?.dismiss()


            Log.d("GETOTPERROR",it.toString())
            Log.d("GETOTPERROR","REQUEST FAILD")
            Log.d("ErrorCode",it.networkResponse.statusCode.toString())

                Toast.makeText(this,"Server Error Please Try Again Later", Toast.LENGTH_SHORT).show()

        })

        que.add(req)
        req.setRetryPolicy(
            DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        )

    }

    fun verifyOTP(otp: String, context: Context) {

        pD?.setMessage("Checking Details...")
        pD?.show()

        val que = Volley.newRequestQueue(context)
        val apiP= APIconfigure()


        val jsonobj = JSONObject()
        jsonobj.put("userId",userid)
        jsonobj.put("otpval",otp)
        Log.d("GETOTP-JSONOBJECT",jsonobj.toString())


        val req = JsonObjectRequest(Request.Method.POST,apiP.BASEURL+apiP.VERIFYOTP,jsonobj, Response.Listener { response ->
            Log.d("VERIFYOTPsuccess","REQUEST GET")
            Log.d("RESPONSEdata",response.toString())

            pD?.dismiss()

            if (response["message"].toString()=="true"){

                ResetPasswordLyt.visibility = View.VISIBLE
                verifyOTPlyt.visibility = View.GONE

            }
            else{
                Toast.makeText(this,"Incorrect OTP", Toast.LENGTH_LONG).show()
            }



        }, Response.ErrorListener {

            pD?.dismiss()


            Log.d("GETOTPERROR",it.toString())
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

    fun ResetPass(newpassword: String, context: Context) {

        pD?.setMessage("Checking Details...")
        pD?.show()

        val que = Volley.newRequestQueue(context)
        val apiP= APIconfigure()


        val jsonobj = JSONObject()
        jsonobj.put("password",newpassword)
        jsonobj.put("userId",userid)
        Log.d("GETOTP-JSONOBJECT",jsonobj.toString())


        val req = JsonObjectRequest(Request.Method.POST,apiP.BASEURL+apiP.RESETPWD,jsonobj, Response.Listener { response ->
            Log.d("VERIFYOTPsuccess","REQUEST GET")
            Log.d("RESPONSEdata",response.toString())

            pD?.dismiss()

            Toast.makeText(this,"Password Change SuccessFully", Toast.LENGTH_LONG).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()



        }, Response.ErrorListener {

            pD?.dismiss()

            Log.d("GETOTPERROR",it.toString())
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