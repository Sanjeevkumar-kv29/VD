package com.example.vd

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
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
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.email
import kotlinx.android.synthetic.main.activity_sign_up.pass
import kotlinx.android.synthetic.main.activity_sign_up.signin
import kotlinx.android.synthetic.main.activity_sign_up.signup
import org.json.JSONObject

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

        MakePayment.setOnClickListener {

            if (Bankname.text.isNullOrEmpty()){
                Bankname.setError("Please Enter Bank Name.")
            }
            else if (AccountNo.text.isNullOrEmpty()){
                AccountNo.setError("Please Enter Account No.")
            }
            else if (ReAccountNo.text.isNullOrEmpty()){
                ReAccountNo.setError("Please Re-Enter Account No")
            }
            else if (AccountNo.text.toString()!= ReAccountNo.text.toString()){
               AccountNo.setError("Account No And Re-Enter account no. Not matched")
               ReAccountNo.setError("Account No And Re-Enter account no. Not matched")
            }
            else if (IFSCcode.text.isNullOrEmpty()){
                IFSCcode.setError("Please enter IFSC Code")
            }
            else if (AccountHolderName.text.isNullOrEmpty()){
                AccountHolderName.setError("Please enter Account Holder's Name")
            }
            else{

                val alert = AlertView("Conformation ", "Bank name - ${Bankname.text} \n Account No. - ${ AccountNo.text} \n IFSC Code - ${IFSCcode.text} \n Account Holder - ${AccountHolderName.text} ", AlertStyle.DIALOG)
                alert.addAction(AlertAction("Edit", AlertActionStyle.DEFAULT, { action -> }))
                alert.addAction(AlertAction("Proceed", AlertActionStyle.POSITIVE, { action ->

                    UsersignupDatamap["bankName"] = Bankname.text
                    UsersignupDatamap["account_no"] = AccountNo.text
                    UsersignupDatamap["account_holder_name"] = AccountHolderName.text
                    UsersignupDatamap["ifsc_code"] = IFSCcode.text

                    Log.d("UserSignUpFullData",UsersignupDatamap.toString())

                    val intent = Intent(this,PaymentPage::class.java)
                    intent.putExtra("UserSignupData",UsersignupDatamap)
                    startActivity(intent)

                     }))
                alert.show(this)


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
                if(response["response"].equals("Refferal is valid")){UserdetailLyt.visibility = View.GONE
                                                                      signin.visibility = View.GONE
                                                                      BankdetailLyt.visibility = View.VISIBLE}

            },
            Response.ErrorListener {
                pD?.dismiss()
                val alert = AlertView("Wrong Referral ID", "Please Check Your Referral ID", AlertStyle.DIALOG)
                alert.addAction(AlertAction("ok", AlertActionStyle.DEFAULT, { action -> }))
                referral.setError("Enter Valid referral ID")
                alert.show(this)
                Log.d("REFFERALERROR",it.toString())
                Log.d("REFFERALERROR","REQUEST FAILD")
            })
            que.add(req)
            req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))

    }



}