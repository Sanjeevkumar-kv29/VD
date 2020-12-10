package com.example.vd

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vd.Apiconfig.APIconfigure
import kotlinx.android.synthetic.main.activity_redeem.*
import org.json.JSONObject

class Redeem : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeem)
        getSupportActionBar()?.hide()

        val Getsharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)
        val id=Getsharepref.getString("user_id","")
        val token=Getsharepref.getString("accessToken","")

        buttonredeem.setOnClickListener {

            if (redeemamnt.text.isNullOrEmpty()) {
                    redeemamnt.setError("Enter amount")
                }
            else if(redeemamnt.text.toString()<100.toString())
            {
                redeemamnt.setError("Amount should be more then 100Rs")
            }
            else{
                try {
                    val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {

                }
                redeemprogressBar.visibility = View.VISIBLE
                redeemApi(id.toString(),redeemamnt.text.toString(),token.toString())
            }
        }

    }


    fun redeemApi(uid:String,wltamount:String,token:String){

        val API = APIconfigure()
        val que = Volley.newRequestQueue(this)

        val jsonobj = JSONObject()
        jsonobj.put("uid",uid)
        jsonobj.put("wallet_amount_input",wltamount)

        Log.d("JSONOBJECT",jsonobj.toString())


        val req: JsonObjectRequest = object:JsonObjectRequest(Request.Method.POST,API.BASEURL+API.REDEEM,jsonobj, Response.Listener { response ->
            Log.d("success","REQUEST GET")
            Log.d("successFetchData",response.toString())

            redeemamnt.visibility = View.GONE
            buttonredeem.visibility = View.GONE
            redeemprogressBar.visibility=View.GONE
            tvmsgwallet.visibility=View.GONE

            check.check()

            Handler().postDelayed(
                {
                    val intent = Intent(this,afterpaymentPoPup::class.java)
                    startActivity(intent)
                    finish()

                },1000 )



        }, Response.ErrorListener {

            redeemamnt.visibility = View.VISIBLE
            buttonredeem.visibility = View.VISIBLE
            redeemprogressBar.visibility=View.GONE
            tvmsgwallet.visibility=View.GONE

            if (it.networkResponse.statusCode==404){

                redeemamnt.setError("Enter a valid amount")
                Toast.makeText(this,"In-sufficient Wallat balance", Toast.LENGTH_LONG).show()
                Log.d("ERRORCODE",it.networkResponse.statusCode.toString())
            }
            else{

            Log.d("ERROR",it.networkResponse.statusCode.toString())
                if ( it.networkResponse.statusCode == 401){

                    val sharepref: SharedPreferences = getSharedPreferences("LoginUserDetails",0)
                    val editor: SharedPreferences.Editor = sharepref.edit()
                    editor.clear()
                    editor.commit()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()


                }
            Toast.makeText(this,"An error Occured or In-sufficient Wallat balance", Toast.LENGTH_LONG).show()
            Log.d("ERROR",it.toString())
            Log.d("ERROR","REQUEST FAILD")
            }

        })


            {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["x-auth-token"] = token
                    return headers
                }
            }

        que.add(req)
        req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))


    }
}