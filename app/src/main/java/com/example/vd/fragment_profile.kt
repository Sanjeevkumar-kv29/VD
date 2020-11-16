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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONException

class fragment_profile : Fragment() {

    private var pD: ProgressDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val Getsharepref: SharedPreferences = this.activity!!.getSharedPreferences("LoginUserDetails",0)
        val id=Getsharepref.getString("user_id","")

        pD = ProgressDialog(context)
        pD?.setMessage("Please Wait...")
        pD?.show()
        getProfile(id!!)

    }

    fun getProfile(Userid:String){

            val url = "https://vk-backend.herokuapp.com/users/c_profile?uid=${Userid}"
            val que = Volley.newRequestQueue(context)

            val req = JsonObjectRequest(Request.Method.GET,url,null, Response.Listener { response ->try {
                Log.d("success","REQUEST GET")
                Log.d("Profile fetch",response.toString())

                pD?.dismiss()
                val payment_status = response["payment_status"]
                val wallet = response["wallet"]
                val lifetimeEarnings = response["lifetimeEarnings"]
                val contactno = response["phone_no"]
                val profileurl = response["profile_picture"]
                val fullname = response["full_name"]
                val address = response["address"]
                val email = response["email"]

                Glide.with(context).asBitmap().load(profileurl).into(profile_image)

                Username.text = fullname.toString()
                UUserid.text=Userid
                UserAddress .text = address.toString()
                UserContact.text = contactno.toString()
                wallatbalance.text = wallet.toString()
                lifetimeearning.text = lifetimeEarnings.toString()
                UserEmail.text = email.toString()



            } catch (e: JSONException) {
                e.printStackTrace()
            }
            },
            Response.ErrorListener {
                pD?.dismiss()
                Toast.makeText(context,"Check Your Connection Or try Again Later".toString(), Toast.LENGTH_LONG).show()
                Log.d("ERROR","REQUEST FAILD")
            })

            que.add(req)

    }





}