package com.example.vd

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import kotlinx.android.synthetic.main.activity_payment_page.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.json.JSONObject


class PaymentPage : AppCompatActivity(),PaymentResultWithDataListener {

    var UsersignupDatamap = hashMapOf<String, Any?>()
    private var pD: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_page)
        getSupportActionBar()?.hide()
        pD = ProgressDialog(this)
        pD?.setMessage("Please wait....")
        pD?.show()

        UsersignupDatamap = intent.getSerializableExtra("UserSignupData") as HashMap<String, Any?>



        CreatingOrder(this,UsersignupDatamap["email"].toString(),UsersignupDatamap["phone_no"].toString())

    }



    fun CreatingOrder(context: Context,email:String,contact:String){

        val que = Volley.newRequestQueue(context)
        val API=APIconfigure()
        var PaymentId = ""


        val req = JsonObjectRequest(Request.Method.POST,API.BASEURL+API.CREATEORDER,null,
            Response.Listener { response ->
                Log.d("success","REQUEST GET")
                Log.d("success",response.toString())
                Log.d("JSONOBJECT",response["payment_id"].toString())
                PaymentId = response["payment_id"].toString()

                startPayment(email,contact,PaymentId)


            },
            Response.ErrorListener {

                pD?.dismiss()
                if (it.networkResponse.statusCode.toString()=="503"){

                    Toast.makeText(this,"Server Error Please Try Again Later", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,"Something Went Wrong Please Try Again Later", Toast.LENGTH_SHORT).show()
                }
            })
        que.add(req)
        req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))

    }

    private fun startPayment(email:String,contact:String,PaymentId:String) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        pD?.dismiss()
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","Vasudhaiv Kutumbakam")
            options.put("description","Demoing Charges")
            options.put("currency","INR")
            options.put("order_id", PaymentId)
            val prefill = JSONObject()
            prefill.put("email",email)
            prefill.put("contact",contact)

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?,paymentData: PaymentData) {
        try{
            Toast.makeText(this,"Payment Successful $razorpayPaymentId",Toast.LENGTH_LONG).show()
            pD?.setMessage("Singing in...")
            pD?.show()

            UsersignupDatamap["order_id"]=paymentData.orderId
            UsersignupDatamap["razorpay_payment_id"]=paymentData.paymentId
            UsersignupDatamap["razorpay_signature"]=paymentData.signature

            signupApiFunCall(UsersignupDatamap,this)


        }catch (e: Exception){
            Log.e("TAG","Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentError(errorCode: Int, response: String?, p2: PaymentData?) {
        try{
            Toast.makeText(this,"Payment failed $errorCode \n $response",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Log.e("TAG","Exception in onPaymentSuccess", e)
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

        jsonobj.put("order_id",DATA["order_id"])
        jsonobj.put("razorpay_payment_id",DATA["razorpay_payment_id"])
        jsonobj.put("razorpay_signature",DATA["razorpay_signature"])
        jsonobj.put("giftName",DATA["giftName"])
        jsonobj.put("giftImage",DATA["giftImage"])
        jsonobj.put("giftDescription",DATA["giftDescription"])

        Log.d("SIGNUP-JSONOBJECT",jsonobj.toString())

        val req = JsonObjectRequest(Request.Method.POST,API.BASEURL+API.SIGNUP,jsonobj, Response.Listener { response ->

            pD?.dismiss()

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
            finish()
            //sharepref.edit().putString("login", response["login"].toString()).apply()

        },
        Response.ErrorListener {

            pD?.dismiss()
            val alert = AlertView("An Error Arrive", "Something went wrong Please Contact us If you amount is deducted", AlertStyle.DIALOG)
            alert.addAction(AlertAction("ok", AlertActionStyle.DEFAULT, { action -> }))
            alert.show(this)

            Log.d("ERROR",it.toString())
            Log.d("ERROR","REQUEST FAILD")

        })

        que.add(req)
        req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))


    }
}