package com.example.vd

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import kotlinx.android.synthetic.main.activity_signup_bankdetail.*

class SignupBankdetail : AppCompatActivity() {


    var UsersignupDatamap = hashMapOf<String, Any?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_bankdetail)
        getSupportActionBar()?.hide()

        UsersignupDatamap = intent.getSerializableExtra("UserSignupData") as HashMap<String, Any?>


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
    }
}