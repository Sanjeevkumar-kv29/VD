package com.example.vd

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.vd.Apiconfig.APIconfigure
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONException

class fragment_profile : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        shimmer_view_container.startShimmerAnimation()

        val Getsharepref: SharedPreferences = this.activity!!.getSharedPreferences("LoginUserDetails",0)
        val id=Getsharepref.getString("user_id","")


        getProfile(id!!)

    }

    fun getProfile(Userid:String){

            val API = APIconfigure()
            val que = Volley.newRequestQueue(context)

            val req = JsonObjectRequest(Request.Method.GET,API.BASEURL+API.PROFILE+Userid,null,
                Response.Listener { response ->try {
                    Log.d("success","REQUEST GET")
                    Log.d("Profile fetch",response.toString())

                    shimmer_view_container.stopShimmerAnimation()
                    shimmer_view_container.visibility=View.GONE

                    val payment_status = response["payment_status"]
                    val wallet = response["wallet"]
                    val lifetimeEarnings = response["lifetimeEarnings"]
                    val contactno = response["phone_no"]
                    val profileurl = response["profile_picture"]
                    val fullname = response["full_name"]
                    val address = response["address"]
                    val email = response["email"]
                    val bankName = response["bankName"]
                    val accountNo = response["account_no"]
                    val Ifsccode = response["ifsc_code"]
                    val Accountholdername = response["account_holder_name"]


                    Glide.with(context).asBitmap().load(profileurl).into(profile_image)

                    Username.text = fullname.toString()
                    UUserid.text=Userid
                    UserAddress .text = address.toString()
                    UserContact.text = contactno.toString()
                    wallatbalance.text = wallet.toString()
                    lifetimeearning.text = lifetimeEarnings.toString()
                    UserEmail.text = email.toString()
                    pstatus.text = payment_status.toString()



            } catch (e: JSONException) {
                e.printStackTrace()
            }
            },
            Response.ErrorListener {

                Toast.makeText(context,"Check Your Connection Or try Again Later".toString(), Toast.LENGTH_LONG).show()
                Log.d("ERROR","REQUEST FAILD")
            })

            que.add(req)
            req.setRetryPolicy(DefaultRetryPolicy(12000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))

    }


    override fun onDestroy() {


            try {
                val fragmentManager: androidx.fragment.app.FragmentManager = activity!!.supportFragmentManager
                val ft: FragmentTransaction = fragmentManager.beginTransaction()
                ft.remove(this)
                ft.commit()
            } catch (e: Exception) {
            }
            super.onDestroy()


    }




}