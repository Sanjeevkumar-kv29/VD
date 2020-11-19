package com.example.vd

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.example.Adapter.transectionAdapter
import com.example.vd.Apiconfig.APIconfigure
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import org.json.JSONException
import java.util.ArrayList

class fragment_wallet : Fragment() {

    var amount= ArrayList<String>()
    var transectiondate= ArrayList<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_wallet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        shimmer_view_containerwlt.startShimmerAnimation()

        val Getsharepref: SharedPreferences = this.activity!!.getSharedPreferences("LoginUserDetails",0)
        val id=Getsharepref.getString("user_id","")

        amount.clear()
        transectiondate.clear()

        getProfile(id.toString())
        gettransetiondetails(id.toString())

        buttonwithdraw.setOnClickListener {
        startActivity(Intent(context,Redeem::class.java))
        }


    }


    fun getProfile(Userid:String){
        val API=APIconfigure()
        val que = Volley.newRequestQueue(context)

        val req = JsonObjectRequest(Request.Method.GET,API.BASEURL+API.PROFILE+Userid,null, Response.Listener { response ->try {
            Log.d("success","REQUEST GET")
            Log.d("Wallet Amount",response["wallet"].toString())

            val wallet = response["wallet"]
            mnyinwlt.text = wallet.toString()

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        },
            Response.ErrorListener {

                Toast.makeText(context,"Check Your Connection Or try Again Later".toString(), Toast.LENGTH_LONG).show()
                Log.d("ERROR","REQUEST FAILD")
            })

        que.add(req)
        req.setRetryPolicy(DefaultRetryPolicy(10000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))

    }



    fun gettransetiondetails(Userid:String){

        val API = APIconfigure()
        val que = Volley.newRequestQueue(context)

        val req = JsonArrayRequest(Request.Method.POST,API.BASEURL+API.TRANSECTION+Userid,null, Response.Listener { response ->try {

                for (i in 0..response.length()-1) {
                    val responseOBJ = response.getJSONObject(i)
                    transectiondate.add(responseOBJ.getString("redemption_placed_at"))
                    amount.add("Rs. "+responseOBJ.getString("redemption_amount"))

                    Log.d("responseVal",response.getJSONObject(i).toString())
                }

                transectiondate.reverse()
                amount.reverse()

                shimmer_view_containerwlt.stopShimmerAnimation()
                shimmer_view_containerwlt.visibility = View.GONE
                val transectionlvadapter = transectionAdapter(context as Activity,amount,transectiondate)
                TransectionLV.adapter = transectionlvadapter


                Log.d("success","Wallet Amount Get")
                //Log.d("Profile fetch",response.toString())


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




}