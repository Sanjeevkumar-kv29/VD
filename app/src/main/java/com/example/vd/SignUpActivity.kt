package com.example.vd

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.vd.Adapter.giftRVAdapter
import com.example.vd.Apiconfig.APIconfigure
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.email
import kotlinx.android.synthetic.main.activity_sign_up.pass
import kotlinx.android.synthetic.main.activity_sign_up.signin
import kotlinx.android.synthetic.main.activity_sign_up.signup
import kotlinx.android.synthetic.main.fragment_tree.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class SignUpActivity : AppCompatActivity() {


    private var pD: ProgressDialog? = null
    var gender =""
    val UsersignupDatamap = hashMapOf<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        getSupportActionBar()?.hide()
        pD = ProgressDialog(this)

        signup.setOnClickListener {

            if (name.text.isNullOrEmpty()){
                name.setError("Please enter name")
            }
            else if (genderRB.checkedRadioButtonId==-1){
                Toast.makeText(this,"Select Gender", Toast.LENGTH_LONG).show()
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

                if (male.isChecked)
                    gender = "Male"
                else if (female.isChecked)
                    gender = "Female"
                else if (other.isChecked)
                    gender = "Other"

                pD?.setMessage("Checking Details...")
                pD?.show()

                signupApiFunCall(referral.text.toString(),this)
            }

       }



        signin.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }


    fun signupApiFunCall(Parentid:String, context: Context){

        val que = Volley.newRequestQueue(context)
        val API=APIconfigure()
        val jsonobj = JSONObject()
        jsonobj.put("parentID",Parentid)
        jsonobj.put("email",email.text)
        jsonobj.put("phone_no",contact.text)
        Log.d("SIGNUP-JSONOBJECT",jsonobj.toString())


        val req = JsonObjectRequest(Request.Method.POST,API.BASEURL+API.REFFERALCHECK,jsonobj,
            Response.Listener { response ->
                Log.d("success","REQUEST GET")
                Log.d("success",response.toString())
                Log.d("JSONOBJECT",response["response"].toString())

                UsersignupDatamap["full_name"] = name.text
                UsersignupDatamap["gender"] = gender
                UsersignupDatamap["phone_no"] = contact.text
                UsersignupDatamap["email"] = email.text
                UsersignupDatamap["address"] = address.text
                UsersignupDatamap["password"] = pass.text
                UsersignupDatamap["parentID"] = referral.text
                Log.d("UserSignUpData",UsersignupDatamap.toString())

                pD?.dismiss()

                val intent = Intent(this,signgetgift::class.java)
                intent.putExtra("UserSignupData",UsersignupDatamap)
                startActivity(intent)


            },
            Response.ErrorListener {
                pD?.dismiss()

                if (it.networkResponse.statusCode.toString()=="503"){

                    Toast.makeText(this,"Server Error Please Try Again Later",Toast.LENGTH_SHORT).show()
                }
                else{


                    val responseBody = String(it.networkResponse.data, charset("utf-8"))
                    val data = JSONObject(responseBody)
                    Log.d("RefferalErrorMsg",data.toString())
                    val message = data.optString("err")
                    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
                    Log.d("RefferalErrorMsg",message)

                }


            })
            que.add(req)
            req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))

    }




}